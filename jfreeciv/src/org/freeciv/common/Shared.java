package org.freeciv.common;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Shared utilities
 */
public class Shared
{
  public static final String FREECIV_PATH = "freeciv.datadir";
  
  // bd: Have tested this with path separators of more than one char.
  public static final String PATH_SEPARATOR = System.getProperty( "path.separator" );
  public static final String DEFAULT_DATA_PATH = "."+PATH_SEPARATOR+"data"+PATH_SEPARATOR+"~/.freeciv";
  private static boolean m_bDataInited = false;
  private static ArrayList m_alDirs = new ArrayList();

  private static final String CLASSPATH_DATADIR = "data/";
  
  
  /**
   * This routine returns true if the capability in cap appears
   * in the capability list in capstr.  The capabilities in capstr
   * are allowed to start with a "+", but the capability in cap must not.
   */
  public static boolean hasCapability( String cap, String capstr )
  {
    String[] tokens = getTokens( capstr, " " );
    for( int i = 0;i < tokens.length;i++ )
    {
      String token = tokens[ i ];
      if( token.charAt( 0 ) == '+' )
      {
        token = token.substring( 1 );
      }
      if( cap.equals( token ) )
      {
        return true;
      }
    }
    return false;
  }
  
  /**
   * This routine returns true if all the mandatory capabilities in
   * us appear in them.
   */
  public static boolean hasCapabilities( String us, String them )
  {
    String[] usTok = getTokens( us, " " );
    for( int i = 0;i < usTok.length;i++ )
    {
      if( usTok[ i ].charAt( 0 ) == '+' && !hasCapability( usTok[ i ].substring( 1 ), them ) )
      {
        return false;
      }
    }
    return true;
  }
  
  /**
   * As getDataURL(), but bail out if the file doesn't exist.
   */
  public static final URL getDataURLRequired( String filename )
  {
    URL u = getDataURL( filename );
    if( u != null )
    {
      return u;
    }
    else
    {
      Logger.log( Logger.LOG_FATAL, 
        "Could not find readable file " + filename + " in data path" );
      System.exit( 1 );
    }
    return null;
  }
  
  /**
   * Returns a URL to access the specified file from a data directory;
   * The file may be in any of the directories in the data path, which is
   * specified internally or may be set as the java property freeciv.datadir
   * (A separated list of directories, where the separator
   * itself can specified internally.)
   * '~' at the start of a component (provided followed by '/' or the only char
   * in the string)
   * is expanded using the java user.home property.
   *
   * The URL can potentially be on the network, or inside a jar file: you 
   * should not make any assumptions about the location of the returned resource.
   *
   * Returns null if the specified filename cannot be found in any of the
   * data directories.  (A file is considered "found" if it can be read-opened.)
   *
   * @param file a file name that needs a full path
   * @returns a full path to the file.
   */
  public static final URL getDataURL( String filename )
  {
    Assert.that( filename != null );
  
    // Mostly lifted from shared.c, but some things are easier in Java (and
    // some are more cumbersome; no dodgy string pointer arithmetic here,
    // guv.)
    
    // This is 1 on all platforms I know of, but this makes sure our code is
    // totally platform independent.
    int fsLen = File.separator.length();
    if( !m_bDataInited )
    {
      String path = System.getProperty( FREECIV_PATH );
      String tok;
      if( path == null )
      {
        path = DEFAULT_DATA_PATH;
      }
      
      // This may be dodgetastic (i.e. wrong)
      String[] allToks = getTokens( path, PATH_SEPARATOR );
      for( int toki = 0;toki < allToks.length;toki++ )
      {
        tok = allToks[ toki ];
        int i;
        tok = tok.trim();
        if( !( File.separator.equals( tok ) ) )
        {
          tok = stripTrailingChars( tok, File.separator );
        }
        i = tok.length();
        
        // PF Issue: ~ forms part of everyday filenames on Win32 due to
        //   dodgy MS FAT* long filenames. Anybody who uses filenames with
        //   spaces in them is asking for trouble anyway, if you ask me :) bd
        //   Might not be a big issue, cos they rarely occur as the first
        //   character
        if( tok.charAt( 0 ) == '~' )
        {
          if( i > fsLen && !tok.substring( 1, 1 + fsLen ).equals( File.separator ) )
          {
            Logger.log( Logger.LOG_NORMAL, "For \"" + tok + "\" in data path cannot expand '~'" + " except as '~" + File.separator + "'; ignoring" );
            i = 0;
          }
          else
          {
            String home = System.getProperty( "user.home" );
            if( home == null )
            {
              Logger.log( Logger.LOG_VERBOSE, "Java user.home property is not set, skipping data " + "path component " + tok );
              i = 0;
            }
            else
            {
              tok = home + tok.substring( 1 ); // Skip the ~
            }
          }
        }
        if( i != 0 )
        {
          // Unlike freeciv C, we'll go and check the status of the dir,
          // since its dead easy in Java.
          File f = new File( tok );
          if( f.exists() && f.isDirectory() && f.canRead() )
          {
            Logger.log( Logger.LOG_VERBOSE, "Data path component (" + m_alDirs.size() + "):" + tok );
            m_alDirs.add( tok );
          }
          else
          {
            Logger.log( Logger.LOG_NORMAL, "\"" + tok + "\" in data path component " + ( !f.exists() ? "doesn't exist" : ( !f.isDirectory() ? "is not a directory" : "is not readable" ) ) + " ignoring" );
          }
        }
      }
      m_bDataInited = true;
    }
    /* NO LONGER SUPPORT THIS
    if( filename == null )
    {
      StringBuffer sb = new StringBuffer();
      for( int i = 0;i < m_alDirs.size();i++ )
      {
        sb.append( (String)m_alDirs.get( i ) );
        if( i < m_alDirs.size() - 1 )
        {
          sb.append( PATH_SEPARATOR );
        }
      }
      return sb.toString();
    }
    */
    for( int i = 0;i < m_alDirs.size();i++ )
    {
      String fname = (String)m_alDirs.get( i ) + File.separator + filename;
      File f = new File( fname );
      if( f.exists() && f.isFile() && f.canRead() )
      {
        try
        {
          return f.toURL();
        }
        catch (MalformedURLException mue )
        {
          Assert.fail( "Malformed URL Exception converting "+f.getPath()+
            " to a file: URL", mue
          );
        }
      }
    }


    // As the very last resort, we load stuff from the classpath. This is
    // the "default": i.e. in the absence of a data dir in the user's home
    // directory, the current directory, or specified by the system
    // property, we look in the classpath.


    StringBuffer resourcePath = new StringBuffer(CLASSPATH_DATADIR);
    resourcePath.append( filename );

    URL u = Shared.class.getResource( resourcePath.toString() );

    return u; // can be null if path not found.
  }
  
  /**
   * Strip the specified characters from a string if they exist.
   */
  public static String stripTrailingChars( String orig, String stripChars )
  {
    if( orig == null || stripChars == null )
    {
      throw new IllegalArgumentException( "Both orig and stripChars must not be null." );
    }
    int origLen = orig.length();
    int stripLen = stripChars.length();
    if( origLen == 0 || stripLen == 0 )
    {
      return orig;
    }
    if( origLen >= stripLen )
    {
      if( stripChars.equals( orig.substring( origLen - stripLen, origLen - 1 ) ) )
      {
        return orig.substring( 0, origLen - stripLen - 1 );
      }
    }
    return orig;
  }
  public static void main( String[] args )
  {
    System.out.println( getDataURL( args[ 0 ] ) );
  }
  
  
  /**
   * Obtains an array of tokens in a String, separated by any number of chars
   * @param s the string
   * @returns an array of Strings corresponding to the tokens in s
   */
  public static String[] getTokens( String s, String c )
  {
    ArrayList v = new ArrayList();
    StringTokenizer t = new StringTokenizer( s, c );
    String cmd[];
    while( t.hasMoreTokens() )
    {
      v.add( t.nextToken() );
    }
    cmd = new String[ v.size() ];
    for( int i = 0;i < cmd.length;i++ )
    {
      cmd[ i ] = (String)v.get( i );
    }
    return cmd;
  }
}
