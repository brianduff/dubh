package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class ACTCityReport extends AbstractClientAction
{
  public ACTCityReport() 
  {
    super();
    putValue( NAME, _( "City Report" ) );
    setAccelerator( KeyEvent.VK_F1 );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
