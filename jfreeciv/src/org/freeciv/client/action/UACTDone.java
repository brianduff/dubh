package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.City;
import org.freeciv.common.CommonConstants;
public class UACTDone extends AbstractUnitAction
{
  public UACTDone() 
  {
    super();
    setName( _( "Done" ) );
    addAccelerator( KeyEvent.VK_SPACE );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      unit.setFocusStatus( Unit.FOCUS_DONE );
      if( getClient().isUnitInFocus( unit ) )
      {
        getClient().advanceUnitFocus();
      }
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return true;
  }
}
