package org.apache.cordova.plugins.truenative;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.*;

import static junit.framework.Assert.*;

public class WindowActivity extends Activity {
  private WindowPlugin mPlugin;
  private String mWindowID;

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

    Intent intent = getIntent();
    int id = intent.getIntExtra("pluginID", 0);
    mPlugin = getPlugin(id);
    mWindowID = intent.getStringExtra("tnUIID");
    assertNotNull(mWindowID);
    mPlugin.onWindowCreate(mWindowID, this);
  }

  protected void onDestroy() {
    super.onDestroy();

    mPlugin.onWindowDestroy(mWindowID);
  }
}

