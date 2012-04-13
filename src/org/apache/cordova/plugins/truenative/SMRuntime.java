package org.apache.cordova.plugins.truenative;

public class SMRuntime {
  private long mJSContext;
  private long mJSGlobalObject;
  private long mJSRuntime;

  public SMRuntime(String... sourceFiles) {
    setupSpiderMonkey(sourceFiles);
  }

  public native String writeJavascript(String sourceCode);

  public void finalize() {
    destroy();
  }

  private native void setupSpiderMonkey(String[] sourceFiles);
  private native void destroy();

  static {
    System.loadLibrary("SMRuntime");
  }
}
