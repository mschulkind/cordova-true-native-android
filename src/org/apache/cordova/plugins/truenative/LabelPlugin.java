package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.widget.TextView;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class LabelPlugin extends ViewPlugin {
  protected class LabelData extends ViewData {
  }

  @Override
  protected Object newComponentInstance() {
    return new TextViewSubclass(getDroidGap());
  }

  @Override
  protected ComponentData newComponentDataInstance() {
    return new LabelData();
  }

  @Override
  protected void setupComponent(Object component, JSONObject options) {
    super.setupComponent(component, options);

    setComponentProperties(component, options, "");
  }

  @Override
  public void setComponentProperty(Object component, String key, Object value) {
    TextView textView = (TextView)component;
    LabelData data = (LabelData)getComponentData(component);

    if (key.equals("text")) {
    } else {
      super.setComponentProperty(component, key, value);
    }
  }
}
