package org.freeciv.client.handler;
import org.freeciv.client.*;
import org.freeciv.net.*;
import org.freeciv.client.dialog.*;
import javax.swing.JOptionPane;


/**
 * Ruleset control packet handler.
 */
public class PHRulesetGovernment implements ClientPacketHandler,ProgressItem
{
  public String getPacketClass()
  {
    return "org.freeciv.net.PktRulesetGovernment";
  }
  /**
   */
  public void handle( Client c, Packet pkt )
  {
    PktRulesetGovernment prt = (PktRulesetGovernment)pkt;
    c.getDialogManager().getProgressDialog().updateProgress( this );
    c.getFactories().getGovernmentFactory().create(prt);
  }
  public String getProgressString()
  {
    return _( "Receiving governments..." );
  }
  // localization
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
