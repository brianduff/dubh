package org.freeciv.client.handler;


import org.freeciv.client.Client;
import org.freeciv.common.Government;
import org.freeciv.common.RulerTitle;
import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetGovernmentRulerTitle;

/**
 * Ruleset government ruler title packet handler
 */
public class PHRulesetGovernmentRulerTitle implements ClientPacketHandler
{
  public String getPacketClass()
  {
    return "org.freeciv.net.PktRulesetGovernmentRulerTitle";
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
