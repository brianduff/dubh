package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class ACTQuit extends AbstractClientAction
{
  public ACTQuit() 
  {
    super();
    putValue( NAME, _( "Quit" ) );
    setAccelerator( KeyEvent.VK_Q, Event.CTRL_MASK );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    getClient().quit();
  }
}
