package org.freeciv.client;

import java.awt.Color;

/**
 * Colors for the client
 */
class Colors 
{

  private static final int COLOR_STD_BLACK = Enum.start(0);
  private static final int COLOR_STD_WHITE = Enum.get();
  private static final int COLOR_STD_RED = Enum.get();
  private static final int COLOR_STD_YELLOW = Enum.get();
  private static final int COLOR_STD_CYAN = Enum.get();
  private static final int COLOR_STD_GROUND = Enum.get();
  private static final int COLOR_STD_OCEAN = Enum.get();
  private static final int COLOR_STD_BACKGROUND = Enum.get();
  private static final int COLOR_STD_RACE0 = Enum.get();
  private static final int COLOR_STD_RACE1 = Enum.get();
  private static final int COLOR_STD_RACE2 = Enum.get();
  private static final int COLOR_STD_RACE3 = Enum.get();
  private static final int COLOR_STD_RACE4 = Enum.get();
  private static final int COLOR_STD_RACE5 = Enum.get();
  private static final int COLOR_STD_RACE6 = Enum.get();
  private static final int COLOR_STD_RACE7 = Enum.get();
  private static final int COLOR_STD_RACE8 = Enum.get();
  private static final int COLOR_STD_RACE9 = Enum.get();
  private static final int COLOR_STD_RACE10 = Enum.get();
  private static final int COLOR_STD_RACE11 = Enum.get();
  private static final int COLOR_STD_RACE12 = Enum.get();
  private static final int COLOR_STD_RACE13 = Enum.get();
    

  private static final Color[] STANDARD_COLORS = new Color[] 
  {
    Color.black,
    Color.white,
    Color.red,
    Color.yellow,
    Color.cyan,
    new Color(0, 200, 0),
    new Color(0, 0, 200),
    new Color(86, 86, 86),  // control system color?
    new Color(128, 0, 0),
    new Color(128, 255, 255),
    Color.red,
    new Color(255, 0, 128),
    new Color(0, 0, 128),
    new Color(255, 0, 255),
    new Color(255, 128, 0),
    new Color(255, 255, 128),
    new Color(255, 128, 128),
    Color.blue,
    Color.green,
    new Color(0, 128, 128),
    new Color(0, 64, 64),
    new Color(198, 198, 198)
  };

  public static final Color getStandardColor( int colIdx )
  {
    return STANDARD_COLORS[ colIdx ];
  }
}