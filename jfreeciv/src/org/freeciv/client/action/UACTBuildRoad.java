package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

public class UACTBuildRoad extends AbstractUnitAction
{
  public UACTBuildRoad() 
  {
    super();
    setName( _( "Build Road" ) );
    addAccelerator( KeyEvent.VK_R );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null )
    {
      if ( unit.canDoActivity( CommonConstants.ACTIVITY_ROAD ) )
      {
        requestNewUnitActivity( unit, CommonConstants.ACTIVITY_ROAD );
      }
      else if ( unit.canDoActivity( CommonConstants.ACTIVITY_RAILROAD ) )
      {
        requestNewUnitActivity( unit, CommonConstants.ACTIVITY_RAILROAD );
      }
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_ROAD )
      || u.canDoActivity( CommonConstants.ACTIVITY_RAILROAD );
  }

}
