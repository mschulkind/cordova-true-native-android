package org.apache.cordova.plugins.truenative;

import android.os.Bundle;
import org.apache.cordova.*;
import org.apache.cordova.plugins.truenative.SMWebView;

import java.util.concurrent.locks.*;

import static junit.framework.Assert.*;

public class TrueNativeActivity extends DroidGap {
  public void onCreate(Bundle savedInstanceState, String... sourceFiles)
  {
    super.onCreate(savedInstanceState);
    appView = new SMWebView(this, sourceFiles);
    super.loadUrl("file:///android_asset/www/index.html");
  }

  @Override
  public void sendJavascript(String statement) {
    ((SMWebView)appView).writeJavascript(statement);
  }

  private class BooleanWrapper {
    public boolean bool;
    BooleanWrapper(boolean b) {
      bool = b;
    }
  }

  public void runOnUiThreadAndWait(final Runnable runnable) {
    final Lock lock = new ReentrantLock();
    final Condition runnableDoneCond = lock.newCondition();
    final BooleanWrapper doneBool = new BooleanWrapper(false);

    lock.lock();

    runOnUiThread(new Runnable() {
      public void run() {
        lock.lock();

        runnable.run();

        doneBool.bool = true;
        runnableDoneCond.signal();
        lock.unlock();
      }
    });

    try {
      while (doneBool.bool == false) { runnableDoneCond.await(); }
    } catch(InterruptedException e) {
      e.printStackTrace();
      fail("failed waiting for completion in runOnUiThreadAndWait()");
    }
    lock.unlock();
  }
}
