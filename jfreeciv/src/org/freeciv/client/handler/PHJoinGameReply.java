package org.freeciv.client.handler;

import java.io.IOException;
import javax.swing.JOptionPane;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.common.Logger;
import org.freeciv.net.Packet;
import org.freeciv.net.PktJoinGameReply;
import org.freeciv.util.Localize;


/**
 * This interface is implemented by objects that handle packets received
 * from the server.
 */
public class PHJoinGameReply implements ClientPacketHandler
{

  public Class getPacketClass()
  {
    return PktJoinGameReply.class;
  }

  /**
   */
  public void handle( Client c, Packet pkt )
  {
    PktJoinGameReply p = (PktJoinGameReply)pkt;

    if( !p.youCanJoin )
    {
      JOptionPane.showMessageDialog(
        c.getMainWindow(),
        "Your request to join the game was refused: \n"+
        p.message,
        c.APP_NAME,
        JOptionPane.WARNING_MESSAGE
      );
      c.getMainWindow().getConsole().println(
        "You were rejected from the game: " + p.message
      );

      try
      {
        // Not sure about this.
        c.disconnect();
      }
      catch ( IOException ioe )
      {
        c.getDialogManager().showMessageDialog(
          translate( "An error occurred disconnecting from the server" )
        );
        Logger.log( Logger.LOG_ERROR, ioe );
      }

      return ;
    }

    c.setGameState( Constants.CLIENT_PRE_GAME_STATE ); //?

    if (c.getCapabilities().equals(p.capabilities))
    {
      return;
    }

    c.getMainWindow().getConsole().println( "Client capability string: "+ c.getCapabilities() );
    c.getMainWindow().getConsole().println( "Server capability string: "+ p.capabilities );

  }

  private static String translate(String s)
  {
    return Localize.translate( s );
  }
}
