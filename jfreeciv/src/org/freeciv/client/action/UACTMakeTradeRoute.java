package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class UACTMakeTradeRoute extends AbstractUnitAction
{
  public UACTMakeTradeRoute()
  {
    super();
    setName( translate( "Make Trade Route" ) );
    addAccelerator( KeyEvent.VK_R, Event.SHIFT_MASK );
  }
  public void actionPerformed( ActionEvent e )
  {

  }
}
