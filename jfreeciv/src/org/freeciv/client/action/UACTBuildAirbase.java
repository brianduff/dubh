package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

public class UACTBuildAirbase extends AbstractUnitAction
{
  public UACTBuildAirbase() 
  {
    super();
    putValue( NAME, _( "Build Airbase" ) );
    setAccelerator( KeyEvent.VK_E );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }

  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_AIRBASE );
  }

}
