package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.City;
import org.freeciv.common.CommonConstants;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktUnitRequest;

public class UACTDisbandUnit extends AbstractUnitAction
{
  public UACTDisbandUnit() 
  {
    super();
    putValue( NAME, _( "Disband Unit" ) );
    setAccelerator( KeyEvent.VK_D, Event.SHIFT_MASK );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      PktUnitRequest pkt = new PktUnitRequest();
      pkt.unit_id = unit.getId();
      pkt.setType( PacketConstants.PACKET_UNIT_DISBAND );
      getClient().sendToServer( pkt );
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return true;
  }
}
