package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class TableViewRowPlugin extends ViewPlugin {
  protected class TableViewRowData extends ViewData {
    TextView textLabel;
  }

  @Override
  protected Object newComponentInstance() {
    return new TableViewRowSubclass(getDroidGap(), this);
  }

  @Override
  protected ComponentData newComponentDataInstance() {
    return new TableViewRowData();
  }

  @Override
  protected void setupComponent(Object component, JSONObject options) {
    super.setupComponent(component, options);

    setComponentProperties(
      component, options);

    TableViewRowData data = (TableViewRowData)getComponentData(component);
    data.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
  }

  @Override
  public Object getComponentProperty(Object component, String key) {
    ViewGroup row = (ViewGroup)component;
    TableViewRowData data = (TableViewRowData)getComponentData(component);

    if (key.equals("")) {
      return null;
    } else {
      return super.getComponentProperty(component, key);
    }
  }

  @Override
  public void setComponentProperty(Object component, String key, Object value) {
    ViewGroup row = (ViewGroup)component;
    TableViewRowData data = (TableViewRowData)getComponentData(component);

    if (key.equals("hasDetail")) {
      // Ignored. Not supported.
    } else if (key.equals("text")) {
      if (value != JSONObject.NULL) {
        // Since android has no built in text label for rows, create our own
        // label to display the text.
        if (data.textLabel == null) {
          data.textLabel = new TextView(getDroidGap());
          data.textLabel.setPadding(10, 10, 10, 10);
          data.textLabel.setTextColor(Util.parseColor("black"));
          data.textLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
          row.addView(
              data.textLabel,
              new ViewSubclass.LayoutParams(
                  ViewSubclass.LayoutParams.MATCH_PARENT,
                  ViewSubclass.LayoutParams.MATCH_PARENT,
                  0, 0));
        }

        data.textLabel.setText((String)value);
      } else {
        // If the text field is being cleared, get rid of the label.
        row.removeView(data.textLabel);
        data.textLabel = null;
      }
    } else {
      super.setComponentProperty(component, key, value);
    }
  }
}
