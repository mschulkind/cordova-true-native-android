package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static junit.framework.Assert.*;

public class ViewPlugin extends ComponentPlugin {
  protected class ViewData extends ComponentData {
    AbsoluteLayout.LayoutParams layoutParams = 
      new AbsoluteLayout.LayoutParams(0, 0, 0, 0);
    AbsoluteLayout parent;
  }

  @Override
  protected Object newComponentInstance() {
    return new AbsoluteLayoutSubclass(getDroidGap());
  }

  @Override
  protected ComponentData newComponentDataInstance() {
    return new ViewData();
  }

  @Override
  protected void setupComponent(Object component, JSONObject options) {
    setComponentProperties(
        component, options, "top", "left", "width", "height", "backgroundColor");
  }

  @Override
  public PluginResult execute(
      String action, JSONArray args, String callbackId) {
    JSONObject options;
    try {
      if (action.equals("add")) { this.add(args.getJSONObject(0)); }
      else { return super.execute(action, args, callbackId); }
    } catch (JSONException e) {
      return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
    }

    return new PluginResult(PluginResult.Status.OK, "");
  }

  private void updateLayoutParams(AbsoluteLayout view) {
    ViewData data = (ViewData)getComponentData(view);
    if (data.parent != null) {
      data.parent.updateViewLayout(view, data.layoutParams);
    }
  }

  private int convertToPixels(Object integer) {
    return (int)(((Integer)integer).intValue() 
                 * ctx.getResources().getDisplayMetrics().density);
  }

  @Override
  public void setComponentProperty(Object component, String key, Object value) {
    AbsoluteLayout view = (AbsoluteLayout)component;
    ViewData viewData = (ViewData)getComponentData(component);

    if (key.equals("backgroundColor")) {
      view.setBackgroundColor(Util.parseColor((String)value));
    } else if (key.equals("top")) {
      viewData.layoutParams.y = convertToPixels(value);
      updateLayoutParams(view);
    } else if (key.equals("left")) {
      viewData.layoutParams.x = convertToPixels(value);
      updateLayoutParams(view);
    } else if (key.equals("width")) {
      viewData.layoutParams.width = convertToPixels(value);
      updateLayoutParams(view);
    } else if (key.equals("height")) {
      viewData.layoutParams.height = convertToPixels(value);
      updateLayoutParams(view);
    } else {
      super.setComponentProperty(component, key, value);
    }
  }

  public void add(final JSONObject options) {
    ctx.runOnUiThread(new Runnable() {
      public void run() {
        try {
          String parentID = options.getString("parentID");
          JSONObject childOptions = options.getJSONObject("child");

          AbsoluteLayout parent = (AbsoluteLayout)getComponent(parentID);
          AbsoluteLayout child = (AbsoluteLayout)createComponent(childOptions);
          ViewData childData = (ViewData)getComponentData(child);
          childData.parent = parent;
          parent.addView(child, childData.layoutParams);
        } catch(JSONException e) {
          e.printStackTrace();
          fail();
        }
      }
    });
  }
}
