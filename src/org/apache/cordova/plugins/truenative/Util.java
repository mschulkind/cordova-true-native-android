package org.apache.cordova.plugins.truenative;

import android.graphics.Color;

public class Util {
  public static int parseColor(String colorString) {
    String s = colorString;

    // Assume it is in '#123' format if the length is 4 and transform it to a
    // format that Color.parseColor() understands.
    if (colorString.length() == 4) {
      s = "" + s.charAt(0) 
        + s.charAt(1) + s.charAt(1)
        + s.charAt(2) + s.charAt(2)
        + s.charAt(3) + s.charAt(3);
    }

    return Color.parseColor(s);
  } 
}
