package org.freeciv.common;

/**
 * The AI
 */
public final class AI 
{

  private boolean m_isControlled = false;
  private int m_techGoal;
  private boolean m_isBarbarian = false;

  // Player usually instantiates this class
  AI()
  {
  }

  public void setControlled(boolean controlled)
  {
    m_isControlled = controlled;
  }

  public boolean isControlled()
  {
    return m_isControlled;
  }

  public void setTechGoal(int techGoal)
  {
    m_techGoal = techGoal;
  }

  public void setTechGoal(Advance techGoal)
  {
    m_techGoal = techGoal.getId();
  }

  // Hmm, this should return Advance.
  public int getTechGoal()
  {
    return m_techGoal;
  }

  public boolean isBarbarian()
  {
    return m_isBarbarian;
  }

  public void setBarbarian(boolean isBarb)
  {
    m_isBarbarian = isBarb;
  }
}