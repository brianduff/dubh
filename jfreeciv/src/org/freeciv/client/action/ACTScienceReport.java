package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class ACTScienceReport extends AbstractClientAction
{
  public ACTScienceReport() 
  {
    super();
    setName( _( "Science Report" ) );
    addAccelerator( KeyEvent.VK_F6 );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    getClient().getDialogManager().getScienceReportDialog().display();
  }
}
