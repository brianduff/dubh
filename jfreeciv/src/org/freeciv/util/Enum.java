package org.freeciv.util;
public class Enum
{
  private static int m_enum_iter_val;
  public static int start()
  {
    return start( 0 );
  }
  public static int start( int i )
  {
    m_enum_iter_val = i + 1;
    return i;
  }
  public static int get()
  {
    int theVal = m_enum_iter_val;
    m_enum_iter_val++;
    return theVal;
  }
}
