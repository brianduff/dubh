package org.freeciv.common;


import javax.swing.Icon;
import java.awt.Color;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Straightforward XPM file parser, which is unusually strict, but
 * serves its purpose for most XPM files.
 */
public class XPMFile implements Sprite
{
  private static final boolean DEBUG = false;
  private String m_filename;
  private BufferedReader m_brInput;
  private int m_rows, m_columns, m_colors, m_colorchars;
  
  // TODO: This needs to be changed.
  private final int TRANSPARENT_COLOR = 0;
  private HashMap m_hashColors;
  
  //   int[] COLOR_MAP = new int[256];
  private String m_transparentColor;
  private int[] data;

  XPMFile( String filename )
  {
    m_filename = filename;
    loadFile( filename );
  }
  
  public XPMFile( URL url )
  {
    m_filename = url.toString();
    loadFile( url );
  }

  // TODO: the error handling in this class is crap.

  private boolean loadFile( URL url )
  {
    try
    {
      m_hashColors = new HashMap();
      m_brInput = new BufferedReader( new InputStreamReader( url.openStream() ) );
      parseFile();
      return true;
    }
    catch (IOException ioe)
    {
      return false;
    }
  }
  
  private boolean loadFile( String filename )
  {
    try
    {
      m_hashColors = new HashMap();
      openFile( filename );
      parseFile();
      return true;
    }
    catch( IOException ioe )
    {
      return false;
    }
  }
  public String getFilename()
  {
    return m_filename;
  }
  public Icon getIcon()
  {
    return new XPMImageIcon( this );
  }
  public Icon getSegmentIcon( int x, int y, int w, int h )
  {
    return new XPMImageIcon( this, x, y, w, h );
  }
  public int[] getData()
  {
    return data;
  }
  public int getRowCount()
  {
    return m_rows;
  }
  public int getColumnCount()
  {
    return m_columns;
  }
  private void openFile( String filename )
  {
    try
    {
      m_brInput = new BufferedReader( new InputStreamReader( new FileInputStream( filename ) ) );
      m_filename = filename;
    }
    catch( IOException ioe )
    {
      ioe.printStackTrace();
    }
  }
  private void parseFile()
                throws IOException
  {
    String line;
    line = m_brInput.readLine();
    line = m_brInput.readLine();
    readMetaData();
    data = new int[ m_rows * m_columns ];
    for( int i = 0;i < m_colors;i++ )
    {
      readColor();
    }
    for( int i = 0;i < m_rows;i++ )
    {
      readImageRow( i );
    }
  }
  private void readMetaData()
                throws IOException
  {
    String line = m_brInput.readLine();
    if( line == null )
    {
      throw new IllegalStateException( "File " + m_filename + " contains no data" );
    }
    String[] tokens = getWords( line.substring( 1, line.length() - 2 ) );
    if( tokens.length < 4 )
    {
      throw new IllegalStateException( "Malformed meta data line: " + line );
    }
    m_columns = Integer.parseInt( tokens[ 0 ] );
    m_rows = Integer.parseInt( tokens[ 1 ] );
    m_colors = Integer.parseInt( tokens[ 2 ] );
    m_colorchars = Integer.parseInt( tokens[ 3 ] );
  }
  
  /**
   * Read a color row and remember it in the color table.
   */
  private void readColor()
                throws IOException
  {
    String line = m_brInput.readLine();
    if( line == null )
    {
      throw new IllegalStateException( "File " + m_filename + " ended prematurely" );
    }
    String[] tokens = getWords( line.substring( 1, line.length() - 2 ) );
    String ch, flag, hexval;
    ch = line.substring( 1, 1 + m_colorchars );
    if( tokens.length == 2 )
    {
      flag = tokens[ 0 ];
      hexval = tokens[ 1 ];
    }
    else
    {
      flag = tokens[ 1 ];
      hexval = tokens[ 2 ];
    }
    
    // Deal with the transparent color
    if( "None".equalsIgnoreCase( hexval ) )
    {
      m_transparentColor = ch;
      m_hashColors.put( ch, new IntWrapper( TRANSPARENT_COLOR ) );
    }
    else
    {
      m_hashColors.put( ch, new IntWrapper( convertColorString( hexval ) ) );
    //COLOR_MAP[(int)c] = convertColorString(hexval);
    //debug("Stored color map for "+c+" ("+(int)c+") as "+COLOR_MAP[(int)c]);
    }
  }
  private class IntWrapper
  {
    public int value;
    public IntWrapper( int i ) 
    {
      value = i;
    }
  }
  private void readImageRow( int row )
                throws IOException
  {
    String line = m_brInput.readLine();
    if( line == null )
    {
      throw new IllegalStateException( "Premature end of file " + m_filename );
    }
    line = line.substring( 1, 1 + m_columns * m_colorchars );
    int offset = row * m_columns;
    for( int i = 0;i < m_columns;i++ )
    {
      int start = i * m_colorchars;
      String c = line.substring( start, start + m_colorchars );
      //System.out.println("->"+c+"<-");
      data[ offset + i ] = ( (IntWrapper)m_hashColors.get( c ) ).value;
    }
  }
  
  /**
   * Obtains an array of the words in a String.
   @param s the string
   @returns an array of Strings corresponding to the words in s
   */
  public static String[] getWords( String s )
  {
    StringBuffer stripTabs = new StringBuffer( s.length() );
    for( int i = 0;i < s.length();i++ )
    {
      if( s.charAt( i ) == '\t' )
      {
        stripTabs.append( ' ' );
      }
      else
      {
        stripTabs.append( s.charAt( i ) );
      }
    }
    return getTokens( stripTabs.toString(), " " );
  }
  
  /**
   * Obtains an array of tokens in a String, separated by any number of chars
   @param s the string
   @returns an array of Strings corresponding to the tokens in s
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
  private int convertColorString( String colString )
  {
    Color c = Color.decode( colString );
    int i = c.getRGB();
    
    //int j = (c.getGreen() << 24) | (c.getRed() << 16) | c.getBlue();
    return i;
  }
  private void debug( String s )
  {
    if( DEBUG )
    {
      System.out.println( "XPMFile:" + s );
    }
  }
  public static void main( String[] args )
                      throws IOException
  {
    XPMFile f = new XPMFile( args[ 0 ] );
  }
}
