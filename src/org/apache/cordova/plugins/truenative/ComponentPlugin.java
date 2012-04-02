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
  
  public void sendJavascriptForComponent(String tnUIID, String statement) {
    //webview("TN.UI.componentMap['" + tnUIID + "']." + statement);
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
    try {
      pluginID = options.getString("pluginID");
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

    plugin.setupComponent(component, options);

    return component;
  }
}
