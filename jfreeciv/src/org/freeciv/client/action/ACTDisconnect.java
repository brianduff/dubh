package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
public class ACTDisconnect extends AbstractClientAction
{
  public ACTDisconnect() 
  {
    super();
    putValue( NAME, _( "Disconnect" ) );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    getClient().setConnected( false );
  }
}
