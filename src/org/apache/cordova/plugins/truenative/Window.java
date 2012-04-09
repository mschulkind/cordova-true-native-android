package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.View;

import org.apache.cordova.DroidGap;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class Window {
  public View view;
  private WindowPlugin mPlugin;

  public Window(WindowPlugin plugin) {
    mPlugin = plugin;
  }

  public void setup(JSONObject options) {
    JSONObject viewOptions = null;
    try {
      viewOptions = options.getJSONObject("view");
    } catch (JSONException e) {
      e.printStackTrace();
      fail();
    }

    view = (View)mPlugin.createComponent(viewOptions);
  }

  public void open() {
    mPlugin.getDroidGap().setContentView(view);
  }
}
