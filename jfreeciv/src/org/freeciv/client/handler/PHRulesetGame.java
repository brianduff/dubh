package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.ProgressItem;
import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetGame;

public class PHRulesetGame implements ClientPacketHandler,ProgressItem
{
  public Class getPacketClass()
  {
    return PktRulesetGame.class;
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    PktRulesetGame gr = (PktRulesetGame)pkt;
    c.getDialogManager().getProgressDialog().updateProgress( this );
    c.getGame().getGameRules().init( gr );
  }
  public String getProgressString()
  {
    return _( "Receiving game rules..." );
  }
  // localization
  private static String _( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
