package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktGenericInteger;
import org.freeciv.net.PktUnitRequest;

public class UACTBuildCity extends AbstractUnitAction
{
  public UACTBuildCity() 
  {
    super();
    setName( _( "Build City" ) );
    addAccelerator( KeyEvent.VK_B );
  }
  
  /**
   * Player pressed 'b' or otherwise instructed unit to build or add to city.
   * If the unit can build a city, we popup the appropriate dialog.
   * Otherwise, we just send a packet to the server.
   * If this action is not appropriate, the server will respond
   * with an appropriate message.  (This is to avoid duplicating
   * all the server checks and messages here.)
   */
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if( unit != null ) 
    {
      if ( unit.canBuildCity() )
      {
        PktGenericInteger pkt = new PktGenericInteger();
        pkt.value = unit.getId();
        pkt.setType( PacketConstants.PACKET_CITY_NAME_SUGGEST_REQ );
        getClient().sendToServer( pkt );
      }
      else
      {
        PktUnitRequest pkt = new PktUnitRequest();
        pkt.unit_id = unit.getId();
        pkt.setType( PacketConstants.PACKET_UNIT_BUILD_CITY );
        getClient().sendToServer( pkt );
      }
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canBuildCity()
      || ( u.isFlagSet( CommonConstants.F_CITIES )
          && getClient().getGame().getMap().getCity( u.getX(), u.getY() ) != null );
  }

}
