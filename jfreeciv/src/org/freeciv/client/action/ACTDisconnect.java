package org.freeciv.client.action;

import java.io.IOException;
import java.awt.event.ActionEvent;

import org.freeciv.common.Logger;


public class ACTDisconnect extends AbstractClientAction
{
  public ACTDisconnect() 
  {
    super();
    setName( _( "Disconnect" ) );
    setEnabled( false );
  }
  public void actionPerformed( ActionEvent e )
  {
    try
    {
      getClient().disconnect();
    }
    catch (IOException ioe)
    {
      getClient().getDialogManager().showMessageDialog(
        "An error occurred disconnecting from the server"
      );
      Logger.log( Logger.LOG_ERROR, ioe );
    }
  }
}
