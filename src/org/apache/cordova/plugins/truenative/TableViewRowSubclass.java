package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.widget.AbsoluteLayout;

public class TableViewRowSubclass extends AbsoluteLayout {
  private TableViewRowPlugin mPlugin;

  public TableViewRowSubclass(Context context, TableViewRowPlugin plugin) { 
    super(context); 
    mPlugin = plugin;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    mPlugin.onViewSizeChanged(this, w, h, oldw, oldh);
  }
}
