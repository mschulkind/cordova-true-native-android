package org.apache.cordova.plugins.truenative;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class WindowComponent {
  private WindowPlugin mPlugin;
  public Activity activity;
  public View view;
  public Runnable onCreateListener;

  public WindowComponent(WindowPlugin plugin) {
    mPlugin = plugin;
  }

  public void open(Context parentContext) {
    Intent intent = new Intent(parentContext, WindowActivity.class);
    WindowActivity.registerPlugin(intent, mPlugin);
    intent.putExtra("tnUIID", mPlugin.getComponentID(this));
    parentContext.startActivity(intent);
  }

  public void close() {
    activity.finish();
  }
}
