package org.apache.cordova.plugins.truenative;

import android.graphics.Color;

public class Util {
  public static int parseColor(String colorString) {
    String s = colorString;

    // Transform '#123' format to the full '#112233' format for
    // Color.parseColor(). 
    if (colorString.length() == 4 && colorString.charAt(0) == '#') {
      s = "" + s.charAt(0) 
        + s.charAt(1) + s.charAt(1)
        + s.charAt(2) + s.charAt(2)
        + s.charAt(3) + s.charAt(3);
    }

    return Color.parseColor(s);
  } 
}
