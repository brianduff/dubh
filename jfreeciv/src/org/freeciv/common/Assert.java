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

  public static void fail( String message )
  {
    that( false, message );
  }

  /**
   * Fail because of an exception
   */
  public static void fail( String s, Throwable t )
  {
    that( false, s + ": "+ t.getMessage() );
    ErrorHandler.getHandler().internalError( t );
  }

  public static void fail( Throwable t )
  {
    fail( "", t );
  }
}