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
    putValue( NAME, _( "Build Irrigation" ) );
    setAccelerator( KeyEvent.VK_I );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_IRRIGATE );
  }

}
