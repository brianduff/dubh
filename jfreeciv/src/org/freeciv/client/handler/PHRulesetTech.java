package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.ProgressItem;
import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetTech;

/**
 * Ruleset tech packet handler.
 */
public class PHRulesetTech implements ClientPacketHandler,ProgressItem
{
  public Class getPacketClass()
  {
    return PktRulesetTech.class;
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    c.getDialogManager().getProgressDialog().updateProgress( this );
    c.getFactories().getAdvanceFactory().create(pkt);
  }
  public String getProgressString()
  {
    return translate( "Receiving technologies..." );
  }
  // localization
  private static String translate( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
