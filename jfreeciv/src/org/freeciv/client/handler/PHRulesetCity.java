package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.ProgressItem;
import org.freeciv.net.Packet;
import org.freeciv.net.PktRulesetCity;
import org.freeciv.common.CityStyle;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetCity implements ClientPacketHandler,ProgressItem
{
  public Class getPacketClass()
  {
    return PktRulesetCity.class;
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    c.getDialogManager().getProgressDialog().updateProgress( this );
    CityStyle style = (CityStyle)c.getFactories().getCityStyleFactory().create( pkt);

    c.getTileSpec().setupCityTiles( style );
  }
  public String getProgressString()
  {
    return _( "Receiving city styles..." );
  }
  // localization
  private static String _( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
