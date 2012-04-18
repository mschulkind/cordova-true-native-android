package org.apache.cordova.plugins.truenative;

import android.content.Context;
import android.widget.EditText;

public class TextFieldSubclass extends EditText {
  private TextFieldPlugin mPlugin;

  public TextFieldSubclass(Context context, TextFieldPlugin plugin) { 
    super(context); 
    mPlugin = plugin;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    mPlugin.onViewSizeChanged(this, w, h, oldw, oldh);
  }
}
