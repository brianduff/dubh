package org.freeciv.client.handler;


import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.common.City;
import org.freeciv.net.Packet;
import org.freeciv.net.PktShortCity;

/**
 * Short City packet handler.  A modified copy of PHCityInfo
 * 
 * @author Brian Duff, Ben Mazur
 */
public class PHShortCity extends AbstractHandler implements Constants
{
  /**
   * Return the class name of the packet that this
   * handler needs
   */
  public Class getPacketClass()
  {
    return PktShortCity.class;
  }
  /**
   * 
   */
  public void handleOnEventThread( Client client, Packet p )
  {
    PktShortCity pkt = (PktShortCity)p;
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

      assert( city.getId() == pkt.id );
    }
    
    city.unpackage( pkt, cityIsNew );

    PHCityInfo.handleCityPacketCommon( client, city, cityIsNew, false, false );

    // TODO: Update descriptions in packhand.c . looks scary
  }
}
