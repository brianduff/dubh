package org.freeciv.client;

/**
 * Represents a connection to a freeciv server
 */
public class Connection implements ClientObject
{
  // struct connection in connnection.h

  private int id;
  // sock
  private boolean isUsed;
  private boolean isEstablished;
  // private int first_packet?
  // private int byte_swap?
  private Player player;

  private boolean isObserver;

  // buffer
  // send_buffer
  // last_write
  // pong?

  private String name;
  private String address;
  private String capability;
  public int accessLevel;
  // map_position route
  // route_length
  
  /**
   * Only the factory instantiates these objects
   */
  Connection()
  {
    isUsed = true;
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public void setEstablished(boolean isEstablished)
  {
    this.isEstablished = isEstablished;
  }

  public boolean isEstablished()
  {
    return isEstablished;
  }

  public void setObserver(boolean isObserver)
  {
    this.isObserver = isObserver;
  }

  public boolean isObserver()
  {
    return isObserver;
  }

  public int getAccessLevel()
  {
    return accessLevel;
  }

  public void setAccessLevel(int accessLevel)
  {
    this.accessLevel = accessLevel;
  }

  public Player getPlayer()
  {
    return player;
  }

  public void setPlayer(Player p)
  {
    player = p;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public String getAddress()
  {
    return address;
  }

  public void setCapability(String capability)
  {
    this.capability = capability;
  }

  public String getCapability()
  {
    return capability;
  }
}