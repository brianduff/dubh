package org.freeciv.common;

/**
 * Use this class to assert.
 */
public final class Assert 
{
  private Assert()
  {
  }

  public static void that(boolean condition)
  {
    that(condition, "");
  }

  public static void that(boolean condition, String message)
  {
    if (!condition)
    {
      throw new IllegalStateException(
        "Assertion Failed"
      );
    }
  }
}