package org.freeciv.client.handler;
import org.freeciv.client.dialog.DlgNation;
import org.freeciv.client.Constants;
import org.freeciv.client.Client;
import org.freeciv.client.Options;
import org.freeciv.net.*;
import org.freeciv.client.dialog.DlgNotifyGoto;
/**
 * Chat message handler
 */
public class PHChatMsg extends PHGenericMessage implements Constants
{
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    PktGenericMessage msg = (PktGenericMessage)pkt;
    int where = Options.MW_OUTPUT;
    if( msg.event >= E_LAST )
    {
      
    

    //c.freelog(0, "Unknown event type "+msg.event);
    }
    else
    {
      if( msg.event >= 0 )
      {
        where = c.getOptions().getMessageWhere( msg.event );
      }
    }
    if( ( where & Options.MW_OUTPUT ) != 0 )
    {
      c.appendOutputWindow( msg.message );
    }
    if( ( where & Options.MW_MESSAGES ) != 0 )
    {
      c.addNotifyWindow( msg );
    }
    if( ( where & Options.MW_POPUP ) != 0 )
    {
      DlgNotifyGoto dng = c.getDialogManager().getNotifyGotoDialog();
      dng.display( c, c.getMainFrame(), msg.message, msg.x, msg.y );
    }
  }
}
