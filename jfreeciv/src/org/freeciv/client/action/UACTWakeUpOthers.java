package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTWakeUpOthers extends AbstractUnitAction
{
  public UACTWakeUpOthers() 
  {
    super();
    putValue( NAME, _( "Wake up Others" ) );
    setAccelerator( KeyEvent.VK_W, Event.SHIFT_MASK );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
