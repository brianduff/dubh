package org.freeciv.client.handler;

import org.freeciv.client.*;
import org.freeciv.net.*;

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
  public void handle( Client c, Packet pkt )
  {
    PktRulesetTerrainControl prt = (PktRulesetTerrainControl)pkt;
    c.getGame().getTerrainRules().initFromPacket(prt);
  }
}
