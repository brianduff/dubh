package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTGoTo extends AbstractUnitAction
{
  public UACTGoTo() 
  {
    super();
    setName( _( "Go To" ) );
    addAccelerator( KeyEvent.VK_G );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
