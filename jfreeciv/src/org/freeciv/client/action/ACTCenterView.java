package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class ACTCenterView extends AbstractClientAction
{
  public ACTCenterView() 
  {
    putValue( NAME, _( "Center View" ) );
    setAccelerator( KeyEvent.VK_C, 0 );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
