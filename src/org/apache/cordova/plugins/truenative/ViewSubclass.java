package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.View;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class ViewSubclass extends View {
  public ViewSubclass(Context context) { super(context); }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    JSONObject data = new JSONObject();
    try {
      data.put("width", w);
      data.put("height", h);
    } catch(Exception e) {
      e.printStackTrace();
      fail();
    }
    ComponentPlugin.fireEventForComponent(this, "resize", data);
  }
}
