package org.apache.cordova.plugins.truenative;

import android.view.View;
import android.content.Context;

public class TableViewRowSubclass extends ViewSubclass {
  public TableViewRowSubclass(Context context, TableViewRowPlugin plugin) { 
    super(context, plugin); 
 }

  @Override
  public void setOnClickListener(View.OnClickListener l) {
    // Prevent the standard view onclick handler from getting set since it
    // doesn't work for table views.
  }
}
