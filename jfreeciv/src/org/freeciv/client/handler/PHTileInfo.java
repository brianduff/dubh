package org.freeciv.client.handler;

import java.util.Iterator;
import javax.swing.SwingUtilities;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.common.Tile;
import org.freeciv.common.Map;
import org.freeciv.net.Packet;
import org.freeciv.net.PktTileInfo;


/**
 * Tile info handler.
 */
public class PHTileInfo extends AbstractHandler implements Constants
{
  public String getPacketClass()
  {
    return "org.freeciv.net.PktTileInfo";
  }
  /**
   */
  public void handleOnCurrentThread( final Client c, Packet pkt )
  {
    final PktTileInfo pmi = (PktTileInfo)pkt;

    Tile tile = c.getGame().getMap().getTile( pmi.x, pmi.y );

    int oldTerrain = tile.getTerrain();
    int oldKnown = tile.getKnown();
    boolean tileChanged = false;

    if ( oldTerrain != pmi.type )
    {
      tileChanged = true;
      tile.setTerrain( pmi.type );
    }

    if ( tile.getSpecial() != pmi.special )
    {
      tileChanged = true;
      tile.setSpecial( pmi.special );
    }

    tile.setKnown( pmi.known );

    // Fog of war
    if ( tile.getKnown() <= TILE_KNOWN_FOGGED && oldKnown == TILE_KNOWN )
    {
      Iterator unitIter = tile.getUnits();
      while (unitIter.hasNext())
      {
        // c.removeUnit( (Unit) unitIter.next() );
      }
    }

    if (( pmi.known >= TILE_KNOWN_FOGGED &&
          oldKnown == TILE_UNKNOWN && tile.getTerrain() != T_OCEAN) ||
        (( oldTerrain == T_OCEAN ) && (tile.getTerrain() != T_OCEAN)))
    {
      c.updateContinents( pmi.x, pmi.y );
    }
    else if (oldKnown >= TILE_KNOWN_FOGGED &&
        ((oldTerrain != T_OCEAN) && (tile.getTerrain() == T_OCEAN)))
    {
      int x, y;

      for( y = 0; y < c.getGame().getMap().getHeight(); y++)
      {
        for ( x = 0; x < c.getGame().getMap().getWidth(); x++)
        {
          c.getGame().getMap().getTile(x, y).setContinent( 0 ); //?
        }
      }

      c.initContinents();

      for ( y = 0; y < c.getGame().getMap().getHeight(); y++ )
      {
        for ( x = 0; x < c.getGame().getMap().getWidth(); x++ )
        {
          Tile t = c.getGame().getMap().getTile( x, y );
          if ( t.isKnown()  && t.getTerrain() != T_OCEAN )
          {
            c.updateContinents( x, y );
          }
              
        }
      }
      
    }

    if ( c.getGameState() == CLIENT_GAME_RUNNING_STATE )
    {
      int x = pmi.x;
      int y = pmi.y;

      Map m = c.getGame().getMap();

      if ( tileChanged || oldKnown != tile.getKnown() )
      {
        c.refreshTileMapCanvas( x, y, true );
      }

      if ( tileChanged )
      {
        if ( m.getTile( x-1, y ).isKnown() )
        {
          c.refreshTileMapCanvas( x-1, y, true );
        }
        else if ( m.getTile( x+1, y ).isKnown() )
        {
          c.refreshTileMapCanvas( x+1, y, true );
        }
        else if ( m.getTile( x, y-1 ).isKnown() )
        {
          c.refreshTileMapCanvas( x, y-1, true );
        }
        else if ( m.getTile( x, y+1 ).isKnown() )
        {
          c.refreshTileMapCanvas( x, y+1, true );
        }
        else if ( m.getTile( x-1, y+1 ).isKnown() )
        {
          c.refreshTileMapCanvas( x-1, y+1, true );
        }
        else if ( m.getTile( x+1, y+1 ).isKnown() )
        {
          c.refreshTileMapCanvas( x+1, y+1, true );
        }
        else if ( m.getTile( x+1, y-1 ).isKnown() )
        {
          c.refreshTileMapCanvas( x+1, y-1, true );
        }
        else if ( m.getTile( x-1, y-1 ).isKnown() )
        {
          c.refreshTileMapCanvas( x-1, y-1, true );
        }

        return;
      }

      if ( oldKnown == TILE_UNKNOWN && pmi.known >= TILE_KNOWN_FOGGED )
      {
        if ( m.getTile( x-1, y ).isKnown() )
        {
          c.refreshTileMapCanvas( x-1, y, true );
        }
        else if ( m.getTile( x+1, y ).isKnown() )
        {
          c.refreshTileMapCanvas( x+1, y , true);
        }
        else if ( m.getTile( x, y-1 ).isKnown() )
        {
          c.refreshTileMapCanvas( x, y-1 , true);
        }
        else if ( m.getTile( x, y+1 ).isKnown() )
        {
          c.refreshTileMapCanvas( x, y+1 , true );
        }
      }
    }

    SwingUtilities.invokeLater(new Runnable() {
      public void run()
      {
        // remove me when the map code has been transitioned
        c.getTileSpec().setTerrain( pmi, true );
      }
    });

  }
}
