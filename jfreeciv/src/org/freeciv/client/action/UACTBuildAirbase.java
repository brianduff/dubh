package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

public class UACTBuildAirbase extends AbstractUnitAction
{
  public UACTBuildAirbase() 
  {
    super();
    setName( _( "Build Airbase" ) );
    addAccelerator( KeyEvent.VK_E );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit) )
    {
      requestNewUnitActivity( unit, CommonConstants.ACTIVITY_AIRBASE );
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_AIRBASE );
  }

}
