package org.freeciv.client.action;

import java.awt.event.ActionEvent;


public class ACTDisconnect extends AbstractClientAction
{
  public ACTDisconnect() 
  {
    super();
    putValue( NAME, _( "Disconnect" ) );
    setEnabled( false );
  }
  public void actionPerformed( ActionEvent e )
  {
    getClient().disconnect();
  }
}
