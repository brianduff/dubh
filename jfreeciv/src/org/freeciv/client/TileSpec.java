package org.freeciv.client;

import org.freeciv.common.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;


import org.freeciv.net.*;

import org.freeciv.tile.*;

//// For test harness
import javax.swing.*;
import java.awt.BorderLayout;

/////

/**
 * Represents a tilespec.
 */
public class TileSpec
{
   private final static String XPM_EXT = "xpm";
   private final static String EXTENSIONS = XPM_EXT;
   private final static String TILESET_DEFAULT = "trident";
   private final static String TILESPEC_CAPSTR = "+tilespec2 duplicates_ok";
   private final static String SPEC_CAPSTR ="+spec2";

   private int m_normalTileWidth, m_normalTileHeight,
      m_smallTileWidth, m_smallTileHeight;

   private String m_cityNamesFont;

   private boolean m_flagsTransparent;

   private String m_mainIntroFilename, m_minimapIntroFilename;
   private String[] m_specFiles;

   private HashMap m_images;

   private Client m_client;

   public TileSpec(Client c)
   {
      m_client = c;
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

   public void loadTileset(String tilesetName)
   {
      readTopLevel(tilesetName);
      loadTiles();
      loadIntroFiles();
   }

   private void loadIntroFiles()
   {
      Sprite main = loadImage(m_mainIntroFilename);
      Sprite mini = loadImage(m_minimapIntroFilename);

      m_images.put("main_intro_file", main.getIcon());
      m_images.put("minimap_intro_file", mini.getIcon());
   }


   /**
    * Finds and reads the toplevel tilespec file based on given name.
    * Sets instance variables, including tile sizes and full names for
    * intro files.
    */
   private void readTopLevel(String tilesetName)
   {
      String fname;

      fname = getTileSpecFullName(tilesetName);

      Logger.log(Logger.LOG_VERBOSE, "tilespec file is "+fname);

      Registry r = new Registry();

      if (!r.loadFile(fname))
      {
         Logger.log(Logger.LOG_FATAL, "Couldn't open \""+fname+"\"");
         System.exit(1);
      }

      checkCapabilities(r, "tilespec", TILESPEC_CAPSTR, fname);

      r.lookup("tilespec.name", null); /* unused */

      m_normalTileWidth = r.lookupInt("tilespec.normal_tile_width");
      m_normalTileHeight = r.lookupInt("tilespec.normal_tile_height");
      m_smallTileWidth = r.lookupInt("tilespec.small_tile_width");
      m_smallTileHeight = r.lookupInt("tilespec.small_tile_height");

      Logger.log(Logger.LOG_VERBOSE,
         "Tile sizes: "+m_normalTileWidth+"x"+m_normalTileHeight+", "+
            m_smallTileWidth+"x"+m_smallTileHeight+" small"
      );

      m_cityNamesFont = r.lookupString("10x20", "tilespec.city_names_font");

      m_flagsTransparent =
         (r.lookupInt("tilespec.flags_are_transparent") != 0);

      m_mainIntroFilename = getGFXFilename(
         r.lookupString("tilespec.main_intro_file")
      );
      if (Logger.DEBUG)
      {
         Logger.log(Logger.LOG_DEBUG, "intro file "+m_mainIntroFilename);
      }

      m_minimapIntroFilename = getGFXFilename(
         r.lookupString("tilespec.minimap_intro_file")
      );
      if (Logger.DEBUG)
      {
         Logger.log(Logger.LOG_DEBUG, "radar file "+m_minimapIntroFilename);
      }

      m_specFiles = r.lookupStringList("tilespec.files");

      if (m_specFiles.length == 0)
      {
         Logger.log(Logger.LOG_FATAL, "No tile files specified in "+fname);
         System.exit(1);
      }

      for (int i=0; i < m_specFiles.length; i++)
      {
         m_specFiles[i] = Shared.getDataFilenameRequired(
            m_specFiles[i]
         );

         if (Logger.DEBUG)
         {
            Logger.log(Logger.LOG_DEBUG, "Spec file "+m_specFiles[i]);
         }
      }


      Logger.log(Logger.LOG_VERBOSE, "Finished reading"+fname);

   }

   static String getGFXFilename(String filename)
   {
      String[] extTok = Shared.getTokens(EXTENSIONS, " ");
      for (int i=0; i < extTok.length; i++)
      {
         String fullName = filename+"."+extTok[i];
         fullName = Shared.getDataFilename(fullName);

         if (fullName != null)
         {
            return fullName;
         }
      }

      Logger.log(Logger.LOG_FATAL,
         "Couldn't find a supported gfx file extension for "+filename
      );

      System.exit(1);
      return null;
   }

   /**
    * Gets full filename for tilespec file, based on input name.
    * Returned data is allocated, and freed by user as required.
    * Input name may be null, in which case uses default.
    * Falls back to default if can't find specified name;
    * dies if can't find default.
    */
   static String getTileSpecFullName(String tilesetName)
   {
      int level;
      StringBuffer fname;
      String dname;

      if (tilesetName == null)
      {
         tilesetName = TILESET_DEFAULT;
      }

      fname = new StringBuffer(tilesetName);
      fname.append(".tilespec");

      dname = Shared.getDataFilename(fname.toString());
      if (dname != null)
      {
         return dname;
      }

      if (TILESET_DEFAULT.equals(tilesetName))
      {
         level = Logger.LOG_FATAL;
      }
      else
      {
         level = Logger.LOG_NORMAL;
      }

      Logger.log(level, "Could not find readable file "+fname+
         " in data path."
      );
      Logger.log(level, "The data path may be set via the Java system"+
         " property "+Shared.FREECIV_PATH+"."
      );
      Logger.log(level, "Current data path is "+
         Shared.getDataFilename(null)
      );

      if (level == Logger.LOG_FATAL)
      {
         System.exit(1);
      }

      Logger.log(level, "Trying "+TILESET_DEFAULT+" tileset.");

      return getTileSpecFullName(TILESET_DEFAULT);

   }

   /**
    * Checks options in filename match what we require and support.
    * Die if not.
    * which should be "tilespec" or "spec".
    */
   static void checkCapabilities(Registry r, String which,
      String us_capstr, String filename)
   {
      String file_capstr = r.lookupString("%s.options",
         new Object[] { which }
      );

      if (!Shared.hasCapabilities(us_capstr, file_capstr))
      {
         Logger.log(Logger.LOG_FATAL,
            which+" file appears incompatible:");
         Logger.log(Logger.LOG_FATAL,
            "file: \""+filename+"\""
         );
         Logger.log(Logger.LOG_FATAL,
            "file options: "+file_capstr
         );
         Logger.log(Logger.LOG_FATAL,
            "supported options: "+us_capstr
         );
         System.exit(1);
      }

      if (!Shared.hasCapabilities(file_capstr, us_capstr))
      {
         Logger.log(Logger.LOG_FATAL,
            which+" claims required option(s) which we don't support:");
         Logger.log(Logger.LOG_FATAL,
            "file: \""+filename+"\""
         );
         Logger.log(Logger.LOG_FATAL,
            "file options: "+file_capstr
         );
         Logger.log(Logger.LOG_FATAL,
            "supported options: "+us_capstr
         );
         System.exit(1);
      }

   }

   /**
    * Load the tiles; requires tilespec_read_toplevel() called previously.
    */
   void loadTiles()
   {
      if (m_specFiles == null || m_specFiles.length < 1)
      {
         throw new IllegalStateException(
            "Must call readTopLevel first."
         );
      }

      for (int i=0; i < m_specFiles.length; i++)
      {
         loadOne(m_specFiles[i]);
      }
   }


   /**
    * Load one specfile and specified xpm file; splits xpm into tiles,
    * and save indices in tag_sf.
    */
   void loadOne(String specFilename)
   {
      if (Logger.DEBUG)
      {
         Logger.log(Logger.LOG_DEBUG,
            "loading spec "+specFilename
         );
      }

      Registry file = new Registry();

      if (!file.loadFile(specFilename))
      {
         Logger.log(Logger.LOG_FATAL,
            "Could not open "+specFilename
         );
         System.exit(1);
      }

      checkCapabilities(file, "spec", SPEC_CAPSTR, specFilename);

      file.lookup("info.artists");  /* Unused */

      String gfx_filename = file.lookupString("file.gfx");

      String[] tok = Shared.getTokens(EXTENSIONS, " ");

      Sprite big_image = null;
      int i=0;

      while(big_image == null && i < tok.length)
      {
         String full_name = gfx_filename + "." + tok[i];
         String real_full_name = Shared.getDataFilename(full_name);

         if (real_full_name != null)
         {
            if (Logger.DEBUG)
            {
               Logger.log(Logger.LOG_DEBUG,
                  "Trying to load gfx file "+real_full_name
               );
               big_image = loadImage(real_full_name);

               if (big_image == null)
               {
                  Logger.log(Logger.LOG_VERBOSE,
                     "Loading the gfx file "+real_full_name+" failed"
                  );
               }
            }
         }
         i++;
      }

      if (big_image == null)
      {
         Logger.log(Logger.LOG_FATAL,
            "Couldn't load gfx file for the spec file "+specFilename
         );
         System.exit(1);
      }

      ArrayList gridNames = file.getSecNamesPrefix("grid_");
      if (gridNames == null)
      {
         Logger.log(Logger.LOG_FATAL, "Spec "+specFilename+
            " has no grid_* sections."
         );
         System.exit(1);
      }

      int x_top_left, y_top_left, dx, dy, x1, y1;
      int row, col;
      String[] tags;
      for (int c=0; c < gridNames.size(); c++)
      {
         String key = (String)gridNames.get(c);
         if (Logger.DEBUG) Logger.log(Logger.LOG_DEBUG, "Key is "+key);
         Object[] subst = new Object[] { key };
         x_top_left = file.lookupInt("%s.x_top_left", subst);
         y_top_left = file.lookupInt("%s.y_top_left", subst);
         dx = file.lookupInt("%s.dx", subst);
         dy = file.lookupInt("%s.dy", subst);

         int j=-1;

         Object[] mySubst;
         while (file.lookup("%s.tiles%d.tag",
            (mySubst = new Object[] { key, new Integer(++j) })))
         {
            row = file.lookupInt("%s.tiles%d.row", mySubst);
            col = file.lookupInt("%s.tiles%d.column", mySubst);
            tags = file.lookupStringList("%s.tiles%d.tag", mySubst);

            if (tags.length == 0)
            {
               throw new IllegalStateException("There should be > 0 tags!");
            }

            x1 = x_top_left + col * dx;
            y1 = y_top_left + row * dy;


            if (m_images == null) m_images = new HashMap();

            for (int k=0; k < tags.length; k++)
            {
               m_images.put(tags[k],
                  big_image.getSegmentIcon(x1, y1, dx, dy)
               );
            }
         }
      }


      if (Logger.DEBUG)
      {
         Logger.log(Logger.LOG_DEBUG, "Finished "+specFilename);
      }


   }

   /**
    * Get the specified image. May return null if the image doesn't
    * exist.
    */
   public Icon getImage(String key)
   {
      if (key == null)
         throw new IllegalArgumentException("Key must != null");

      Icon i = (Icon)m_images.get(key);

      if (Logger.DEBUG)
      {
//         Logger.log(Logger.LOG_DEBUG,
//            "Looked up image >>"+key+"<< found: "+i
//         );
      }

      return i;
   }


   private Sprite loadImage(String filename)
   {
      // Get the extension.
      int dotPos = filename.lastIndexOf('.');
      if (dotPos < 0)
      {
         return null;
      }
      String strExt = filename.substring(dotPos+1);

      // Figure out how to load the image based on its extension.
      if (XPM_EXT.equalsIgnoreCase(strExt))
      {
         return new XPMFile(filename);
      }

      return null;

   }


///     Image retrieval

   private Terrain[] m_terrains;

   private RoadOverlay emptyRoad = new RoadOverlay(null, 0);
   private RoadOverlay emptyRail = new RoadOverlay(null, 32);

   /**
    * Get an icon for the specified terrain type and variation.
    * You must not call this before all the PktRulesetTerrains have
    * been received from the server.
    *
    *
    * @param icoType the full icon id (use getFullTerrainID to compute
    * this).
    */
   public Icon getTerrainIcon(int icoType, int variation)
   {

      // Get the ruleset for the terrain type.
      PktRulesetTerrain rt = getClient().getRulesetManager().
         getRulesetTerrain(icoType);

      if (rt == null)
      {
         throw new IllegalStateException("Unknown terrain type "+icoType);
      }

      String icoName = rt.graphic_str;
      StringBuffer key = new StringBuffer(30);
      key.append(icoName);
      key.append("_");
      key.append(NESW_STRINGS[variation]);

      Icon i = getImage(key.toString());
      if (i == null)
      {
         throw new IllegalStateException(
            "Couldn't find terrain image "+key.toString()
         );
      }
      return i;
   }

   /**
    * Get and create if necessary a Terrain object of the
    * specified type and variation.
    */
   private Terrain getTerrain(int id, int variation)
   {
      int fullID = getFullTerrainId(id, variation);
      // We do this lazily to avoid having to guess what size
      // the array should be. This method must not be called
      // before rulesets have been received, but that should be OK.
      if (m_terrains == null)
      {
         //int numTerrains =
         //   getClient().getRulesetManager().getRulesetTerrainCount();
         //m_terrains = new Terrain[16*numTerrains];
         m_terrains = new Terrain[512];      // BD: BAD BAD BAD
      };

      Terrain t = m_terrains[fullID];

      if (t == null)
      {
         // I think this should store the fullID because of variations.
         // but this breaks other stuff, need to check this later BD
         t = new Terrain(getTerrainIcon(id, variation), id, variation);
         m_terrains[fullID] = t;
      }

      return t;
   }

   private UnknownTerrain[] m_unknownTerrains;

   /**
    * Get a terrain object for the specified tile. Will instantiate
    * a new object if necessary.
    */
   private Terrain getTerrain(PktTileInfo pkt)
   {
      if (pkt.known <= Constants.TILE_UNKNOWN)
      {
         if (m_unknownTerrains == null)
         {
            // Hmm. dodgy.
            m_unknownTerrains = new UnknownTerrain[Constants.T_LAST];
         }
         UnknownTerrain t = m_unknownTerrains[pkt.type];
         if (t == null)
         {
            t = new UnknownTerrain(pkt.type);
            m_unknownTerrains[pkt.type] = t;
         }

         return t;
      }
      return getTerrain(pkt.type, computeVariation(pkt.type, pkt.x, pkt.y));
   }

   //
   // These behave like boolean values, e.g. NESW_STRINGS[5] = n0e1s0w1
   // (5d = 0101b)
   //
   private final static String[] NESW_STRINGS = {
      "n0s0e0w0",
      "n0s0e0w1",
      "n0s0e1w0",
      "n0s0e1w1",
      "n0s1e0w0",
      "n0s1e0w1",
      "n0s1e1w0",
      "n0s1e1w1",
      "n1s0e0w0",
      "n1s0e0w1",
      "n1s0e1w0",
      "n1s0e1w1",
      "n1s1e0w0",
      "n1s1e0w1",
      "n1s1e1w0",
      "n1s1e1w1"
   };

   private int getFullTerrainId(int type, int variation)
   {
      return ((type << 5) + variation);
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
   private int getVariationID(boolean north, boolean south, boolean east,
      boolean west)
   {
      return
         (north ? 8 : 0) + (south ? 4 : 0) + (east ? 2 : 0) + (west ? 1 : 0);

   }

   /**
    * Compute the variation for the tile at x, y based on the tiles
    * around it.
    */
   private int computeVariation(int typeId, int x, int y)
   {
      CivMap m = getClient().getMap();

		Terrain terNorth = (y > 0) ? m.getTerrain(x, y-1) : null;
		Terrain terEast  =
		   (x < m.getHorizontalTiles()-1) ? m.getTerrain(x+1, y) :
		   (m.isWrapHorizontal() ? m.getTerrain(0, y) : null);
	   Terrain terSouth = (y < m.getVerticalTiles()-1) ?
	      m.getTerrain(x, y+1) : null;
	   Terrain terWest  =
	      (x > 0) ? m.getTerrain(x-1, y) :
	      (m.isWrapHorizontal() ? m.getTerrain(m.getHorizontalTiles()-1, y) : null);
		
		// TODO: Rivers & ocean.
      return getVariationID(
         (terNorth != null && terNorth.id == typeId),
         (terSouth != null && terSouth.id == typeId),
         (terEast != null && terEast.id == typeId),
         (terWest != null && terWest.id == typeId)
      );
   }

   // Temporary
   boolean doneCentering = false;

   /**
    * For an incoming tile info packet, set the terrain for the
    * relevant tile.
    */
   public void setTerrain(PktTileInfo pkt, boolean update)
   {
      CivMap m = getClient().getMap();
      List l = m.getTileList(pkt.x, pkt.y);

		Terrain tnew = null;
		Terrain told = (Terrain)l.get(CivMap.TERRAIN_LAYER);
		RoadOverlay oldRoad = (RoadOverlay)l.get(CivMap.ROAD_LAYER);
		
		RoadOverlay nRoad = (RoadOverlay)
			(( (pkt.special & Constants.S_RAILROAD) != 0 ) ?
				getEmptyRail() :
				( (pkt.special & Constants.S_ROAD) != 0 ) ?
				getEmptyRoad() :
				null );

		l.set(CivMap.ROAD_LAYER,nRoad);

		   tnew = getTerrain(pkt);
         //Logger.log(Logger.LOG_NORMAL,
         //   "Set terrain layer ("+CivMap.TERRAIN_LAYER+") at "+pkt.x+", "+pkt.y+" to "+
         //      tnew
         //);
			l.set(CivMap.TERRAIN_LAYER, tnew );

			if ( update )
			   computeAndSetOverlayTerrain(pkt.x,pkt.y);

			/*l.set(CivMap.RESOURCE_LAYER,
				( (pkt.special & Constants.S_SPECIAL) != 0 ) ?
				client.getTerrainSpecial(pkt.type):
				null);
         */
			//l.set(HUT_LAYER,
			//	((pkt.special & Constants.S_HUT) != 0)  ?
			//	client.getHutIcon() :
			//	null);


		// Figure out if we need to update the tiles around this
		// tile.
		boolean needUpdate = false;

		
		// The first time round this will always be true, because
		// told != tnew (told = null)
		
		if ( !update )
		{
		}
		else if ( !tnew.equals(told) )
		{
			needUpdate = true;
		}
		else if ( tnew.isKnown() != told.isKnown() )
		{
			needUpdate = true;
		}
		else if ( (oldRoad == null) != (nRoad == null) )
		{
			needUpdate = true;
		}
		else if ((oldRoad != null) && (nRoad != null) &&
			(oldRoad.isRail() !=nRoad.isRail()))
		{
			needUpdate = true;
		}
		


		if ( needUpdate )
		{
			recomputeVariation(pkt.x-1,pkt.y);
			recomputeVariation(pkt.x+1,pkt.y);
			recomputeVariation(pkt.x,pkt.y-1);
			recomputeVariation(pkt.x,pkt.y+1);
			computeAndSetOverlayTerrain(pkt.x-1,pkt.y);
			computeAndSetOverlayTerrain(pkt.x+1,pkt.y);
			computeAndSetOverlayTerrain(pkt.x,pkt.y-1);
			computeAndSetOverlayTerrain(pkt.x,pkt.y+1);
			computeAndSetOverlayTerrain(pkt.x-1,pkt.y-1);
			computeAndSetOverlayTerrain(pkt.x+1,pkt.y+1);
			computeAndSetOverlayTerrain(pkt.x+1,pkt.y-1);
			computeAndSetOverlayTerrain(pkt.x-1,pkt.y+1);
			
			m.repaintTilesAround(pkt.x,pkt.y);
			// TODO smallMap.updateMap(pkt.x,pkt.y);
		}
		else
		{
			m.repaintOneTile(pkt.x,pkt.y);
		}
		
		if (!doneCentering)
		{
		   // Remove this later
		   m.centerOnTile(pkt.x, pkt.y);
         doneCentering = true;
      }
   }

   private void recomputeVariation(int x, int y)
   {
      CivMap m = getClient().getMap();

      if (x < 0 || x > m.getHorizontalTiles()-1 ||
          y < 0 || y > m.getVerticalTiles()-1)
      {
         return;
      }

      List l = m.getTileList(x, y);

      Terrain current = (Terrain)l.get(CivMap.TERRAIN_LAYER);

      if (current == null || !current.isKnown())
      {
         return;
      }

      int variation = computeVariation(current.getId(), x, y);
      Terrain newTerrain = getTerrain(current.getId(), variation);
      l.set(CivMap.TERRAIN_LAYER, newTerrain);

   }

   /**
    * Return a road overlay that doesn't contain any rails
    */
   public RoadOverlay getEmptyRail()
   {
      return emptyRail;
   }

   /**
    * Return a road overlay that doesn't contain any roads.
    */
   public RoadOverlay getEmptyRoad()
   {
      return emptyRoad;
   }

   /**
    * Figure out all the terrain overlays (road, shadows) for
    * the specified grid square and put them on the map
    */
	private void computeAndSetOverlayTerrain( int x, int y )
	{
	   CivMap m = getClient().getMap();
	
		if ( y<0 || y > m.getVerticalTiles()-1 )
			return;
		if ( x < 0 )
			x = m.getHorizontalTiles()-1;
		if ( x > m.getHorizontalTiles() -1 )
			x =0;
		Terrain t = m.getTerrain(x,y);

		if ( (t == null) || !t.isKnown() )
			return;

	   List l = m.getTileList(x, y);
		if ( l.get(CivMap.ROAD_LAYER) != null )
			computeAndSetRoad(x,y);

		computeAndSetShadow(x,y);

		//if ( t.getId() != Constants.T_OCEAN )
		//	return;
		
		//l.set(TERRAIN_OVERLAY_LAYER, computeOverlayTerrain(x,y));

	}

	/**
	 * Figure out the roads that need to be displayed for the specifed
	 * map co-ordinates, and add the relevant icons to the grid square.
	 */
	private void computeAndSetRoad(int x, int y)
	{
	   CivMap m = getClient().getMap();

	   int horizontalTiles = m.getHorizontalTiles();
	   int verticalTiles = m.getVerticalTiles();
	   boolean hWrap = m.isWrapHorizontal();
	
		RoadOverlay n,w,e,s,ne,nw,se,sw, center;
		n=w=e=s=ne=nw=se=sw=null;
		center = m.getRoad(x,y);
		if ( y > 0 )
		{
			n = m.getRoad(x,y-1);
			nw = (x > 0) ? m.getRoad(x-1,y-1) :
				(hWrap ? m.getRoad(horizontalTiles-1,y-1):null);
			ne = (x< horizontalTiles-1 ) ? m.getRoad(x+1,y-1) :
				(hWrap ? m.getRoad(0,y-1):null);
		}

		if ( y < verticalTiles-1 )
		{
			s = m.getRoad(x,y+1);
			sw = (x > 0) ? m.getRoad(x-1,y+1) :
				(hWrap ? m.getRoad(horizontalTiles-1,y+1):null);
			se = (x< horizontalTiles-1 ) ? m.getRoad(x+1,y+1) :
				(hWrap ? m.getRoad(0,y+1):null);
		}

		w = (x > 0) ? m.getRoad(x-1,y) :
			(hWrap ? m.getRoad(horizontalTiles-1,y):null);
		e = (x< horizontalTiles-1 ) ? m.getRoad(x+1,y) :
			(hWrap ? m.getRoad(0,y):null);


		int variation = getVariationID(
		   (n != null),
		   (s != null),
		   (e != null),
		   (w != null)
		);
		int dvariation = getVariationID(
		   (ne != null),
		   (sw != null),
		   (se != null),
		   (nw != null)
		);

	   List l = m.getTileList(x, y);

		if ( center.isRail() )
		{
			l.set(CivMap.ROAD_LAYER, getNormalRailOverlay(variation));
			l.set(CivMap.ROAD_DIAGONAL_LAYER, getDiagonalRailOverlay(dvariation));
		}
		else
		{
			l.set(CivMap.ROAD_LAYER, getNormalRoadOverlay(variation));
			l.set(CivMap.ROAD_DIAGONAL_LAYER, getDiagonalRoadOverlay(dvariation));
		}
	}	
	
	private RoadOverlay[] m_roads;
	
	
	private FlashingIcon getNormalRoadOverlay(int variation)
	{
	   return getRoadInternal(variation);
	}
	
	private FlashingIcon getDiagonalRoadOverlay(int variation)
	{
	   return getRoadInternal(variation+64);
	}
	
	private FlashingIcon getNormalRailOverlay(int variation)
	{
	   return getRoadInternal(variation+32);
	}
	
	private FlashingIcon getDiagonalRailOverlay(int variation)
	{
	   return getRoadInternal(variation+96);
	}
	
	private FlashingIcon getRoadInternal(int idx)
	{
	   if (m_roads == null)
	   {
	      m_roads = new RoadOverlay[128];
	   }
	
	   RoadOverlay ro = m_roads[idx];
	
	   if (ro == null)
	   {
	      ro = new RoadOverlay(getRoadOrRailIcon(idx), idx);
	      m_roads[idx] = ro;
	   }
	
	   return ro;
	}
	
	/**
	 * Get a road or rail icon. The index encodes the type in the following
	 * way:
	 *   bits 0-3: The nsew variation
	 *   bit  4:   1 = rail, 0 = road
	 *   bit  5:   1 = diagonal, 0 = cardinal
	 */
	private Icon getRoadOrRailIcon(int idx)
	{
	   int variation = idx & 31;   // Get the last 4 bits
	   boolean rail = ((idx & 32) > 0); // Only true if bit 4 == 1
	   boolean diagonal = ((idx & 64) > 0); // Only true if bit 5 == 1
	
	   StringBuffer sbIconTag  = new StringBuffer(15);
	
	   if (variation == 0)
	   {
	      sbIconTag.append("r.");
	      sbIconTag.append(rail ? "rail" : "road");
	      sbIconTag.append("_isolated");
	   }
	   else
	   {
	      sbIconTag.append("r.");
	      sbIconTag.append(diagonal ? "d_" : "c_");
         sbIconTag.append(rail ? "rail_" : "road_");
         sbIconTag.append(NESW_STRINGS[variation]);
      }

      Icon i = getImage(sbIconTag.toString());

      if (i == null)
      {
         Logger.log(Logger.LOG_NORMAL,
            "Failed to find road image for "+sbIconTag.toString()
         );
      }

      return i;
	}

	
	public void computeAndSetShadow(int x, int y)
	{
	   CivMap m = getClient().getMap();
	
		Terrain n,s,w,e;
		n=s=w=e=null;
		if ( y > 0 )
			n = m.getTerrain(x,y-1);
		if ( y < m.getVerticalTiles()-1)
			s = m.getTerrain(x,y+1);

		w = m.getTerrain(m.adjustX(x-1),y);
		e = m.getTerrain(m.adjustX(x+1),y);
		int variation = getVariationID(
	      ( n == null || !n.isKnown() ),
	      ( s == null || !s.isKnown() ),
	      ( e == null || !e.isKnown() ),
	      ( w == null || !w.isKnown() )
		);
		
		List l = m.getTileList(x, y);
		if ( variation == 0 )
			l.set(CivMap.SHADOW_LAYER,null);
		else
         l.set(CivMap.SHADOW_LAYER, getDarkOverlay(variation));
	}
	
	private Icon[] m_darkIcons = new Icon[16];
	
	
	
	private Icon getDarkOverlay(int variation)
	{
	   Icon i = m_darkIcons[variation];
	   if (i == null)
	   {
	      i = getDarkOverlayIcon(variation);
	      m_darkIcons[variation] = i;
	   }
	   return i;
	}
	
	private Icon getDarkOverlayIcon(int variation)
	{
	   StringBuffer key = new StringBuffer(15);
	   key.append("tx.darkness_");
	   key.append(NESW_STRINGS[variation]);
	
	   Icon i = getImage(key.toString());
	
	   if (i == null)
	   {
	      Logger.log(Logger.LOG_NORMAL,
	         "Didn't find darkness overlay for "+key.toString());
	   }
	
	   return i;
	}
	
	
   public static void main(String[] args)
   {
      // Test harness
      TileSpec t = new TileSpec(null);
      t.loadTileset(args[0]);

      JFrame f = new JFrame();
      f.getContentPane().setLayout(new BorderLayout());
      JLabel l = new JLabel();
      l.setIcon(t.getImage(args[1]));
      f.getContentPane().add(l, BorderLayout.CENTER);
      f.pack();
      f.setLocation(100, 100);
      f.setVisible(true);

   }

}