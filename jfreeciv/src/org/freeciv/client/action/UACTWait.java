package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.City;
import org.freeciv.common.CommonConstants;

public class UACTWait extends AbstractUnitAction
{
  public UACTWait() 
  {
    super();
    putValue( NAME, _( "Wait" ) );
    setAccelerator( KeyEvent.VK_W );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      //TODO
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return false; // should be true, once working
  }
}
