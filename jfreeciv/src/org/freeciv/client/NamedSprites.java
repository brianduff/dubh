package org.freeciv.client;

import javax.swing.Icon;

/**
 * Named sprites
 */
class NamedSprites implements Constants
{

  // Move these
  public static int NUM_TILES_PROGRESS = 8;
  public static int NUM_TILES_CITIZEN = 9;
  public static int NUM_TILES_HP_BAR = 11;
  public static int NUM_TILES_DIGITS  = 10;

  private Icon[] m_bulb = new Icon[ NUM_TILES_PROGRESS ];
  private Icon[] m_warming = new Icon[ NUM_TILES_PROGRESS ];
  private Icon[] m_cooling = new Icon[ NUM_TILES_PROGRESS ];
  private Icon[] m_citizen = new Icon[ NUM_TILES_CITIZEN ];
  private Icon[] m_treaty_thumb = new Icon[ 2 ];  // 0=disagree 1=agree
  private Icon m_right_arrow;
  private Icon m_black_tile;  // for isometric view
  private Icon m_dither_tile; // for isometric view
  private Icon m_coast_color; // for isometric view

  // corresponds to struct named_sprites in tilespec.h
  NamedSprites()
  {
  }

  /**
   * Initialize the sprites from the loaded images in the tilespec
   */
  void init(TileSpec ts)
  {
    // tilespec.c:tilespec_lookup_sprite_tags()
    m_treaty_thumb[ 0 ] = getSprite(ts, "treaty.disagree_thumb_down");
    m_treaty_thumb[ 1 ] = getSprite(ts, "treaty.agree_thumb_up");

    for (int i=0; i < NUM_TILES_PROGRESS; i++)
    {
      m_bulb[ i ] = getSprite(ts, "s.science_bulb_"+i);
      m_warming[ i ] = getSprite(ts, "s.warming_sun_"+i);
      m_cooling[ i ] = getSprite(ts, "s.cooling_flake_"+i);
    }    

    m_right_arrow = getSprite(ts, "s.right_arrow");

    if (ts.isIsometric())
    {
      m_black_tile = getSprite(ts, "t.black_tile");
      m_dither_tile = getSprite(ts, "t.dither_tile");
      m_coast_color = getSprite(ts, "t.coast_color");
    }

    m_citizen[0] = getSprite(ts, "citizen.entertainer");
    m_citizen[1] = getSprite(ts, "citizen.scientist");
    m_citizen[2] = getSprite(ts, "citizen.tax_collector");
    m_citizen[3] = getSprite(ts, "citizen.content_0");
    m_citizen[4] = getSprite(ts, "citizen.content_1");
    m_citizen[5] = getSprite(ts, "citizen.happy_0");
    m_citizen[6] = getSprite(ts, "citizen.happy_1");
    m_citizen[7] = getSprite(ts, "citizen.unhappy_0");
    m_citizen[8] = getSprite(ts, "citizen.unhappy_1");
  }

  private Icon getSprite(TileSpec ts, String name)
  {
    return (Icon) ts.getImage(name);
  }


  public Icon getBulbSprite(int idx)
  {
    return m_bulb[idx];
  }

  public Icon getWarmingSprite(int idx)
  {
    return m_warming[idx];
  }

  public Icon getCoolingSprite(int idx)
  {
    return m_cooling[idx];
  }

  public Icon getCitizenSprite(int idx)
  {
    return m_citizen[idx];
  }

  public Icon getTreatyThumbSprite(boolean agree)
  {
    return agree ? m_treaty_thumb[1] : m_treaty_thumb[0];
  }

  public Icon getRightArrowSprite()
  {
    return m_right_arrow;
  }

  public Icon getBlackTileSprite()
  {
    return m_black_tile;
  }

  public Icon getDitherTileSprite()
  {
    return m_dither_tile;
  }

  public Icon getCoastColorSprite()
  {
    return m_coast_color;
  }
  
}