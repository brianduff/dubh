package org.freeciv.client.handler;


import org.freeciv.client.*;
import org.freeciv.client.dialog.*;
import org.freeciv.net.*;

import javax.swing.JOptionPane;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetNation implements ClientPacketHandler, ProgressItem
{

  public String getPacketClass()
  {
    return "org.freeciv.net.PktRulesetNation";
  }

  /**
   */
  public void handle(Client c, Packet pkt)
  {
    PktRulesetNation prt = (PktRulesetNation)pkt;
    c.getDialogManager().getProgressDialog().updateProgress(this);
    c.getRulesetManager().setRulesetNation(prt.id, prt);
  }


   public String getProgressString()
   {
      return _("Receiving nations...");
   }

	// localization
	private static String _(String txt)
	{
		return Localize.translation.translate(txt);
	}
}