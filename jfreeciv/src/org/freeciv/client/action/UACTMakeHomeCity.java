package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import org.freeciv.common.Unit;
import org.freeciv.common.City;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktUnitRequest;

public class UACTMakeHomeCity extends AbstractUnitAction
{
  public UACTMakeHomeCity() 
  {
    super();
    setName( _( "Make Home City" ) );
    addAccelerator( KeyEvent.VK_H );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      City city = getClient().getGame().getMap().getCity( unit.getX(), unit.getY() );
      if( city != null )
      {
        PktUnitRequest pkt = new PktUnitRequest();
        pkt.unit_id = unit.getId();
        pkt.city_id = city.getId();
        pkt.setType( PacketConstants.PACKET_UNIT_CHANGE_HOMECITY );
        getClient().sendToServer( pkt );
      }
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canChangeHomecity();
  }

}
