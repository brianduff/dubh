package org.freeciv.common;

import org.freeciv.util.Enum;


public final class DiplomacyState 
{

  public int DS_NEUTRAL = Enum.start();
  public int DS_WAR = Enum.get();
  public int DS_CEASEFIRE = Enum.get();
  public int DS_PEACE = Enum.get();
  public int DS_ALLIANCE = Enum.get();
  public int DS_NO_CONTACT = Enum.get();
  public int DS_LAST = Enum.get(); /* leave this last */
  private int m_type;
  private int m_turnsLeft;
  private boolean m_reasonToCancel;


  public DiplomacyState()
  {
    m_type = DS_NEUTRAL;
  }
  
  public int getType()
  {
    return m_type;
  }
  
  public void setType( int i )
  {
    m_type = i;
  }
  
  public int getTurnsLeft()
  {
    return m_turnsLeft;
  }
  
  public void setTurnsLeft( int turnsLeft )
  {
    m_turnsLeft = turnsLeft;
  }
  
  public void setReasonToCancel( boolean reasonToCancel )
  {
    m_reasonToCancel = reasonToCancel;
  }
  public boolean hasReasonToCancel()
  {
    return m_reasonToCancel;
  }
  /**
   * Return a diplomatic state as a human-readable string
   * 
   * player.c:diplstate_text()
   */
  public String getName()
  {
    final String[] ds_names = { "Neutral", "War", "Cease-fire",
                              "Peace", "Alliance", "No Contact" };
    return ds_names[ getType() ];
  }
}