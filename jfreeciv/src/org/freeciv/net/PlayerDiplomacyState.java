package org.freeciv.net;

import org.freeciv.client.Constants;
import org.freeciv.client.Enum;

public class PlayerDiplomacyState implements Constants
{
  public int DS_NEUTRAL = Enum.start();
  public int DS_WAR = Enum.get();
  public int DS_CEASEFIRE = Enum.get();
  public int DS_PEACE = Enum.get();
  public int DS_ALLIANCE = Enum.get();
  public int DS_NO_CONTACT = Enum.get();
  public int DS_LAST = Enum.get();      /* leave this last */

  private int m_type;
  private int m_turnsLeft;
  private int m_reasonToCancel;

  public int getType()
  {
    return m_type;
  }

  public void setType(int i)
  {
    m_type = i;
  }

  public int getTurnsLeft()
  {
    return m_turnsLeft;
  }

  public void setTurnsLeft(int turnsLeft)
  {
    m_turnsLeft = turnsLeft;
  }

  public void setReasonToCancel(int reasonToCancel)
  {
    m_reasonToCancel = reasonToCancel;
  }

  public int getReasonToCancel()
  {
    return m_reasonToCancel;
  }

}