package org.freeciv.common;

/**
 * Utility class that makes it easier to implement something roughly equivalent
 * to C's enum facility, including the ability to explicity convert integers
 * to and from the enum type.
 *
 * The downside is that this is almost certainly less efficient than pure
 * enums. On the other hand, code using this facility will be much more 
 * readable (else, we use lots of ints), and safer.
 * 
 * @author Brian.Duff@dubh.org
 */
public final class TypesafeEnum
{
  private Object[] m_sorted;
  public TypesafeEnum( Object[] sortedList ) 
  {
    m_sorted = sortedList;
  }
  
  /**
   * The subclass should call this to convert a typed enum into an integer
   */
  public int _toInt( Object o )
  {
    for( int i = 0;i < m_sorted.length;i++ )
    {
      if( o == m_sorted[ i ] )
      {
        return i;
      }
    }
    throw new IllegalArgumentException( "Invalid Enumeration Object " + o );
  }
  
  /**
   * The subclass should call this to convert an integer into a type enum
   * value
   */
  public Object _fromInt( int i )
  {
    try
    {
      return m_sorted[ i ];
    }
    catch( ArrayIndexOutOfBoundsException aioobe )
    {
      throw new IllegalArgumentException( "Invalid Enumeration value:" + i );
    }
  }
}
