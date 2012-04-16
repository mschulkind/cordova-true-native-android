package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class ButtonPlugin extends ViewPlugin {
  protected class ButtonData extends ViewData {
  }

  @Override
  protected Object newComponentInstance() {
    return new ButtonSubclass(getDroidGap(), this);
  }

  @Override
  protected ComponentData newComponentDataInstance() {
    return new ButtonData();
  }

  @Override
  protected void setupComponent(Object component, JSONObject options) {
    super.setupComponent(component, options);

    setComponentProperties(
        component, options, 
        "title", "fontColor", "fontSize");

    ButtonSubclass button = (ButtonSubclass)component;
    button.setPadding(0,0,0,0);
  }

  @Override
  public void setComponentProperty(Object component, String key, Object value) {
    Button textView = (Button)component;
    ButtonData data = (ButtonData)getComponentData(component);

    if (key.equals("title")) {
      textView.setText((String)value);
    } else if (key.equals("fontColor")) {
      textView.setTextColor(Util.parseColor((String)value));
    } else if (key.equals("fontSize")) {
      textView.setTextSize(
          TypedValue.COMPLEX_UNIT_DIP, ((Integer)value).intValue());
    } else {
      super.setComponentProperty(component, key, value);
    }
  }
}
