package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;

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
    super.setupComponent(component, options);

    setComponentProperties(
        component, options, 
        "top", "left", "width", "height", "backgroundColor");
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

  private void updateLayoutParams(View view) {
    ViewData data = (ViewData)getComponentData(view);
    if (data.parent != null) {
      data.parent.updateViewLayout(view, data.layoutParams);
    }
  }

  private static int convertToPixels(Object dips) {
    return (int)(((Integer)dips).intValue() 
        * sSingleton.getDroidGap().getResources().getDisplayMetrics().density);
  }

  private static int convertToDips(int pixels) {
    return (int)(pixels 
        / sSingleton.getDroidGap().getResources().getDisplayMetrics().density);
  }

  public static void onViewSizeChanged(
      View view, int w, int h, int oldw, int oldh) {
    JSONObject data = new JSONObject();
    try {
      data.put("width", convertToDips(w));
      data.put("height", convertToDips(h));
    } catch(Exception e) {
      e.printStackTrace();
      fail();
    }
    ComponentPlugin.fireEvent(view, "resize", data);
  }

  @Override
  public Object getComponentProperty(Object component, String key) {
    View view = (View)component;
    ViewData data = (ViewData)getComponentData(component);

    if (key.equals("top")) {
      return convertToDips(data.layoutParams.y);
    } else if (key.equals("left")) {
      return convertToDips(data.layoutParams.x);
    } else if (key.equals("width")) {
      return convertToDips(data.layoutParams.width);
    } else if (key.equals("height")) {
      return convertToDips(data.layoutParams.height);
    } else {
      return super.getComponentProperty(component, key);
    }
  }

  @Override
  public void setComponentProperty(Object component, String key, Object value) {
    View view = (View)component;
    ViewData data = (ViewData)getComponentData(component);

    if (key.equals("backgroundColor")) {
      view.setBackgroundColor(Util.parseColor((String)value));
    } else if (key.equals("top")) {
      data.layoutParams.y = convertToPixels(value);
      updateLayoutParams(view);
    } else if (key.equals("left")) {
      data.layoutParams.x = convertToPixels(value);
      updateLayoutParams(view);
    } else if (key.equals("width")) {
      data.layoutParams.width = convertToPixels(value);
      updateLayoutParams(view);
    } else if (key.equals("height")) {
      data.layoutParams.height = convertToPixels(value);
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
          View child = (View)createComponent(childOptions);
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
