package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.freeciv.common.CommonConstants;
import org.freeciv.common.Unit;

public class UACTSentry extends AbstractUnitAction
{
  public UACTSentry()
  {
    super();
    setName( translate( "Sentry" ) );
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
