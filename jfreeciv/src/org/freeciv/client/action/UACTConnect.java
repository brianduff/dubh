package org.freeciv.client.action;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTConnect extends AbstractUnitAction
{
  public UACTConnect()
  {
    super();
    setName( translate( "Connect" ) );
    addAccelerator( KeyEvent.VK_C, Event.SHIFT_MASK );
  }
  public void actionPerformed( ActionEvent e )
  {

  }
}
