package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

public class UACTFortify extends AbstractUnitAction
{
  public UACTFortify() 
  {
    super();
    setName( _( "Fortify" ) );
    addAccelerator( KeyEvent.VK_F );    
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      requestNewUnitActivity( unit, CommonConstants.ACTIVITY_FORTIFYING );
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_FORTIFYING );
  }

}
