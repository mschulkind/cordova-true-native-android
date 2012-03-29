package org.apache.cordova.plugins.truenative;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class Component extends Plugin {
  public PluginResult execute(
      String action, JSONArray args, String callbackId) {
    //try {
      assertTrue("action '"+ action + "' not found", false);
      return null;
    //} catch (JSONException e) {
      //return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
    //}
  }
}
