package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTMakeTradeRoute extends AbstractUnitAction
{
  public UACTMakeTradeRoute() 
  {
    super();
    putValue( NAME, _( "Make Trade Route" ) );
    setAccelerator( KeyEvent.VK_R, Event.SHIFT_MASK );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
