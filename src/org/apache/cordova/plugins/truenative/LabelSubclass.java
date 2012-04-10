package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.widget.TextView;
import org.json.JSONObject;

import static junit.framework.Assert.*;

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
