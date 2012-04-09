package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.widget.AbsoluteLayout;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class AbsoluteLayoutSubclass extends AbsoluteLayout {
  private ViewPlugin mPlugin;

  public AbsoluteLayoutSubclass(Context context, ViewPlugin plugin) { 
    super(context); 
    mPlugin = plugin;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    mPlugin.onViewSizeChanged(this, w, h, oldw, oldh);
  }
}
