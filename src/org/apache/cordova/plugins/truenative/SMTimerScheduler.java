package org.apache.cordova.plugins.truenative;

import org.apache.cordova.DroidGap;

import java.util.*;
import java.util.concurrent.*;

public class SMTimerScheduler {
  private ScheduledThreadPoolExecutor executor = 
    new ScheduledThreadPoolExecutor(1);

  private HashMap<Long, ScheduledFuture> timerMap = 
    new HashMap<Long, ScheduledFuture>();

  private DroidGap mDroidGap;

  public SMTimerScheduler(DroidGap droidGap) {
    mDroidGap = droidGap;
  }

  public void registerTimer(
      int msecs, boolean repeats, final long timerPrivate) {
    Runnable fireRunnable = 
      new Runnable() {
        public void run() {
          mDroidGap.runOnUiThread(new Runnable() {
            public void run() {
              fireTimer(timerPrivate);
            }
          });
        }
      };

    if (repeats) {
      executor.scheduleAtFixedRate(
          fireRunnable, msecs, msecs, TimeUnit.MILLISECONDS);
    } else {
      executor.schedule(fireRunnable, msecs, TimeUnit.MILLISECONDS);
    }
  }

  public void clearTimer(long timerPrivate) {
    ScheduledFuture future = timerMap.get(new Long(timerPrivate));
    future.cancel(true);
  }

  public static native void fireTimer(long timerPrivate);
}
