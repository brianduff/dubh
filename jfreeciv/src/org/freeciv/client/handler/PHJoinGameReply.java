package org.freeciv.client.handler;
import org.freeciv.client.*;
import org.freeciv.net.*;
import javax.swing.JOptionPane;
/**
 * This interface is implemented by objects that handle packets received
 * from the server.
 */
public class PHJoinGameReply implements ClientPacketHandler
{

  public String getPacketClass()
  {
    return "org.freeciv.net.PktJoinGameReply";
  }
  
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    PktJoinGameReply p = (PktJoinGameReply)pkt;

    c.appendOutputWindow( "Client capability string: "+ c.getCapabilities() );
    c.appendOutputWindow( "Server capability string: "+ p.capabilities ); 

    
    if( !p.youCanJoin )
    {
      JOptionPane.showMessageDialog( c, p.message, "Join Game Refused", JOptionPane.ERROR_MESSAGE );
      c.appendOutputWindow( "You were rejected from the game: " + p.message );
      return ;
    }

    c.setGameState( Constants.CLIENT_PRE_GAME_STATE ); //?

    if (c.getCapabilities().equals(p.capabilities))
    {
      return;
    }
    
  
    

  }
}
