package org.apache.cordova.plugins.truenative;

import android.content.res.AssetManager;

public class SMRuntime {
  private long mJSContext;
  private long mJSGlobalObject;
  private long mJSRuntime;

  public SMRuntime(AssetManager assetManager, String... sourceFilenames) {
    setupSpiderMonkey(assetManager, sourceFilenames);
  }

  public native String writeJavascript(String sourceCode);

  public void finalize() {
    destroy();
  }

  private native void setupSpiderMonkey(
      AssetManager assetManager, String[] sourceFilenames);
  private native void destroy();

  static {
    System.loadLibrary("SMRuntime");
  }
}
