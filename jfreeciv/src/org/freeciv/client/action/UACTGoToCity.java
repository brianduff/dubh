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
    putValue( NAME, _( "Go/Airlift to City" ) );
    setAccelerator( KeyEvent.VK_L );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
