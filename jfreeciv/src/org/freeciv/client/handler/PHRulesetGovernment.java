package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.ProgressItem;
import org.freeciv.common.Government;
import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetGovernment;


/**
 * Ruleset control packet handler.
 */
public class PHRulesetGovernment implements ClientPacketHandler,ProgressItem
{
  public Class getPacketClass()
  {
    return PktRulesetGovernment.class;
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    PktRulesetGovernment prt = (PktRulesetGovernment)pkt;
    c.getDialogManager().getProgressDialog().updateProgress( this );
    Government gov = (Government) 
      c.getFactories().getGovernmentFactory().create(prt);

    c.getTileSpec().setupGovernment( gov );
  }
  public String getProgressString()
  {
    return _( "Receiving governments..." );
  }
  // localization
  private static String _( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
