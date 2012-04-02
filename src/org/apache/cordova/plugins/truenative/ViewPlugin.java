package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.View;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONObject;

public class ViewPlugin extends ComponentPlugin {
  private class ViewSubclass extends View {
    public ViewSubclass(Context context) { super(context); }
  }

  @Override
  protected Object newComponentInstance() {
    return new ViewSubclass(getDroidGap());
  }

  @Override
  protected void setupComponent(Object component, JSONObject options) {
  }
}
