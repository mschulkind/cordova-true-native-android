package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.View;

import org.apache.cordova.DroidGap;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class Window {
  private WindowPlugin mPlugin;

  public Window(WindowPlugin plugin) {
    mPlugin = plugin;
  }

  public void setup(JSONObject options) {
  }

  public void open() {
    try {
      JSONObject viewOptions = new JSONObject(
          mPlugin.writeJavascriptForComponent(this, "createView()"));

      View view = (View)mPlugin.createComponent(viewOptions);

      ViewPlugin.ViewData data = 
        (ViewPlugin.ViewData)mPlugin.getComponentData(view); 
      data.layoutParams.width = ViewSubclass.LayoutParams.MATCH_PARENT;
      data.layoutParams.height = ViewSubclass.LayoutParams.MATCH_PARENT;

      mPlugin.getDroidGap().setContentView(view);
    } catch(JSONException e) {
      e.printStackTrace();
      fail();
    }
  }
}
