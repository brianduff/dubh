package org.freeciv.client.handler;
import org.freeciv.client.Client;
import org.freeciv.net.Packet;
import org.freeciv.net.PktGenericMessage;
import org.freeciv.client.Localize;
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
    c.disconnect();
    c.getDialogManager().hideAllDialogs();
    c.getDialogManager().showMessageDialogBlocking( _( "The server has been shut down." ) + ( (PktGenericMessage)pkt ).message, _( "Server Shutdown" ) );
  }
  // localization
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
