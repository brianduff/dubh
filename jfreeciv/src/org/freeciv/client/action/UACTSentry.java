package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

public class UACTSentry extends AbstractUnitAction
{
  public UACTSentry() 
  {
    super();
    setName( _( "Sentry" ) );
    addAccelerator( KeyEvent.VK_S );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null )
    {
      if ( unit.canDoActivity( CommonConstants.ACTIVITY_SENTRY ) )
      {
        requestNewUnitActivity( unit, CommonConstants.ACTIVITY_SENTRY );
      }
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_SENTRY );
  }

}
