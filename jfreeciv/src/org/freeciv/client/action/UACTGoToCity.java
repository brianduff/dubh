package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTGoToCity extends AbstractUnitAction
{
  public UACTGoToCity() 
  {
    super();
    setName( _( "Go/Airlift to City" ) );
    addAccelerator( KeyEvent.VK_L );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
