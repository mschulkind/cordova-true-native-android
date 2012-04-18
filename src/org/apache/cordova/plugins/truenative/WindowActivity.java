package org.apache.cordova.plugins.truenative;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import static junit.framework.Assert.*;

public class WindowActivity extends Activity {
  private WindowPlugin mPlugin;
  private WindowComponent mWindow;

  private static HashMap<Integer, WindowPlugin> mPluginMap =
    new HashMap<Integer, WindowPlugin>();

  public synchronized static void registerPlugin(
      Intent intent, WindowPlugin plugin) {
    int id = intent.hashCode();
    mPluginMap.put(new Integer(id), plugin);
    intent.putExtra("pluginID", id);
  }
  public synchronized static WindowPlugin getPlugin(int id) {
    WindowPlugin plugin = mPluginMap.get(new Integer(id));
    assertNotNull("No plugin found for intent with ID "+id, plugin);
    mPluginMap.remove(new Integer(id));
    return plugin;
  }

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);

    Intent intent = getIntent();
    int id = intent.getIntExtra("pluginID", 0);
    mPlugin = getPlugin(id);

    try {
      String windowID = intent.getStringExtra("tnUIID");
      assertNotNull(windowID);
      mWindow = (WindowComponent)mPlugin.getComponent(windowID);

      mWindow.activity = this;
      mPlugin.onWindowCreate(mWindow);

      // Create the window's backing view.
      JSONObject viewOptions = new JSONObject(
          mPlugin.writeJavascriptForComponent(mWindow, "createView()"));
      mWindow.view = (View)mPlugin.createComponent(this, viewOptions);

      // Set the view to expand to the window's full size.
      ViewPlugin.ViewData data = 
        (ViewPlugin.ViewData)mPlugin.getComponentData(mWindow.view); 
      data.layoutParams.width = ViewSubclass.LayoutParams.MATCH_PARENT;
      data.layoutParams.height = ViewSubclass.LayoutParams.MATCH_PARENT;

      setContentView(mWindow.view);
    } catch(JSONException e) {
      e.printStackTrace();
      fail();
    }
  }

  protected void onDestroy() {
    super.onDestroy();

    mPlugin.onWindowDestroy(mWindow);
  }
}

