package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

public class UACTCleanPollution extends AbstractUnitAction
{
  public UACTCleanPollution() 
  {
    super();
    setName( _( "Clean Pollution" ) );
    addAccelerator( KeyEvent.VK_P );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      requestNewUnitActivity( unit, CommonConstants.ACTIVITY_POLLUTION );
    }
  }

  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_POLLUTION );
  }

}
