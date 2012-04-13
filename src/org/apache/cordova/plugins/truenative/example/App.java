package org.apache.cordova.plugins.truenative.example;

import android.os.Bundle;
import android.os.Process;
import android.provider.Settings.Secure;
import org.apache.cordova.*;
import org.apache.cordova.plugins.truenative.SMRuntime;

public class App extends DroidGap
{
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    SMRuntime runtime = new SMRuntime();
    System.out.println("js answer: "+runtime.writeJavascript("3+4"));
    super.onCreate(savedInstanceState);
    super.loadUrl("file:///android_asset/www/index.html");
  }

}
