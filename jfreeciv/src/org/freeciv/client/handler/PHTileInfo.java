package org.freeciv.client.handler;
import org.freeciv.client.*;
import org.freeciv.client.dialog.*;
import org.freeciv.net.*;
import javax.swing.SwingUtilities;
/**
 * Tile info handler.
 */
public class PHTileInfo extends AbstractHandler implements Constants
{
  public String getPacketClass()
  {
    return "org.freeciv.net.PktTileInfo";
  }
  /**
   */
  public void handleOnEventThread( Client c, Packet pkt )
  {
    PktTileInfo pmi = (PktTileInfo)pkt;
    c.getTileSpec().setTerrain( pmi, true /* Need to sort out the state stuff(c.getGameState()==CLIENT_GAME_RUNNING_STATE)*/ );
  }
}
