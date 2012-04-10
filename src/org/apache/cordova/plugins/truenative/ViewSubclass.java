package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.widget.AbsoluteLayout;

public class ViewSubclass extends AbsoluteLayout {
  private ViewPlugin mPlugin;

  public ViewSubclass(Context context, ViewPlugin plugin) { 
    super(context); 
    mPlugin = plugin;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    mPlugin.onViewSizeChanged(this, w, h, oldw, oldh);
  }
}
