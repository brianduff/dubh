package org.freeciv.common;

/**
 * Constants for the different types of unit
 */
public final class UnitClass
{
  public static final UnitClass UCL_AIR = new UnitClass();
  public static final UnitClass UCL_HELICOPTER = new UnitClass();
  public static final UnitClass UCL_LAND = new UnitClass();
  public static final UnitClass UCL_MISSILE = new UnitClass();
  public static final UnitClass UCL_NUCLEAR = new UnitClass();
  public static final UnitClass UCL_SEA = new UnitClass();
  public static final UnitClass UCL_LAST = new UnitClass();

  private static final TypesafeEnum ENUM = new TypesafeEnum(
    new UnitClass[] {
      UCL_AIR,
      UCL_HELICOPTER,
      UCL_LAND,
      UCL_MISSILE,
      UCL_NUCLEAR,
      UCL_SEA,
      UCL_LAST
  });

  public static UnitClass fromInt(int i)
  {
    return (UnitClass)ENUM._fromInt(i);
  }

  public static int toInt(UnitClass c)
  {
    return ENUM._toInt(c);
  }

  private UnitClass() {}

}