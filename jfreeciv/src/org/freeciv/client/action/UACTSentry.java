package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTSentry extends AbstractUnitAction
{
  public UACTSentry() 
  {
    super();
    putValue( NAME, _( "Sentry" ) );
    setAccelerator( KeyEvent.VK_F );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
