package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTMakeHomeCity extends AbstractUnitAction
{
  public UACTMakeHomeCity() 
  {
    super();
    putValue( NAME, _( "Make Home City" ) );
    setAccelerator( KeyEvent.VK_H );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
