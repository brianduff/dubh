package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Connection;
import org.freeciv.client.Player;
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
  public void handle(Client c, Packet pkt)
  {
    // packhand.c: handle_conn_info()
    
    PktConnInfo pinfo = (PktConnInfo)pkt;
    
    Logger.log(Logger.LOG_DEBUG, "conn_info "+pinfo.toString());


    Connection conn = (Connection)c.getConnectionFactory().findById(pinfo.id);

    if (!pinfo.used)
    {
      if (conn == null)
      {
        Logger.log(Logger.LOG_VERBOSE, 
          "Server removed unknown connection "+pinfo.id
        );

        return;
      }
      /* Forget the connection */
      c.getGame().getAllConnections().remove(conn);
      c.getGame().getEstablishedConnections().remove(conn);
      c.getGame().getGameConnections().remove(conn);
    }
    else
    {
      /* Add or update the connection */
      Player player = (Player) c.getPlayerFactory().findById(pinfo.player_num);

      if (conn == null)
      {
        Logger.log(Logger.LOG_VERBOSE, "Server reports new connection "+
          pinfo.id+" "+pinfo.name
        );

        conn = (Connection)c.getConnectionFactory().create();
        c.getGame().getAllConnections().add(conn);
        c.getGame().getEstablishedConnections().add(conn);
        c.getGame().getGameConnections().add(conn);
      }
      else
      {
        Logger.log(Logger.LOG_VERBOSE, "Server reports updated connection "+
          pinfo.id+" "+pinfo.name
        );

        if (player != null && !player.equals(conn.getPlayer()))
        {
          if (conn.getPlayer() != null)
          {
            conn.getPlayer().getConnections().remove(conn);
          }
          if (player != null)
          {
            player.getConnections().add(conn);
          }
        }
      }
      conn.setId(pinfo.id);
      conn.setEstablished(pinfo.established);
      conn.setObserver(pinfo.observer);
      conn.setAccessLevel(pinfo.access_level);
      conn.setPlayer(player);
      conn.setName(pinfo.name);
      conn.setAddress(pinfo.addr);
      conn.setCapability(pinfo.capability);
    }

    // update_players_dialog()
  }
}