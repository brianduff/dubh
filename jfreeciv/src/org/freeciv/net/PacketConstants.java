package org.freeciv.net;
import org.freeciv.util.Enum;

/**
 * This inteface contains integer constants for the different types of packet
 * which can be sent and received by the client or server.
 *
 * @author Brian.Duff@dubh.org
 */
public interface PacketConstants
{
  // source/common/packets.h
  



































  // This space left intentionally blank
  

























  //... so that the first packet constant starts on line 100
  // making it easier to debug packet numbers
  public static final int PACKET_REQUEST_JOIN_GAME = Enum.start();
  public static final int PACKET_JOIN_GAME_REPLY = Enum.get();
  public static final int PACKET_SERVER_SHUTDOWN = Enum.get();
  public static final int PACKET_UNIT_INFO = Enum.get();
  public static final int PACKET_MOVE_UNIT = Enum.get();
  public static final int PACKET_TURN_DONE = Enum.get();
  public static final int PACKET_NEW_YEAR = Enum.get();
  public static final int PACKET_TILE_INFO = Enum.get();
  public static final int PACKET_SELECT_NATION = Enum.get();
  public static final int PACKET_ALLOC_NATION = Enum.get();
  public static final int PACKET_SHOW_MESSAGE = Enum.get();
  public static final int PACKET_PLAYER_INFO = Enum.get();
  public static final int PACKET_GAME_INFO = Enum.get();
  public static final int PACKET_MAP_INFO = Enum.get();
  public static final int PACKET_CHAT_MSG = Enum.get();
  public static final int PACKET_CITY_INFO = Enum.get();
  public static final int PACKET_CITY_SELL = Enum.get();
  public static final int PACKET_CITY_BUY = Enum.get();
  public static final int PACKET_CITY_CHANGE = Enum.get();
  public static final int PACKET_CITY_WORKLIST = Enum.get();
  public static final int PACKET_CITY_MAKE_SPECIALIST = Enum.get();
  public static final int PACKET_CITY_MAKE_WORKER = Enum.get();
  public static final int PACKET_CITY_CHANGE_SPECIALIST = Enum.get();
  public static final int PACKET_CITY_RENAME = Enum.get();
  public static final int PACKET_PLAYER_RATES = Enum.get();
  public static final int PACKET_PLAYER_REVOLUTION = Enum.get();
  public static final int PACKET_PLAYER_GOVERNMENT = Enum.get();
  public static final int PACKET_PLAYER_RESEARCH = Enum.get();
  public static final int PACKET_PLAYER_WORKLIST = Enum.get();
  public static final int PACKET_UNIT_BUILD_CITY = Enum.get();
  public static final int PACKET_UNIT_DISBAND = Enum.get();
  public static final int PACKET_REMOVE_UNIT = Enum.get();
  public static final int PACKET_REMOVE_CITY = Enum.get();
  public static final int PACKET_UNIT_CHANGE_HOMECITY = Enum.get();
  public static final int PACKET_UNIT_COMBAT = Enum.get();
  public static final int PACKET_UNIT_ESTABLISH_TRADE = Enum.get();
  public static final int PACKET_UNIT_HELP_BUILD_WONDER = Enum.get();
  public static final int PACKET_UNIT_GOTO_TILE = Enum.get();
  public static final int PACKET_GAME_STATE = Enum.get();
  public static final int PACKET_NUKE_TILE = Enum.get();
  public static final int PACKET_DIPLOMAT_ACTION = Enum.get();
  public static final int PACKET_PAGE_MSG = Enum.get();
  public static final int PACKET_REPORT_REQUEST = Enum.get();
  public static final int PACKET_DIPLOMACY_INIT_MEETING = Enum.get();
  public static final int PACKET_DIPLOMACY_CREATE_CLAUSE = Enum.get();
  public static final int PACKET_DIPLOMACY_REMOVE_CLAUSE = Enum.get();
  public static final int PACKET_DIPLOMACY_CANCEL_MEETING = Enum.get();
  public static final int PACKET_DIPLOMACY_ACCEPT_TREATY = Enum.get();
  public static final int PACKET_DIPLOMACY_SIGN_TREATY = Enum.get();
  public static final int PACKET_UNIT_AUTO = Enum.get();
  public static final int PACKET_BEFORE_NEW_YEAR = Enum.get();
  public static final int PACKET_REMOVE_PLAYER = Enum.get();
  public static final int PACKET_UNITTYPE_UPGRADE = Enum.get();
  public static final int PACKET_UNIT_UNLOAD = Enum.get();
  public static final int PACKET_PLAYER_TECH_GOAL = Enum.get();
  public static final int PACKET_CITY_REFRESH = Enum.get();
  public static final int PACKET_INCITE_INQ = Enum.get();
  public static final int PACKET_INCITE_COST = Enum.get();
  public static final int PACKET_UNIT_UPGRADE = Enum.get();
  public static final int PACKET_PLAYER_CANCEL_PACT = Enum.get();
  public static final int PACKET_RULESET_TECH = Enum.get();
  public static final int PACKET_RULESET_UNIT = Enum.get();
  public static final int PACKET_RULESET_BUILDING = Enum.get();
  public static final int PACKET_CITY_OPTIONS = Enum.get();
  public static final int PACKET_SPACESHIP_INFO = Enum.get();
  public static final int PACKET_SPACESHIP_ACTION = Enum.get();
  public static final int PACKET_UNIT_NUKE = Enum.get();
  public static final int PACKET_RULESET_TERRAIN = Enum.get();
  public static final int PACKET_RULESET_TERRAIN_CONTROL = Enum.get();
  public static final int PACKET_RULESET_GOVERNMENT = Enum.get();
  public static final int PACKET_RULESET_GOVERNMENT_RULER_TITLE = Enum.get();
  public static final int PACKET_RULESET_CONTROL = Enum.get();
  public static final int PACKET_CITY_NAME_SUGGEST_REQ = Enum.get();
  public static final int PACKET_CITY_NAME_SUGGESTION = Enum.get();
  public static final int PACKET_RULESET_NATION = Enum.get();
  public static final int PACKET_UNIT_PARADROP_TO = Enum.get();
  public static final int PACKET_RULESET_CITY = Enum.get();
  public static final int PACKET_UNIT_CONNECT = Enum.get();
  public static final int PACKET_SABOTAGE_LIST = Enum.get();
  public static final int PACKET_ADVANCE_FOCUS = Enum.get();
  public static final int PACKET_RULESET_GAME = Enum.get();
  public static final int PACKET_CONN_INFO = Enum.get();
  public static final int PACKET_SHORT_CITY = Enum.get();
  public static final int PACKET_PLAYER_REMOVE_VISION = Enum.get();
  public static final int PACKET_GOTO_ROUTE = Enum.get();
  public static final int PACKET_PATROL_ROUTE = Enum.get();
  public static final int PACKET_CONN_PING = Enum.get();
  public static final int PACKET_CONN_PONG = Enum.get();
  public static final int PACKET_UNIT_AIRLIFT = Enum.get();
  public static final int PACKET_LAST = Enum.get();
}
