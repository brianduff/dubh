package org.freeciv.util;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

import org.freeciv.common.Logger;

/**
 * Support for localization (language independent strings)
 *
 * JFreeciv's strings are stored in unix po format files. We decided to use
 * this mechanism rather than the standard java properties files so that
 * Freeciv's translated resources could be used in JFreeciv whenever possible.
 *
 * Nevertheless, it would be too limiting to just use the translated resources
 * from freeciv, so there is an extended jfreeciv specific set of po files.
 *
 * To translate a string, call translate(String message, Object[] args) or
 * translate( String )
 *
 * If the message is located in the jfreeciv po file, the translated result
 * is returned, failing that the freeciv po file is examined, and finally, if
 * all else fails, the original string is returned.
 *
 * @author Brian Duff
 */
public final class Localize
{
  private static final String PO_PATH = "po/";
  private static final String JAVA_PO_PATH = "poja/";
  private static final String PO_EXT = ".po";
  private static final String PO_SEP = "_";

  private static final String PO_MSGID = "msgid";
  private static final String PO_MSGSTR = "msgstr";

  private static HashMap m_poMap = null;

  /**
   * This class cannot be instantiated
   */
  private Localize() {}

  static
  {
    // Use the line below to test other locales
    // Locale.setDefault( Locale.CANADA_FRENCH );
    //Locale.setDefault( Locale.GERMAN );
    loadPO();
  }

  public static void loadPO()
  {
    
    Locale l = Locale.getDefault();


    m_poMap = new HashMap();

    // Load the two po files for the language and country. The java one
    // overrides the C one.
    tryLoadingPO( PO_PATH, l );
    tryLoadingPO( JAVA_PO_PATH, l );

  }

  /**
   * Attempt to load a po file for the specified language and country in the
   * specified location in the CLASSPATH
   *
   * If the country and language are not found, try just the language. If the
   * language is not found, do nothing (which causes strings to be returned
   * in the default locale and language, which is US English)
   *
   * @param base The path in the classpath for the po files
   * @param locale the locale 
   */
  private static void tryLoadingPO( String base, Locale locale)
  {
    String language = locale.getLanguage();
    String country = locale.getCountry();
  
    String poRes = base + language.toLowerCase() + PO_SEP + country.toUpperCase() +
      PO_EXT;
    URL poURL = Localize.class.getResource( poRes );

    Logger.log( Logger.LOG_NORMAL, "Trying to load po at: "+poURL+" ("+poRes+")" );

    if ( poURL == null )
    {
      Logger.log( Logger.LOG_NORMAL,
        "No "+base+" .po file for "+locale.getDisplayName()
      );
      poRes = base + language.toLowerCase() + PO_EXT;
      poURL = Localize.class.getResource( poRes );

      Logger.log( Logger.LOG_NORMAL, "Trying to load po at: "+poURL );
    }

    if ( poURL == null )
    {
      Logger.log( Logger.LOG_NORMAL, 
        "No "+base+" .po file for "+locale.getDisplayLanguage()+". Using English (United States)"
      );
    }
    else
    {
      loadPO( poURL );
    }
  }


  public static void main(String[] args) throws Exception
  {


    System.out.println(
      translate( "Game: %s have caused an incident while inciting a revolt in %s.", new Object[] { "Creme-Brulee", "Tattersberg" } )
    );
  }

  private static void loadPO( URL poURL )
  {
    try
    {
      InputStream pois = poURL.openStream();
      BufferedReader br = new BufferedReader( new InputStreamReader(pois) );

      int lineno = 0;
      String line = br.readLine();
      while ( line != null )
      {
        line = line.trim();
        if ( line.length() != 0 )
        {
          if ( line.indexOf( '#' ) == 0 )
          {
            line = br.readLine();
            continue; // Ignore lines that are comments
          }

          if ( line.indexOf( PO_MSGID ) == 0 )
          {
            readKey( line, br );
          }
        }

        lineno++;
        line = br.readLine();
      }

      br.close();
    }
    catch (IOException ioe)
    {
      m_poMap = null;
    }
  }

  private static void readKey( String line, BufferedReader br )
    throws IOException
  {
    String[] nextLine = new String[1];
    String theKey = readValue( PO_MSGID, line, br, nextLine );
    String theValue = readValue( PO_MSGSTR, nextLine[0], br, null );

    // Don't bother storing if the value is empty (we use the defualt)
    if ( theValue.length() > 0 )
    {
      m_poMap.put( theKey, theValue );
    }
  }

  private static String readValue( String keyword, String currentLine, BufferedReader br,
    String[] outNextLine )
    throws IOException
  {
    StringBuffer value = new StringBuffer();
    // Current line is an msgid
    currentLine = currentLine.substring( keyword.length() );
    currentLine = stripQuotes( currentLine );

    // Maybe the key is split over several lines
    if ( currentLine.length() == 0 )
    {
      String nextLine = br.readLine();
      while ( nextLine != null )
      {
        nextLine = nextLine.trim();


        if ( nextLine.indexOf( PO_MSGSTR ) >= 0 || nextLine.length() == 0 || nextLine.charAt(0) != '"' )
        {
          if ( outNextLine != null )
          {
            outNextLine[0] = nextLine;
          }
          return value.toString();
        }

        nextLine = stripQuotes( nextLine );

        value.append( nextLine );

        nextLine = br.readLine();
      }
    }
    else
    {
      value.append( currentLine );
      if (outNextLine != null)
      {
        outNextLine[0] = br.readLine();
      }

      return value.toString();
    }

    return "";
  }

  /**
   * Strip quotes at the start and end of a string
   *
   * @param line the line from which to strip quotes
   */
  private static String stripQuotes( String line )
  {
    line = line.trim();

    int length = line.length();
    if ( length == 0 )
    {
      return line;
    }

    if ( length == 2 )
    {
      if ( line.charAt(0) == '"' && line.charAt( 1 )== '"' )
      {
        return "";
      }
    }

    int start = 0;
    int count = length;

    if ( line.charAt( 0 ) == '"' )
    {
      start = 1;
      count--;
    }
    else
    {
      return line;
    }



    return line.substring( start, count );

  }

  public static String translate( String message )
  {
    return translate( message, null );
  }

  public static String translate( String message, Object[] args )
  {
    if (m_poMap == null)
    {
      return format( message, args );
    }

    String transText = (String) m_poMap.get( message );

    if ( transText == null || transText.length() == 0 )
    {
      return format( message, args );
    }
    else
    {
      return format( transText, args );
    }
  }

  /**
   * Substitutes % parameters in the original string with object values.
   * Unlike the C version, this doesn't care what the letter after the
   * % is, it just substitutes parameters in the correct order using
   * the toString() method on the objects in the subst array.
   */
  private static String format( String original, Object[] subst )
  {
    if ( subst == null )
    {
      return original;
    }

    int lastPos = -2;
    int nextPos;
    int curIndex = 0;
    nextPos = original.indexOf( '%' );
    if( nextPos < 0 )
    {
      return original;
    }
    StringBuffer bufStr = new StringBuffer( original.length() );
    while( nextPos >= 0 )
    {
      bufStr.append( original.substring( lastPos + 2, nextPos ) );
      if( original.charAt( nextPos + 1 ) == '%' )
      {
        bufStr.append( "%" );
      }
      else
      {
        bufStr.append( subst[ curIndex++ ].toString() );
      }
      lastPos = nextPos;
      nextPos = original.indexOf( '%', lastPos + 2 );
    }


    // append any remaining bits of the string.
    int lastSubst = original.lastIndexOf( '%' );
    if( lastSubst + 2 <= original.length() )
    {
      bufStr.append( original.substring( lastSubst + 2 ) );
    }
    return bufStr.toString();
  }
}