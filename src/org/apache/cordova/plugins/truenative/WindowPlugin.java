package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.os.Process;
import android.view.View;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import static junit.framework.Assert.*;

public class WindowPlugin extends ComponentPlugin {
  protected Stack<WindowComponent> windowStack = new Stack<WindowComponent>();

  protected void onWindowCreate(String windowID, WindowActivity activity) {
    WindowComponent window = (WindowComponent)getComponent(windowID);
    window.activity = activity;

    windowStack.push(window);

    activity.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);

    try {
      // Create the window's backing view.
      JSONObject viewOptions = new JSONObject(
          writeJavascriptForComponent(window, "createView()"));
      window.view = (View)createComponent(activity, viewOptions);

      // Set the view to expand to the window's full size.
      ViewPlugin.ViewData data = 
        (ViewPlugin.ViewData)getComponentData(window.view); 
      data.layoutParams.width = ViewSubclass.LayoutParams.MATCH_PARENT;
      data.layoutParams.height = ViewSubclass.LayoutParams.MATCH_PARENT;

      activity.setContentView(window.view);
    } catch(JSONException e) {
      e.printStackTrace();
      fail();
    }
  }

  protected void onWindowDestroy(String windowID) {
    WindowComponent window = (WindowComponent)getComponent(windowID);

    assertEquals(windowStack.peek(), window);
    windowStack.pop();

    // Quit if we just popped the last window off the stack.
    if (windowStack.empty()) {
      getTrueNativeActivity().finish();
    }
  }

  @Override
  protected Object newComponentInstance(Context context) {
    return new WindowComponent(this);
  }

  @Override
  protected void setupComponent(Object component, JSONObject options) {
    super.setupComponent(component, options);
  }

  @Override
  public PluginResult execute(
      String action, JSONArray args, String callbackId) {
    JSONObject options;
    try {
      if (action.equals("open")) { open(args.getJSONObject(0)); }
      else { return super.execute(action, args, callbackId); }
    } catch (JSONException e) {
      return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
    }

    return new PluginResult(PluginResult.Status.OK, "");
  }

  public void open(final JSONObject options) {
    try {
      final JSONObject windowOptions = options.getJSONObject("window");

      ctx.runOnUiThread(new Runnable() {
        public void run() {
          // Either use the context on the top of the window stack, or if there
          // isn't one yet, use the original context since this is the first
          // window.
          Context context = getTrueNativeActivity();
          if (!windowStack.empty()) {
            context = windowStack.peek().view.getContext();
          }

          WindowComponent window =
              (WindowComponent)createComponent(null, windowOptions);
          window.open(context);
        }
      });
    } catch (JSONException e) {
      e.printStackTrace();
      fail();
    }
  }
}
