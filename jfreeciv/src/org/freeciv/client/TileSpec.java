package org.freeciv.client;

import org.freeciv.common.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import org.freeciv.net.*;

//// For test harness
import javax.swing.*;
import java.awt.BorderLayout;
/////
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
  private static final int NUM_TILES_HP_BAR = 11;

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
    if( Logger.DEBUG )
    {



    //         Logger.log(Logger.LOG_DEBUG,
    //            "Looked up image >>"+key+"<< found: "+i
    //         );
    }
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
  //
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

  public boolean getIsometricSprites( List sprites, List coasts, List dither,
    int x, int y, boolean cityMode, boolean[] solidBg )
  {
    org.freeciv.common.Map map = getClient().getGame().getMap();

    int ttype, ttype_north, ttype_south, ttype_east, ttype_west;
    int ttype_north_east, ttype_south_east, ttype_south_west, ttype_north_west;
    int tspecial, tspecial_north, tspecial_south, tspecial_east, tspecial_west;
    int tspecial_north_east, tspecial_south_east, tspecial_south_west, tspecial_north_west;

    int ttype_near[] = new int [NUM_DIRECTION_NSEW ];
    int tspecial_near[] = new int [NUM_DIRECTION_NSEW ];

    int tileno;
    Tile tile;
    org.freeciv.common.City city;
    int dir, i;

    solidBg[0] = false;

    MapPosition pos = new MapPosition( x, y );

    if ( !map.normalizeMapPosition( pos ) )
    {
      return false;
    }
    x = pos.x;
    y = pos.y;

    tile = map.getTile( x, y );

    if ( tile.getKnown() == TILE_UNKNOWN )
    {
      return false;
    }

    city = tile.getCity();
    ttype = tile.getTerrain();
    tspecial = tile.getSpecial();

    if ( ttype == T_RIVER )
    {
      ttype = T_GRASSLAND;
      tspecial |= S_RIVER;
    }

    for ( dir = 0 ; dir < NUM_DIRECTION_NSEW; dir++)
    {
      MapPosition mp = new MapPosition(
        x + map.DIR_DX2[ dir ],
        y + map.DIR_DY2[ dir ]
      );
      if ( map.normalizeMapPosition( mp ) )
      {
        ttype_near[ dir ] = map.getTerrain( mp.x, mp.y );
        tspecial_near[ dir ] = map.getSpecial( mp.x, mp.y );

        if ( ttype_near[dir] == T_RIVER )
        {
          ttype_near[dir] = T_GRASSLAND;
          tspecial_near[dir] |= S_RIVER;
        }
      }
      else
      {
        ttype_near[dir] = T_UNKNOWN;
        tspecial_near[dir] = S_NO_SPECIAL;
      }
    }
    int idx = 0;
    ttype_north = ttype_near[idx++];
    ttype_north_east = ttype_near[idx++];
    ttype_east = ttype_near[idx++];
    ttype_south_east = ttype_near[idx++];
    ttype_south = ttype_near[idx++];
    ttype_south_west = ttype_near[idx++];
    ttype_west = ttype_near[idx++];
    ttype_north_west = ttype_near[idx++];

    idx = 0;
    tspecial_north = ttype_near[idx++];
    tspecial_north_east = ttype_near[idx++];
    tspecial_east = ttype_near[idx++];
    tspecial_south_east = ttype_near[idx++];
    tspecial_south = ttype_near[idx++];
    tspecial_south_west = ttype_near[idx++];
    tspecial_west = ttype_near[idx++];
    tspecial_north_west = ttype_near[idx++];

    if (getOptions().drawTerrain)
    {
      if ( ttype != T_OCEAN)
      {
        sprites.add( getTerrainType( ttype ).getSprite( 0 ) );
      }

      if (ttype == T_HILLS)
      {
        tileno = getVariationID((ttype_north==T_HILLS || ttype_north==T_HILLS),
                        (ttype_south==T_HILLS || ttype_south==T_HILLS),
                        (ttype_east==T_HILLS || ttype_east==T_HILLS),
                        (ttype_west==T_HILLS || ttype_west==T_HILLS));
        sprites.add( getImage( "tx.s_hill_" + NSEW_STRINGS[ tileno ] ) );
      }

      if (ttype == T_FOREST)
      {
        tileno = getVariationID((ttype_north==T_FOREST || ttype_north==T_FOREST),
                        (ttype_south==T_FOREST || ttype_south==T_FOREST),
                        (ttype_east==T_FOREST || ttype_east==T_FOREST),
                        (ttype_west==T_FOREST || ttype_west==T_FOREST));
        sprites.add( getImage( "tx.s_forest_" + NSEW_STRINGS[ tileno ] ) );
      }

      if (ttype == T_MOUNTAINS)
      {
        tileno = getVariationID((ttype_north==T_MOUNTAINS || ttype_north==T_MOUNTAINS),
                        (ttype_south==T_MOUNTAINS || ttype_south==T_MOUNTAINS),
                        (ttype_east==T_MOUNTAINS || ttype_east==T_MOUNTAINS),
                        (ttype_west==T_MOUNTAINS || ttype_west==T_MOUNTAINS));
        sprites.add( getImage( "tx.s_mountain_"+NSEW_STRINGS[ tileno ] ) );
      }

      if ((tspecial&S_RIVER) != 0)
      {
        tileno = getVariationID(((tspecial_north&S_RIVER) != 0 || ttype_north==T_OCEAN),
                        ((tspecial_south&S_RIVER) != 0 || ttype_south==T_OCEAN),
                        ((tspecial_east&S_RIVER) != 0 || ttype_east==T_OCEAN),
                        ((tspecial_west&S_RIVER) != 0 || ttype_west==T_OCEAN));
        sprites.add( getImage( "tx.s_river_" + NSEW_STRINGS[ tileno ] ) ) ;
      }

      if (ttype == T_OCEAN)
      {
        if( (tspecial_north&S_RIVER) != 0 || ttype_north==T_RIVER)
          sprites.add( getImage( "tx.river_outlet_n" ) );
        if( (tspecial_west&S_RIVER) != 0 || ttype_west==T_RIVER)
          sprites.add( getImage( "tx.river_outlet_w" ) );
        if( (tspecial_south&S_RIVER) != 0 || ttype_south==T_RIVER)
          sprites.add( getImage( "tx.river_outlet_s" ) );
        if( (tspecial_east&S_RIVER) != 0 || ttype_east==T_RIVER)
          sprites.add( getImage( "tx.river_outlet_e" ) );
      }

    }
    else
    {
      solidBg[0] = true;
    }

    if (getOptions().drawSpecials)
    {
      if ( (tspecial & S_SPECIAL_1) != 0 )
      {
        sprites.add( getTerrainType( ttype ).getSpecialSprite( 0 ) );
      }
      else if ( (tspecial & S_SPECIAL_2) != 0 )
      {
        sprites.add( getTerrainType( ttype ).getSpecialSprite ( 1 ) );
      }
    }

    if ( (tspecial & S_MINE) != 0 && getOptions().drawMines )
    {
      /* We do not have an oil tower in isometric view yet... */
      sprites.add( getImage( "tx.mine" ) );
    }

    if ( (tspecial & S_IRRIGATION) != 0  && city == null && getOptions().drawIrrigation )
    {
      if ( (tspecial & S_FARMLAND) != 0)
      {
        sprites.add( getImage( "tx.farmland" ) );
      }
      else
      {
        sprites.add( getImage( "tx.irrigation" ) );
      }
    }

    if ((tspecial & S_RAILROAD) != 0 && getOptions().drawRoadsRails)
    {
      boolean found = false;

      for (dir=0; dir<NUM_DIRECTION_NSEW; dir++)
      {
        if ( (tspecial_near[dir] & S_RAILROAD) != 0)
        {
          sprites.add( getImage( "r.rail"+dir ) );
          found = true;
        }
        else if ( (tspecial_near[dir] & S_ROAD) != 0)
        {
          sprites.add( getImage( "r.road"+dir ) ) ;
          found = true;
        }
      }

      if (!found && city == null)
      {
        sprites.add( getImage( "r.rail_isolated" ) );
      }

    }
    else if ( (tspecial & S_ROAD) != 0 && getOptions().drawRoadsRails )
    {
      boolean found = false;

      for (dir=0; dir<NUM_DIRECTION_NSEW; dir++)
      {
        if ( (tspecial_near[dir] & S_ROAD) != 0)
        {
          sprites.add( getImage( "r.road"+dir ) );
          found = true;
        }
      }

      if (!found && city == null)
      {
        sprites.add( getImage( "r.road_isolated" ) );
      }
    }

    if ( (tspecial & S_HUT) != 0 && getOptions().drawSpecials)
    {
      sprites.add( getImage( "tx.village" ) );
    }

    /* put coasts */
    if (ttype == T_OCEAN)
    {
      for (i = 0; i < 4; i++)
      {
        String loc = "";


        int[] ttype_adj = new int[3];
        int array_index;
        switch (i)
        {
          case 0: /* up */
            ttype_adj[0] = ttype_west;
            ttype_adj[1] = ttype_north_west;
            ttype_adj[2] = ttype_north;
            loc = "u";
            break;
          case 1: /* down */
            ttype_adj[0] = ttype_east;
            ttype_adj[1] = ttype_south_east;
            ttype_adj[2] = ttype_south;
            loc = "d";
            break;
          case 2: /* left */
            ttype_adj[0] = ttype_south;
            ttype_adj[1] = ttype_south_west;
            ttype_adj[2] = ttype_west;
            loc = "l";
            break;
          case 3: /* right*/
            ttype_adj[0] = ttype_north;
            ttype_adj[1] = ttype_north_east;
            ttype_adj[2] = ttype_east;
            loc = "r";
            break;
          default:
            Assert.fail();
        }

        array_index = (ttype_adj[0] != T_OCEAN ? 1 : 0)
          +  (ttype_adj[1] != T_OCEAN ? 2 : 0)
          +  (ttype_adj[2] != T_OCEAN ? 4 : 0);
        coasts.add( getImage( "tx.coast_cape_"+loc+i) );
      }
    }

    dither.add(getDither(ttype, map.getTile(x, y-1).isKnown() ? ttype_north : T_UNKNOWN));
    dither.add(getDither(ttype, map.getTile(x, y+1).isKnown() ? ttype_south : T_UNKNOWN));
    dither.add(getDither(ttype, map.getTile(x+1, y).isKnown() ? ttype_east : T_UNKNOWN));
    dither.add(getDither(ttype, map.getTile(x-1, y).isKnown() ? ttype_west : T_UNKNOWN));

    return true;

  }

  private TerrainType getTerrainType( int ttype )
  {
    return  (TerrainType) getClient().getFactories().getTerrainTypeFactory().findById( ttype );
  }

  /**
   * For the tile at position abs_x0, abs_y0, get an ordered list of all
   * sprites to be displayed on that tile.
   *
   */
  public List getSpritesAt( int abs_x0, int abs_y0,
    boolean cityMode, boolean[] solidBg, Player[] player)
  {
    // fill_tile_sprite_array in tilespec.c

    org.freeciv.common.Map map = getClient().getGame().getMap();

    int ttype, ttype_north, ttype_south, ttype_east, ttype_west;
    int ttype_north_east, ttype_south_east, ttype_south_west, ttype_north_west;
    int tspecial, tspecial_north, tspecial_south, tspecial_east, tspecial_west;
    int tspecial_north_east, tspecial_south_east, tspecial_south_west, tspecial_north_west;
    int rail_card_tileno=0, rail_semi_tileno=0, road_card_tileno=0, road_semi_tileno=0;
    int rail_card_count=0, rail_semi_count=0, road_card_count=0, road_semi_count=0;

    int tileno;
    Tile tile;
    Icon mySprite;
    org.freeciv.common.City city;
    org.freeciv.common.Unit focus;
    org.freeciv.common.Unit unit;
    int den_y = map.getHeight() * 24;
    ArrayList l = new ArrayList( 80 );

    solidBg[0] = false;
    player[0] = null;

    // Only done in isometric mode in tilespec.c
    MapPosition mp = new MapPosition( abs_x0, abs_y0 );
    if (!map.normalizeMapPosition( mp ))
    {
      return l;
    }

    abs_x0 = mp.x;
    abs_y0 = mp.y;

    tile = map.getTile( abs_x0, abs_y0 );

    if ( tile.getKnown() == TILE_UNKNOWN )
    {
      return l;
    }

    city = map.getCity( abs_x0, abs_y0 );
    focus = getClient().getUnitInFocus();

    if ( !m_flagsAreTransparent ||
          getClient().getOptions().solidColorBehindUnits )
    {
      unit = getDrawableUnit( abs_x0, abs_y0, cityMode );

      if ( unit != null &&
          ( getClient().getOptions().drawUnits ||
            (getClient().getOptions().drawFocusUnit && focus == unit ) ))
      {
        fillUnitSprites( l, unit, solidBg );

        player[0] = unit.getOwner();
        if ( tile.hasUnitStack() )
        {
          l.add( getImage( "unit.stack" ) );
        }

        return l;
      }

      if ( city != null && getOptions().drawCities )
      {
        fillCitySprites( l, city, solidBg );
        player[0] = city.getOwner();
        return l;
      }
    }

    ttype = map.getTile( abs_x0, abs_y0 ).getTerrain();
    ttype_east = map.getTile( abs_x0+1, abs_y0 ).getTerrain();
    ttype_west = map.getTile( abs_x0-1, abs_y0 ).getTerrain();

    if ( abs_y0 == 0 )
    {
      ttype_north = ttype;
      ttype_north_east = ttype_east;
      ttype_north_west = ttype_west;
    }
    else
    {
      ttype_north = map.getTile( abs_x0, abs_y0-1 ).getTerrain();
      ttype_north_east = map.getTile( abs_x0+1, abs_y0-1).getTerrain();
      ttype_north_west = map.getTile( abs_x0-1, abs_y0-1).getTerrain();
    }

    if ( abs_y0 == map.getHeight()-1)
    {
      ttype_south = ttype;
      ttype_south_east = ttype_east;
      ttype_south_west = ttype_west;
    }
    else
    {
      ttype_south = map.getTile( abs_x0, abs_y0+1).getTerrain();
      ttype_south_east = map.getTile( abs_x0+1, abs_y0+1).getTerrain();
      ttype_south_west = map.getTile( abs_x0-1, abs_y0+1).getTerrain();
    }

    tspecial = map.getSpecial( abs_x0, abs_y0 );
    tspecial_north = map.getSpecial( abs_x0, abs_y0-1 );
    tspecial_east = map.getSpecial( abs_x0+1, abs_y0 );
    tspecial_south = map.getSpecial( abs_x0, abs_y0+1);
    tspecial_west = map.getSpecial( abs_x0-1, abs_y0 );
    tspecial_north_east = map.getSpecial( abs_x0 + 1, abs_y0 - 1 );
    tspecial_south_east = map.getSpecial( abs_x0 + 1, abs_y0 + 1 );
    tspecial_south_west = map.getSpecial( abs_x0 - 1, abs_y0 + 1 );
    tspecial_north_west = map.getSpecial( abs_x0 - 1, abs_y0 - 1 );

    // Evil hack for denmark. Sheesh.
    if ( map.isEarth() &&
        abs_x0 >= 34 && abs_x0 <= 36 && abs_y0 >= den_y && abs_y0 <= den_y+1 )
    {
      mySprite = getImage( "tx.denmark_" + (abs_y0 - den_y) + (abs_x0 - 34 ) );
    }
    else
    {
      tileno = getVariationID(
        (ttype_north == ttype), (ttype_south == ttype),
        (ttype_east == ttype), (ttype_west == ttype)
      );

      if ( ttype == T_RIVER )
      {
        tileno |= getVariationID(
          (ttype_north == T_OCEAN), (ttype_south == T_OCEAN),
          (ttype_east == T_OCEAN), (ttype_west == T_OCEAN)
        );
      }

      TerrainType tt = (TerrainType)
        getClient().getFactories().getTerrainTypeFactory().findById( ttype );

      mySprite = tt.getSprite( tileno );
    }

    if ( getOptions().drawTerrain )
    {
      l.add( mySprite );
    }
    else
    {
      solidBg[0] = true;
    }

    if ( ttype == T_OCEAN && getOptions().drawTerrain )
    {
      tileno = getVariationID(
        (ttype_north == T_OCEAN && ttype_east == T_OCEAN && ttype_north_east != T_OCEAN),
        (ttype_south == T_OCEAN && ttype_west == T_OCEAN && ttype_south_west != T_OCEAN),
        (ttype_east == T_OCEAN && ttype_south == T_OCEAN && ttype_south_east != T_OCEAN),
        (ttype_north == T_OCEAN && ttype_west == T_OCEAN && ttype_north_west != T_OCEAN)
      );

      if ( tileno != 0 )
      {
        l.add( getImage( "tx.coast_cape_"+NSEW_STRINGS[ tileno ] ) );
      }

      if ( (tspecial_north & S_RIVER) != 0 || ttype_north == T_RIVER )
      {
        l.add( getImage( "tx.s_river_"+NSEW_STRINGS[ DIR_NORTH ] ) );
      }
      if ( (tspecial_west&S_RIVER) != 0 || ttype_west == T_RIVER )
      {
        l.add( getImage( "tx.s_river_"+NSEW_STRINGS[ DIR_WEST ] ) );
      }
      if ( (tspecial_south&S_RIVER) != 0 || ttype_south == T_RIVER )
      {
        l.add( getImage( "tx.s_river_"+NSEW_STRINGS[ DIR_SOUTH ] ) );
      }
      if ( (tspecial_east&S_RIVER) != 0 || ttype_east == T_RIVER )
      {
        l.add( getImage( "tx.s_river_"+NSEW_STRINGS[ DIR_EAST ] ) );
      }


    }

    if ( (tspecial&S_RIVER) != 0 && getClient().getOptions().drawTerrain )
    {
      tileno = getVariationID(
        ((tspecial_north&S_RIVER) != 0 || ttype_north == T_OCEAN),
        ((tspecial_south&S_RIVER) != 0 || ttype_south == T_OCEAN),
        ((tspecial_east&S_RIVER) != 0 || ttype_east == T_OCEAN),
        ((tspecial_west&S_RIVER) != 0 || ttype_west == T_OCEAN)
      );

      l.add( getImage( "tx.s_river_"+NSEW_STRINGS[ tileno ] ) );
    }

    if ( (tspecial& S_IRRIGATION) != 0 && getClient().getOptions().drawIrrigation )
    {
      if ( (tspecial&S_FARMLAND) != 0 )
      {
        l.add( getImage( "tx.farmland" ) );
      }
      else
      {
        l.add( getImage( "tx.irrigation" ) );
      }
    }

    if ((( (tspecial&S_ROAD) != 0 ) || ( (tspecial&S_RAILROAD) != 0 )) && getClient().getOptions().drawRoadsRails )
    {
      boolean n, s, e, w;

      n = ((tspecial_north&S_RAILROAD) != 0);
      s = ((tspecial_south&S_RAILROAD) != 0);
      e = ((tspecial_east&S_RAILROAD) != 0);
      w = ((tspecial_west&S_RAILROAD) != 0);

      rail_card_count = (n ? 1 : 0) + (s ? 1 : 0) + (e ? 1 : 0) + (w ? 1 : 0);
      rail_card_tileno = getVariationID( n, s, e, w );

      n = ((tspecial_north&S_ROAD) != 0);
      s = ((tspecial_south&S_ROAD) != 0);
      e = ((tspecial_east&S_ROAD) != 0);
      w = ((tspecial_west&S_ROAD) != 0);

      road_card_count = (n?1:0) + (s?1:0) + (e?1:0) + (w?1:0);
      road_card_tileno = getVariationID( n, s, e, w );


      n = ((tspecial_north_east&S_RAILROAD) != 0);
      s = ((tspecial_south_west&S_RAILROAD) != 0);
      e = ((tspecial_south_east&S_RAILROAD) != 0);
      w = ((tspecial_north_west&S_RAILROAD) != 0);

      rail_semi_count = (n?1:0) + (s?1:0) + (e?1:0) + (w?1:0);
      rail_semi_tileno = getVariationID( n, s, e, w );

      n = ((tspecial_north_east&S_ROAD) != 0);
      s = ((tspecial_south_west&S_ROAD) != 0);
      e = ((tspecial_south_east&S_ROAD) != 0);
      w = ((tspecial_north_west&S_ROAD) != 0);

      road_semi_count = (n?1:0) + (s?1:0) + (e?1:0) + (w?1:0);
      road_semi_tileno = getVariationID( n, s, e, w );


      if ( (tspecial&S_RAILROAD) != 0 )
      {
        road_card_tileno &= ~rail_card_tileno;
        road_semi_tileno &= ~rail_semi_tileno;
      }
      else if ( (tspecial&S_ROAD) != 0 )
      {
        rail_card_tileno &= ~road_card_tileno;
        rail_semi_tileno &= ~road_semi_tileno;
      }

      if ( road_semi_count > road_card_count )
      {
        if ( road_card_tileno != 0 )
        {
          l.add( getImage( "r.c_road_"+NSEW_STRINGS[ road_card_tileno ] ) );
        }
        if ( road_semi_tileno!= 0 && getClient().getOptions().drawDiagonalRoads )
        {
          l.add( getImage( "r.d_road_"+NSEW_STRINGS[ road_semi_tileno ] ) );
        }
      }
      else
      {
        if ( road_semi_tileno != 0 && getClient().getOptions().drawDiagonalRoads )
        {
          l.add( getImage( "r.d_road_"+NSEW_STRINGS[ road_semi_tileno ] ) );
        }
        if ( road_card_tileno != 0 )
        {
          l.add( getImage( "r.c_road_"+NSEW_STRINGS[ road_card_tileno ] ) );
        }
      }

      if ( rail_semi_count > rail_card_count )
      {
        if ( rail_card_tileno != 0 )
        {
          l.add( getImage( "r.c_rail_"+NSEW_STRINGS[ rail_card_tileno ] ) );
        }
        if ( rail_semi_tileno!= 0 && getClient().getOptions().drawDiagonalRoads )
        {
          l.add( getImage( "r.d_rail_"+NSEW_STRINGS[ rail_semi_tileno ] ) );
        }
      }
      else
      {
        if ( rail_semi_tileno != 0 && getClient().getOptions().drawDiagonalRoads )
        {
          l.add( getImage( "r.d_rail_"+NSEW_STRINGS[ rail_semi_tileno ] ) );
        }
        if ( rail_card_tileno != 0 )
        {
          l.add( getImage( "r.c_rail_"+NSEW_STRINGS[ rail_card_tileno ] ) );
        }
      }
    }

    if ( getClient().getOptions().drawSpecials )
    {
      if ( (tspecial&S_SPECIAL_1) != 0 )
      {
        // TODO
      }
    }

    if ( ( (tspecial&S_MINE) != 0) && getOptions().drawMines )
    {
      if ( ttype == T_HILLS || ttype == T_MOUNTAINS )
      {
        l.add( getImage( "tx.mine" ) );
      }
      else
      {
        l.add( getImage( "tx.oil_mine" ) );
      }
    }

    if ( getOptions().drawRoadsRails )
    {
      if ( (tspecial&S_RAILROAD) != 0 )
      {
        int adjacent = rail_card_tileno;
        if ( getOptions().drawDiagonalRoads )
        {
          adjacent |= rail_semi_tileno;
        }
        if ( adjacent == 0 )
        {
          l.add( getImage("r.rail_isolated") );
        }
      }
      else if ( (tspecial&S_ROAD) != 0 )
      {
        int adjacent = road_card_tileno;
        if ( getOptions().drawDiagonalRoads )
        {
          adjacent |= road_semi_tileno;
        }
        if ( adjacent == 0 )
        {
          l.add( getImage("r.road_isolated") );
        }
      }
    }

    if (( (tspecial&S_HUT) != 0) && getOptions().drawSpecials )
    {
      l.add( getImage( "tx.village" ) );
    }
    if (( (tspecial&S_FORTRESS) != 0) && getOptions().drawFortressAirbase )
    {
      l.add( getImage( "tx.fortress" ) );
    }
    if (( (tspecial&S_AIRBASE) != 0) && getOptions().drawFortressAirbase )
    {
      l.add( getImage( "tx.airbase" ) );
    }
    if (( (tspecial&S_POLLUTION) != 0) && getOptions().drawPollution )
    {
      l.add( getImage( "tx.pollution" ) );
    }
    if (( (tspecial&S_FALLOUT) != 0) && getOptions().drawFallout )
    {
      l.add( getImage( "tx.fallout" ) );
    }
    if (tile.getKnown() == TILE_KNOWN_FOGGED && getOptions().drawFogOfWar )
    {
      l.add( getImage( "tx.fog" ) );
    }

    if (!cityMode)
    {
      tileno = getVariationID(
        !map.getTile( abs_x0, abs_y0-1 ).isKnown(),
        !map.getTile( abs_x0, abs_y0+1 ).isKnown(),
        !map.getTile( abs_x0+1, abs_y0 ).isKnown(),
        !map.getTile( abs_x0-1, abs_y0 ).isKnown()
      );

      if ( tileno != 0 )
      {
        l.add( getImage ("tx.darkness_"+NSEW_STRINGS[ tileno ] ) );
      }
    }

    if (m_flagsAreTransparent)
    {
      boolean[] dummy = new boolean[1];

      if ( city != null && getOptions().drawCities )
      {
        fillCitySprites( l, city, dummy );
      }

      unit = findVisibleUnit( tile );

      if ( unit !=  null )
      {
        if ( !cityMode || !unit.isOwner(getClient().getGame().getCurrentPlayer()) )
        {
          if (( true /*!focus_unit_hidden*/ || focus != unit ) &&
            ( getOptions().drawUnits || (getOptions().drawFocusUnit && true /*!focus_unit_hidden*/ && unit == focus )))
          {
            //no_backdrop = (city != null);
            fillUnitSprites( l, unit, dummy );
            //no_backdrop = false;
            if ( tile.hasUnitStack() )
            {
              l.add( getImage( "unit.stack" ) );
            }
          }
        }
      }
    }

    return l;
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

  /**
   * Add sprites for the specified city to the specified list of sprites.
   *
   * @param l a list of sprites being built up for a tile
   * @param city the city whose sprites have to be added
   * @param solidBg
   */
  private void fillCitySprites( List l, org.freeciv.common.City city, boolean[] solidBg )
  {
    // we avoid the hackiness in tilespec.c and combine
    // fill_city_sprite_array and fill_city_sprite_array_iso into one method.

    solidBg[0] = false;

    Tile tile = getClient().getGame().getMap().getTile( city.getX(), city.getY() );

    if ( !m_noBackdrop )
    {
      if ( !getOptions().solidColorBehindUnits  || isIsometric() )
      {
        l.add( getCityNationFlagSprite( city ) );
      }
      else
      {
        solidBg[0] = true;
      }
    }

    if ( tile.hasUnits() )
    {
      l.add( getCityOccupiedSprite( city ) );
    }

    l.add( getCitySprite( city ) );

    if ( !isIsometric() )
    {
      if ( city.hasWalls() )
      {
        l.add( getCityWallSprite( city ) );
      }

      if ( tile.isPolluted() )
      {
        l.add( getImage( "tx.pollution" ) );
      }

      if ( tile.hasFallout() )
      {
        l.add( getImage( "tx.fallout" ) );
      }
    }

    if ( city.isUnhappy() )
    {
      l.add( getImage( "city.disorder" ) );
    }

    if ( !isIsometric() )
    {
      if ( tile.getKnown() == TILE_KNOWN_FOGGED && getOptions().drawFogOfWar )
      {
        l.add( getImage( "tx.fog" ) );
      }

      if ( city.getSize() >= 10 )
      {
        l.add( getImage( "city.size_"+city.getSize() / 10 + "0" ) );
      }

      l.add( getImage( "city.size_"+city.getSize() ) );
    }

  }

  private Icon getDither( int ttype, int ttype_other )
  {
    if ( ttype_other == T_UNKNOWN )
    {
      return getImage( "t.black_tile" );
    }

    if ( ttype == T_OCEAN || ttype == T_JUNGLE )
    {
      return getImage( "t.coast_color" );
    }

    if ( ttype_other != T_UNKNOWN && ttype_other != T_LAST )
    {
      TerrainType tt = (TerrainType) getClient().getFactories().getTerrainTypeFactory().findById( ttype_other );
      return tt.getSprite( 0 );
    }
    else
    {
      return null;
    }
  }

  private Icon getCityNationFlagSprite( org.freeciv.common.City c )
  {
    Nation n = c.getOwner().getNation();
    return n.getFlagSprite();
  }

  private Icon getCityOccupiedSprite( org.freeciv.common.City c )
  {
    CityStyle cs = c.getStyle();
    return cs.getTileSprite( cs.getNumTiles() + 1 );
  }

  private Icon getCitySprite( org.freeciv.common.City city )
  {
    int size;
    CityStyle cs = city.getStyle();

    for( size=0; size < cs.getNumTiles(); size++)
    {
      if ( city.getSize() < cs.getThreshold( size ) )
      {
        break;
      }
    }

    if ( isIsometric() )
    {
      if ( city.hasWalls() )
      {
        return cs.getWallSprite( size-1 );
      }
      else
      {
        return cs.getTileSprite( size - 1);
      }
    }
    else
    {
      return cs.getTileSprite( size - 1 );
    }
  }

  private Icon getCityWallSprite( org.freeciv.common.City city )
  {
    CityStyle cs = city.getStyle();
    return cs.getTileSprite( cs.getNumTiles() );
  }



  /**
   * Add sprites for the specified unit to the specified list of sprites.
   *
   * @param l a list of sprites being built up for a tile
   * @param unit the unit whose sprites have to be added
   * @param solidBg ?
   */
  public void fillUnitSprites( List l, org.freeciv.common.Unit unit, boolean[] solidBg )
  {
    solidBg[0] = false;

    if ( isIsometric() )
    {
      if (! m_noBackdrop )
      {
        l.add( getUnitNationFlagSprite( unit ) );
      }
    }
    else
    {
      if ( !m_noBackdrop )
      {
        if ( !getOptions().solidColorBehindUnits )
        {
          l.add( getUnitNationFlagSprite( unit ) );
        }
        else
        {
          solidBg[0] = true;
        }
      }
    }

    l.add( getUnitTypeSprite( unit ) );

    // unit activity icons
    if ( unit.getActivity() != ACTIVITY_IDLE )
    {
      Icon sprite = null;
      int a = unit.getActivity();
      if ( ACTIVITY_MINE == a )
        sprite = getImage( "unit.mine" );
      else if ( ACTIVITY_POLLUTION == a )
        sprite = getImage( "unit.pollution" );
      else if ( ACTIVITY_FALLOUT  ==  a )
        sprite = getImage( "unit.fallout" );
      else if ( ACTIVITY_PILLAGE == a )
        sprite = getImage( "unit.pillage" );
      else if ( ACTIVITY_ROAD == a || ACTIVITY_RAILROAD == a )
        sprite = getImage( "unit.road" );
      else if ( ACTIVITY_IRRIGATE == a )
        sprite = getImage( "unit.irrigate" );
      else if ( ACTIVITY_EXPLORE == a )
        sprite = getImage( "unit.explore" );
      else if ( ACTIVITY_FORTIFIED == a )
        sprite = getImage( "unit.fortified" );
      else if ( ACTIVITY_FORTRESS == a )
        sprite = getImage( "unit.fortress" );
      else if ( ACTIVITY_FORTIFYING == a )
        sprite = getImage( "unit.fortifying" );
      else if ( ACTIVITY_AIRBASE == a )
        sprite = getImage( "unit.airbase" );
      else if ( ACTIVITY_SENTRY == a )
        sprite = getImage( "unit.sentry" );
      else if ( ACTIVITY_GOTO == a )
        sprite = getImage( "unit.goto" );
      else if ( ACTIVITY_TRANSFORM == a )
        sprite = getImage( "unit.transform" );

      l.add( sprite );
    }

    if ( unit.getAI().isControlled() )
    {
      if ( unit.isMilitary() )
      {
        l.add( getImage( "unit.auto_attack" ) );
      }
      else
      {
        l.add( getImage( "unit.auto_settler" ) );
      }
    }

    if ( unit.isConnecting() )
    {
      l.add( getImage( "unit.connect" ) );
    }

    if ( unit.getActivity() == ACTIVITY_PATROL )
    {
      l.add( getImage( "unit.patrol" ) );
    }

    int ihp = (( NUM_TILES_HP_BAR - 1 ) * unit.getHitPoints() )  / unit.getUnitType().getHitPoints();
    ihp = clip( 0, ihp, NUM_TILES_HP_BAR-1 );

    l.add( getImage( "unit.hp_"+ihp*10 ) ); /// uuhhhhh.. may want to fix this.
  }

  private int clip(int lower, int thisOne, int upper)
  {
    return ( thisOne < lower ? lower : ( thisOne > upper ? upper : thisOne ) );
  }

  private Icon getUnitTypeSprite( org.freeciv.common.Unit unit )
  {
    return unit.getUnitType().getSprite();
  }

  private Icon getUnitNationFlagSprite( org.freeciv.common.Unit unit )
  {
    return unit.getOwner().getNation().getFlagSprite();
  }

  private org.freeciv.common.Unit getDrawableUnit( int x, int y, boolean cityMode )
  {
    org.freeciv.common.Unit unit = findVisibleUnit( getClient().getGame().getMap().getTile( x, y ) );
    org.freeciv.common.Unit focus = getClient().getUnitInFocus();

    if ( unit == null )
    {
      return null;
    }

    if ( cityMode && unit.getOwner() == getClient().getGame().getCurrentPlayer() )
    {
      return null;
    }

    if ( unit != focus
      //|| !getClient().getOptions().focusUnitHidden // ?? may be flash behaviour
      || !getClient().getGame().getMap().isSamePosition( unit.getX(), unit.getY(), focus.getY(), focus.getY() ) )
    {
      return unit;
    }
    else
    {
      return null;
    }
  }

  /**
   * Find the unit that is visible at the specified tile.
   *
   * @param tile the tile to find the unit for
   * @return the unit which is visible at the specified tile, null if there
   *    are no visible units on the tile.
   */
  private org.freeciv.common.Unit findVisibleUnit( Tile tile )
  {
    // candidate for moving elsewhere (e.g. Tile.java, but is client specific...)
    // from control.c: find_visible_unit()

    if ( !tile.hasUnits() )
    {
      return null;
    }

    org.freeciv.common.Map map = getClient().getGame().getMap();

    org.freeciv.common.Unit unit;

    // If a unit is attacking, show it on top
    unit = getClient().getAttackingUnit();
    if ( unit != null && map.getTile( unit.getX(), unit.getY() ) == tile )
    {
      return unit;   // c weirdness here...
    }

    // If a unit is defending, show it on top.
    unit = getClient().getDefendingUnit();
    if ( unit != null && map.getTile( unit.getX(), unit.getY() ) == tile )
    {
      return unit;
    }

    // If the unit is in focus at this tile, show it on top.
    unit = getClient().getUnitInFocus();
    if ( unit != null && map.getTile( unit.getX(), unit.getY() ) == tile )
    {
      return unit;
    }

    // If a city is here, show nothing ( unit hidden by city )
    if ( tile.hasCity() )
    {
      return null;
    }

    // Iterate through the units to find the best one. We prioritize like this:
    // 1. Owned transported
    // 2. Any owned unit
    // 3. Any Transporter
    // 4. Any Unit

    org.freeciv.common.Unit anyowned = null, tpother = null, anyother = null;

    Iterator unitIter = tile.getUnits();
    while (unitIter.hasNext())
    {
      unit = (org.freeciv.common.Unit) unitIter.next();

      if ( unit.isOwner( getClient().getGame().getCurrentPlayer() ) )
      {
        if ( unit.isTransporter() )
        {
          return unit;
        }
        else if ( anyowned == null )
        {
          anyowned = unit;
        }
      }
      else if ( tpother == null &&
        getClient().getGame().getCurrentPlayer().canSeeUnit( unit ) )
      {
        if ( unit.isTransporter() )
        {
          tpother = unit;
        }
        else
        {
          anyother = unit;
        }
      }

    }

    return ( anyowned != null ? anyowned :  ( tpother != null ? tpother : anyother ) );

  }

  public void setTerrain( PktTileInfo pkt, boolean update )
  {
     // TODO
  }
}
