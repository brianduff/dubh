package org.freeciv.client.handler;

import org.freeciv.client.Client;

import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetTerrainControl;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetTerrainControl implements ClientPacketHandler
{
  public Class getPacketClass()
  {
    return PktRulesetTerrainControl.class;
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    PktRulesetTerrainControl prt = (PktRulesetTerrainControl)pkt;
    c.getGame().getTerrainRules().initFromPacket(prt);
  }
}
