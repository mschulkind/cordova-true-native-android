package org.apache.cordova.plugins.truenative;

import android.content.res.AssetManager;
import org.json.JSONArray;
import org.json.JSONException;

import static junit.framework.Assert.*;

public class SMRuntime {
  private long mJSContext;
  private long mJSGlobalObject;
  private long mJSRuntime;
  private SMTimerScheduler mScheduler;
  private TrueNativeActivity mTrueNativeActivity;

  public SMRuntime(
      TrueNativeActivity trueNativeActivity, String... sourceFilenames) {
    mTrueNativeActivity = trueNativeActivity;
    mScheduler = new SMTimerScheduler(mTrueNativeActivity);

    setupSpiderMonkey(mTrueNativeActivity.getAssets(), sourceFilenames);
  }

  public native String writeJavascript(String sourceCode);

  public void finalize() {
    destroy();
  }

  public String nativeExec(String command, String args) {
    if (!command.startsWith("gap:")) {
      return "";
    }

    String result = null;
    try {
      JSONArray array;
      array = new JSONArray(command.substring(4));
      String service = array.getString(0);
      String action = array.getString(1);
      String callbackId = array.getString(2);
      boolean async = array.getBoolean(3);
      result = 
        mTrueNativeActivity.pluginManager.exec(
            service, action, callbackId, args, async);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }

    return result;
  }

  public void registerTimer(int msecs, boolean repeats, long timerPrivate) {
    mScheduler.registerTimer(msecs, repeats, timerPrivate);
  }

  public void clearTimer(long timerPrivate) {
    mScheduler.clearTimer(timerPrivate);
  }

  private native void setupSpiderMonkey(
      AssetManager assetManager, String[] sourceFilenames);
  private native void destroy();

  static {
    System.loadLibrary("SMRuntime");
  }
}
