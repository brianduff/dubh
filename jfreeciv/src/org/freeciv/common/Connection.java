package org.freeciv.common;

import org.freeciv.net.Packet;
import org.freeciv.net.PktConnInfo;

/**
 * Represents a connection to a freeciv server
 */
public class Connection implements GameObject
{
  // struct connection in connnection.h

  // sock

  // private int first_packet?
  // private int byte_swap?

  // buffer
  // send_buffer
  // last_write
  // pong?

  // map_position route
  // route_length

  private PktConnInfo m_packet;
  private Player m_player = null;

  private GameObjectFactory m_connFactory;
  
  /**
   * Only the factory instantiates these objects
   */
  Connection( GameObjectFactory connFactory ) 
  {
    m_connFactory = connFactory;
  }

  public void initFromPacket(Packet p)
  {
    m_packet = (PktConnInfo)p;
  }
  
  public int getId()
  {
    return m_packet.id;
  }


  public boolean isEstablished()
  {
    return m_packet.established;
  }

  public boolean isObserver()
  {
    return m_packet.observer;
  }
  
  public int getAccessLevel()
  {
    return m_packet.access_level;
  }

  public Player getPlayer()
  {
    if (m_player == null)
    {
      m_player = (Player) m_connFactory.getParent().getPlayerFactory().findById(
        m_packet.player_num
      );
    }
    return m_player;
  }

  public String getName()
  {
    return m_packet.name;
  }

  public String toString()
  {
    return getName();
  }
  public String getAddress()
  {
    return m_packet.addr;
  }
  public String getCapability()
  {
    return m_packet.capability;
  }
}
