package org.freeciv.client.handler;
import org.freeciv.client.*;
import org.freeciv.net.*;
import org.freeciv.client.dialog.*;
import javax.swing.JOptionPane;
/**
 * Ruleset control packet handler.
 */
public class PHRulesetTech implements ClientPacketHandler,ProgressItem
{
  public String getPacketClass()
  {
    return "org.freeciv.net.PktRulesetTech";
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    PktRulesetTech prt = (PktRulesetTech)pkt;
    c.getDialogManager().getProgressDialog().updateProgress( this );
    c.getRulesetManager().setRulesetTech( prt.id, prt );
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
