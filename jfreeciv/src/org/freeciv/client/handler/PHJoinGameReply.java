package org.freeciv.client.handler;

import javax.swing.JOptionPane;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.net.Packet;
import org.freeciv.net.PktJoinGameReply;


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

      // Not sure about this.
      c.disconnect();
      
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
}
