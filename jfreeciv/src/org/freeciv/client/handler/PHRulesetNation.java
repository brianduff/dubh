package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.ProgressItem;
import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetNation;
import org.freeciv.common.Nation;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetNation implements ClientPacketHandler,ProgressItem
{
  public Class getPacketClass()
  {
    return PktRulesetNation.class;
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    c.getDialogManager().getProgressDialog().updateProgress( this );
    Nation n = (Nation) c.getFactories().getNationFactory().create(pkt);

    c.getTileSpec().setupNationFlag( n );
    
  }
  public String getProgressString()
  {
    return _( "Receiving nations..." );
  }
  // localization
  private static String _( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
