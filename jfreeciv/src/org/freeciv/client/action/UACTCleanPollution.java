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
    putValue( NAME, _( "Clean Pollution" ) );
    setAccelerator( KeyEvent.VK_P );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }

  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_POLLUTION );
  }

}
