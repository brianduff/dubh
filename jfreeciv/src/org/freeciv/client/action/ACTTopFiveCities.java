package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class ACTTopFiveCities extends AbstractClientAction
{
  public ACTTopFiveCities() 
  {
    super();
    putValue( NAME, _( "Top Five Cities" ) );
    setAccelerator( KeyEvent.VK_F8 );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
