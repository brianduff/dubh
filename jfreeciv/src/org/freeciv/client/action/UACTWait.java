package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.freeciv.common.Unit;

public class UACTWait extends AbstractUnitAction
{
  public UACTWait() 
  {
    super();
    setName( _( "Wait" ) );
    addAccelerator( KeyEvent.VK_W );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      unit.setFocusStatus( Unit.FOCUS_WAIT );
      if( getClient().isUnitInFocus( unit ) )
      {
        getClient().advanceUnitFocus();
      }
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return true;
  }
}
