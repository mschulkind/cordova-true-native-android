package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.util.TypedValue;
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
  protected Object newComponentInstance(Context context) {
    return new LabelSubclass(context, this);
  }

  @Override
  protected ComponentData newComponentDataInstance() {
    return new LabelData();
  }

  @Override
  protected void setupComponent(Object component, JSONObject options) {
    super.setupComponent(component, options);

    setComponentProperties(
        component, options, 
        "color", "text", "fontSize");
  }

  @Override
  public void setComponentProperty(Object component, String key, Object value) {
    TextView textView = (TextView)component;
    LabelData data = (LabelData)getComponentData(component);

    if (key.equals("text")) {
      textView.setText((String)value);
    } else if (key.equals("color")) {
      textView.setTextColor(Util.parseColor((String)value));
    } else if (key.equals("fontSize")) {
      textView.setTextSize(
          TypedValue.COMPLEX_UNIT_DIP, ((Integer)value).intValue());
    } else {
      super.setComponentProperty(component, key, value);
    }
  }
}
