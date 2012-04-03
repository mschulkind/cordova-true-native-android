package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.widget.AbsoluteLayout;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class AbsoluteLayoutSubclass extends AbsoluteLayout {
  public AbsoluteLayoutSubclass(Context context) { super(context); }

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
