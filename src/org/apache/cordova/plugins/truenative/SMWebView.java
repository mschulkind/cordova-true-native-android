package org.apache.cordova.plugins.truenative;

import android.webkit.WebView;
import org.apache.cordova.DroidGap;

public class SMWebView extends WebView {
  private SMRuntime mSMRuntime;
  private DroidGap mDroidGap;

  public SMWebView(DroidGap droidGap, String... sourceFiles) {
    super(droidGap);
    mDroidGap = droidGap;
    mSMRuntime = new SMRuntime(droidGap, sourceFiles);

    writeJavascript(
        "require('cordova/channel').onNativeReady.fire(); _nativeReady = true;");
  }

  public String writeJavascript(String code) {
    System.out.println("javascript: "+code);
    return mSMRuntime.writeJavascript(code);
  }

  @Override
  public void loadUrl(String url) {
    mDroidGap.resetLoadUrlTimeout();
    writeJavascript("document.dispatchEvent({'type': 'DOMContentLoaded'})");
  }
}
