package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.freeciv.common.Unit;
import org.freeciv.net.PktMoveUnit;

/**
 * Move up unit action
 */
public class UACTMoveNorth extends AbstractUnitAction
{
  public UACTMoveNorth() 
  {
    super();
    putValue( NAME, _( "Move North" ) );
    setAccelerator( KeyEvent.VK_KP_UP );
    setEnabled( false );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    // Send a PktMoveUnit off to the server
    Unit u = getClient().getUnitInFocus();
    int nx = u.getX();
    int ny = u.getY() - 1;

    if ( ny >= 0 && ny < getClient().getGame().getMap().getHeight() )
    {
      PktMoveUnit packet = 
        (PktMoveUnit)getClient().createPacket( PktMoveUnit.class );
      packet.unitId = u.getId();
      packet.nx = nx;
      packet.ny = ny;
      getClient().sendToServer( packet );
    }
    
  }
}
