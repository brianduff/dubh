package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.common.Assert;
import org.freeciv.common.City;
import org.freeciv.common.Unit;
import org.freeciv.net.Packet;
import org.freeciv.net.PktCityNameSuggestion;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktUnitRequest;

/**
 * Handle a city name suggestion packet
 * 
 * @author Ben Mazur
 */
public class PHCityNameSuggestion extends AbstractHandler implements Constants
{

  /**
   * Return the class name of the packet that this
   * handler needs
   */
  public Class getPacketClass()
  {
    return PktCityNameSuggestion.class;
  }
  
  /**
   * Your handler should implement this method to actually
   * handle an incoming packet.
   */
  public void handleOnEventThread( final Client c, final Packet pkt )
  {
    PktCityNameSuggestion packet = (PktCityNameSuggestion)pkt;
    
    Unit unit = c.getGame().getCurrentPlayer().getUnit( packet.id );
    if ( unit != null )
    {
      String cityName = c.getDialogManager().showInputDialog( 
              "What should we name the new city?",
              packet.name );
      
      if( cityName != null && cityName.length() > 0 
          && !cityName.equalsIgnoreCase( "uninitializedValue" ) ) // XXX
      {
        PktUnitRequest sendPacket = new PktUnitRequest();
        sendPacket.unit_id = unit.getId();
        sendPacket.name = cityName;
        sendPacket.setType( PacketConstants.PACKET_UNIT_BUILD_CITY );
        c.sendToServer( sendPacket );
      }
      // else, they hit "cancel"; no action.
    }
    // otherwise, maybe unit died; ignore
  }
}