package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class ACTMilitaryReport extends AbstractClientAction
{
  public ACTMilitaryReport() 
  {
    super();
    putValue( NAME, _( "Military Report" ) );
    setAccelerator( KeyEvent.VK_F2 );
    
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    getClient().getDialogManager().getMilitaryReport().display();
  }
}
