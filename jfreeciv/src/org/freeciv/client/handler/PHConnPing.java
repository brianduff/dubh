package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.net.Packet;
import org.freeciv.net.PktGenericEmpty;


/**
 * Handles "ping" packets". Simply sends back a "pong" packet.
 */
public class PHConnPing implements ClientPacketHandler,Constants
{

  public Class getPacketClass()
  {
    return PktGenericEmpty.class;
  }
  
  public void handle( Client c, Packet pkt )
  {
    // bounce back a pong
    c.sendToServer( new PktGenericEmpty( PACKET_CONN_PONG ) );
  }
}
