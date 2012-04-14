package org.apache.cordova.plugins.truenative;

import android.webkit.WebView;
import org.apache.cordova.DroidGap;

import java.util.concurrent.locks.*;

import static junit.framework.Assert.*;

public class SMWebView extends WebView {
  private SMRuntime mSMRuntime;
  private DroidGap mDroidGap;

  public SMWebView(DroidGap droidGap, String... sourceFiles) {
    super(droidGap);
    mDroidGap = droidGap;
    mSMRuntime = new SMRuntime(droidGap, sourceFiles);

    writeJavascript(
        "window.onBodyLoad(); "
        + "require('cordova/channel').onNativeReady.fire();"
        + "_nativeReady = true;");
  }

  private class WJRunnable implements Runnable {
    public String result;
    private SMRuntime mSMRuntime;
    private String mCode;
    final public Lock lock = new ReentrantLock();
    final public Condition hasResult = lock.newCondition();

    public WJRunnable(SMRuntime smRuntime, String code) {
      mSMRuntime = smRuntime;
      mCode = code;
    }

    public void run() {
      lock.lock();
      result = mSMRuntime.writeJavascript(mCode);
      hasResult.signal();
      lock.unlock();
    }
  }

  public String writeJavascript(String code) {
    //System.out.println("javascript: "+code);

    WJRunnable runnable = new WJRunnable(mSMRuntime, code);
    runnable.lock.lock();
    mDroidGap.runOnUiThread(runnable);
    try {
      while (runnable.result == null) runnable.hasResult.await();
    } catch(InterruptedException e) {
      e.printStackTrace();
      fail("failed waiting for a result from writeJavascript");
    }
    runnable.lock.unlock();

    return runnable.result;
  }

  @Override
  public void loadUrl(String url) {
    mDroidGap.resetLoadUrlTimeout();
    writeJavascript("document.dispatchEvent({'type': 'DOMContentLoaded'})");
  }
}
