package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Localize;
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
    return _( "Receiving technologies..." );
  }
  // localization
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
