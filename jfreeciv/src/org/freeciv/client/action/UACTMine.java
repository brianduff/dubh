package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTMine extends AbstractUnitAction
{
  public UACTMine() 
  {
    super();
    putValue( NAME, _( "Mine" ) ); // Needs to be able to change
    setAccelerator( KeyEvent.VK_M );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
