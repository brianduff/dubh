package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.net.AbstractPacket;
/**
 * This interface is implemented by objects that handle packets received
 * from the server.
 */
abstract class PHGenericValues implements ClientPacketHandler
{
  /**
   * Return the class name of the packet that this
   * handler needs
   */
  public String getPacketClass()
  {
    return "org.freeciv.net.PktGenericValues";
  }
  /**
   * Your handler should implement this method to actually
   * handle an incoming packet.
   */
  public abstract void handle(Client c, AbstractPacket pkt);
}