package org.freeciv.client.handler;

import java.util.Iterator;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.common.City;
import org.freeciv.common.Logger;
import org.freeciv.common.Player;
import org.freeciv.common.Unit;
import org.freeciv.net.Packet;
import org.freeciv.net.PktCityInfo;

/**
 * Handler for CityInfo packets
 *
 * @author Brian Duff
 */
public class PHCityInfo extends AbstractHandler implements Constants
{
  public Class getPacketClass()
  {
    return PktCityInfo.class;
  }

  void handleOnEventThread( Client client, Packet p )
  {
    PktCityInfo pkt = (PktCityInfo)p;
    boolean cityIsNew = false;
    boolean updateDescriptions = false;

    City city = City.findById( pkt.id );

    if ( city != null && ( city.getOwner().getId() != pkt.owner ) )
    {
      client.removeCity( city );
      city = null;
    }

    if ( city == null )
    {
      cityIsNew = true;
      city = new City( client.getGame(), pkt.id );
    }
    else
    {
      cityIsNew = false;

      // Update city descriptions?
      if ( client.getOptions().drawCityNames 
        && !pkt.name.equals( city.getName() ) )
      {
        updateDescriptions = true;
      }

      if ( client.getOptions().drawCityProductions
        && ( pkt.is_building_unit != city.isBuildingUnit() 
          || pkt.currently_building != city.getCurrentlyBuildingId() ))
      {
        updateDescriptions = true;
      }

      assert( city.getId() == pkt.id );
    }

    city.unpackage( pkt, cityIsNew );

    boolean popup = (cityIsNew 
        && client.getGameState() == CLIENT_GAME_RUNNING_STATE
        && city.getOwner() == client.getGame().getCurrentPlayer())
      || pkt.diplomat_investigate;

    handleCityPacketCommon( client, city, cityIsNew, popup, 
      pkt.diplomat_investigate );

    // TODO: Update descriptions in packhand.c . looks scary
  
  }

  // Unsure where else this is used...
  // It's also called by PHShortCity -- BenM
  static void handleCityPacketCommon( Client client, City city, boolean isNew, 
    boolean popup, boolean investigate )
  {
    int i;

    if ( isNew )
    {
      city.getOwner().addCity( city );
      client.getGame().getMap().getTile( city.getX(), city.getY() ).setCity(
        city 
      );
      if ( city.getOwner() == client.getGame().getCurrentPlayer() )
      {
        if( client.getDialogManager().getCityReport().isShowing() )
        {
          client.getDialogManager().getCityReport().refresh();
        }
      }

      for ( i = 0; i < client.getGame().getNumberOfPlayers(); i++ )
      {
        Player p = client.getGame().getPlayer( i );
        Iterator it = p.getUnits();
        while ( it.hasNext() )
        {
          Unit u = (Unit) it.next();
          if ( u.getHomeCityId() == city.getId() )
          {
            city.addSupportedUnit( u );
          }
        }
      }
    }

    if ( client.getOptions().drawMapGrid
      && isNew && client.getGameState() == CLIENT_GAME_RUNNING_STATE )
    {
      int r = ((CITY_MAP_SIZE+1)/2);
      int d = (2*r)+1;
      int x = client.getGame().getMap().adjustX( city.getX() - r );
      int y = client.getGame().getMap().adjustY( city.getY() - r );

      client.getMainWindow().getMapViewManager().updateMapBuffer(
        x, y, d, d, true
      );
    }
    else
    {
      client.getMainWindow().getMapViewManager().refreshTileMapCanvas(
        city.getX(), city.getY()
      );
    }

    // City workers stuff

    // Popup stuff

    // Refresh city dialog
    client.getDialogManager().refreshCityDialog( city );

    Unit unit = client.getUnitInFocus();
    if ( unit != null 
      && client.getGame().getMap().isSamePosition( 
          unit.getX(), unit.getY(), city.getX(), city.getY() ))
    {
      // TODO: Update menus
    }

    if ( isNew )
    {
      Logger.log( Logger.LOG_DEBUG, 
        "New "+city.getOwner().getNation().getName() +" city "+
        city.getName() + " id "+ city.getId() + " ("+
        city.getX() + " " + city.getY() + ")" );
    }

    client.getGame().getMap().resetMoveCosts( city.getX(), city.getY() );
    
    
  }
}