package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Localize;
import org.freeciv.client.dialog.ProgressItem;
import org.freeciv.net.Packet;
/**
 * Ruleset control packet handler.
 */
public class PHRulesetBuilding implements ClientPacketHandler,ProgressItem
{
  public String getPacketClass()
  {
    return "org.freeciv.net.PktRulesetBuilding";
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    c.getDialogManager().getProgressDialog().updateProgress( this );
    c.getFactories().getBuildingFactory().create(pkt);
  }
  public String getProgressString()
  {
    return _( "Receiving units..." );
  }
  // localization
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
