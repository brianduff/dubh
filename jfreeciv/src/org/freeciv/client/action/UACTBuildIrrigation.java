package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.freeciv.common.CommonConstants;
import org.freeciv.common.Unit;

public class UACTBuildIrrigation extends AbstractUnitAction
{
  public UACTBuildIrrigation() 
  {
    super();
    setName( _( "Build Irrigation" ) );
    addAccelerator( KeyEvent.VK_I );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      requestNewUnitActivity( unit, CommonConstants.ACTIVITY_IRRIGATE );
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_IRRIGATE );
  }

}
