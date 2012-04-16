package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.View;
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

  @Override
  public void setOnClickListener(View.OnClickListener l) {
    // Prevent the standard view onclick handler from getting set since it
    // doesn't work for table views.
  }
}
