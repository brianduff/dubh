package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktUnitRequest;

public class UACTUnload extends AbstractUnitAction
{
  public UACTUnload() 
  {
    super();
    setName( _( "Unload" ) );
    addAccelerator( KeyEvent.VK_U );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      if( unit.getTransportCapacity() == 0 ) 
      {
        getClient().getMainWindow().getConsole().println(
                "Game: Only transporter units can be unloaded." );
        return;
      }
      
      //TODO: request_unit_wait(punit);    /* RP: unfocus the ship */
      
      PktUnitRequest pkt = new PktUnitRequest();
      pkt.unit_id = unit.getId();
      pkt.setType( PacketConstants.PACKET_UNIT_UNLOAD );
      getClient().sendToServer( pkt );
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.getTransportCapacity() > 0;
  }

}
