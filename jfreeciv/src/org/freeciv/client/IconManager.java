package org.freeciv.client;


// TODO: Sort me out.
class IconManager
{

/*

   private SoftReference[] tileIcons = new SoftReference[400];
   private SoftReference[] unitIcons = new SoftReference[60];
   private SoftReference[] unitIconsDim = new SoftReference[60];
   private SoftReference[] smallIcons = new SoftReference[31];
   private SoftReference[] dblsizeSmallIcons = new SoftReference[31];
   private SoftReference[] roadIcons = new SoftReference[64];
   private SoftReference[] flagIcons = new SoftReference[28];
   private SoftReference[] spaceIcons;
   private SoftReference[] darkIcons = new SoftReference[16];

   Terrain[] terrains = new Terrain[400]; // more or less ...
   Terrain[] unknownTerrains = new Terrain[Constants.T_LAST];
   RoadOverlay[] roadOverlays = new RoadOverlay[16*4];
   RoadOverlay emptyRoad;
   RoadOverlay emptyRail;


  private Client m_client;

  public IconManager(Client c)
  {
    m_client = c;
  }



   private void initTileMetrics()
   {
      try {
         ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
            new FileInputStream(new File(cacheDir,"cache.timestamp"))));
         Dimension d = (Dimension)ois.readObject();
         ois.close();
         origTileWidth = d.width;
         origTileHeight = d.height;

      } catch (Exception e )
         {
            System.out.println(e);
            e.printStackTrace();
            System.exit(1);
         }

   }


   private FlashingImageIcon getIcon( SoftReference[] table, String name, int id)
   {
      if ( table[id].get() == null )
      {
         Image i =Toolkit.getDefaultToolkit().createImage(new FileImageSource(
                  new File(cacheDir,name+"." + id)
               ));
         if ( scaleMul != scaleDiv )
         {
            int w = -1;
            int h = -1;
            while ( (w=i.getWidth(null)) < 0 )
               sleep(100);
            while ( (h=i.getHeight(null)) < 0 )
               sleep(100);
            i = i.getScaledInstance(scaleMul*w/scaleDiv,scaleMul*h/scaleDiv,Image.SCALE_FAST );
         }
         FlashingImageIcon fii = new FlashingImageIcon(i);
         table[id] = new SoftReference(fii);
         return fii;
      }

      return (FlashingImageIcon)table[id].get();
   }

   private FlashingImageIcon getUnscalableIcon( SoftReference[] table, String name, int id)
   {
      if ( table[id].get() == null )
      {
         Image i =Toolkit.getDefaultToolkit().createImage(new FileImageSource(
                  new File(cacheDir,name+"." + id)
               ));
         FlashingImageIcon fii = new FlashingImageIcon(i);
         table[id] = new SoftReference(fii);
         return fii;
      }

      return (FlashingImageIcon)table[id].get();
   }

   private FlashingImageIcon getDblsizeUnscalableIcon( SoftReference[] table, String name, int id)
   {
      if ( table[id].get() == null )
      {
         Image i =Toolkit.getDefaultToolkit().createImage(new FileImageSource(
                  new File(cacheDir,name+"." + id)
               ));
         i = i.getScaledInstance(i.getWidth(null)*2,i.getHeight(null)*2,
            Image.SCALE_SMOOTH);
         FlashingImageIcon fii = new FlashingImageIcon(i);
         table[id] = new SoftReference(fii);
         return fii;
      }

      return (FlashingImageIcon)table[id].get();
   }




   public static final int BORDER_TILES = Constants.T_LAST;

   int[] fewTerrainIndex = new int[] {
      2,2,3,3,
      2,2,3,3,
      1,1,0,0,
      1,1,0,0
   };


   private int findTerrainIconIndex(int id, int variation)
   {

         if (id== Constants.T_GRASSLAND)
            return variation+1*20;
         else if (id == Constants.T_DESERT)
            return variation+2*20;
         else if (id == Constants.T_ARCTIC)
            return variation+3*20;
         else if (id == Constants.T_JUNGLE)
            return variation+4*20;
         else if (id == Constants.T_PLAINS)
            return variation+5*20;
         else if (id == Constants.T_SWAMP)
            return variation+6*20;
         else if (id == Constants.T_TUNDRA)
            return variation+7*20;
         else if (id == Constants.T_RIVER)
            return 15-variation+8*20;
         else if (id == Constants.T_OCEAN)
            return variation+9*20;
         else if (id == Constants.T_HILLS)
            return fewTerrainIndex[variation] + 10*20;
         else if (id == Constants.T_FOREST)
            return fewTerrainIndex[variation] +10*20+4;
         else if (id == Constants.T_MOUNTAINS)
            return fewTerrainIndex[variation] +10*20+8;
         else if (id == BORDER_TILES)
            return variation + 0*20;
         else
            throw new RuntimeException("Unknown terrain type " + id);
   }


   public Terrain getTerrain(int id, int variation )
   {
      int index = id*16 + variation;
      if ( terrains[index] == null )
      {
         terrains[index] = new Terrain(getIcon(tileIcons,"tiles",
            findTerrainIconIndex(id,variation)),id);
      }
      return terrains[index];
   }

   public Terrain getUnknownTerrain(int id)
   {
      return unknownTerrains[id];
   }

   public FlashingIcon getRiverDelta(int direction)
   {
      return getIcon(tileIcons,"tiles" ,8*20+16+direction);
   }

   public FlashingIcon getEmptyIcon()
   {
      return getIcon(unitIcons,"units",59);
   }

   private Icon getFlagIcon(int id)
   {
      return getIcon(flagIcons,"flags",id);
   }

   public Icon getFlagIcon(PktPlayerInfo plr)
   {
      return getFlagIcon(plr.nation);
   }

   public Icon getFlagForPlayer(int plrid)
   {
      return getFlagIcon(players[plrid].nation);
   }

   private FlashingIcon getRoadInternal(int index)
   {
      if ( roadOverlays[index] == null )
      {
         roadOverlays[index] = new RoadOverlay(getIcon(roadIcons,
            "roads",index), index >= 32);
      }
      return roadOverlays[index];
   }

   public FlashingIcon getNormalRoadOverlay(int type)
   {
      return getRoadInternal(type);
   }

   public FlashingIcon getDiagonalRoadOverlay(int type)
   {
      return getRoadInternal(type+16);
   }

   public FlashingIcon getNormalRailOverlay(int type)
   {
      return getRoadInternal(type+32);
   }

   public FlashingIcon getDiagonalRailOverlay(int type)
   {
      return getRoadInternal(type+48);
   }

   public FlashingIcon getEmptyRoad()
   {
      return emptyRoad;
   }


   public FlashingIcon getEmptyRail()
   {
      return emptyRail;
   }

   public FlashingIcon getUnitIcon(int gid)
   {
      return getIcon(unitIcons,"units",gid);
   }

   public FlashingIcon getUnitIconDim(int gid)
   {
      return getIcon(unitIconsDim,"units_dim",gid);
   }

   public FlashingIcon getHpIcon(int hp11)
   {
      return getIcon(tileIcons,"tiles",16*20+hp11);
   }

   public FlashingIcon getTerrainSpecial( int id)
   {
      return getIcon(tileIcons,"tiles",11*20+id);
   }

   public FlashingIcon getDarkOverlay( int variation )
   {
      FlashingIcon i = getIcon(darkIcons,"dark",variation);
      i.setVisible(showShadowBordersMI.isSelected());
      return i;
   }

   public FlashingIcon getHutIcon()
   {
      return getIcon(tileIcons,"tiles",12*20+14);

   }

   public FlashingIcon getCityIcon()
   {
      return getIcon(tileIcons,"tiles",12*20+12);
   }

   public FlashingIcon getWorkerIcon()
   {
      return getIcon(unitIcons,"units",41);
   }

   public FlashingIcon getSmallIcon( int id )
   {
      return getUnscalableIcon(smallIcons,"small",id);
   }

   public FlashingIcon getDblsizeSmallIcon( int id )
   {
      return getDblsizeUnscalableIcon(dblsizeSmallIcons,"small",id);
   }


   public FlashingIcon getScienceBulb(int fullness0_7 )
   {
      return getSmallIcon(fullness0_7);
   }

   public FlashingIcon getGovernmentIcon( int gvr )
   {
      return getSmallIcon(gvr + 8);
   }

   public FlashingIcon getSmallBalls( int id ) // ???
   {
      return getSmallIcon(id+14);
   }

   public FlashingIcon getElvisIcon()
   {
      return getDblsizeSmallIcon(22);
   }

   public FlashingIcon getSciencistIcon()
   {
      return getDblsizeSmallIcon(23);
   }

   public FlashingIcon getTaxmanIcon()
   {
      return getDblsizeSmallIcon(24);
   }

   public FlashingIcon getNormalCitizenIcon(boolean female)
   {
      return getDblsizeSmallIcon(25+ (female ? 1 : 0));
   }

   public FlashingIcon getHappyCitizenIcon(boolean female)
   {
      return getDblsizeSmallIcon(27+ (female ? 1 : 0));
   }

   public FlashingIcon getAngryCitizenIcon(boolean female)
   {
      return getDblsizeSmallIcon(29);
   }




   private void scaleIconArray( SoftReference[] icons, String name,
      int xsize, int ysize )
   {
      for ( int i=0; i < icons.length; i++)
      {
         if ( icons[i].get() == null )
            continue;
         ImageIcon ii = (ImageIcon)icons[i].get();
//       ii.setImage(ii.getImage().getScaledInstance(xsize,ysize,Image.SCALE_FAST));
         ii.setImage(Toolkit.getDefaultToolkit().createImage(
            new FileImageSource(new File(cacheDir, name + "." + i) )
               ).getScaledInstance(xsize,ysize,Image.SCALE_FAST ));
      }
   }


   public void scaleIcons(int mul, int div)
   {
      Coords c = map.getCentralTile();
      System.out.println("Started scaling");
      long start = System.currentTimeMillis();
      scaleMul = mul;
      scaleDiv = div;


      scaleIconArray(tileIcons,"tiles",
         mul*origTileWidth/div,mul*origTileHeight/div);

      scaleIconArray(unitIcons,"units",
         mul*origTileWidth/div,mul*origTileHeight/div);

      scaleIconArray(unitIconsDim,"units_dim",
         mul*origTileWidth/div,mul*origTileHeight/div);

      scaleIconArray(roadIcons,"roads",
         mul*origTileWidth/div,mul*origTileHeight/div);

      scaleIconArray(flagIcons,"flags",
         mul*origTileWidth/div,mul*origTileHeight/div);

      scaleIconArray(darkIcons,"dark",
         mul*origTileWidth/div,mul*origTileHeight/div);

      // do not scale smallIcons

      map.changeTileSize(
         mul*origTileWidth/div,mul*origTileHeight/div);


      mapPanel.invalidate();

      System.gc();
      System.out.println("Scaled in " + (System.currentTimeMillis()-start) );
      map.centerOnTile(c.x,c.y);

   }

   public void changeTileset(String dir)
   {
      Coords c = map.getCentralTile();
      File nCacheDir = new File(dataDir,"cache");
      boolean recompute = false;
      String iDir = dir;
      if ( dir != null )
      {
         nCacheDir = new File(nCacheDir,dir);
         recompute = !(new File(nCacheDir,"cache.timestamp" )).exists();
      }
      else
      {
         recompute = true;
         nCacheDir = cacheDir;
         iDir = cacheDir.getName();
      }

      if ( recompute )
      {
         File imgDir = new File(dataDir,"images");
         imgDir = new File(imgDir, iDir);
         try {
            new ImageSplitAndWrite().run(imgDir,nCacheDir);
         } catch ( IOException e )
            {
               System.out.println(e);
               System.exit(1);
            }
      }
      cacheDir = nCacheDir;
      initTileMetrics();
      map.centerOnTile(c.x,c.y);
      if ( dir != null )
         scaleIcons(1,1);
   }



*/

}