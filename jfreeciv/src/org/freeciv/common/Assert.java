package org.freeciv.common;

/**
 * Use this class to assert. Replace with intrinsic assert support in java 1.4
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

  /**
   * Unconditionally fail an assertion.
   */
  public static void fail()
  {
    that(false);
  }
}