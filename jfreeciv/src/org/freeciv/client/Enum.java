package org.freeciv.client;

public class Enum
{
  private static int m_enum_iter_val;

  static int start()
  {
    return start(0);
  }
  static int start(int i)
  {
    m_enum_iter_val = i+1;
    return i;
  }
  static int get()
  {
    int theVal = m_enum_iter_val;
    m_enum_iter_val++;
    return theVal;
  }
}