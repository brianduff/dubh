package org.freeciv.common;

import org.freeciv.net.PktRulesetGovernmentRulerTitle;

/**
 * The title of a ruler
 */
public final class RulerTitle 
{

  private Factories m_factories;
  private PktRulesetGovernmentRulerTitle m_packet;
  //private Nation m_nation;
  private Government m_gov;
  

  /**
   * Construct a ruler title. RulerTitles are usually constructed by the
   * createRulerTitle() method on Government
   */ 
  RulerTitle(Factories f, Government g)
  {
    m_factories = f;
    m_gov = g;
  }

  void initFromPacket(PktRulesetGovernmentRulerTitle prgrt)
  {
    m_packet = prgrt;
  }

  public String getMaleTitle()
  {
    return m_packet.male_title;
  }

  public String getFemaleTitle()
  {
    return m_packet.female_title;
  }

  //public Nation getNation()
  //{
    /*if (m_nation == null)
    {
      m_nation = (Nation) m_factories.getNationFactory().findById(
        m_packet.nation
      );
    }*/
    //return m_nation;
    //return null;
  //}

  public int getId()
  {
    return m_packet.id;
  }

  public Government getGovernment()
  {
    return m_gov;
  }
  
}