package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Localize;
import org.freeciv.client.dialog.ProgressItem;
import org.freeciv.net.Packet;
import org.freeciv.common.UnitType;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetUnit implements ClientPacketHandler,ProgressItem
{
  public String getPacketClass()
  {
    return "org.freeciv.net.PktRulesetUnit";
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    c.getDialogManager().getProgressDialog().updateProgress( this );
    UnitType ut = (UnitType)c.getFactories().getUnitTypeFactory().create(pkt);
    c.getTileSpec().setupUnitType( ut );
    
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
