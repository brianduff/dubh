package org.freeciv.client.handler;
import org.freeciv.client.ui.CivInfoPanel;
import org.freeciv.client.*;
import org.freeciv.client.dialog.*;
import org.freeciv.net.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
/**
 * Game info handler
 */
public class PHPlayerInfo implements ClientPacketHandler
{
  public String getPacketClass()
  {
    return "org.freeciv.net.PktPlayerInfo";
  }
  /**
   */
  public void handle( final Client c, Packet pkt )
  {
    final PktPlayerInfo ppi = (PktPlayerInfo)pkt;
    c.setPlayer( ppi.playerno, ppi );
    if( ppi.playerno == c.getGameInfo().player_idx )
    {
      c.setCurrentPlayer( ppi );
      SwingUtilities.invokeLater( new Runnable() 
      {
        public void run()
        {
          CivInfoPanel p = c.getCivInfoPanel();
          p.setGold( ppi.gold );
          p.setTax( ppi.tax, ppi.science, ppi.luxury );
          p.setNationName( c.getRulesetManager().getRulesetNation( ppi.nation ).name );
        // TODO: Update research dialog.
        }
      } );
    }
  }
}
