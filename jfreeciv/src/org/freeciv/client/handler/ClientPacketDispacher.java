package org.freeciv.client.handler;
import org.freeciv.net.Packet;
import org.freeciv.net.InStream;

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
  private static final ClientPacketHandler[] PACKET_HANDLERS = 
  {
    null, /* REQUEST_JOIN_GAME*/ 
    new PHJoinGameReply(), /* JOIN_GAME_REPLY*/ 
    new PHServerShutdown(), /* SERVER_SHUTDOWN*/ 
    new PHUnitInfo(), /* UNIT_INFO*/ 
    null, /* MOVE_UNIT*/ 
    null, /* TURN_DONE*/ 
    new PHNewYear(), /* NEW_YEAR*/ 
    new PHTileInfo(), /* TILE_INFO*/ 
    new PHSelectNation(), /* SELECT_NATION*/ 
    null, /* ALLOC_NATION*/ 
    null, /* SHOW_MESSAGE*/ 
    new PHPlayerInfo(), /* PLAYER_INFO*/ 
    new PHGameInfo(), /* GAME_INFO*/ 
    new PHMapInfo(), /* MAP_INFO*/ 
    new PHChatMsg(), /* CHAT_MESSAGE*/
    new PHCityInfo(), /*CITY_INFO 15*/ 
    null, /*CITY_SELL*/ 
    null, /*CITY_BUY*/
    null, /*CITY_CHANGE*/ 
    null, /*CITY_WORKLIST*/ 
    null, /*CITY_MAKE_SPECIALIST*/ 
    null, /*CITY_MAKE_WORKER*/ 
    null, /*CITY_CHANGE_SPECIALIST*/ 
    null, /*CITY_RENAME*/ 
    null, /*PLAYER_RATES*/ 
    null, /*PLAYER_REVOLUTION*/ 
    null, /*PLAYER_GOVERNMENT*/ 
    null, /*PLAYER_RESEARCH*/ 
    null, /*PLAYER_WORKLIST*/ 
    null, /*UNIT_BUILD_CITY*/ 
    null, /*UNIT_DISBAND 30*/ 
    new PHRemoveUnit(), /*REMOVE_UNIT*/ 
    null, /*REMOVE_CITY*/ 
    null, /*UNIT_CHANGE_HOMECITY*/ 
    null, /*UNIT_COMBAT*/ 
    null, /*UNIT_ESTABLISH_TRADE*/ 
    null, /*UNIT_HELP_BUILD_WONDER*/ 
    null, /*UNIT_GOTO_TILE*/ 
    new PHGameState(), /*GAME_STATE*/ 
    null, /*NUKE_TILE*/ 
    null, /*DIPLOMAT_ACTION*/ 
    new PHPageMsg(), /*PAGE_MSG*/ 
    null, /*REPORT_REQUEST*/ 
    null, /*DIPLOMACY_INIT_MEETING*/ 
    null, /*DIPLOMACY_CREATE_CLAUSE*/ 
    null, /*DIPLOMACY_REMOVE_CLAUSE 45*/ 
    null, /*DIPLOMACY_CANCEL_MEETING*/ 
    null, /*DIPLOMACY_ACCEPT_TREATY*/ 
    null, /*DIPLOMACY_SIGN_TREATY*/ 
    null, /*UNIT_AUTO*/ 
    new PHBeforeNewYear(), /*BEFORE_NEW_YEAR*/ 
    null, /*REMOVE_PLAYER*/ 
    null, /*UNITTYPE_UPGRADE*/ 
    null, /*UNIT_UNLOAD*/ 
    null, /*PLAYER_TECH_GOAL*/ 
    null, /*CITY_REFRESH*/ 
    null, /*INCITE_INQ*/ 
    null, /*INCITE_COST*/ 
    null, /*UNIT_UPGRADE*/ 
    null, /*PACKET_PLAYER_CANCEL_PACT*/ 
    new PHRulesetTech(), /*RULESET_TECH*/ 
    new PHRulesetUnit(), /*RULESET_UNIT             */ 
    new PHRulesetBuilding(), /*RULESET_BUILDING*/ 
    null, /*CITY_OPTIONS*/ 
    new PHSpaceshipInfo(), /*SPACESHIP_INFO*/ 
    null, /*SPACESHIP_ACTION*/ 
    null, /*UNIT_NUKE*/ 
    new PHRulesetTerrain(), /*RULESET_TERRAIN*/ 
    new PHRulesetTerrainControl(), /*RULESET_TERRAIN_CONTROL*/ 
    new PHRulesetGovernment(), /*RULESET_GOVERNMENT*/ 
    new PHRulesetGovernmentRulerTitle(), /*RULESET_GOVERNMENT_RULER_TITLE*/ 
    new PHRulesetControl(), /*RULESET_CONTROL*/ 
    null, /*CITY_NAME_SUGGEST_REQ*/ 
    new PHCityNameSuggestion(), /*CITY_NAME_SUGGESTION*/ 
    new PHRulesetNation(), /*RULESET_NATION*/ 
    null, /*UNIT_PARADROP_TO*/ 
    new PHRulesetCity(), /*RULESET_CITY             */ 
    null, /*UNIT_CONNECT*/ 
    null, /*PACKET_SABOTAGE_LIST */ 
    null, /* PACKET_ADVANCE_FOCUS */ 
    new PHRulesetGame(), /*PACKET_RULESET_GAME*/ 
    new PHConnInfo(), /* PACKET_CONN_INFO */ 
    new PHShortCity(), /* PACKET_SHORT_CITY*/ 
    null, /* PACKET_PLAYER_REMOVE_VISION */ 
    null, /* PACKET_GOTO_ROUTE*/ 
    null, /* PACKET_PATROL_ROUTE*/ 
    new PHConnPing(), /* PACKET_CONN_PING*/ 
    null, /* PACKET_CONN_PONG*/ 
    null, // PACKET_UNIT_AIRLIFT
  };
  private Client m_client;
  public ClientPacketDispacher( Client c ) 
  {
    m_client = c;
  }
  public void dispach( InStream in )
  {
    int packetType = in.getInputPacketType();
    ClientPacketHandler ph = getHandlerFor( packetType );
    Packet pck = createPacketFor( ph, in, packetType );
    ph.handle( m_client, pck );
  }
  /**
   * Gets a handler for the specified packet type
   */
  public ClientPacketHandler getHandlerFor( int packetType )
  {
    ClientPacketHandler cph;
    try
    {
      cph = PACKET_HANDLERS[ packetType ];
      if( cph == null )
      {
        cph = new PHEmpty();
        Logger.log( Logger.LOG_NORMAL, "Unhandled packet type: " + packetType );
      }
    }
    catch( ArrayIndexOutOfBoundsException aioobe )
    {
      cph = new PHEmpty();
      Logger.log( Logger.LOG_NORMAL, "Unrecognized packet type: " + packetType );
    }
    return cph;
  }
  /**
   * Instantiates and returns a new packet for the specified
   * packet handler.
   */
  public Packet createPacketFor( ClientPacketHandler cph, InStream in, int type )
  {
    try
    {
      Class packetClass = cph.getPacketClass();
      if( packetClass == null )
      {
        // Make sure we consume any data
        in.consume();
        return null;
      }
      
      Packet packet = (Packet)packetClass.newInstance();
      packet.setType( type );
      packet.receive( in );
      return packet;
    }
    catch( Exception e )
    {
      e.printStackTrace();
      throw new RuntimeException( "Unable to create packet handler:" + e );
    }
  }
}
