package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.widget.Button;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class ButtonSubclass extends Button {
  public ButtonSubclass(Context context) { super(context); }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    ViewPlugin.onViewSizeChanged(this, w, h, oldw, oldh);
  }
}
