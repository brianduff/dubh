package org.freeciv.common;

import javax.swing.Icon;

import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetCity;

/**
 * The styles of cities
 */
public class CityStyle implements GameObject
{
  private GameObjectFactory m_cityStyleFactory;
  private PktRulesetCity m_ruleset;
  private Advance m_requiredAdvance = null;
  private CityStyle m_replacedBy = null;
  public static final int MAX_CITY_TILES = 8;
  
  private Icon[] m_tileSprites = new Icon[MAX_CITY_TILES + 1];
  private int m_numTiles = 0;
  private int[] m_threshold = new int[MAX_CITY_TILES + 1];
  private Icon[] m_wallSprites = new Icon[ MAX_CITY_TILES + 1 ];



  CityStyle(GameObjectFactory cityStyleFactory)
  {
    m_cityStyleFactory = cityStyleFactory;
  }

  public void initFromPacket(Packet pkt)
  {
    m_ruleset = (PktRulesetCity) pkt;

  }

  public int getId()
  {
    return m_ruleset.style_id;
  }

  /**
   * Is an advance required in order to use this city style? If so, you
   * can find out what the advance is by calling getRequiredAdvance()
   */
  public boolean isAdvanceRequired()
  {
    return (getRequiredAdvance() != null);
  }

  /**
   * This can be null
   */
  public Advance getRequiredAdvance()
  {
    if (m_ruleset.techreq == 0) // A_NONE
    {
      return null;
    }

    
    if (m_requiredAdvance == null)
    {
      m_requiredAdvance = (Advance)
        m_cityStyleFactory.getParent().getAdvanceFactory().findById(
          m_ruleset.techreq
        );
    }
    return m_requiredAdvance;
  }

  public CityStyle getReplacedBy()
  {
    if (m_replacedBy == null)
    {
      m_replacedBy = (CityStyle)
        m_cityStyleFactory.findById(m_ruleset.replaced_by);
    }
    return m_replacedBy;
  }

  public String getName()
  {
    return m_ruleset.name;
  }

  public String getGraphic()
  {
    return m_ruleset.graphic;
  }

  public String getGraphicAlt()
  {
    return m_ruleset.graphic_alt;
  }

  public int getNumTiles()
  {
    return m_numTiles;
  }

  public void setNumTiles( int numTiles)
  {
    m_numTiles = numTiles;
  }

  public int getThreshold(int uh)
  {
    return m_threshold[ uh];
  }

  public void setThreshold( int uh, int erm )
  {
    m_threshold[uh] = erm;
  }

  public Icon getTileSprite( int idx )
  {
    return m_tileSprites[idx];
  }

  public void setTileSprite( int idx, Icon icon)
  {
    m_tileSprites[idx] = icon;
  }

  public void setWallSprite( int idx, Icon icon)
  {
    m_wallSprites[idx] = icon;
  }

  public Icon getWallSprite( int idx )
  {
    return m_wallSprites[idx];
  }
}