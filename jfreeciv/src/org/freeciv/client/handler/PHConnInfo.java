package org.freeciv.client.handler;
import org.freeciv.client.Client;
import org.freeciv.common.Connection;
import org.freeciv.common.Player;
import org.freeciv.net.Packet;
import org.freeciv.net.PktConnInfo;
import org.freeciv.common.Logger;
/**
 * Packet handler for the CONN_INFO packet.
 * 
 * @author Brian.Duff@dubh.org
 */
public class PHConnInfo implements ClientPacketHandler
{
  /**
   * Return the class name of the packet that this
   * handler needs
   */
  public String getPacketClass()
  {
    return "org.freeciv.net.PktConnInfo";
  }
  /**
   * Your handler should implement this method to actually
   * handle an incoming packet.
   */
  public void handle( Client c, Packet pkt )
  {
    // packhand.c: handle_conn_info()
    PktConnInfo pinfo = (PktConnInfo)pkt;
    Logger.log( Logger.LOG_DEBUG, "conn_info " + pinfo.toString() );
    boolean connExists =  
      c.getFactories().getConnectionFactory().doesExist( pinfo.id );
      
    if( !pinfo.used )
    {
      if( !connExists )
      {
        Logger.log( Logger.LOG_VERBOSE, "Server removed unknown connection " + pinfo.id );
        return ;
      }
      Connection conn = (Connection)
        c.getFactories().getConnectionFactory().findById( pinfo.id );
      /* Forget the connection */
      c.getGame().getAllConnections().remove( conn );
      c.getGame().getEstablishedConnections().remove( conn );
      c.getGame().getGameConnections().remove( conn );
    }
    else
    {
      Connection conn;
      /* Add or update the connection */
      boolean playerExists = 
        c.getFactories().getPlayerFactory().doesExist( pinfo.player_num );
      if( !connExists )
      {
        Logger.log( Logger.LOG_VERBOSE, "Server reports new connection " + pinfo.id + " " + pinfo.name );
        conn = (Connection)c.getFactories().getConnectionFactory().create(pinfo);
        c.getGame().getAllConnections().add( conn );
        c.getGame().getEstablishedConnections().add( conn );
        c.getGame().getGameConnections().add( conn );
      }
      else
      {
        Logger.log( Logger.LOG_VERBOSE, "Server reports updated connection " + pinfo.id + " " + pinfo.name );
        conn = (Connection)
          c.getFactories().getConnectionFactory().findById( pinfo.id );

        if ( playerExists )
        {
          Player player = (Player)
            c.getFactories().getPlayerFactory().findById( pinfo.player_num );
            
          if (!player.equals( conn.getPlayer() ) )
          {
            if ( conn.getPlayer() != null )
            {
              conn.getPlayer().getConnections().remove( conn );
            }

            player.getConnections().add( conn );

          }
        }
      }
    }
  // update_players_dialog()
  }
}
