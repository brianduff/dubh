package org.freeciv.common;

import org.freeciv.net.Packet;

/**
 * Marker interface for any object that is manipulated by the client and
 * server.
 * 
 */
public interface GameObject
{
  public int getId();

  /**
   * You can do nothing
   */
  public void initFromPacket(Packet p);
}
