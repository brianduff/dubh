package org.freeciv.client.handler;

import org.freeciv.client.handler.ClientPacketHandler;
import org.freeciv.client.Client;
import org.freeciv.common.Player;
import org.freeciv.net.Packet;
import org.freeciv.net.PktSpaceshipInfo;

/**
 * Packet handler for spaceship_info packets.
 */
final class PHSpaceshipInfo implements ClientPacketHandler
{

  public Class getPacketClass()
  {
    return PktSpaceshipInfo.class;
  }
  
  /**
   */
  public void handle( final Client c, final Packet pkt )
  {
    PktSpaceshipInfo p = (PktSpaceshipInfo) pkt;
    Player player = c.getGame().getPlayer( p.player_num );

    player.getSpaceship().initFromPacket( pkt );

    if ( !c.getGame().isCurrentPlayer( player ) )
    {
      // c.refreshSpaceshipDialog(); ??
      return;
    }

    // c.updateMenus();

    // if (!c.getSpaceship().isAutoplace())
    // {
    //   c.refreshSpaceshipDialog();
    // }
  }
}