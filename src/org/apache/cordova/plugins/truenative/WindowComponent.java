package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import org.apache.cordova.DroidGap;

import static junit.framework.Assert.*;

public class WindowComponent {
  private WindowPlugin mPlugin;
  public Context parentContext;
  public View view;

  public WindowComponent(Context parentContext, WindowPlugin plugin) {
    mPlugin = plugin;
    this.parentContext = parentContext;
  }

  public void open() {
    Intent intent = new Intent(parentContext, WindowActivity.class);
    WindowActivity.registerPlugin(intent, mPlugin);
    intent.putExtra("tnUIID", mPlugin.getComponentID(this));
    parentContext.startActivity(intent);
  }
}
