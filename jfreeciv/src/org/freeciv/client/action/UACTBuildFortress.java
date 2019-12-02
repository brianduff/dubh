package org.freeciv.client.action;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.freeciv.common.CommonConstants;
import org.freeciv.common.Unit;

public class UACTBuildFortress extends AbstractUnitAction
{
  public UACTBuildFortress()
  {
    super();
    setName( translate( "Build Fortress" ) ); // Needs to be able to change
    addAccelerator( KeyEvent.VK_F, Event.SHIFT_MASK );
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
