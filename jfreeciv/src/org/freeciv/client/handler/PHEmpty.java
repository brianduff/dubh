package org.freeciv.client.handler;
import org.freeciv.client.Client;
import org.freeciv.net.Packet;
/**
 * This interface is implemented by objects that handle packets received
 * from the server.
 */
public class PHEmpty implements ClientPacketHandler
{
  /**
   * Return the class name of the packet that this
   * handler needs
   */
  public String getPacketClass()
  {
    return null;
  }
  /**
   * Your handler should implement this method to actually
   * handle an incoming packet.
   */
  public void handle( Client c, Packet pkt )
  {
    
  }
}
