package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTDone extends AbstractUnitAction
{
  public UACTDone() 
  {
    super();
    putValue( NAME, _( "Done" ) );
    setAccelerator( KeyEvent.VK_SPACE );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
