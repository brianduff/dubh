package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTDisbandUnit extends AbstractUnitAction
{
  public UACTDisbandUnit() 
  {
    super();
    putValue( NAME, _( "Disband Unit" ) );
    setAccelerator( KeyEvent.VK_D, Event.SHIFT_MASK );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
