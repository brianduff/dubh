
package org.freeciv.client;
public interface Constants {

// BD: Much work here, updated to 1.10.0 and changed to use
// a static enumerator for easier maintainence.

  public static final int E_NOEVENT = Enum.start();
  public static final int E_LOW_ON_FUNDS = Enum.get();
  public static final int E_POLLUTION = Enum.get();
  public static final int E_WARMING = Enum.get();
  public static final int E_CITY_DISORDER = Enum.get();
  public static final int E_CITY_LOVE = Enum.get();
  public static final int E_CITY_NORMAL = Enum.get();
  public static final int E_CITY_GROWTH = Enum.get();
  public static final int E_CITY_AQUEDUCT = Enum.get();
  public static final int E_CITY_FAMINE = Enum.get();
  public static final int E_CITY_LOST = Enum.get();
  public static final int E_CITY_CANTBUILD = Enum.get();
  public static final int E_WONDER_STARTED = Enum.get();
  public static final int E_WONDER_BUILD = Enum.get();
  public static final int E_IMP_BUILD = Enum.get();
  public static final int E_IMP_AUTO = Enum.get();
  public static final int E_IMP_AUCTIONED = Enum.get();
  public static final int E_UNIT_UPGRADED = Enum.get();
  public static final int E_UNIT_BUILD = Enum.get();
  public static final int E_UNIT_LOST = Enum.get();
  public static final int E_UNIT_WIN = Enum.get();
  public static final int E_ANARCHY = Enum.get();
  public static final int E_DIPLOMATED = Enum.get();
  public static final int E_TECH_GAIN = Enum.get();
  public static final int E_DESTROYED = Enum.get();
  public static final int E_IMP_BUY = Enum.get();
  public static final int E_IMP_SOLD = Enum.get();
  public static final int E_UNIT_BUY = Enum.get();
  public static final int E_WONDER_STOPPED = Enum.get();
  public static final int E_CITY_AQ_BUILDING = Enum.get();
  public static final int E_MY_DIPLOMAT = Enum.get();
  public static final int E_UNIT_LOST_ATT = Enum.get();
  public static final int E_UNIT_WIN_ATT = Enum.get();
  public static final int E_CITY_GRAN_THROTTLE = Enum.get();
  public static final int E_SPACESHIP = Enum.get();
  public static final int E_UPRISING = Enum.get();
  public static final int E_WORKLIST = Enum.get();
  public static final int E_LAST = Enum.get();


  // Defined in freeciv/common/shared.h
   public static final int GAME_START_YEAR = -4000;
   public static final int MAX_NUM_PLAYERS = 30;
   public static final int MAX_NUM_BARBARIANS = 2;
   public static final int MAX_NUM_ITEMS = 200;
   public static final int MAX_NUM_TECH_LIST = 10;
   public static final int MAX_LEN_NAME = 32;
   public static final int MAX_LEN_ADDR = 32;




   public static final int MAX_LEN_WORKLIST = 16;
   public static final int MAX_NUM_WORKLISTS = 16;

   public static final int B_AIRPORT = Enum.start();
   public static final int B_AQUEDUCT = Enum.get();
   public static final int B_BANK = Enum.get();
   public static final int B_BARRACKS = Enum.get();
   public static final int B_BARRACKS2 = Enum.get();
   public static final int B_BARRACKS3 = Enum.get();
   public static final int B_CATHEDRAL = Enum.get();
   public static final int B_CITY = Enum.get();
   public static final int B_COASTAL = Enum.get();
   public static final int B_COLOSSEUM = Enum.get();
   public static final int B_COURTHOUSE = Enum.get();
   public static final int B_FACTORY = Enum.get();
   public static final int B_GRANARY = Enum.get();
   public static final int B_HARBOUR = Enum.get();
   public static final int B_HYDRO = Enum.get();
   public static final int B_LIBRARY = Enum.get();
   public static final int B_MARKETPLACE = Enum.get();
   public static final int B_MASS = Enum.get();
   public static final int B_MFG = Enum.get();
   public static final int B_NUCLEAR = Enum.get();
   public static final int B_OFFSHORE = Enum.get();
   public static final int B_PALACE = Enum.get();
   public static final int B_POLICE = Enum.get();
   public static final int B_PORT = Enum.get();
   public static final int B_POWER = Enum.get();
   public static final int B_RECYCLING = Enum.get();
   public static final int B_RESEARCH = Enum.get();
   public static final int B_SAM = Enum.get();
   public static final int B_SDI = Enum.get();
   public static final int B_SEWER = Enum.get();
   public static final int B_SOLAR = Enum.get();
   public static final int B_SCOMP = Enum.get();
   public static final int B_SMODULE = Enum.get();
   public static final int B_SSTRUCTURAL = Enum.get();
   public static final int B_STOCK = Enum.get();
   public static final int B_SUPERHIGHWAYS = Enum.get();
   public static final int B_SUPERMARKET = Enum.get();
   public static final int B_TEMPLE = Enum.get();
   public static final int B_UNIVERSITY = Enum.get();
   public static final int B_APOLLO = Enum.get();
   public static final int B_ASMITHS = Enum.get();
   public static final int B_COLLOSSUS = Enum.get();
   public static final int B_COPERNICUS = Enum.get();
   public static final int B_CURE = Enum.get();
   public static final int B_DARWIN = Enum.get();
   public static final int B_EIFFEL = Enum.get();
   public static final int B_GREAT = Enum.get();
   public static final int B_WALL = Enum.get();
   public static final int B_HANGING = Enum.get();
   public static final int B_HOOVER = Enum.get();
   public static final int B_ISAAC = Enum.get();
   public static final int B_BACH = Enum.get();
   public static final int B_RICHARDS = Enum.get();
   public static final int B_LEONARDO = Enum.get();
   public static final int B_LIGHTHOUSE = Enum.get();
   public static final int B_MAGELLAN = Enum.get();
   public static final int B_MANHATTEN = Enum.get();
   public static final int B_MARCO = Enum.get();
   public static final int B_MICHELANGELO = Enum.get();
   public static final int B_ORACLE = Enum.get();
   public static final int B_PYRAMIDS = Enum.get();
   public static final int B_SETI = Enum.get();
   public static final int B_SHAKESPEARE = Enum.get();
   public static final int B_LIBERTY = Enum.get();
   public static final int B_SUNTZU = Enum.get();
   public static final int B_UNITED = Enum.get();
   public static final int B_WOMENS = Enum.get();
   public static final int B_CAPITAL = Enum.get();
   public static final int B_LAST  = Enum.get();

   public static final int SP_ELVIS = Enum.start();
   public static final int SP_SCIENTIST = Enum.get();
   public static final int SP_TAXMAN  = Enum.get();

   public static final int C_TILE_EMPTY = Enum.start();
   public static final int C_TILE_WORKER = Enum.get();
   public static final int C_TILE_UNAVAILABLE  = Enum.get();

   public static final int CITYO_ATT_LAND = Enum.start();
   public static final int CITYO_ATT_SEA = Enum.get();
   public static final int CITYO_ATT_HELI = Enum.get();
   public static final int CITYO_ATT_AIR = Enum.get();
   public static final int CITYO_DISBAND = Enum.get();
   public static final int CITYO_NEW_EINSTEIN = Enum.get();
   public static final int CITYO_NEW_TAXMAN  = Enum.get();

   public static final int CITYOPT_AUTOATTACK_BITS = 0xF;
   public static final int CITYOPT_DEFAULT = (CITYOPT_AUTOATTACK_BITS);
   public static final int CITY_MAP_SIZE = 5;

   // check this
   public static final int CLAUSE_ADVANCE = Enum.start();
   public static final int CLAUSE_GOLD = Enum.get();
   public static final int CLAUSE_MAP = Enum.get();
   public static final int CLAUSE_SEAMAP = Enum.get();
   public static final int CLAUSE_CITY = Enum.get();

   public static final int PRE_GAME_STATE = Enum.start();
   public static final int SELECT_RACES_STATE = Enum.get();
   public static final int RUN_GAME_STATE = Enum.get();
   public static final int GAME_OVER_STATE  = Enum.get();

   public static final int CLIENT_BOOT_STATE = Enum.start();
   public static final int CLIENT_PRE_GAME_STATE = Enum.get();
   public static final int CLIENT_SELECT_RACE_STATE = Enum.get();
   public static final int CLIENT_WAITING_FOR_GAME_START_STATE = Enum.get();
   public static final int CLIENT_GAME_RUNNING_STATE  = Enum.get();

   public static final int GAME_DEFAULT_RANDSEED = 0;
   public static final int GAME_MIN_RANDSEED = 0;

   public static final int GAME_DEFAULT_GOLD = 50;
   public static final int GAME_MIN_GOLD = 0;
   public static final int GAME_MAX_GOLD = 5000;

   public static final int GAME_DEFAULT_SETTLERS = 2;
   public static final int GAME_MIN_SETTLERS = 1;
   public static final int GAME_MAX_SETTLERS = 10;

   public static final int GAME_DEFAULT_EXPLORER = 1;
   public static final int GAME_MIN_EXPLORER = 0;
   public static final int GAME_MAX_EXPLORER = 10;

   public static final int GAME_DEFAULT_TECHLEVEL = 3;
   public static final int GAME_MIN_TECHLEVEL = 0;
   public static final int GAME_MAX_TECHLEVEL = 50;

   public static final int GAME_DEFAULT_UNHAPPYSIZE = 4;
   public static final int GAME_MIN_UNHAPPYSIZE = 1;
   public static final int GAME_MAX_UNHAPPYSIZE = 6;

   public static final int GAME_DEFAULT_END_YEAR = 2000;
   public static final int GAME_MIN_END_YEAR = GAME_START_YEAR;
   public static final int GAME_MAX_END_YEAR = 5000;

   public static final int GAME_DEFAULT_MIN_PLAYERS = 1;
   public static final int GAME_MIN_MIN_PLAYERS = 1;
   public static final int GAME_MAX_MIN_PLAYERS = MAX_NUM_PLAYERS;

   public static final int GAME_DEFAULT_MAX_PLAYERS = 14;
   public static final int GAME_MIN_MAX_PLAYERS = 1;
   public static final int GAME_MAX_MAX_PLAYERS = MAX_NUM_PLAYERS;

   public static final int GAME_DEFAULT_AIFILL = 0;
   public static final int GAME_MIN_AIFILL = 0;
   public static final int GAME_MAX_AIFILL = GAME_MAX_MAX_PLAYERS;

   public static final int GAME_DEFAULT_RESEARCHLEVEL = 10;
   public static final int GAME_MIN_RESEARCHLEVEL = 4;
   public static final int GAME_MAX_RESEARCHLEVEL = 100;

   public static final int GAME_DEFAULT_DIPLCOST = 0;
   public static final int GAME_MIN_DIPLCOST = 0;
   public static final int GAME_MAX_DIPLCOST = 100;

   public static final int GAME_DEFAULT_DIPLCHANCE = 80;
   public static final int GAME_MIN_DIPLCHANCE = 1;
   public static final int GAME_MAX_DIPLCHANCE = 99;

   public static final int GAME_DEFAULT_FREECOST = 0;
   public static final int GAME_MIN_FREECOST = 0;
   public static final int GAME_MAX_FREECOST = 100;

   public static final int GAME_DEFAULT_CONQUERCOST = 0;
   public static final int GAME_MIN_CONQUERCOST = 0;
   public static final int GAME_MAX_CONQUERCOST = 100;

   public static final int GAME_DEFAULT_CITYFACTOR = 14;
   public static final int GAME_MIN_CITYFACTOR = 6;
   public static final int GAME_MAX_CITYFACTOR = 100;

   public static final int GAME_DEFAULT_CIVILWARSIZE = 10;
   public static final int GAME_MIN_CIVILWARSIZE = 6;
   public static final int GAME_MAX_CIVILWARSIZE = 1000;

   public static final int GAME_DEFAULT_FOODBOX = 10;
   public static final int GAME_MIN_FOODBOX = 5;
   public static final int GAME_MAX_FOODBOX = 30;

   public static final int GAME_DEFAULT_AQUEDUCTLOSS = 0;
   public static final int GAME_MIN_AQUEDUCTLOSS = 0;
   public static final int GAME_MAX_AQUEDUCTLOSS = 100;

   public static final int GAME_DEFAULT_TECHPENALTY = 100;
   public static final int GAME_MIN_TECHPENALTY = 0;
   public static final int GAME_MAX_TECHPENALTY = 100;

   public static final int GAME_DEFAULT_RAZECHANCE = 20;
   public static final int GAME_MIN_RAZECHANCE = 0;
   public static final int GAME_MAX_RAZECHANCE = 100;

   public static final int GAME_DEFAULT_CIVSTYLE = 2;
   public static final int GAME_MIN_CIVSTYLE = 1;
   public static final int GAME_MAX_CIVSTYLE = 2;

   public static final int GAME_DEFAULT_SCORELOG = 0;
   public static final int GAME_MIN_SCORELOG = 0;
   public static final int GAME_MAX_SCORELOG = 1;

   public static final int GAME_DEFAULT_SPACERACE = 1;
   public static final int GAME_MIN_SPACERACE = 0;
   public static final int GAME_MAX_SPACERACE = 1;

   public static final int GAME_DEFAULT_TIMEOUT = 0;
   public static final int GAME_MIN_TIMEOUT = 0;
   public static final int GAME_MAX_TIMEOUT = 999;

   public static final int GAME_DEFAULT_BARBARIANRATE = 2;
   public static final int GAME_MIN_BARBARIANRATE = 0;
   public static final int GAME_MAX_BARBARIANRATE = 4;

   public static final int GAME_DEFAULT_ONSETBARBARIAN = GAME_MIN_END_YEAR;
   public static final int GAME_MIN_ONSETBARBARIAN = GAME_MIN_END_YEAR;
   public static final int GAME_MAX_ONSETBARBARIAN = GAME_MAX_END_YEAR;

   public static final int GAME_DEFAULT_OCCUPYCHANCE = 0;
   public static final int GAME_MIN_OCCUPYCHANCE = 0;
   public static final int GAME_MAX_OCCUPYCHANCE = 100;

   public static final String GAME_DEFAULT_RULESET = "default";

   public static final int GAME_DEFAULT_SKILL_LEVEL = 3;
   public static final int GAME_OLD_DEFAULT_SKILL_LEVEL = 5;

   public static final String GAME_DEFAULT_DEMOGRAPHY = "NASRLPEMOqrb";




   public static final int LOG_FATAL = Enum.start();
   public static final int LOG_NORMAL = Enum.get();
   public static final int LOG_VERBOSE = Enum.get();
   public static final int LOG_DEBUG = Enum.get();


   public static final int S_NO_SPECIAL = 0;
   public static final int S_SPECIAL_1 = 1;
   public static final int S_ROAD = 2;
   public static final int S_IRRIGATION = 4;
   public static final int S_RAILROAD = 8;
   public static final int S_MINE = 16;
   public static final int S_POLLUTION = 32;
   public static final int S_HUT = 64;
   public static final int S_FORTRESS = 128;
   public static final int S_SPECIAL_2 = 256;
   public static final int S_RIVER = 512;
   public static final int S_FARMLAND = 1024;
   public static final int S_AIRBASE = 2048;




   public static final int T_ARCTIC = Enum.start();
   public static final int T_DESERT = Enum.get();
   public static final int T_FOREST = Enum.get();
   public static final int T_GRASSLAND = Enum.get();
   public static final int T_HILLS = Enum.get();
   public static final int T_JUNGLE = Enum.get();
   public static final int T_MOUNTAINS = Enum.get();
   public static final int T_OCEAN = Enum.get();
   public static final int T_PLAINS = Enum.get();
   public static final int T_RIVER = Enum.get();
   public static final int T_SWAMP = Enum.get();
   public static final int T_TUNDRA = Enum.get();
   public static final int T_UNKNOWN = Enum.get();
   public static final int T_LAST  = Enum.get();

   public static final int T_FIRST = T_ARCTIC;
   public static final int T_COUNT = T_UNKNOWN;

   public static final int TILE_UNKNOWN = Enum.start();
   public static final int TILE_KNOWN_NODRAW = Enum.get();
   public static final int TILE_KNOWN  = Enum.get();

   public static final int MAP_DEFAULT_HUTS = 50;
   public static final int MAP_MIN_HUTS = 0;
   public static final int MAP_MAX_HUTS = 500;

   public static final int MAP_DEFAULT_WIDTH = 80;
   public static final int MAP_MIN_WIDTH = 40;
   public static final int MAP_MAX_WIDTH = 200;

   public static final int MAP_DEFAULT_HEIGHT = 50;
   public static final int MAP_MIN_HEIGHT = 25;
   public static final int MAP_MAX_HEIGHT = 100;

   public static final int MAP_DEFAULT_SEED = 0;
   public static final int MAP_MIN_SEED = 0;

   public static final int MAP_DEFAULT_LANDMASS = 30;
   public static final int MAP_MIN_LANDMASS = 15;
   public static final int MAP_MAX_LANDMASS = 85;

   public static final int MAP_DEFAULT_RICHES = 250;
   public static final int MAP_MIN_RICHES = 0;
   public static final int MAP_MAX_RICHES = 1000;

   public static final int MAP_DEFAULT_MOUNTAINS = 30;
   public static final int MAP_MIN_MOUNTAINS = 10;
   public static final int MAP_MAX_MOUNTAINS = 100;

   public static final int MAP_DEFAULT_GRASS = 35;
   public static final int MAP_MIN_GRASS = 20;
   public static final int MAP_MAX_GRASS = 100;

   public static final int MAP_DEFAULT_SWAMPS = 5;
   public static final int MAP_MIN_SWAMPS = 0;
   public static final int MAP_MAX_SWAMPS = 100;

   public static final int MAP_DEFAULT_DESERTS = 5;
   public static final int MAP_MIN_DESERTS = 0;
   public static final int MAP_MAX_DESERTS = 100;

   public static final int MAP_DEFAULT_RIVERS = 50;
   public static final int MAP_MIN_RIVERS = 0;
   public static final int MAP_MAX_RIVERS = 1000;

   public static final int MAP_DEFAULT_FORESTS = 20;
   public static final int MAP_MIN_FORESTS = 0;
   public static final int MAP_MAX_FORESTS = 100;

   public static final int MAP_DEFAULT_GENERATOR = 1;
   public static final int MAP_MIN_GENERATOR = 1;
   public static final int MAP_MAX_GENERATOR = 4;


   public static final int MAX_LEN_PACKET = 4096;
   public static final int MAX_LEN_USERNAME = 10;
   public static final int MAX_LEN_MSG = 1536;
//   public static final int ADDR_LENGTH = 32;
   public static final int MAX_LEN_CAPSTR = 512;


   // The different types of packet. This must be synched up with the latest
   // FreeCiv enumeration in source/common/packets.h


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
   public static final int PACKET_UNIT_NUKE  = Enum.get();
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



   public static final int REPORT_WONDERS_OF_THE_WORLD = Enum.start();
   public static final int REPORT_TOP_5_CITIES = Enum.get();
   public static final int REPORT_DEMOGRAPHIC = Enum.get();
   public static final int REPORT_SERVER_OPTIONS = Enum.get();  /* obsolete */
   public static final int REPORT_SERVER_OPTIONS1 = Enum.get();
   public static final int REPORT_SERVER_OPTIONS2  = Enum.get();



   public static final int SSHIP_ACT_LAUNCH = Enum.start();
   public static final int SSHIP_ACT_PLACE_STRUCTURAL = Enum.get();
   public static final int SSHIP_ACT_PLACE_FUEL = Enum.get();
   public static final int SSHIP_ACT_PLACE_PROPULSION = Enum.get();
   public static final int SSHIP_ACT_PLACE_HABITATION = Enum.get();
   public static final int SSHIP_ACT_PLACE_LIFE_SUPPORT = Enum.get();
   public static final int SSHIP_ACT_PLACE_SOLAR_PANELS  = Enum.get();

   public static final int PLAYER_DEFAULT_TAX_RATE = 0;
   public static final int PLAYER_DEFAULT_SCIENCE_RATE = 100;
   public static final int PLAYER_DEFAULT_LUXURY_RATE = 0;
   public static final int MAX_NUM_TECH_GOALS = 10;                /**==MAX_TECH_GOALS?*/

   public static final int MAX_NUM_NATIONS = 63;
   public static final int MAX_NUM_LEADERS = 16;

   /** Missing: handicap_type from player.h */

   /** Missing: client command levels */

   public static final int ADV_ISLAND = Enum.start();
   public static final int ADV_MILITARY = Enum.get();
   public static final int ADV_TRADE = Enum.get();
   public static final int ADV_SCIENCE = Enum.get();
   public static final int ADV_FOREIGN = Enum.get();
   public static final int ADV_ATTITUDE = Enum.get();
   public static final int ADV_DOMESTIC = Enum.get();
   public static final int ADV_LAST = Enum.get();


   public static final int /* access levels for users to issue commands        */  ALLOW_NONE  = 0;
   public static final int /* user may issue no commands at all                */   ALLOW_INFO = 1;
   public static final int /* user may issue informational commands            */   ALLOW_CTRL = 2;
   public static final int /* user may issue commands that affect game & users */   ALLOW_HACK = 3;
   public static final int /* user may issue *all* commands - dangerous!       */    ALLOW_NUM = 4;
   public static final int /* the number of levels                             */   ALLOW_UNRECOGNIZED  /* used as a failure return code                    */  = 5;
   public static final int DO_HASH = 1;
   public static final int HASH_DEBUG = 1;
   public static final int SAVE_TABLES = 1;
   public static final int BROADCAST_EVENT = -2;



   public static final int SSHIP_NONE = Enum.start();
   public static final int SSHIP_STARTED = Enum.get();
   public static final int SSHIP_LAUNCHED = Enum.get();
   public static final int SSHIP_ARRIVED = Enum.get();

   public static final int NUM_SS_STRUCTURALS = 32;
   public static final int NUM_SS_COMPONENTS = 16;
   public static final int NUM_SS_MODULES = 12;


   public static final int A_NONE = 0;
   public static final int A_FIRST = 1;
   public static final int A_LAST  = MAX_NUM_ITEMS;

   public static final int TECH_UNKNOWN = 0;
   public static final int TECH_KNOWN = 1;
   public static final int TECH_REACHABLE = 2;
   public static final int TECH_MARKED = 3;

   public static final int TF_BONUS_TECH = Enum.start();
   public static final int TF_BOAT_FAST = Enum.get();
   public static final int TF_BRIDGE = Enum.get();
   public static final int TF_RAILROAD = Enum.get();
   public static final int TF_FORTRESS = Enum.get();
   public static final int TF_POPULATION_POLLUTION_INC = Enum.get();
   public static final int TF_TRADE_REVENUE_REDUCE = Enum.get();
   public static final int TF_AIRBASE = Enum.get();
   public static final int TF_FARMLAND = Enum.get();
   public static final int TF_LAST = Enum.get();



   public static final int U_LAST  = MAX_NUM_ITEMS;

   // enum unit_activity
   public static final int ACTIVITY_IDLE = Enum.start();
   public static final int ACTIVITY_POLLUTION = Enum.get();
   public static final int ACTIVITY_ROAD = Enum.get();
   public static final int ACTIVITY_MINE = Enum.get();
   public static final int ACTIVITY_IRRIGATE = Enum.get();
   public static final int ACTIVITY_FORTIFY = Enum.get();
   public static final int ACTIVITY_FORTRESS = Enum.get();
   public static final int ACTIVITY_SENTRY = Enum.get();
   public static final int ACTIVITY_RAILROAD = Enum.get();
   public static final int ACTIVITY_PILLAGE = Enum.get();
   public static final int ACTIVITY_GOTO = Enum.get();
   public static final int ACTIVITY_EXPLORE = Enum.get();
   public static final int ACTIVITY_TRANSFORM = Enum.get();
   public static final int ACTIVITY_UNKNOWN  = Enum.get();
   public static final int ACTIVITY_AIRBASE  = Enum.get();
   public static final int ACTIVITY_LAST  = Enum.get();

   // enum unit_move_type
   public static final int LAND_MOVING  = Enum.start(1);
   public static final int SEA_MOVING = Enum.get();
   public static final int HELI_MOVING = Enum.get();
   public static final int AIR_MOVING  = Enum.get();
   // enum unit_focus_status
   public static final int FOCUS_AVAIL = Enum.start();
   public static final int FOCUS_WAIT = Enum.get();
   public static final int FOCUS_DONE    = Enum.get();
   // enum diplomat_actions
   public static final int DIPLOMAT_BRIBE = Enum.start();
   public static final int DIPLOMAT_EMBASSY = Enum.get();
   public static final int DIPLOMAT_SABOTAGE = Enum.get();
   public static final int DIPLOMAT_STEAL = Enum.get();
   public static final int DIPLOMAT_INCITE = Enum.get();
   public static final int SPY_POISON = Enum.get();
   public static final int DIPLOMAT_INVESTIGATE = Enum.get();
   public static final int SPY_SABOTAGE_UNIT = Enum.get();
   public static final int DIPLOMAT_ANY_ACTION  = Enum.get();
   // enum unit_flag_id
   public static final int F_CARAVAN = Enum.start(0);
   public static final int F_MISSILE = Enum.get();
   public static final int F_IGZOC = Enum.get();
   public static final int F_NONMIL = Enum.get();
   public static final int F_IGTER = Enum.get();
   public static final int F_CARRIER = Enum.get();
   public static final int F_ONEATTACK = Enum.get();
   public static final int F_PIKEMEN = Enum.get();
   public static final int F_HORSE = Enum.get();
   public static final int F_IGWALL = Enum.get();
   public static final int F_FIELDUNIT = Enum.get();
   public static final int F_AEGIS = Enum.get();
   public static final int F_FIGHTER = Enum.get();
   public static final int F_MARINES = Enum.get();
   public static final int F_SUBMARINE = Enum.get();
   public static final int F_SETTLERS = Enum.get();
   public static final int F_DIPLOMAT = Enum.get();
   public static final int F_TRIREME = Enum.get();
   public static final int F_NUCLEAR = Enum.get();
   public static final int F_SPY = Enum.get();
   public static final int F_TRANSFORM = Enum.get();
   public static final int F_PARATROOPERS = Enum.get();
   public static final int F_AIRBASE = Enum.get();
   public static final int F_CITIES = Enum.get();
   public static final int F_LAST  = Enum.get();


   public static final int L_FIRST = 64;

   public static final int L_FIRSTBUILD = Enum.start(L_FIRST);
   public static final int L_EXPLORER = Enum.get();
   public static final int L_HUT = Enum.get();
   public static final int L_HUT_TECH = Enum.get();
   public static final int L_PARTISAN = Enum.get();
   public static final int L_DEFEND_OK = Enum.get();
   public static final int L_DEFEND_GOOD = Enum.get();
   public static final int L_ATTACK_FAST = Enum.get();
   public static final int L_ATTACK_STRONG = Enum.get();
   public static final int L_FERRYBOAT = Enum.get();
   public static final int L_BARBARIAN = Enum.get();
   public static final int L_BARBARIAN_TECH = Enum.get();
   public static final int L_BARBARIAN_BOAT = Enum.get();
   public static final int L_BARBARIAN_BUILD = Enum.get();
   public static final int L_BARBARIAN_BUILD_TECH = Enum.get();
   public static final int L_BARBARIAN_LEADER = Enum.get();
   public static final int L_BARBARIAN_SEA = Enum.get();
   public static final int L_BARBARIAN_SEA_TECH = Enum.get();
   public static final int L_LAST = Enum.get();


   public static final int MAJOR_VERSION = 1;
   public static final int MINOR_VERSION = 11;
   public static final int PATCH_VERSION = 7;
  public static final String VERSION_LABEL = "-devel";
  public static final int IS_DEVEL_VERSION = 1;
}
