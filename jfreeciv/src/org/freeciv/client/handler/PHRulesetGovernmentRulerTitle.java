package org.freeciv.client.handler;


import org.freeciv.client.*;
import org.freeciv.net.*;

import javax.swing.JOptionPane;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetGovernmentRulerTitle implements ClientPacketHandler
{

  public String getPacketClass()
  {
    return "org.freeciv.net.PktRulesetGovernmentRulerTitle";
  }

  /**
   */
  public void handle(Client c, AbstractPacket pkt)
  {
    PktRulesetGovernmentRulerTitle prt =
      (PktRulesetGovernmentRulerTitle)pkt;

    int govId = prt.gov;
    PktRulesetGovernment gov = c.getRulesetManager().getRulesetGovernment(govId);
    gov.ruler_titles[prt.id] = prt;
  }
}