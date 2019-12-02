package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.ProgressItem;
import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetControl;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetControl implements ClientPacketHandler,ProgressItem
{
  private final static int NUM_RULESETS = 9;

  public Class getPacketClass()
  {
    return PktRulesetControl.class;
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    c.getDialogManager().getProgressDialog().display(
      translate( "Game has been started" ), NUM_RULESETS
    );
    c.getDialogManager().getProgressDialog().updateProgress( this );
    c.getGame().setRulesetControl( (PktRulesetControl)pkt );
  }
  public String getProgressString()
  {
    return translate( "Receiving overall game rules..." );
  }
  // localization
  private static String translate( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
