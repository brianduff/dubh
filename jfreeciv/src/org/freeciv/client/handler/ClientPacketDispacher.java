package org.freeciv.client.handler;

import org.freeciv.net.AbstractPacket;
import org.freeciv.net.InStream;
import org.freeciv.client.Constants;
import org.freeciv.client.Client;

import org.freeciv.common.Logger;

public class ClientPacketDispacher
{

   // Incidentally, the initial protocol in 1.10.0 is:

   // --->REQUEST_JOIN_GAME
   // JOIN_GAME_REPLY
   // RULESET_CONTROL
   // *RULESET_TECH
   // *RULESET_GOVERNMENT / *RULESET_GOVERNMENT_RULER_TITLE
   // *RULESET_UNIT
   // *RULESET_BUILDING
   // RULESET_TERRAIN_CONTROL
   // *RULESET_TERRAIN
   // *RULESET_NATION
   // *RULESET_CITY
   // SELECT_NATION
   // --->ALLOC_NATION
   // ?*SELECT_NATION

   // GAME_INFO
   // MAP_INFO
   // *PLAYER_INFO
   // SPACESHIP_INFO
   // *TILE_INFO

   //

  /**
   * This array must have a one to one correspondence with
   * the packet number. It contains instances of all handlers
   * N.b. Your handlers MUST NOT contain any state, because
   * they are reused.
   */
  private static final ClientPacketHandler[] PACKET_HANDLERS = {
    null,  // REQUEST_JOIN_GAME
    new PHJoinGameReply(),     // JOIN_GAME_REPLY
    new PHServerShutdown(),  // SERVER_SHUTDOWN
    null,  // UNIT_INFO
    null,  // MOVE_UNIT
    null,  // TURN_DONE
    null,  // NEW_YEAR
    new PHTileInfo(),  // TILE_INFO
    new PHSelectNation(),  // SELECT_NATION
    null,  // ALLOC_NATION
    null,  // SHOW_MESSAGE
    new PHPlayerInfo(),  // PLAYER_INFO
    new PHGameInfo(),  // GAME_INFO
    new PHMapInfo(),  // MAP_INFO
    new PHChatMsg(),  // CHAT_MESSAGE
    null, //CITY_INFO 15
    null, //CITY_SELL
    null, //CITY_BUY
    null, //CITY_CHANGE
    null, //CITY_WORKLIST
    null, //CITY_MAKE_SPECIALIST
    null, //CITY_MAKE_WORKER
    null, //CITY_CHANGE_SPECIALIST
    null, //CITY_RENAME
    null, //PLAYER_RATES
    null, //PLAYER_REVOLUTION
    null, //PLAYER_GOVERNMENT
    null, //PLAYER_RESEARCH
    null, //PLAYER_WORKLIST
    null, //UNIT_BUILD_CITY
    null, //UNIT_DISBAND 30
    null, //REMOVE_UNIT
    null, //REMOVE_CITY
    null, //UNIT_CHANGE_HOMECITY
    null, //UNIT_COMBAT
    null, //UNIT_ESTABLISH_TRADE
    null, //UNIT_HELP_BUILD_WONDER
    null, //UNIT_GOTO_TILE
    null, //GAME_STATE
    null, //NUKE_TILE
    null, //DIPLOMAT_ACTION
    null, //PAGE_MSG
    null, //REPORT_REQUEST
    null, //DIPLOMACY_INIT_MEETING
    null, //DIPLOMACY_CREATE_CLAUSE
    null, //DIPLOMACY_REMOVE_CLAUSE 45
    null, //DIPLOMACY_CANCEL_MEETING
    null, //DIPLOMACY_ACCEPT_TREATY
    null, //DIPLOMACY_SIGN_TREATY
    null, //UNIT_AUTO
    null, //BEFORE_NEW_YEAR
    null, //REMOVE_PLAYER
    null, //UNITTYPE_UPGRADE
    null, //UNIT_UNLOAD
    null, //PLAYER_TECH_GOAL
    null, //CITY_REFRESH
    null, //INCITE_INQ
    null, //INCITE_COST
    null, //UNIT_UPGRADE
    new PHRulesetTech(), //RULESET_TECH
    new PHRulesetUnit(), //RULESET_UNIT              60
    new PHRulesetBuilding(), //RULESET_BUILDING
    null, //CITY_OPTIONS
    null, //SPACESHIP_INFO
    null, //SPACESHIP_ACTION
    null, //UNIT_NUKE
    new PHRulesetTerrain(), //RULESET_TERRAIN
    new PHRulesetTerrainControl(), //RULESET_TERRAIN_CONTROL
    new PHRulesetGovernment(), //RULESET_GOVERNMENT
    new PHRulesetGovernmentRulerTitle(), //RULESET_GOVERNMENT_RULER_TITLE
    new PHRulesetControl(), //RULESET_CONTROL
    null, //CITY_NAME_SUGGEST_REQ
    null, //CITY_NAME_SUGGESTION
    new PHRulesetNation(), //RULESET_NATION
    null, //UNIT_PARADROP_TO
    new PHRulesetCity(), //RULESET_CITY                75
    null //UNIT_CONNECT
  };

  private Client m_client;

  public ClientPacketDispacher(Client c)
  {
    m_client = c;
  }

  public void dispach(InStream in)
  {
    int packetType = in.getInputPacketType();
    ClientPacketHandler ph = getHandlerFor(packetType);
    AbstractPacket pck = createPacketFor(ph, in);
    ph.handle(m_client, pck);
  }

  /**
   * Gets a handler for the specified packet type
   */
  public ClientPacketHandler getHandlerFor(int packetType)
  {
    ClientPacketHandler cph;
    try
    {
      cph = PACKET_HANDLERS[packetType];
      if (cph == null)
      {
        cph = new PHEmpty();
        Logger.log(Logger.LOG_NORMAL, "Unhandled packet type: "+packetType);
      }
    }
    catch (ArrayIndexOutOfBoundsException aioobe)
    {
      cph = new PHEmpty();
      Logger.log(Logger.LOG_NORMAL, "Unrecognized packet type: "+packetType);
    }
    return cph;
  }

  /**
   * Instantiates and returns a new packet for the specified
   * packet handler.
   */
  public AbstractPacket createPacketFor(ClientPacketHandler cph, InStream in)
  {
    try
    {
      String packetClassName = cph.getPacketClass();
      if (packetClassName == null)
      {
        // Make sure we consume any data
        in.consume();
        return null;
      }
      Class packetClass = Class.forName(packetClassName);
      AbstractPacket packet = (AbstractPacket)packetClass.newInstance();
      packet.receive(in);
      return packet;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new RuntimeException("Unable to create packet handler:"+e);
    }

  }
}