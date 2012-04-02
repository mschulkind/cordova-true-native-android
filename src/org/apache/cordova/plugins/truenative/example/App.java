package org.apache.cordova.plugins.truenative.example;

import android.os.Bundle;
import android.os.Process;
import android.provider.Settings.Secure;
import org.apache.cordova.*;

public class App extends DroidGap
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    super.loadUrl("file:///android_asset/www/index.html");
  }
}
