package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

public class UACTAutoExplore extends AbstractUnitAction
{
  public UACTAutoExplore() 
  {
    super();
    setName( _( "Auto Explore" ) );
    addAccelerator( KeyEvent.VK_X );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      requestNewUnitActivity( unit, CommonConstants.ACTIVITY_EXPLORE );
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_EXPLORE );
  }

}
