package org.freeciv.client.handler;

import java.util.Iterator;
import java.util.ArrayList;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.common.Tile;
import org.freeciv.common.Map;
import org.freeciv.common.Unit;
import org.freeciv.net.Packet;
import org.freeciv.net.PktTileInfo;


/**
 * Tile info handler.
 */
public class PHTileInfo extends AbstractHandler implements Constants
{
  public Class getPacketClass()
  {
    return PktTileInfo.class;
  }
  /**
   */
  public void handleOnCurrentThread( final Client c, Packet pkt )
  {
    final PktTileInfo pmi = (PktTileInfo)pkt;

    Tile tile = c.getGame().getMap().getTile( pmi.x, pmi.y );

    if ( tile == null ) return; // TODO: Why is this happennig?

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
      // To avoid a ConcurrentModificationException, first get the units 
      // of the tile to remove, then really remove them.
      ArrayList unitsToRemove = new ArrayList();
      Iterator unitIter = tile.getUnits();
      while ( unitIter.hasNext())
        unitsToRemove.add((Unit)unitIter.next());

      // Really remove the units
      int nbunits = unitsToRemove.size();
      for (int i=0; i<nbunits; i++)
        c.removeUnit( (Unit) unitsToRemove.get(i) );
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
        c.getMainWindow().getMapViewManager().refreshTileMapCanvas( 
          x, y );
      }

      /*
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
      */
    }


  }
}
