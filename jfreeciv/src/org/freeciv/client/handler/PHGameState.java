package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.client.action.ACTEndTurn;
import org.freeciv.common.Assert;
import org.freeciv.common.City;
import org.freeciv.common.Game;
import org.freeciv.common.Map;
import org.freeciv.common.MapPosition;
import org.freeciv.common.MapPositionIterator;
import org.freeciv.common.Tile;
import org.freeciv.common.Unit;
import org.freeciv.net.Packet;
import org.freeciv.net.PktGenericInteger;

/**
 * Handle the "game state" packet
 */
public class PHGameState extends AbstractHandler implements Constants
{

  /**
   * Return the class name of the packet that this
   * handler needs
   */
  public Class getPacketClass()
  {
    return PktGenericInteger.class;
  }
  
  /**
   * Your handler should implement this method to actually
   * handle an incoming packet.
   */
  public void handleOnEventThread( final Client c, final Packet pkt )
  {
    PktGenericInteger packet = (PktGenericInteger) pkt;
    
    if (c.getGameState() == CLIENT_SELECT_RACE_STATE &&
        packet.value == CLIENT_GAME_RUNNING_STATE &&
        c.getGame().getCurrentPlayer().getNation() == null) // Check this maybe replace with isNationSet()
    {
      c.getDialogManager().getNationDialog().undisplay();
    }

    c.setGameState( packet.value );

    if (c.getGameState() == CLIENT_GAME_RUNNING_STATE)
    {
    
      c.getMainWindow().getMapOverview().refresh();

      c.getAction( ACTEndTurn.class ).setEnabled( true );
      c.getGame().getCurrentPlayer().initUnitFocusStatus();

      c.updateInfoLabel();
      c.updateUnitFocus();
      c.updateUnitInfoLabel( c.getUnitInFocus() );

      // First, try centering on the focused unit
      Unit u = c.getUnitInFocus();
      City city;
      if ( u != null )
      {
        c.getMainWindow().getMapViewManager().centerOnTile(
          u.getX(), u.getY()
        );
      }
      else if ( (city = c.getGame().getCurrentPlayer().findPalace()) != null )
      {
        // Focus on the capital
        if ( city != null )
        {
          c.getMainWindow().getMapViewManager().centerOnTile(
            city.getX(), city.getY()
          );
        }
      }
      else if ( c.getGame().getCurrentPlayer().getCityCount() > 0 )
      {
        // focus on any city
        city = (City)c.getGame().getCurrentPlayer().getCities().next();
        Assert.that( city != null );
        c.getMainWindow().getMapViewManager().centerOnTile(
          city.getX(), city.getY()
        );
      }
      else if ( c.getGame().getCurrentPlayer().getUnitCount() > 0 )
      {
        // focus on any unit
        u= (Unit)c.getGame().getCurrentPlayer().getUnits().next();
        Assert.that( u != null );
        c.getMainWindow().getMapViewManager().centerOnTile(
          u.getX(), u.getY()
        );
      }
      else
      {
        // Any old tile near the center of the map
        int hx = c.getGame().getMap().getWidth() / 2;
        int hy = c.getGame().getMap().getHeight() / 2;
        c.getGame().getMap().iterateOutwards( hx, hy, Math.max( hx, hy ), 
          new MapPositionIterator() 
          {
            public void iteratePosition( MapPosition pos )
            {
              Game g = c.getGame();
              Map m = g.getMap();
              Tile t = m.getTile( pos.x, pos.y );
              if ( t.isKnown() )
              {
                c.getMainWindow().getMapViewManager().centerOnTile(
                  pos.x, pos.y
                );
                setFinished( true );
              }
            }
          }
        );
      }
    }
  }
}