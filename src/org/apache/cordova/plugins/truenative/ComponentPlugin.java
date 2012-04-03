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
  protected class ComponentData {};
  protected ComponentData newComponentDataInstance() {
    return new ComponentData();
  }
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
    String tnUIID = getComponentID(component);
    sSingleton.sendJavascript("TN.UI.componentMap['"+tnUIID+"']."+statement);
  }

  public static void fireEventForComponent(
      Object component, String name, JSONObject data) {
    sendJavascriptForComponent(component, "fireEvent('"+name+"', "+data+")");
  }

  // Maps tnUIID -> component.
  private static HashMap<String, Object> componentMap = 
    new HashMap<String, Object>();
  // Maps component -> tnUIID.
  private static HashMap<Object, String> reverseComponentMap = 
    new HashMap<Object, String>();
  // Maps tnUIID -> component data.
  private static HashMap<String, ComponentData> componentDataMap =
    new HashMap<String, ComponentData>();
  public static void registerComponent(
      Object component, ComponentData componentData, String tnUIID) {
    componentMap.put(tnUIID, component);
    reverseComponentMap.put(component, tnUIID);
    componentDataMap.put(tnUIID, componentData);
  }
  public static String getComponentID(Object component) {
    try {
      String tnUIID = reverseComponentMap.get(component);
      assertNotNull(tnUIID);
      return tnUIID;
    } catch(Exception e) {
      e.printStackTrace();
      fail("No ID found for component: "+component);
    }

    return null;
  }
  public static Object getComponent(String tnUIID) {
    try {
      Object component = componentMap.get(tnUIID);
      assertNotNull(component);
      return component;
    } catch(Exception e) {
      e.printStackTrace();
      fail("Component with ID '"+tnUIID+"' not found.");
    }

    return null;
  }
  public static ComponentData getComponentData(String tnUIID) {
    try {
      ComponentData componentData = componentDataMap.get(tnUIID);
      assertNotNull(componentData);
      return componentData;
    } catch(Exception e) {
      e.printStackTrace();
      fail("Component data for component with ID '"+tnUIID+"' not found.");
    }

    return null;
  }
  public static ComponentData getComponentData(Object component) {
    return getComponentData(getComponentID(component));
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

  public static Object createComponent(JSONObject options) {
    String tnUIID = null;
    try {
      tnUIID = options.getString("tnUIID");
    } catch (JSONException e) {
      e.printStackTrace();
      fail();
    }

    ComponentPlugin plugin = sSingleton.getPluginFor(options);

    Object component = plugin.newComponentInstance();
    ComponentData componentData = plugin.newComponentDataInstance();
    plugin.registerComponent(component, componentData, tnUIID);
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

  public void setComponentProperties(
      Object component, JSONObject options, String... propertyNames) {
    try {
      for (String name : propertyNames) {
        Object value = options.get(name);
        setComponentProperty(component, name, value);
      }
    } catch(Exception e) {
      e.printStackTrace();
      assertTrue(false);
    }
  }

  public void setProperties(final JSONObject options) {
    ctx.runOnUiThread(new Runnable() {
      public void run() {
        try {
          String tnUIID = options.getString("componentID");
          JSONObject properties = options.getJSONObject("properties");

          Object component = getComponent(tnUIID);
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
