package org.freeciv.common;

import org.freeciv.net.Packet;
import org.freeciv.net.PktSpaceshipInfo;

/**
 * A spaceship.
 */
public final class Spaceship 
{
  private PktSpaceshipInfo m_ssinfo;

  /**
   * Normally, Spaceships are constructed by Player
   */
  Spaceship()
  {
  }

  public void initFromPacket(Packet p)
  {
    m_ssinfo = (PktSpaceshipInfo) p;
  }

  
}