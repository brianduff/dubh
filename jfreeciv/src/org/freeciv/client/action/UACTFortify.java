package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTFortify extends AbstractUnitAction
{
  public UACTFortify() 
  {
    super();
    putValue( NAME, _( "Fortify" ) );
    setAccelerator( KeyEvent.VK_F );
    
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
