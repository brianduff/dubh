package org.freeciv.common;

/**
 * The possible ranges of an effect.
 */
public final class EffectRange
{
  // from freeciv/common/improvement.h

  public static final EffectRange EFR_NONE = new EffectRange();
  public static final EffectRange EFR_BUILDING = new EffectRange();
  public static final EffectRange EFR_CITY = new EffectRange();
  public static final EffectRange EFR_ISLAND = new EffectRange();
  public static final EffectRange EFR_PLAYER = new EffectRange();
  public static final EffectRange EFR_WORLD = new EffectRange();
  public static final EffectRange EFR_LAST = new EffectRange();

  private static final TypesafeEnum ENUM = new TypesafeEnum(
    new EffectRange[] {
      EFR_NONE,
      EFR_BUILDING,
      EFR_CITY,
      EFR_ISLAND,
      EFR_PLAYER,
      EFR_WORLD,
      EFR_LAST
  });

  public static EffectRange fromInt(int i)
  {
    return (EffectRange)ENUM._fromInt(i);
  }

  public static int toInt(EffectRange er)
  {
    return ENUM._toInt(er);
  }

  private EffectRange() {}
 
}