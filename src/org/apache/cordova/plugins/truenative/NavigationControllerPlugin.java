package org.apache.cordova.plugins.truenative;

import android.content.Context;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class NavigationControllerPlugin extends WindowPlugin {
  @Override
  protected Object newComponentInstance(Context context) {
    return new NavigationController(this);
  }

  @Override
  protected void setupComponent(Object component, JSONObject options) {
    super.setupComponent(component, options);

    NavigationController navController = (NavigationController)component;

    try {
      JSONArray windowStack = options.getJSONArray("windowStack");
      for (int i = 0; i < windowStack.length(); ++i) {
        navController.push(
            (WindowComponent)createComponent(
                null, windowStack.getJSONObject(i)));

      }
    } catch(JSONException e) {
      e.printStackTrace();
      fail();
    }
  }
      
  @Override
  public PluginResult execute(
      String action, JSONArray args, String callbackId) {
    JSONObject options;
    try {
      if (action.equals("push")) { push(args.getJSONObject(0)); }
      else if (action.equals("pop")) { pop(args.getJSONObject(0)); }
      else { return super.execute(action, args, callbackId); }
    } catch (JSONException e) {
      return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
    }

    return new PluginResult(PluginResult.Status.OK, "");
  }

  public void push(final JSONObject options) {
    ctx.runOnUiThread(new Runnable() {
      public void run() {
        try {
          String parentID = options.getString("parentID");
          NavigationController parent = 
              (NavigationController)getComponent(parentID);

          WindowComponent child = 
              (WindowComponent)createComponent(
                  null, options.getJSONObject("child"));

          parent.push(child);
        } catch(JSONException e) {
          e.printStackTrace();
          fail();
        }
      }
    });
  }

  public void pop(JSONObject options) {
    fail();
  }
}
