package org.freeciv.util;
import java.io.*;
import java.util.*;
public class EnumParser
{
  static Writer out;
  static void output( String name, int val )
               throws IOException
  {
    out.write( "\tpublic static final int " + name + " = " + val + ";\n" );
  }
  static void parse( File file )
               throws IOException
  {
    if( file.isDirectory() )
    {
      String[] names = file.list();
      for( int i = 0;i < names.length;i++ )
      {
        parse( new File( file, names[ i ] ) );
      }
    }
    else
    {
      BufferedReader r = new BufferedReader( new FileReader( file ) );
      while( true )
      {
        String str = r.readLine();
        if( str == null )
        {
          break;
        }
        else
        {
          if( str.startsWith( "#define" ) )
          {
            StringTokenizer st = new StringTokenizer( str );
            st.nextToken();
            String name = st.nextToken();
            if( !st.hasMoreTokens() )
            {
              continue;
            }
            String val = st.nextToken();
            int ival = 0;
            try
            {
              ival = Integer.parseInt( val );
            }
            catch( NumberFormatException e )
            {
              continue;
            }
            output( name, ival );
          }
          else
          {
            if( str.startsWith( "enum" ) && ( str.indexOf( '(' ) < 0 ) )
            {
              int enumVal = 0;
              StringBuffer sb = new StringBuffer( 4000 );
              sb.append( str );
              do
              {
                str = r.readLine();
                sb.append( str );
                sb.append( ' ' );
              }while( ( str != null ) && ( str.indexOf( '}' ) < 0 ) );
              str = sb.toString();
              str = str.substring( 4 ).trim();
              int i = 0;
              while( Character.isUnicodeIdentifierPart( str.charAt( i ) ) || Character.isWhitespace( str.charAt( i ) ) )
              {
                i++;
              }
              String name = str.substring( 0, i ).trim();
              if( name.equals( "" ) )
              {
                continue;
              }
              out.write( "// enum " + name + "\n" );
              str = str.substring( str.indexOf( '{' ) + 1 ).trim();
              i = 0;
              while( true )
              {
                while( Character.isWhitespace( str.charAt( i ) ) )
                {
                  i++;
                }
                int j = i;
                while( ( str.charAt( j ) != '=' ) && ( str.charAt( j ) != ',' ) && ( str.charAt( j ) != '}' ) )
                {
                  j++;
                }
                name = str.substring( i, j );
                if( str.charAt( j ) == '=' )
                {
                  i = j;
                  while( Character.isWhitespace( str.charAt( i ) ) )
                  {
                    i++;
                  }
                  j = ++i;
                  while( ( str.charAt( j ) != ',' ) && ( str.charAt( j ) != '}' ) )
                  {
                    j++;
                  }
                  String val = str.substring( i, j ).trim();
                  try
                  {
                    enumVal = Integer.parseInt( val );
                  }
                  catch( NumberFormatException e )
                  {
                    enumVal = -123456;
                  }
                  out.write( "\tpublic static final int " + name + " = " + enumVal++ + ";\n" );
                }
                else
                {
                  out.write( "\tpublic static final int " + name + " = " + enumVal++ + ";\n" );
                }
                if( str.charAt( j ) == ',' )
                {
                  i = j + 1;
                  continue;
                }
                break;
              }
            }
          }
        }
      }
    }
  }
  public static void main( String[] argv )
                      throws IOException
  {
    try
    {
      out = new BufferedWriter( new FileWriter( "Constants.java" ) );
      out.write( "//machine generated - do not edit\n" );
      out.write( "package org.freeciv.client;\n" );
      out.write( "public interface Constants {\n" );
      for( int i = 0;i < argv.length;i++ )
      {
        parse( new File( argv[ i ] ) );
      }
      out.write( "}" );
    }
    finally
    {
      out.flush();
      out.close();
    }
  }
}
