package org.freeciv.client.handler;


import org.freeciv.client.*;
import org.freeciv.net.*;

import javax.swing.JOptionPane;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetTerrainControl implements ClientPacketHandler
{

  public String getPacketClass()
  {
    return "org.freeciv.net.PktRulesetTerrainControl";
  }

  /**
   */
  public void handle(Client c, Packet pkt)
  {
    PktRulesetTerrainControl prt = (PktRulesetTerrainControl)pkt;

    c.getRulesetManager().setRulesetTerrainControl(prt);
  }
}