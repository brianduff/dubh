package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class UACTHelpBuildWonder extends AbstractUnitAction
{
  public UACTHelpBuildWonder()
  {
    super();
    setName( translate( "Help Build Wonder" ) );
    addAccelerator( KeyEvent.VK_B, Event.SHIFT_MASK );
  }
  public void actionPerformed( ActionEvent e )
  {

  }
}
