package org.freeciv.client.handler;
import org.freeciv.client.*;
import org.freeciv.client.dialog.*;
import org.freeciv.net.*;
import javax.swing.JOptionPane;
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
    PktRulesetCity prt = (PktRulesetCity)pkt;
    c.getDialogManager().getProgressDialog().updateProgress( this );
    c.getRulesetManager().setRulesetCity( prt.style_id, prt );
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
