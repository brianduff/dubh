package org.freeciv.client.action;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.freeciv.common.Unit;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktUnitRequest;

public class UACTAutoAttack extends AbstractUnitAction
{
  public UACTAutoAttack()
  {
    super();
    setName( translate( "Auto Attack" ) );
    addAccelerator( KeyEvent.VK_A, Event.SHIFT_MASK );
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
    return u.canDoAutoAttack();
  }
}
