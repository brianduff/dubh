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
    setName( _( "City Report" ) );
    addAccelerator( KeyEvent.VK_F1 );
    
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    getClient().getDialogManager().getCityReport().display();
  }
}
