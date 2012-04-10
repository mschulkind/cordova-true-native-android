package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.widget.ListView;

public class TableViewSubclass extends ListView {
  private TableViewPlugin mPlugin;

  public TableViewSubclass(Context context, TableViewPlugin plugin) { 
    super(context); 
    mPlugin = plugin;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    mPlugin.onViewSizeChanged(this, w, h, oldw, oldh);
  }
}
