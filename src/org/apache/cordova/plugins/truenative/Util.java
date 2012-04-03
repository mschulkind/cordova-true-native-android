package org.apache.cordova.plugins.truenative;

import android.graphics.Color;

import java.util.*;

import static junit.framework.Assert.*;

public class Util {
  private static final Map<String, String> colorMap;
  static {
      Map<String, String> map = new HashMap<String, String>();
      map.put("clear", "#00000000");
      colorMap = Collections.unmodifiableMap(map);
  }

  public static int parseColor(String colorString) {
    String s = colorString;

    // Map any custom colors.
    String mappedColor = colorMap.get(s);
    if (mappedColor != null) {
      s = mappedColor;
    }

    // Transform '#123' format to the full '#112233' format for
    // Color.parseColor(). 
    if (colorString.length() == 4 && colorString.charAt(0) == '#') {
      s = "" + s.charAt(0) 
        + s.charAt(1) + s.charAt(1)
        + s.charAt(2) + s.charAt(2)
        + s.charAt(3) + s.charAt(3);
    }

    try {
      return Color.parseColor(s);
    } catch (IllegalArgumentException e) {
      fail("Unknown color '"+colorString+"'");
    }
    return 0;
  } 
}
