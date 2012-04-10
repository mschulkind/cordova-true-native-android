package org.apache.cordova.plugins.truenative;

import android.view.View;
import android.widget.AbsoluteLayout;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class TableViewRowPlugin extends ViewPlugin {
  protected class TableViewRowData extends ViewData {
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
  }

  @Override
  public Object getComponentProperty(Object component, String key) {
    AbsoluteLayout row = (AbsoluteLayout)component;
    TableViewRowData data = (TableViewRowData)getComponentData(component);

    if (key.equals("")) {
      return null;
    } else {
      return super.getComponentProperty(component, key);
    }
  }

  @Override
  public void setComponentProperty(Object component, String key, Object value) {
    AbsoluteLayout row = (AbsoluteLayout)component;
    TableViewRowData data = (TableViewRowData)getComponentData(component);

    if (key.equals("")) {
    } else {
      super.setComponentProperty(component, key, value);
    }
  }
}
