package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

public class UACTBuildFortress extends AbstractUnitAction
{
  public UACTBuildFortress() 
  {
    super();
    putValue( NAME, _( "Build Fortress" ) ); // Needs to be able to change
    setAccelerator( KeyEvent.VK_F, Event.SHIFT_MASK );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      requestNewUnitActivity( unit, CommonConstants.ACTIVITY_FORTRESS );
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_FORTRESS );
  }

}
