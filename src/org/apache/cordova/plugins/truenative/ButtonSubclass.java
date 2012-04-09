package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.widget.Button;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class ButtonSubclass extends Button {
  private ButtonPlugin mPlugin;

  public ButtonSubclass(Context context, ButtonPlugin plugin) { 
    super(context); 
    mPlugin = plugin;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    mPlugin.onViewSizeChanged(this, w, h, oldw, oldh);
  }
}
