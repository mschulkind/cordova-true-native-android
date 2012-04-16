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

public class ComponentPlugin extends Plugin {
  private int mNextUIID = 0;

  // Maps tnUIID -> component.
  private HashMap<String, Object> mComponentMap = 
    new HashMap<String, Object>();
  // Maps component -> tnUIID.
  private HashMap<Object, String> mReverseComponentMap = 
    new HashMap<Object, String>();
  // Maps tnUIID -> component data.
  private HashMap<String, ComponentData> mComponentDataMap =
    new HashMap<String, ComponentData>();

  protected Object newComponentInstance() { 
    fail(); 
    return null;
  }
  protected class ComponentData {};
  protected ComponentData newComponentDataInstance() {
    return new ComponentData();
  }
  protected void setupComponent(Object component, JSONObject options) {
  }

  @Override
  public PluginResult execute(
      String action, JSONArray args, String callbackId) {
    try {
      if (action.equals("getProperties")) { 
        this.getProperties(args.getJSONObject(0)); 
      } else if (action.equals("setProperties")) { 
        this.setProperties(args.getJSONObject(0)); 
      } else if (action.equals("loadJavascript")) {
        // TODO(mschulkind): Actually do something here.
      } else {
        assertTrue("action '"+ action + "' not found", false);
      }
    } catch (JSONException e) {
      return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
    }

    return new PluginResult(PluginResult.Status.OK, "");
  }

  protected DroidGap getDroidGap() {
    return ((DroidGap)ctx);
  }

  // Returns the ComponentPlugin plugin instance, not just the current plugin
  // casted to a ComponentPlugin.
  protected ComponentPlugin getComponentPlugin() {
    if (getClass() == ComponentPlugin.class) {
      return this;
    } else {
      return (ComponentPlugin)getPluginFor("cordovatruenative.component");
    }
  }

  protected String writeJavascript(String statement) {
    return ((SMWebView)webView).writeJavascript(
        "JSON.stringify("+statement+")");
  }

  protected String writeJavascriptForComponent(
      String tnUIID, String statement) {
    String wrappedStatement = "TN.UI.componentMap['"+tnUIID+"']."+statement;
    return writeJavascript(wrappedStatement);
  }
  protected String writeJavascriptForComponent(
      Object component, String statement) {
    return writeJavascriptForComponent(getComponentID(component), statement);
  }

  protected void fireEvent(
      String tnUIID, String name, JSONObject data) {
    writeJavascriptForComponent(tnUIID, "fireEvent('"+name+"', "+data+")");
  }
  protected void fireEvent(
      Object component, String name, JSONObject data) {
    String tnUIID = getComponentID(component);
    fireEvent(tnUIID, name, data);
  }

  protected void registerComponent(
      Object component, ComponentData componentData, String tnUIID) {
    ComponentPlugin componentPlugin = getComponentPlugin();

    componentPlugin.mComponentMap.put(tnUIID, component);
    componentPlugin.mReverseComponentMap.put(component, tnUIID);
    componentPlugin.mComponentDataMap.put(tnUIID, componentData);
  }
  protected String getComponentID(Object component) {
    String tnUIID = getComponentPlugin().mReverseComponentMap.get(component);
    assertNotNull("No ID found for component: "+component, tnUIID);
    return tnUIID;
  }
  protected Object getComponent(String tnUIID) {
    Object component = getComponentPlugin().mComponentMap.get(tnUIID);
    assertNotNull("Component with ID '"+tnUIID+"' not found.", component);
    return component;
  }
  protected ComponentData getComponentData(String tnUIID) {
    try {
      ComponentData componentData = 
        getComponentPlugin().mComponentDataMap.get(tnUIID);
      assertNotNull(componentData);
      return componentData;
    } catch(Exception e) {
      e.printStackTrace();
      fail("Component data for component with ID '"+tnUIID+"' not found.");
    }

    return null;
  }
  protected ComponentData getComponentData(Object component) {
    return getComponentData(getComponentID(component));
  }

  private ComponentPlugin getPluginFor(String pluginID) {
    ComponentPlugin plugin = 
      (ComponentPlugin)getDroidGap()
        .pluginManager
        .getPlugin(pluginID);
    assertNotNull(plugin);

    return plugin;
  }
  private ComponentPlugin getPluginFor(JSONObject options) {
    String pluginID = null;
    try {
      pluginID = options.getString("pluginID");
    } catch (JSONException e) {
      e.printStackTrace();
      fail("Plugin with ID '"+pluginID+"' not found.");
    }

    return getPluginFor(pluginID);
  }

  protected Object createComponent(JSONObject options) {
    String tnUIID = null;
    try {
      tnUIID = options.getString("tnUIID");
    } catch (JSONException e) {
      e.printStackTrace();
      fail();
    }

    ComponentPlugin plugin = getPluginFor(options);

    Object component = plugin.newComponentInstance();
    ComponentData componentData = plugin.newComponentDataInstance();
    plugin.registerComponent(component, componentData, tnUIID);
    plugin.setupComponent(component, options);

    return component;
  }

  protected void setComponentProperty(
      Object component, String key, Object value) {
    if (false) {
    } else {
      fail("Unknown property '"+key+"'.");
    }
  }

  protected void setComponentProperties(
      Object component, JSONObject options, String... propertyNames) {
    for (String name : propertyNames) {
      Object value = options.opt(name);
      if (value != null) {
        setComponentProperty(component, name, value);
      }
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

  protected Object getComponentProperty(Object component, String key) {
    if (false) {
      return null;
    } else {
      fail("Unknown property '"+key+"'.");
      return null;
    }
  }

  public PluginResult getProperties(final JSONObject options) {
    final JSONObject properties = new JSONObject();

    ctx.runOnUiThread(new Runnable() {
      public void run() {
        try {
          String tnUIID = options.getString("componentID");
          JSONArray propertyNames = options.getJSONArray("propertyNames");

          Object component = getComponent(tnUIID);
          for (int i = 0; i < propertyNames.length(); ++i) {
            String key = (String)propertyNames.get(i);
            Object value = getComponentProperty(component, key);
            properties.put(key, value);
          }
        } catch(JSONException e) {
          e.printStackTrace();
          fail();
        }
      }
    });

    return new PluginResult(PluginResult.Status.OK, properties);
  }
}
