package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTAutoAttack extends AbstractUnitAction
{
  public UACTAutoAttack() 
  {
    super();
    putValue( NAME, _( "Auto Attack" ) );
    setAccelerator( KeyEvent.VK_A, Event.SHIFT_MASK );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
