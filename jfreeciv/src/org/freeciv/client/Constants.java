package org.freeciv.client;

import org.freeciv.common.CommonConstants;
import org.freeciv.net.PacketConstants;
import org.freeciv.util.Enum;


/**
 * Constants that are specific to the client. For ease of use, this extends
 * the network and common constants
 *
 * @author Brian.Duff@dubh.org
 */
public interface Constants extends PacketConstants, CommonConstants
{

  public static final int PRE_GAME_STATE = Enum.start();
  public static final int SELECT_RACES_STATE = Enum.get();
  public static final int RUN_GAME_STATE = Enum.get();
  public static final int GAME_OVER_STATE = Enum.get();
  public static final int CLIENT_BOOT_STATE = Enum.start();
  public static final int CLIENT_PRE_GAME_STATE = Enum.get();
  public static final int CLIENT_SELECT_RACE_STATE = Enum.get();
  public static final int CLIENT_WAITING_FOR_GAME_START_STATE = Enum.get();
  public static final int CLIENT_GAME_RUNNING_STATE = Enum.get();

  public static final int TILE_UNKNOWN = Enum.start();
  public static final int TILE_KNOWN_NODRAW = Enum.get();
  public static final int TILE_KNOWN = Enum.get();
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

}
