package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.ProgressItem;
import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetUnit;
import org.freeciv.common.UnitType;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetUnit implements ClientPacketHandler,ProgressItem
{
  public Class getPacketClass()
  {
    return PktRulesetUnit.class;
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
    return translate( "Receiving units..." );
  }
  // localization
  private static String translate( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
