package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktUnitRequest;

public class UACTAutoSettler extends AbstractUnitAction
{
  public UACTAutoSettler() 
  {
    super();
    setName( _( "Auto Settler" ) );
    addAccelerator( KeyEvent.VK_A );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      PktUnitRequest pkt = new PktUnitRequest();
      pkt.unit_id = unit.getId();
      pkt.setType( PacketConstants.PACKET_UNIT_AUTO );
      getClient().sendToServer( pkt );
    }
    // else, maybe print a message about invalid auto moding?
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canDoAutoSettler();
  }

}
