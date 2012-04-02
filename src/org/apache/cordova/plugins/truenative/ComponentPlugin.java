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
    if (action.equals("allocateUIID")) { return this.allocateUIID(); }
    else {
      assertTrue("action '"+ action + "' not found", false);
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
      assertTrue(false);
    }

    return null;
  }

  private static int sNextUIID = 0;
  public PluginResult allocateUIID() {
    int allocatedID = sNextUIID;
    sNextUIID++;
    return new PluginResult(
        PluginResult.Status.OK, Integer.toString(allocatedID));
  }

  public static Object createComponentWithOptions(JSONObject options) {
    String pluginID = null;
    String tnUIID = null;
    try {
      pluginID = options.getString("pluginID");
      tnUIID = options.getString("tnUIID");
    } catch (JSONException e) {
      e.printStackTrace();
      assertTrue(false);
    }

    ComponentPlugin plugin = 
      (ComponentPlugin)sSingleton
        .getDroidGap()
        .pluginManager
        .getPlugin(pluginID);
    Object component = plugin.newComponentInstance();

    sSingleton.registerComponent(component, tnUIID);

    plugin.setupComponent(component, options);

    return component;
  }
}
