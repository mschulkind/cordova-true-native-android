package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.widget.TextView;

public class LabelSubclass extends TextView {
  private LabelPlugin mPlugin;

  public LabelSubclass(Context context, LabelPlugin plugin) { 
    super(context); 
    mPlugin = plugin;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    mPlugin.onViewSizeChanged(this, w, h, oldw, oldh);
  }
}
