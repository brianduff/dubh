package org.freeciv.client.handler;


import org.freeciv.client.Client;
import org.freeciv.common.Government;
import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetGovernmentRulerTitle;

/**
 * Ruleset government ruler title packet handler
 */
public class PHRulesetGovernmentRulerTitle implements ClientPacketHandler
{
  public Class getPacketClass()
  {
    return PktRulesetGovernmentRulerTitle.class;
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    PktRulesetGovernmentRulerTitle prt = (PktRulesetGovernmentRulerTitle)pkt;
    int govId = prt.gov;
    Government g = 
      (Government) c.getFactories().getGovernmentFactory().findById(govId);

    g.createRulerTitle(prt);
  }
}
