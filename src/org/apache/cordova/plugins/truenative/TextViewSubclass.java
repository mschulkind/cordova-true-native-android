package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.widget.TextView;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class TextViewSubclass extends TextView {
  public TextViewSubclass(Context context) { super(context); }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    ViewPlugin.onViewSizeChanged(this, w, h, oldw, oldh);
  }
}
