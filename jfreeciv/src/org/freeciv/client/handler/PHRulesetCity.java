package org.freeciv.client.handler;
import org.freeciv.client.*;
import org.freeciv.client.dialog.*;
import org.freeciv.net.*;
import javax.swing.JOptionPane;

import org.freeciv.common.CityStyle;
/**
 * Ruleset control packet handler.
 */
public class PHRulesetCity implements ClientPacketHandler,ProgressItem
{
  public String getPacketClass()
  {
    return "org.freeciv.net.PktRulesetCity";
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
    return Localize.translation.translate( txt );
  }
}
