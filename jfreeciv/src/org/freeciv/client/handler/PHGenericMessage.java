package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.net.Packet;
import org.freeciv.net.PktGenericMessage;

/**
 * This interface is implemented by objects that handle packets received
 * from the server.
 */
abstract class PHGenericMessage implements ClientPacketHandler
{
  /**
   * Return the class name of the packet that this
   * handler needs
   */
  public Class getPacketClass()
  {
    return PktGenericMessage.class;
  }
  /**
   * Your handler should implement this method to actually
   * handle an incoming packet.
   */
  public abstract void handle( Client c, Packet pkt );
}
