package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.common.City;
import org.freeciv.common.Player;
import org.freeciv.common.Unit;
import org.freeciv.net.Packet;
import org.freeciv.net.PktUnitInfo;


/**
 * Unit info handler.
 */
public class PHUnitInfo extends AbstractHandler implements Constants
{
  public static final int UNIT_INFO_IDENTITY = 0;
  public static final int UNIT_INFO_CITY_SUPPORTED = 1;
  public static final int UNIT_INFO_CITY_PRESENT = 2;

  public Class getPacketClass()
  {
    return PktUnitInfo.class;
  }

  static int last_serial_num = 0;

  /**
   */
  public void handleOnCurrentThread( final Client c, Packet pkt )
  {
    PktUnitInfo packet = (PktUnitInfo) pkt;

    City city;
    Unit unit;

    if ( (packet.packet_use == UNIT_INFO_CITY_SUPPORTED) ||
         (packet.packet_use == UNIT_INFO_CITY_PRESENT) )
    {
     // hummm. int last_serial_num = 0;

      try
      {
        city = City.findById( packet.info_city_id );
      }
      catch (IllegalArgumentException ille)
      {
        return;
      }

      if ( last_serial_num != packet.serial_num )
      {
        last_serial_num = packet.serial_num;

        if ( city != null )
        {
          city.clearAllSupportedUnits();
          city.clearAllPresentUnits();
        }
      }

      if ( packet.packet_use == UNIT_INFO_CITY_SUPPORTED )
      {
        unit = new Unit( c.getGame() );
        unit.unpackage( packet );
        if ( city != null )
        {
          city.addSupportedUnit( unit );
        }
      }
      else if ( packet.packet_use == UNIT_INFO_CITY_PRESENT )
      {
        unit = new Unit( c.getGame() );
        unit.unpackage( packet );
        if ( city != null )
        {
          city.addPresentUnit( unit );
        }
      }

      return;
    }

    boolean repaintUnit = false;
    boolean repaintCity = false;

    unit = ((Player)c.getFactories().getPlayerFactory().findById( packet.owner )).getUnit( packet.id );

    if ( unit != null )
    {
      int dest_x, dest_y;

      // Activity changed
      if ( ( unit.getActivity() != packet.activity ) ||
           ( unit.getActivityTarget() != packet.activity_target ) )
      {
        repaintUnit = true;

        if ( c.getOptions().wakeupFocus
             && c.getGame().isCurrentPlayer( unit.getOwner() )
             && unit.getActivity() == ACTIVITY_SENTRY)
        {
          c.setUnitFocus( unit );
        }

        unit.setActivity( packet.activity );
        unit.setActivityTarget( packet.activity_target );

        if ( c.getGame().isCurrentPlayer( unit.getOwner() ) )
        {
          // c.refreshUnitCityDialogs( unit );
        }

        // if ( c.isFocusUnit( unit ) )
        // {
        //   c.updateMenus();
        // }
      }

      // Homecity changed
      if ( unit.getHomeCityId() != packet.homecity )
      {
        city = unit.getHomeCity();
        if (city != null)
        {
          city.removeSupportedUnit( unit );

          c.getDialogManager().refreshCityDialog( city );
        }
        unit.setHomeCity( packet.homecity );

        city = City.findById( packet.homecity ); // May throw IllegalArgumentException
        city.addSupportedUnit( unit );
        repaintCity = true;
      }




      // Hitpoints changed
      if ( unit.getHitPoints() != packet.hp )
      {
        unit.setHitPoints( packet.hp );
        repaintUnit = true;
      }

      // Change of type
      if ( unit.getType() != packet.type )
      {
        city = c.getGame().getMap().getCity( unit.getX(), unit.getY() );

        unit.setType( packet.type );
        repaintUnit = true;
        repaintCity = true;

        if (city != null && ( city != unit.getHomeCity() ) )
        {
          c.getDialogManager().refreshCityDialog( city );
        }
      }

      // Change of AI control
      if ( unit.getAI().isControlled() != packet.ai )
      {
        unit.getAI().setControlled( packet.ai );
        repaintUnit = true;
      }

      // Change location
      if ( unit.getX() != packet.x || unit.getY() != packet.y )
      {

        city = c.getGame().getMap().getCity( unit.getX(), unit.getY() );

        if ( c.getGame().getMap().getTile( unit.getX(), unit.getY() ).getKnown() == TILE_KNOWN )
        {
          c.doMoveUnit( unit, packet );
          // c.updateUnitFocus( );
        }
        else
        {
          c.doMoveUnit( unit, packet );
          c.removeUnit( unit );
          c.refreshTileMapCanvas( packet.x, packet.y, true );
          return;
        }

        if ( city != null )
        {
          if ( unit.getHomeCity() == city )
          {
            repaintCity = true;
          }
          else
          {
            c.getDialogManager().refreshCityDialog( city );
          }

          // packhand.c has a chunk of duplicate code here :)

          if ( unit.isFlagSet( F_CARAVAN )
            && ( !c.getGame().getCurrentPlayer().getAI().isControlled()  || c.getOptions().aiPopupWindows )
            && unit.getOwner() == c.getGame().getCurrentPlayer()
            && ( unit.getActivity() != ACTIVITY_GOTO || ( unit.getGotoDestX() == city.getX() && unit.getGotoDestY() == city.getY() ))
            && ( unit.canHelpBuildWonderHere() || unit.canSetTraderouteHere() ))
          {
            // c.processCaravanArrival( unit );
          }
        }

        repaintUnit = false;    // ?
      }

      if (unit.getUnhappiness() != packet.unhappiness )
      {
        unit.setUnhappiness( packet.unhappiness );
        repaintCity = true;
      }

      if (unit.getUpkeep() != packet.upkeep )
      {
        unit.setUpkeep( packet.upkeep );
        repaintCity = true;
      }

      if (unit.getUpkeepFood() != packet.upkeep_food )
      {
        unit.setUpkeepFood( packet.upkeep_food );
        repaintCity = true;
      }

      if ( unit.getUpkeepGold() != packet.upkeep_gold )
      {
        unit.setUpkeepGold( packet.upkeep_gold );
        repaintCity = true;
      }

      if ( repaintCity )
      {
        city = unit.getHomeCity();

        c.getDialogManager().refreshCityDialog( city );
      }

      unit.setVeteran( packet.veteran );
      unit.setMovesLeft( packet.movesleft );
      unit.setBribeCost( 0 ); // ?
      unit.setFuel( packet.fuel );
      unit.setGotoDestX( packet.goto_dest_x );
      unit.setGotoDestY( packet.goto_dest_y );
      unit.setParadropped( packet.paradropped );
      unit.setConnecting( packet.connecting );

      dest_x= packet.x;
      dest_y = packet.y;

      // fog of war
      if ( ! (c.getGame().getMap().getTile( unit.getX(), unit.getY() ).getKnown() == TILE_KNOWN))
      {
        // c.removeUnit( unit );
        c.refreshTileMapCanvas( dest_x, dest_y, true );
      }
    }
    else  // create a new unit
    {
      unit = new Unit( c.getGame() );
      unit.unpackage( packet );

      unit.setActivityCount( 0 );

      c.getGame().getPlayer( packet.owner ).addUnit( unit );
      c.getGame().getMap().getTile( unit.getX(), unit.getY() ).addUnit( unit );

      city = unit.getHomeCity();

      if ( city != null )
      {
        city.addSupportedUnit( unit );
      }

      repaintUnit = ! packet.carried;

    }

    if ( unit != null && c.isUnitInFocus( unit ) )
    {
      c.updateUnitInfoLabel( unit );
    }
    else if ( c.getUnitInFocus() != null &&
              c.getUnitInFocus().getX() == unit.getX() &&
              c.getUnitInFocus().getY() == unit.getY() )
    {
      c.updateUnitInfoLabel( c.getUnitInFocus() );
    }

    if ( repaintUnit )
    {
      c.refreshTileMapCanvas( unit.getX(), unit.getY(), true);
    }

    if ( packet.select_it && ( unit.isOwner( c.getGame().getCurrentPlayer() ) ) )
    {
      c.setUnitFocusAndSelect( unit );
    }
    else
    {
      c.updateUnitFocus();
    }

  }
}