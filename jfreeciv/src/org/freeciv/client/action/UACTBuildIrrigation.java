package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

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
