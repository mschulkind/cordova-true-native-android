package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.View;

import org.apache.cordova.DroidGap;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class Window {
  private View mView;

  public void setup(JSONObject options) {
    JSONObject viewOptions = null;
    try {
      viewOptions = options.getJSONObject("view");
    } catch (JSONException e) {
      e.printStackTrace();
      assertTrue(false);
    }

    mView = (View)ComponentPlugin.createComponentWithOptions(viewOptions);
  }

  public void open() {
    ComponentPlugin.getSingleton().getDroidGap().setContentView(mView);
  }
}
