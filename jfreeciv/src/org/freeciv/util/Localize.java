package org.freeciv.util;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

/**
 * Support for localization. The correct .po file for  the current locale
 * is read on initialization. If not found, no translation takes place. The
 * .po files should exist on the CLASSPATH in the po path.
 *
 * To translate a string, call translate(String message, Object[] args).
 */
public final class Localize
{

  private static final String PO_ROOT="";
  private static final String PO_EXT = ".po";
  private static final String PO_SEP = "_";

  private static final String PO_MSGID = "msgid";
  private static final String PO_MSGSTR = "msgstr";

  private static HashMap m_poMap = null;

  private Localize() {}

  static
  {
    // Use the line below to test other locales
    //Locale.setDefault( Locale.CANADA_FRENCH );
    //Locale.setDefault( Locale.GERMAN );
    loadPO();
  }

  public static void loadPO()
  {
    Locale l = Locale.getDefault();

    String language = l.getLanguage();
    String country = l.getCountry();

    String poRes = PO_ROOT + language.toLowerCase() + PO_SEP + country.toUpperCase() + PO_EXT;

    URL poURL = ClassLoader.getSystemClassLoader().getResource( poRes );
    System.out.println("Looking for "+poRes );

    if ( poURL == null )
    {
      poRes = PO_ROOT + language.toLowerCase() + PO_EXT;
      poURL = ClassLoader.getSystemClassLoader().getResource( poRes );
      System.out.println("Looking for "+poRes );
    }

    if ( poURL != null )
    {
      loadPO( poURL );
    }
    else
    {
      System.out.println("No .po file for default locale: "+l.getDisplayName()+". Using English (United States)");
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
      m_poMap = new HashMap();
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