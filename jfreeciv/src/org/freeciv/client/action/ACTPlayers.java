package org.freeciv.client.action;

import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
public class ACTPlayers extends AbstractClientAction
{
  public ACTPlayers() 
  {
    super();
    putValue( NAME, _( "Players" ) );
    setAccelerator( KeyEvent.VK_F3, 0 );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    getClient().getDialogManager().getPlayersDialog().display();
  }
}
