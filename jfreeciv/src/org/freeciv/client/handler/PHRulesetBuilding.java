package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.ProgressItem;
import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetBuilding;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetBuilding implements ClientPacketHandler,ProgressItem
{
  public Class getPacketClass()
  {
    return PktRulesetBuilding.class;
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
    return translate( "Receiving units..." );
  }
  // localization
  private static String translate( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
