package org.freeciv.client.handler;

import org.freeciv.client.handler.ClientPacketHandler;
import org.freeciv.client.Client;
import org.freeciv.net.Packet;

/**
 * Packet handler for spaceship_info packets.
 */
final class PHSpaceshipInfo implements ClientPacketHandler
{

  public String getPacketClass()
  {
    return "org.freeciv.net.PktSpaceshipInfo";
  }
  
  /**
   */
  public void handle( final Client c, final Packet pkt )
  {
    
  }
}