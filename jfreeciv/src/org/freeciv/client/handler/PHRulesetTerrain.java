package org.freeciv.client.handler;

import org.freeciv.client.Localize;
import org.freeciv.client.Client;

import org.freeciv.client.dialog.ProgressItem;
import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetTerrain;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetTerrain implements ClientPacketHandler,ProgressItem
{
  public Class getPacketClass()
  {
    return PktRulesetTerrain.class;
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    c.getDialogManager().getProgressDialog().updateProgress( this );
    c.getFactories().getTerrainTypeFactory().create(pkt);

    c.getTileSpec().setupTileType( ((PktRulesetTerrain)pkt).id );
  }
  public String getProgressString()
  {
    return _( "Receiving terrain..." );
  }
  // localization
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
