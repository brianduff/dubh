package org.freeciv.common;


/**
 * The different effects that (improvements?) can have
 *
 * @author Brian.Duff@dubh.org
 */
public final class EffectType
{
  
  // from freeciv/common/improvement.h
  public static final EffectType EFT_ADV_PARASITE = new EffectType();
  public static final EffectType EFT_AIRLIFT = new EffectType();
  public static final EffectType EFT_ANY_GOVERNMENT = new EffectType();
  public static final EffectType EFT_BARB_ATTACK = new EffectType();
  public static final EffectType EFT_BARB_DEFEND = new EffectType();
  public static final EffectType EFT_CAPITAL_CITY = new EffectType();
  public static final EffectType EFT_CAPITAL_EXISTS = new EffectType();
  public static final EffectType EFT_ENABLE_NUKE = new EffectType();
  public static final EffectType EFT_ENABLE_SPACE = new EffectType();
  public static final EffectType EFT_ENEMY_PEACEFUL = new EffectType();
  public static final EffectType EFT_FOOD_ADD_TILE = new EffectType();
  public static final EffectType EFT_FOOD_BONUS = new EffectType();
  public static final EffectType EFT_FOOD_INC_TILE = new EffectType();
  public static final EffectType EFT_FOOD_PER_TILE = new EffectType();
  public static final EffectType EFT_GIVE_IMM_ADV = new EffectType();
  public static final EffectType EFT_GROWTH_FOOD = new EffectType();
  public static final EffectType EFT_HAVE_EMBASSIES = new EffectType();
  public static final EffectType EFT_IMPROVE_REP = new EffectType();
  public static final EffectType EFT_LUXURY_BONUS = new EffectType();
  public static final EffectType EFT_LUXURY_PCT = new EffectType();
  public static final EffectType EFT_MAKE_CONTENT = new EffectType();
  public static final EffectType EFT_MAKE_CONTENT_MIL = new EffectType();
  public static final EffectType EFT_MAKE_CONTENT_PCT = new EffectType();
  public static final EffectType EFT_MAKE_HAPPY = new EffectType();
  public static final EffectType EFT_MAY_DECLARE_WAR = new EffectType();
  public static final EffectType EFT_NO_ANARCHY = new EffectType();
  public static final EffectType EFT_NO_SINK_DEEP = new EffectType();
  public static final EffectType EFT_NUKE_PROOF = new EffectType();
  public static final EffectType EFT_POLLU_ADJ = new EffectType();
  public static final EffectType EFT_POLLU_ADJ_POP = new EffectType();
  public static final EffectType EFT_POLLU_ADJ_PROD = new EffectType();
  public static final EffectType EFT_POLLU_SET = new EffectType();
  public static final EffectType EFT_POLLU_SET_POP = new EffectType();
  public static final EffectType EFT_POLLU_SET_PROD = new EffectType();
  public static final EffectType EFT_PROD_ADD_TILE = new EffectType();
  public static final EffectType EFT_PROD_BONUS = new EffectType();
  public static final EffectType EFT_PROD_INC_TILE = new EffectType();
  public static final EffectType EFT_PROD_PER_TILE = new EffectType();
  public static final EffectType EFT_PROD_TO_GOLD = new EffectType();
  public static final EffectType EFT_REDUCE_CORRUPT = new EffectType();
  public static final EffectType EFT_REDUCE_WASTE = new EffectType();
  public static final EffectType EFT_REVEAL_CITIES = new EffectType();
  public static final EffectType EFT_REVEAL_MAP = new EffectType();
  public static final EffectType EFT_REVOLT_DIST = new EffectType();
  public static final EffectType EFT_SCIENCE_BONUS = new EffectType();
  public static final EffectType EFT_SCIENCE_PCT = new EffectType();
  public static final EffectType EFT_SIZE_UNLIMIT = new EffectType();
  public static final EffectType EFT_SLOW_NUKE_WINTER = new EffectType();
  public static final EffectType EFT_SLOW_GLOBAL_WARM = new EffectType();
  public static final EffectType EFT_SPACE_PART = new EffectType();
  public static final EffectType EFT_SPY_RESISTANT = new EffectType();
  public static final EffectType EFT_TAX_BONUS = new EffectType();
  public static final EffectType EFT_TAX_PCT = new EffectType();
  public static final EffectType EFT_TRADE_ADD_TILE = new EffectType();
  public static final EffectType EFT_TRADE_BONUS = new EffectType();
  public static final EffectType EFT_TRADE_INC_TILE = new EffectType();
  public static final EffectType EFT_TRADE_PER_TILE = new EffectType();
  public static final EffectType EFT_TRADE_ROUTE_PCT = new EffectType();
  public static final EffectType EFT_UNIT_DEFEND = new EffectType();
  public static final EffectType EFT_UNIT_MOVE = new EffectType();
  public static final EffectType EFT_UNIT_NO_LOSE_POP = new EffectType();
  public static final EffectType EFT_UNIT_RECOVER = new EffectType();
  public static final EffectType EFT_UNIT_REPAIR = new EffectType();
  public static final EffectType EFT_UNIT_VET_COMBAT = new EffectType();
  public static final EffectType EFT_UNIT_VETERAN = new EffectType();
  public static final EffectType EFT_UPGRADE_UNITS = new EffectType();
  public static final EffectType EFT_UPGRADE_ONE_STEP = new EffectType();
  public static final EffectType EFT_UPGRADE_ONE_LEAP = new EffectType();
  public static final EffectType EFT_UPGRADE_ALL_STEP = new EffectType();
  public static final EffectType EFT_UPGRADE_ALL_LEAP = new EffectType();
  public static final EffectType EFT_UPKEEP_FREE = new EffectType();
  public static final EffectType EFT_LAST = new EffectType();
  private static final TypesafeEnum ENUM = new TypesafeEnum( new EffectType[]
  {
    EFT_ADV_PARASITE, EFT_AIRLIFT, EFT_ANY_GOVERNMENT, 
    EFT_BARB_ATTACK, EFT_BARB_DEFEND, EFT_CAPITAL_CITY, 
    EFT_CAPITAL_EXISTS, EFT_ENABLE_NUKE, EFT_ENABLE_SPACE, 
    EFT_ENEMY_PEACEFUL, EFT_FOOD_ADD_TILE, EFT_FOOD_BONUS, 
    EFT_FOOD_INC_TILE, EFT_FOOD_PER_TILE, EFT_GIVE_IMM_ADV, 
    EFT_GROWTH_FOOD, EFT_HAVE_EMBASSIES, EFT_IMPROVE_REP, 
    EFT_LUXURY_BONUS, EFT_LUXURY_PCT, EFT_MAKE_CONTENT, 
    EFT_MAKE_CONTENT_MIL, EFT_MAKE_CONTENT_PCT, EFT_MAKE_HAPPY, 
    EFT_MAY_DECLARE_WAR, EFT_NO_ANARCHY, EFT_NO_SINK_DEEP, 
    EFT_NUKE_PROOF, EFT_POLLU_ADJ, EFT_POLLU_ADJ_POP, EFT_POLLU_ADJ_PROD, 
    EFT_POLLU_SET, EFT_POLLU_SET_POP, EFT_POLLU_SET_PROD, 
    EFT_PROD_ADD_TILE, EFT_PROD_BONUS, EFT_PROD_INC_TILE, 
    EFT_PROD_PER_TILE, EFT_PROD_TO_GOLD, EFT_REDUCE_CORRUPT, 
    EFT_REDUCE_WASTE, EFT_REVEAL_CITIES, EFT_REVEAL_MAP, 
    EFT_REVOLT_DIST, EFT_SCIENCE_BONUS, EFT_SCIENCE_PCT, 
    EFT_SIZE_UNLIMIT, EFT_SLOW_NUKE_WINTER, EFT_SLOW_GLOBAL_WARM, 
    EFT_SPACE_PART, EFT_SPY_RESISTANT, EFT_TAX_BONUS, EFT_TAX_PCT, 
    EFT_TRADE_ADD_TILE, EFT_TRADE_BONUS, EFT_TRADE_INC_TILE, 
    EFT_TRADE_PER_TILE, EFT_TRADE_ROUTE_PCT, EFT_UNIT_DEFEND, 
    EFT_UNIT_MOVE, EFT_UNIT_NO_LOSE_POP, EFT_UNIT_RECOVER, 
    EFT_UNIT_REPAIR, EFT_UNIT_VET_COMBAT, EFT_UNIT_VETERAN, 
    EFT_UPGRADE_UNITS, EFT_UPGRADE_ONE_STEP, EFT_UPGRADE_ONE_LEAP, 
    EFT_UPGRADE_ALL_STEP, EFT_UPGRADE_ALL_LEAP, EFT_UPKEEP_FREE, 
    EFT_LAST
  } );
  public static EffectType fromInt( int i )
  {
    return (EffectType)ENUM._fromInt( i );
  }
  public static int toInt( EffectType et )
  {
    return ENUM._toInt( et );
  }
}
