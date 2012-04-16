package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import static junit.framework.Assert.*;

public class ViewSubclass extends ViewGroup {
  private ViewPlugin mPlugin;

  public ViewSubclass(Context context, ViewPlugin plugin) { 
    super(context); 
    mPlugin = plugin;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    mPlugin.onViewSizeChanged(this, w, h, oldw, oldh);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    measureChildren(widthMeasureSpec, heightMeasureSpec);

    int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
    int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

    ViewPlugin.ViewData data = 
      (ViewPlugin.ViewData)mPlugin.getComponentData(this);
    assertNotNull(data);
    ViewSubclass.LayoutParams params = data.layoutParams;

    int width = params.width;
    int height = params.height;

    if (width == ViewGroup.LayoutParams.MATCH_PARENT) {
      width = parentWidth;
    }
    if (height == ViewGroup.LayoutParams.MATCH_PARENT) {
      height = parentHeight;
    }

    setMeasuredDimension(width, height);
  }
  
  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int count = getChildCount();
    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);

      ViewPlugin.ViewData data = 
        (ViewPlugin.ViewData)mPlugin.getComponentData(this);
      assertNotNull(data);
      ViewSubclass.LayoutParams params = data.layoutParams;

      int parentWidth = r - l;
      int parentHeight = b - t;

      int width = params.width;
      int height = params.height;

      if (width == ViewGroup.LayoutParams.MATCH_PARENT) {
        width = parentWidth;
      }
      if (height == ViewGroup.LayoutParams.MATCH_PARENT) {
        height = parentHeight;
      }

      child.layout(params.x, params.y, params.x + width, params.y + height);
    }
  }

  public static class LayoutParams extends ViewGroup.LayoutParams {
    public int x;
    public int y;

    public LayoutParams(int width, int height, int x, int y) {
      super(width, height);
      this.x = x;
      this.y = y;
    }
  }
}
