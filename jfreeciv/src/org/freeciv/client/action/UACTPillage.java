package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

public class UACTPillage extends AbstractUnitAction
{
  public UACTPillage() 
  {
    super();
    setName( _( "Pillage" ) );
    addAccelerator( KeyEvent.VK_P, Event.SHIFT_MASK );
    setEnabled( false );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null )
    {
      if ( unit.canDoActivity( CommonConstants.ACTIVITY_PILLAGE ) )
      {
        // TODO
      }
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_PILLAGE );
  }

}
