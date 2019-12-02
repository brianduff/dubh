package org.freeciv.client.handler;

import java.io.IOException;

import org.freeciv.client.Client;
import org.freeciv.common.Logger;
import org.freeciv.net.Packet;
import org.freeciv.util.Localize;


/**
 * This interface is implemented by objects that handle packets received
 * from the server.
 */
class PHServerShutdown extends PHGenericMessage
{
  /**
   * Your handler should implement this method to actually
   * handle an incoming packet.
   */
  public void handle( Client c, Packet pkt )
  {
    try
    {
      c.disconnect();
    }
    catch ( IOException ioe )
    {
      c.getDialogManager().showMessageDialog(
        translate( "An error occurred disconnecting from the server" )
      );
      Logger.log( Logger.LOG_ERROR, ioe );
    }

    c.getDialogManager().showMessageDialog(
      translate( "Lost connection to server!")
    );


  }
  // localization
  private static String translate( String txt )
  {
    return Localize.translate( txt );
  }
}
