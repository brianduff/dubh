/*
 * Written by Artur Biesiadowski <abies@pg.gda.pl>
 * This file is public domain - you can use/modify/distribute it as long
 * as some credit is given to me. You are not required to keep it open
 * sourced, nor to give back all changes to me, BUT if you do, everybody
 * will benefit.
 * For latest version contact me at <abies@pg.gda.pl> or check
 * http://www.gjt.org/servlets/JCVSlet/list/gjt/top.org.gjt.abies
 * This file comes with no guarantee of anything - you have been WARNED.
 */

package org.gjt.abies;

public final class ArrayMangler
{

  // no need to instantiate sdfsdf
  private ArrayMangler()
  {
  }

  public static boolean[] stringToBooleanArray(String data)
  {
    char[] str = data.toCharArray();
    int size = (str[0]<<16) + str[1];
    boolean[] array = new boolean[size];
    for ( int i =0; i < size; i +=1 )
    {
      array[i] = (str[(i+2)>>4] & (1<<(i&0xf))) != 0;
    }
    return array;
  }

  public static byte[] stringToByteArray( String data )
  {
    char[] str = data.toCharArray();
    int size = (str[0]<<16) + str[1];
    byte[] array = new byte[size];
    for ( int i =0; i < size; i +=1 )
    {
      if ( (i & 1) != 0 )
        array[i] = (byte)(str[(i+2)>>1]);
      else
        array[i] = (byte)(str[(i+2)>>1]>>8);
    }
    return array;
  }


  public static int[] stringToIntArray( String data )
  {
    char[] str = data.toCharArray();
    int[] array = new int[str.length / 2];
    for ( int i =0; i < str.length; i +=2 )
    {
      array[i/2] = (str[i] << 16) + str[i+1];
    }
    return array;
  }

  public static long[] stringToLongArray( String data )
  {
    char[] str = data.toCharArray();
    long[] array = new long[str.length / 4];
    for ( int i =0; i < str.length; i +=4 )
    {
      array[i/4] = ((long)str[i] << 48) + ((long)str[i+1] << 32) +
        ((long)str[i+2]<<16) + str[i+3];
    }
    return array;
  }

  public static String booleanArrayToString( boolean[] array )
  {
    int size = array.length/16;
    if ( array.length%16 != 0 )
      size++;
    char[] str = new char[size+2];
    str[0] = (char)(array.length >> 16);
    str[1] = (char)(array.length & 0xffff);
    for ( int i =0; i < array.length; i++ )
    {
      if ( array[i] )
        str[i>>4] |= 1 << (i&0xf);
    }
    return new String(str);
  }

  public static String intArrayToString( int[] array )
  {
    char[] str = new char[array.length*2];
    for ( int i =0; i < array.length; i++ )
    {
      str[i*2] = (char) (array[i] >> 16);
      str[i*2+1] = (char) (array[i] & 0xffff);
    }
    return new String(str);
  }

  public static String longArrayToString( long[] array )
  {
    char[] str = new char[array.length*4];
    for ( int i =0; i < array.length; i++ )
    {
      str[i*2] = (char) (array[i] >> 48);
      str[i*2+1] = (char) ((array[i] >> 32)&0xffff);
      str[i*2+2] = (char) ((array[i] >> 16)&0xffff);
      str[i*2+3] = (char) (array[i] & 0xffff);
    }
    return new String(str);
  }

  public static void main(String argv[] )
  {
    while(true)
    {
      long start = System.currentTimeMillis();
      test();
      System.out.println(System.currentTimeMillis() - start);
    }
  }

  static void test()
  {
     int[] arr = new int[1<<20];
      for ( int i = 0; i < arr.length; i++ )
        arr[i] = i;
      String str = intArrayToString(arr);
      int[] arr2 = stringToIntArray(str);
      if ( arr.length != arr2.length )
        throw new RuntimeException();
      for ( int i =0; i < arr.length; i++ )
      {
        if ( arr[i] != arr2[i] )
          throw new RuntimeException();
      }
  }




}