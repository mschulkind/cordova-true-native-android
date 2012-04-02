package org.apache.cordova.plugins.truenative;

import android.content.Context;

import org.apache.cordova.DroidGap;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginManager;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.*;

import static junit.framework.Assert.*;

public abstract class ComponentPlugin extends Plugin {
  private static ComponentPlugin sSingleton;
  public ComponentPlugin() {
    if (sSingleton == null) {
      sSingleton = this;
    }
  }
  public static ComponentPlugin getSingleton() {
    return sSingleton;
  }

  protected abstract Object newComponentInstance();
  protected abstract void setupComponent(Object component, JSONObject options);

  @Override
	public boolean isSynch(String action) {
		return action.equals("allocateUIID");
  }

  @Override
  public PluginResult execute(
      String action, JSONArray args, String callbackId) {
    try {
      if (action.equals("allocateUIID")) { 
        return this.allocateUIID(); 
      } else if (action.equals("setProperties")) { 
        this.setProperties(args.getJSONObject(0)); 
      } else {
        assertTrue("action '"+ action + "' not found", false);
      }
    } catch (JSONException e) {
      return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
    }

    return new PluginResult(PluginResult.Status.OK, "");
  }

  public DroidGap getDroidGap() {
    return ((DroidGap)ctx);
  }

  public static void sendJavascriptForComponent(
      Object component, String statement) {
    String tnUIID = lookupComponentID(component);
    sSingleton.sendJavascript("TN.UI.componentMap['"+tnUIID+"']."+statement);
  }

  public static void fireEventForComponent(
      Object component, String name, JSONObject data) {
    sendJavascriptForComponent(component, "fireEvent('"+name+"', "+data+")");
  }

  // Maps tnUIID -> component.
  private static HashMap<String, Object> tnUIIDMap = 
    new HashMap<String, Object>();
  // Maps component -> tnUIID.
  private static HashMap<Object, String> componentMap = 
    new HashMap<Object, String>();
  public static void registerComponent(Object component, String tnUIID) {
    tnUIIDMap.put(tnUIID, component);
    componentMap.put(component, tnUIID);
  }
  public static String lookupComponentID(Object component) {
    try {
      String tnUIID = componentMap.get(component);
      assertNotNull(tnUIID);
      return tnUIID;
    } catch(Exception e) {
      e.printStackTrace();
      fail("No ID found for component: "+component);
    }

    return null;
  }
  public static Object lookupComponent(String tnUIID) {
    try {
      Object component = tnUIIDMap.get(tnUIID);
      assertNotNull(component);
      return component;
    } catch(Exception e) {
      e.printStackTrace();
      fail("Component with ID '"+tnUIID+"' not found.");
    }

    return null;
  }

  private ComponentPlugin getPluginFor(JSONObject options) {
    String pluginID = null;
    try {
      pluginID = options.getString("pluginID");
    } catch (JSONException e) {
      e.printStackTrace();
      fail("Plugin with ID '"+pluginID+"' not found.");
    }

    ComponentPlugin plugin = 
      (ComponentPlugin)sSingleton
        .getDroidGap()
        .pluginManager
        .getPlugin(pluginID);
    assertNotNull(plugin);

    return plugin;
  }

  public static Object createComponentWithOptions(JSONObject options) {
    String tnUIID = null;
    try {
      tnUIID = options.getString("tnUIID");
    } catch (JSONException e) {
      e.printStackTrace();
      fail();
    }

    ComponentPlugin plugin = sSingleton.getPluginFor(options);

    Object component = plugin.newComponentInstance();
    plugin.registerComponent(component, tnUIID);
    plugin.setupComponent(component, options);

    return component;
  }

  private static int sNextUIID = 0;
  public PluginResult allocateUIID() {
    int allocatedID = sNextUIID;
    sNextUIID++;
    return new PluginResult(
        PluginResult.Status.OK, Integer.toString(allocatedID));
  }

  public void setComponentProperty(Object component, String key, Object value) {
    if (false) {
    } else {
      fail("Unknown property '"+key+"'.");
    }
  }

  public void setProperties(final JSONObject options) {
    ctx.runOnUiThread(new Runnable() {
      public void run() {
        try {
          String tnUIID = options.getString("componentID");
          JSONObject properties = options.getJSONObject("properties");

          Object component = lookupComponent(tnUIID);
          for (Iterator it = properties.keys(); it.hasNext();) {
            String key = (String)it.next();
            Object value = properties.get(key);
            setComponentProperty(component, key, value);
          }
        } catch(JSONException e) {
          e.printStackTrace();
          fail();
        }
      }
    });
  }
}
