package org.freeciv.client.handler;
import org.freeciv.client.*;
import org.freeciv.client.dialog.*;
import org.freeciv.net.*;
import javax.swing.JOptionPane;
/**
 * Handles "ping" packets". Simply sends back a "pong" packet.
 */
public class PHConnPing implements ClientPacketHandler,Constants
{
  public String getPacketClass()
  {
    return "org.freeciv.net.PktGenericEmpty";
  }
  public void handle( Client c, Packet pkt )
  {
    // bounce back a pong
    c.sendToServer( new PktGenericEmpty( PACKET_CONN_PONG ) );
  }
}
