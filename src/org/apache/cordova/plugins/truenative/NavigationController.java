package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.content.Intent;

import java.util.*;

import static junit.framework.Assert.*;

public class NavigationController extends WindowComponent {
  private boolean mOpened = false;
  private NavigationControllerPlugin mPlugin;
  protected Stack<WindowComponent> mWindowStack = new Stack<WindowComponent>();

  public NavigationController(NavigationControllerPlugin plugin) {
    super(plugin);
  }

  private void openWindow(final WindowComponent window, Context parentContext) {
    window.onCreateListener = new Runnable() {
      public void run() {
        // Open the next window if any have been pushed after this window.
        int pos = mWindowStack.search(window);
        if (pos != 1) {
          openWindow(
              mWindowStack.get(mWindowStack.size() - pos + 1),
              window.activity);
        }
      }
    };
    window.open(parentContext);
  }

  public void push(WindowComponent window) {
    if (mOpened) {
      Context parentContext = mWindowStack.peek().activity;

      // If the top of the stack does not yet have an activity, it means all of
      // the windows on the stack have not yet fully opened. We only need to
      // push this new window onto the stack and it will get opened after the
      // previous is opened.
      if (parentContext != null) {
        openWindow(window, parentContext);
      }
    }

    mWindowStack.push(window);
  }

  public void pop() {
    if (mOpened) {
      mWindowStack.peek().close();
    }

    mWindowStack.pop();
  }

  @Override
  public void open(Context parentContext) {
    assertFalse(
        "Navigation controller must have at least one window"
        + " before being opened", 
        mWindowStack.empty());

    assertFalse(mOpened);
    mOpened = true;

    openWindow(mWindowStack.get(0), parentContext);
  }
}

