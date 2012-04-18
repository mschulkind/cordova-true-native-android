package org.apache.cordova.plugins.truenative;

import android.webkit.WebView;
import org.apache.cordova.DroidGap;
import org.apache.cordova.plugins.truenative.example.App;

import static junit.framework.Assert.*;

public class SMWebView extends WebView {
  private SMRuntime mSMRuntime;
  private TrueNativeActivity mTrueNativeActivity;
  private boolean mLoaded = false;

  public SMWebView(
      TrueNativeActivity trueNativeActivity, String... sourceFiles) {
    super(trueNativeActivity);
    mTrueNativeActivity = trueNativeActivity;
    mSMRuntime = new SMRuntime(trueNativeActivity, sourceFiles);

    writeJavascript(
        "window.onBodyLoad();"
        + " require('cordova/channel').onNativeReady.fire();"
        + " _nativeReady = true;");
  }

  public String writeJavascript(final String code) {
    //System.out.println("javascript: "+code);

    final StringBuilder result = new StringBuilder();

    mTrueNativeActivity.runOnUiThreadAndWait(new Runnable() {
      public void run() {
        result.append(mSMRuntime.writeJavascript(code));
      }
    });

    return result.toString();
  }

  @Override
  public void loadUrl(String url) {
    if (!mLoaded) {
      mLoaded = true;
      mTrueNativeActivity.resetLoadUrlTimeout();
      writeJavascript("document.dispatchEvent({'type': 'DOMContentLoaded'})");
    }
  }
}
