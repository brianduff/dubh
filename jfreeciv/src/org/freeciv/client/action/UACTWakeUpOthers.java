package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
import java.util.Iterator;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

public class UACTWakeUpOthers extends AbstractUnitAction
{
  public UACTWakeUpOthers() 
  {
    super();
    setName( _( "Wake up Others" ) );
    addAccelerator( KeyEvent.VK_W, Event.SHIFT_MASK );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      for ( Iterator i = getClient().getGame().getMap().getTile( 
              unit.getX(), unit.getY() ).getUnits();
            i.hasNext(); )
      {
        Unit wUnit = (Unit)i.next();
        requestNewUnitActivity( wUnit, CommonConstants.ACTIVITY_IDLE );
      }
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    //TODO: is_unit_activity_on_tile(ACTIVITY_SENTRY, u.getX(), u.getY() );
    return false;
  }
}
