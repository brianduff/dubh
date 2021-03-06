package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.freeciv.common.CommonConstants;
import org.freeciv.common.Unit;

public class UACTBuildRoad extends AbstractUnitAction
{
  public UACTBuildRoad()
  {
    super();
    setName( translate( "Build Road" ) );
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
