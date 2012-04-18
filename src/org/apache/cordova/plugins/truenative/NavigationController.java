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

  public void push(WindowComponent window) {
    if (mOpened) {
      Context parentContext = mWindowStack.peek().activity;
      window.open(parentContext);
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

    Context nextParentContext = parentContext;
    for (WindowComponent window : mWindowStack) {
      window.open(nextParentContext);
      nextParentContext = window.activity;
    }
  }
}

