package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

public class UACTMine extends AbstractUnitAction
{
  public UACTMine() 
  {
    super();
    setName( _( "Mine" ) ); // Needs to be able to change
    addAccelerator( KeyEvent.VK_M );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit) )
    {
      requestNewUnitActivity( unit, CommonConstants.ACTIVITY_MINE );
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_MINE );
  }

}
