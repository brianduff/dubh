package org.freeciv.common;

/**
 * Unit AI
 */
public class UnitAI 
{
  // unit.h

  private boolean m_isControlled;
  private int m_aiRole;
  private int m_ferryBoat;
  private int m_passenger;
  private int m_bodyguard;
  private int m_charge;
  
  UnitAI()
  {
  }

  public void setControlled(boolean isControlled)
  {
    m_isControlled = isControlled;
  }

  public boolean isControlled()
  {
    return m_isControlled;
  }
}