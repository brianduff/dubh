package org.freeciv.client;

import org.freeciv.common.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

import javax.swing.Icon;
import org.freeciv.net.*;


/**
 * Represents a tilespec.
 */
public class TileSpec implements Constants
{
  // roughly corresponds to tilespec.c

  private final static String XPM_EXT = "xpm";
  private final static String EXTENSIONS = XPM_EXT;
  private final static String TILESET_DEFAULT = "trident";
  private final static String TILESET_ISOMETRIC_DEFAULT = "hires";

  private String m_mainIntroFilename, m_minimapIntroFilename;

  private NamedSprites m_sprites = new NamedSprites();

  private int
    m_normalTileWidth, m_normalTileHeight,
    m_unitTileWidth, m_unitTileHeight,
    m_smallTileWidth, m_smallTileHeight;

  private boolean m_isIsometric;

  private String m_cityNamesFont;
  private String m_cityProductionsFontName;

  private boolean m_flagsAreTransparent = true;

  private int m_numTilesExplodeUnit = 0;

  private String[] m_specFiles;

  private HashMap m_images = new HashMap();

  private final static String TILESPEC_CAPSTR = "+tilespec2 duplicates_ok";
  private final static String SPEC_CAPSTR = "+spec2";

  private boolean m_isFocusUnitHidden = false;

  private boolean m_noBackdrop = false;

  private Client m_client;
  public TileSpec( Client c )
  {
    m_client = c;
  }

  public boolean areFlagsTransparent()
  {
    return m_flagsAreTransparent;
  }

  public boolean isIsometric()
  {
    return m_isIsometric;
  }

  private Options getOptions()
  {
    return getClient().getOptions();
  }

  public Client getClient()
  {
    return m_client;
  }
  public int getNormalTileWidth()
  {
    return m_normalTileWidth;
  }
  public int getNormalTileHeight()
  {
    return m_normalTileHeight;
  }
  public int getSmallTileHeight()
  {
    return m_smallTileHeight;
  }
  public int getSmallTileWidth()
  {
    return m_smallTileWidth;
  }
  // Convert into a java font here?
  public String getCityNamesFont()
  {
    return m_cityNamesFont;
  }

  public void loadTileset( String tilesetName )
  {
    readTopLevel( tilesetName );
    loadTiles();
    loadIntroFiles();
  }

  private void loadIntroFiles()
  {
    Sprite main = loadImage( m_mainIntroFilename );
    Sprite mini = loadImage( m_minimapIntroFilename );
    m_images.put( "main_intro_file", main.getIcon() );
    m_images.put( "minimap_intro_file", mini.getIcon() );
  }
  /**
   * Finds and reads the toplevel tilespec file based on given name.
   * Sets instance variables, including tile sizes and full names for
   * intro files.
   */
  private void readTopLevel( String tilesetName )
  {
    // tilespec.c: tilespec_read_top_level()

    String fname;

    fname = getTileSpecFullName( tilesetName );
    Logger.log( Logger.LOG_VERBOSE, "tilespec file is " + fname );

    Registry r = new Registry();
    try
    {
      r.loadFile( fname );
    }
    catch (IOException ioe )
    {
      Logger.log( Logger.LOG_FATAL, "Couldn't open \"" + fname + "\"" );
      Logger.log( Logger.LOG_FATAL, ioe );
      System.exit( 1 );
    }
    catch (RegistryParseException rpe)
    {
      Logger.log( Logger.LOG_FATAL, "Error parsing \""+ fname+"\"");
      Logger.log( Logger.LOG_FATAL, rpe );
      System.exit( 1 );
    }

    checkCapabilities( r, "tilespec", TILESPEC_CAPSTR, fname );

    r.lookup( "tilespec.name", null ); /* unused */

    m_isIsometric = (r.lookupInt( "tilespec.is_isometric" ) != 0);

    if (m_isIsometric && !isIsometricViewSupported())
    {
      Logger.log( Logger.LOG_ERROR,
        "Client does not support isometric tilesets. Using default tileset instead."
      );
      readTopLevel( null );
      return;
    }

    if (!m_isIsometric && !isOverheadViewSupported())
    {
      Logger.log( Logger.LOG_ERROR,
        "Client does not support overhead view tilesets. Using default tileset instead."
      );
      readTopLevel( null );
      return;
    }

    m_normalTileWidth = r.lookupInt( "tilespec.normal_tile_width" );
    m_normalTileHeight = r.lookupInt( "tilespec.normal_tile_height" );



    if (m_isIsometric)
    {
      m_unitTileWidth = m_normalTileWidth;
      m_unitTileHeight = 3 * m_normalTileHeight/2;
    }
    else
    {
      m_unitTileWidth = m_normalTileWidth;
      m_unitTileHeight = m_normalTileHeight;
    }
    m_smallTileWidth = r.lookupInt( "tilespec.small_tile_width" );
    m_smallTileHeight = r.lookupInt( "tilespec.small_tile_height" );
    Logger.log( Logger.LOG_VERBOSE, "Tile sizes: " + m_normalTileWidth + "," + m_normalTileHeight + " normal " + m_smallTileWidth + "," + m_smallTileHeight + " small " + m_unitTileWidth + ","+m_unitTileHeight+" unit" );

    m_cityNamesFont = r.lookupString( "10x20", "tilespec.city_names_font" );
    m_cityProductionsFontName = r.lookupString( "8x16", "tilespec.city_productions_font" );
    m_flagsAreTransparent = ( r.lookupInt( "tilespec.flags_are_transparent" ) != 0 );

    m_mainIntroFilename = getGFXFilename( r.lookupString( "tilespec.main_intro_file" ) );
    Logger.log( Logger.LOG_DEBUG, "intro file " + m_mainIntroFilename );

    m_minimapIntroFilename = getGFXFilename( r.lookupString( "tilespec.minimap_intro_file" ) );
    Logger.log( Logger.LOG_DEBUG, "radar file " + m_minimapIntroFilename );

    m_specFiles = r.lookupStringList( "tilespec.files" );
    if( m_specFiles.length == 0 )
    {
      Logger.log( Logger.LOG_FATAL, "No tile files specified in " + fname );
      System.exit( 1 );
    }
    for( int i = 0;i < m_specFiles.length;i++ )
    {
      m_specFiles[ i ] = Shared.getDataFilenameRequired( m_specFiles[ i ] );
      if( Logger.DEBUG )
      {
        Logger.log( Logger.LOG_DEBUG, "Spec file " + m_specFiles[ i ] );
      }
    }
    Logger.log( Logger.LOG_VERBOSE, "Finished reading" + fname );
  }

  /**
   * Given a base filename, get a proper, full path including the correct
   * extension for the graphics file.
   */
  static String getGFXFilename( String filename )
  {
    // tilespec.c: tilespec_gfx_filename()

    String[] extTok = Shared.getTokens( EXTENSIONS, " " );
    for( int i = 0;i < extTok.length;i++ )
    {
      String fullName = filename + "." + extTok[ i ];
      fullName = Shared.getDataFilename( fullName );
      if( fullName != null )
      {
        return fullName;
      }
    }
    Logger.log( Logger.LOG_FATAL, "Couldn't find a supported gfx file extension for " + filename );
    System.exit( 1 );
    return null;
  }

  /**
   * In c-land, this is implemented differently by each client implementation.
   * For java, we don't support multiple client flavours, and just always
   * support whatever we can. Inclusing isometrics...
   *
   * @return true (eventually)
   */
  private static boolean isIsometricViewSupported()
  {
    return false; // Working on it...
  }

  private static boolean isOverheadViewSupported()
  {
    return true;
  }

  /**
   * Gets full filename for tilespec file, based on input name.
   * Returned data is allocated, and freed by user as required.
   * Input name may be null, in which case uses default.
   * Falls back to default if can't find specified name;
   * dies if can't find default.
   */
  static String getTileSpecFullName( String tilesetName )
  {
    // tilespec.c: tilespec_fullname()
    int level;
    StringBuffer fname;
    String dname;
    String tileset_default;

    if (isIsometricViewSupported())
    {
      tileset_default = TILESET_ISOMETRIC_DEFAULT;
    }
    else
    {
      tileset_default = TILESET_DEFAULT;
    }

    if( tilesetName == null )
    {
      tilesetName = tileset_default;
    }

    fname = new StringBuffer( tilesetName );
    fname.append( ".tilespec" );
    dname = Shared.getDataFilename( fname.toString() );

    if( dname != null )
    {
      return dname;
    }
    if( tileset_default.equals( tilesetName ) )
    {
      level = Logger.LOG_FATAL;
    }
    else
    {
      level = Logger.LOG_NORMAL;
    }
    Logger.log( level, "Could not find readable file " + fname + " in data path." );
    Logger.log( level, "The data path may be set via the Java system" + " property " + Shared.FREECIV_PATH + "." );
    Logger.log( level, "Current data path is " + Shared.getDataFilename( null ) );
    if( level == Logger.LOG_FATAL )
    {
      System.exit( 1 );
    }
    Logger.log( level, "Trying " + TILESET_DEFAULT + " tileset." );
    return getTileSpecFullName( TILESET_DEFAULT );
  }
  /**
   * Checks options in filename match what we require and support.
   * Die if not.
   * @param r a Registry file (called a section_file) now.
   * @param which Either "tilespec" or "spec"
   * @param us_capstr our capabilities
   * @param filename the filename to check
   */
  static void checkCapabilities( Registry r, String which, String us_capstr, String filename )
  {
    String file_capstr = r.lookupString( "%s.options", new Object[]
    {
      which
    } );
    if( !Shared.hasCapabilities( us_capstr, file_capstr ) )
    {
      Logger.log( Logger.LOG_FATAL, which + " file appears incompatible:" );
      Logger.log( Logger.LOG_FATAL, "file: \"" + filename + "\"" );
      Logger.log( Logger.LOG_FATAL, "file options: " + file_capstr );
      Logger.log( Logger.LOG_FATAL, "supported options: " + us_capstr );
      System.exit( 1 );
    }
    if( !Shared.hasCapabilities( file_capstr, us_capstr ) )
    {
      Logger.log( Logger.LOG_FATAL, which + " claims required option(s) which we don't support:" );
      Logger.log( Logger.LOG_FATAL, "file: \"" + filename + "\"" );
      Logger.log( Logger.LOG_FATAL, "file options: " + file_capstr );
      Logger.log( Logger.LOG_FATAL, "supported options: " + us_capstr );
      System.exit( 1 );
    }
  }
  /**
   * Load the tiles; requires tilespec_read_toplevel() called previously.
   */
  void loadTiles()
  {
    if( m_specFiles == null || m_specFiles.length < 1 )
    {
      throw new IllegalStateException( "Must call readTopLevel first." );
    }
    for( int i = 0;i < m_specFiles.length;i++ )
    {
      loadOne( m_specFiles[ i ] );
    }

    lookupSpriteTags();
  }
  /**
   * Load one specfile and specified xpm file; splits xpm into tiles,
   * and save sprites in the hash table
   */
  void loadOne( String specFilename )
  {
    if( Logger.DEBUG )
    {
      Logger.log( Logger.LOG_DEBUG, "loading spec " + specFilename );
    }
    Registry file = new Registry();
    try
    {
      file.loadFile( specFilename );
    }
    catch (IOException ioe )
    {
      Logger.log( Logger.LOG_FATAL, "Could not open " + specFilename );
      Logger.log( Logger.LOG_FATAL, ioe );
      System.exit( 1 );
    }
    catch ( RegistryParseException rpe )
    {
      Logger.log( Logger.LOG_FATAL, "Failed to parse "+ specFilename );
      Logger.log( Logger.LOG_FATAL, rpe );
      System.exit( 1 );
    }
    checkCapabilities( file, "spec", SPEC_CAPSTR, specFilename );
    file.lookup( "info.artists" ); /* Unused */
    String gfx_filename = file.lookupString( "file.gfx" );
    String[] tok = Shared.getTokens( EXTENSIONS, " " );
    Sprite big_image = null;

    for (int i=0;  big_image == null && i < tok.length; i++ )
    {
      String full_name = gfx_filename + "." + tok[ i ];
      String real_full_name = Shared.getDataFilename( full_name );
      if( real_full_name != null )
      {
        if( Logger.DEBUG )
        {
          Logger.log( Logger.LOG_DEBUG, "Trying to load gfx file " + real_full_name );
        }
        big_image = loadImage( real_full_name );
        if( big_image == null )
        {
          Logger.log( Logger.LOG_VERBOSE, "Loading the gfx file " + real_full_name + " failed" );
        }
      }
    }

    if( big_image == null )
    {
      Logger.log( Logger.LOG_FATAL, "Couldn't load gfx file for the spec file " + specFilename );
      System.exit( 1 );
    }

    ArrayList gridNames = file.getSecNamesPrefix( "grid_" );
    if( gridNames == null )
    {
      Logger.log( Logger.LOG_FATAL, "Spec " + specFilename + " has no grid_* sections." );
      System.exit( 1 );
    }
    int x_top_left, y_top_left, dx, dy, x1, y1;
    int row, col;
    String[] tags;
    for( int c = 0;c < gridNames.size();c++ )
    {
      String key = (String)gridNames.get( c );
      Object[] subst = new Object[]
      {
        key
      };

      boolean is_pixel_border =
        (file.lookupInt(0, "%s.is_pixel_border", subst) != 0);
      x_top_left = file.lookupInt( "%s.x_top_left", subst );
      y_top_left = file.lookupInt( "%s.y_top_left", subst );
      dx = file.lookupInt( "%s.dx", subst );
      dy = file.lookupInt( "%s.dy", subst );
      int j = -1;
      Object[] mySubst;
      while( file.lookup( "%s.tiles%d.tag", ( mySubst = new Object[]
      {
        key, new Integer( ++j )
      } ) ) )
      {
        row = file.lookupInt( "%s.tiles%d.row", mySubst );
        col = file.lookupInt( "%s.tiles%d.column", mySubst );
        tags = file.lookupStringList( "%s.tiles%d.tag", mySubst );
        if( tags.length == 0 )
        {
          throw new IllegalStateException( "There should be > 0 tags!" );
        }
        x1 = x_top_left + col * dx + (is_pixel_border ? col : 0);
        y1 = y_top_left + row * dy + (is_pixel_border ? row : 0);

        for( int k = 0;k < tags.length;k++ )
        {
          m_images.put( tags[ k ], big_image.getSegmentIcon( x1, y1, dx, dy ) );
        }
      }
    }
    if( Logger.DEBUG )
    {
      Logger.log( Logger.LOG_DEBUG, "Finished " + specFilename );
    }
  }
  /**
   * Get the specified image. May return null if the image doesn't
   * exist.
   */
  public Icon getImage( String key )
  {
    if( key == null )
    {
      throw new IllegalArgumentException( "Key must != null" );
    }
    Icon i = (Icon)m_images.get( key );


    return i;
  }
  private Sprite loadImage( String filename )
  {
    // Get the extension.
    int dotPos = filename.lastIndexOf( '.' );
    if( dotPos < 0 )
    {
      return null;
    }
    String strExt = filename.substring( dotPos + 1 );
    // Figure out how to load the image based on its extension.
    if( XPM_EXT.equalsIgnoreCase( strExt ) )
    {
      return new XPMFile( filename );
    }
    return null;
  }

  //
  // These behave like boolean values, e.g. NESW_STRINGS[5] = n0e1s0w1
  // (5d = 0101b)

  private final static String[] NSEW_STRINGS =
  {
    "n0s0e0w0", "n0s0e0w1", "n0s0e1w0", "n0s0e1w1", "n0s1e0w0",
    "n0s1e0w1", "n0s1e1w0", "n0s1e1w1", "n1s0e0w0", "n1s0e0w1",
    "n1s0e1w0", "n1s0e1w1", "n1s1e0w0", "n1s1e0w1", "n1s1e1w0",
    "n1s1e1w1"
  };


  private static final int DIR_NORTH = 8;
  private static final int DIR_EAST = 2;
  private static final int DIR_SOUTH = 4;
  private static final int DIR_WEST = 1;

  private static final int NUM_DIRECTION_NSEW = NSEW_STRINGS.length;


  private int getFullTerrainId( int type, int variation )
  {
    return ( ( type << 5 ) + variation );
  }
  /**
   * Get a number between 0 and 15 which represents the variation of
   * terrain based on squares around it.
   *
   * @param north whether the tile to the north matches the current tile
   * @param east  whether the tile to the east matches the current tile
   * @param south whether the tile to the south matches the current tile
   * @param west  whether the tile to the west matches the current tile
   * @return a value between 0-15 which encodes the four conditions
   *   in binary
   */
  private int getVariationID( boolean north, boolean south, boolean east, boolean west )
  {
    // INDEX_NSEW in tilespec.h
    return ( north ? DIR_NORTH : 0 ) + ( south ? DIR_SOUTH : 0 ) + ( east ? DIR_EAST : 0 ) + ( west ? DIR_WEST : 0 );
  }



  private void lookupSpriteTags()
  {
    // tilespec.c: tilespec_lookup_sprite_tags()
    m_sprites.init(this);

    // More to do here ?
  }

  /**
   * Look up the sprite for a tag, or else lookup alternative tag.
   *
   * @param tag the tag to look up
   * @param alt the alternative tag
   * @param isRequired controls the log level if sprites are not found
   * @param what a text description for the log of what is being looked up
   * @param name a text description for the log of the actual item being
   *    looked up
   *
   * @return the image for tag or alt, or null if neither image could be found
   */
  public Icon lookupSpriteTagAlt(String tag, String alt, boolean isRequired,
    String what, String name)
  {
    Icon s;

    s = (Icon) getImage(tag);
    if (s != null)
    {
      return s;
    }

    s = (Icon) getImage(alt);
    if ( s != null )
    {
      Logger.log(isRequired ? Logger.LOG_NORMAL : Logger.LOG_DEBUG,
        "Using alternate graphic "+alt+" instead of "+tag+" for "+what+" "+name
      );
      return s;
    }

    Logger.log(isRequired ? Logger.LOG_NORMAL : Logger.LOG_DEBUG,
      "Don't have graphics tags "+tag+" or "+alt+" for "+what+" "+name
    );

    return null;
  }

  private TerrainType getTerrainType( int ttype )
  {
    return  (TerrainType) getClient().getFactories().getTerrainTypeFactory().findById( ttype );
  }


  /**
   * The ruleset terrain packet handler calls this to set up the sprites
   * for terrain
   *
   * @param id the id of the terrain type to set up
   */
  public void setupTileType( int id )
  {
    // tilespec.c:tilespec_setup_tile_type( id )
    TerrainType tt = (TerrainType)
      getClient().getFactories().getTerrainTypeFactory().findById( id );

    StringBuffer buffer1 = new StringBuffer( 100 );
    StringBuffer buffer2 = new StringBuffer( 100 );
    String nsew;

    int i;

    if ( tt.getName().length() == 0 )
    {
      for ( i = 0; i < NUM_DIRECTION_NSEW; i++)
      {
        tt.setSprite( i, null );
      }
      for ( i = 0; i < 2; i++)
      {
        tt.setSpecialSprite( i, null );
      }
      return;
    }

    if ( m_isIsometric )
    {
      if ( id != T_RIVER )
      {
        tt.setSprite( 0,
          lookupSpriteTagAlt(
            tt.getGraphicStr(), null, true, "tile_type", tt.getName()
          )
        );
      }
      else
      {
        tt.setSprite( 0, null );
      }
    }
    else
    {
      for ( i = 0; i < NUM_DIRECTION_NSEW; i++)
      {
        nsew = NSEW_STRINGS[ i ];
        buffer1.setLength( 0 );
        buffer1.append( tt.getGraphicStr() );
        buffer1.append( '_' );
        buffer1.append( nsew );
        buffer2.setLength( 0 );
        buffer2.append( tt.getGraphicAlt() );
        buffer2.append( '_' );
        buffer2.append( nsew );

        tt.setSprite( i,
          lookupSpriteTagAlt(
            buffer1.toString(), buffer2.toString(), true, "tile_type",
            tt.getName()
          )
        );
      }
    }

    for ( i = 0; i < 2; i++)
    {
      String name = tt.getSpecialName( i );
      if ( name != null && name.length() != 0 )
      {
        tt.setSpecialSprite( i,
          lookupSpriteTagAlt(
            tt.getSpecialGraphicStr( i ), tt.getSpecialGraphicAlt( i ),
            true, "tile_type special", name
          )
        );
      }
      else
      {
        tt.setSpecialSprite( i, null );
      }
    }

  }

  /**
   * Called by the ruleset government packet handler to initialize the sprite
   * for a government.
   *
   * @param id the government to initialize
   */
  public void setupGovernment( Government gov )
  {
    gov.setSprite( lookupSpriteTagAlt(
      gov.getGraphicStr(), gov.getGraphicAlt(), true, "government",
      gov.getName()
    ));
  }

  /**
   * Called by the ruleset nation packet handler to initialize the sprite
   * for a nation flag
   *
   * @param id the nation to initialize
   */
  public void setupNationFlag( Nation n )
  {
    n.setFlagSprite( lookupSpriteTagAlt(
      n.getFlagGraphicStr(), n.getFlagGraphicAlt(), true, "nation",
      n.getName()
    ));
  }

  /**
   * Called by the ruleset unit packet handler to initialize the sprite for
   * a unit type.
   *
   * @param id the unit type to initialize
   */
  public void setupUnitType( UnitType ut )
  {
    ut.setSprite( lookupSpriteTagAlt(
      ut.getGraphicStr(), ut.getGraphicAlt(),
      true, // unit_type_exists()
      "unit_type", ut.getName()
    ));
  }

  /**
   */
  private void setupStyleTile( CityStyle style, String graphics )
  {
    style.setNumTiles( 0 );
    StringBuffer buffer = new StringBuffer( 128 );
    StringBuffer bufferWall = new StringBuffer( 128 );
    Icon sp = null, sp_wall = null;

    for ( int j = 0; j < 32 && style.getNumTiles() < style.MAX_CITY_TILES; j++ )
    {
      buffer.setLength( 0 );
      buffer.append( graphics );
      buffer.append( "_" );
      buffer.append( String.valueOf( j ) );

      sp = getImage( buffer.toString() );

      if ( isIsometric() )
      {
        bufferWall.setLength( 0 );
        bufferWall.append( graphics );
        bufferWall.append( "_" );
        bufferWall.append( String.valueOf( j ) );
        bufferWall.append( "_wall" );

        sp_wall = getImage( bufferWall.toString() );
      }

      if ( sp != null )
      {
        style.setTileSprite( style.getNumTiles(), sp );

        if ( isIsometric() )
        {
          Assert.that( sp_wall != null );
          style.setWallSprite( style.getNumTiles(), sp_wall );
        }
        style.setThreshold( style.getNumTiles(), j );
        style.setNumTiles( style.getNumTiles() + 1 );
      }
    }

    if ( style.getNumTiles() == 0 )
    {
      return;
    }

    if (! isIsometric() )
    {
      buffer.setLength( 0 );
      buffer.append( graphics );
      buffer.append( "_wall" );

      sp = getImage( buffer.toString() );

      if ( sp != null )
      {
        style.setTileSprite( style.getNumTiles(), sp );
      }
    }

    buffer.setLength( 0 );
    buffer.append( graphics );
    buffer.append( "_occupied" );
    sp = getImage( buffer.toString() );
    if ( sp != null )
    {
      style.setTileSprite( style.getNumTiles()+1, sp );
    }
  }

  public void setupCityTiles( CityStyle style )
  {
    setupStyleTile( style, style.getGraphic() );

    if ( style.getNumTiles() == 0 )
    {
      setupStyleTile ( style, style.getGraphicAlt() );
    }

    if ( style.getNumTiles() == 0 )
    {
        style.setTileSprite( 0, getImage( "cd.city" ) );
        style.setTileSprite( 1, getImage( "cd.city_wall" ) );
        style.setTileSprite( 2, getImage( "cd.occupied" ) );
        style.setNumTiles( 1 );
        style.setThreshold( 0, 0 );
    }
  }

  public void setTerrain( PktTileInfo pkt, boolean update )
  {
     // TODO
  }

}
