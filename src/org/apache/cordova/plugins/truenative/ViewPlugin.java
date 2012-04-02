package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.View;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONObject;

public class ViewPlugin extends ComponentPlugin {
  @Override
  protected Object newComponentInstance() {
    return new ViewSubclass(getDroidGap());
  }

  @Override
  protected void setupComponent(Object component, JSONObject options) {
  }

  @Override
  public void setComponentProperty(
      final Object component, final String key, final Object value) {
    final ViewSubclass view = (ViewSubclass)component;

    if (key.equals("backgroundColor")) {
      view.setBackgroundColor(Util.parseColor((String)value));
    } else {
      super.setComponentProperty(component, key, value);
    }
  }
}
