package org.freeciv.client;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class representing a player in Freeciv. You can't directly instantiate this
 * class: instances of this object are
 * created and retrieved using the player factory on Client: use
 * Client.getPlayerFactory().
 */
public class Player implements ClientObject
{

  private int playerNumber;
  private String name;
  private String username;
  private boolean isMale;
  private int government;
  private int nation;
  private boolean isTurnDone;
  private int nTurnsIdle;
  private boolean isAlive;
  private boolean gotTech;
  private int revolution;
  private int capital;
  private int embassy;
  private int reputation;
  // private int[] diplomacyStates;
  private int cityStyle;
  // private Collection units;
  // private Collection cities;
  // private Score score;
  // private PlayerEconomic economic;
  // private PlayerResearch research;
  // private PlayerSpaceship spaceship;
  private int futureTech;
  // private AI ai;
  private boolean isConnected;
  private Connection connection;
  private Collection connections;
  // private Worklist[] worklists;
  // private PlayerTile[] privateMap;
  private int givesSharedVision;
  private int reallyGivesSharedVision;

  /**
   * The constructor is package protected: only the player factory instantiates
   * players
   */
  Player()
  {
    connections = new ArrayList();

    // TODO: other defaults? (player.c)
  }

  public int getId()
  {
    return getPlayerNumber();
  }

  public int getPlayerNumber()
  {
    return playerNumber;
  }

  public void setPlayerNumber(int number)
  {
    playerNumber = number;
  }

  /**
   * Get the connections for this player
   *
   * @return a collection of connections
   */
  public Collection getConnections()
  {
    return connections;
  }

  
}