package org.freeciv.client.handler;


import org.freeciv.client.*;
import org.freeciv.client.dialog.*;
import org.freeciv.net.*;

import javax.swing.JOptionPane;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetUnit implements ClientPacketHandler , DlgProgress.ProgressItem
{

   public String getPacketClass()
   {
     return "org.freeciv.net.PktRulesetUnit";
   }

   /**
    */
   public void handle(Client c, AbstractPacket pkt)
   {
     PktRulesetUnit prt = (PktRulesetUnit)pkt;
     c.getDialogManager().getProgressDialog().updateProgress(this);
     c.getRulesetManager().setRulesetUnit(prt.id, prt);
   }

   public String getProgressString()
   {
      return _("Receiving units...");
   }

	// localization
	private static String _(String txt)
	{
		return Localize.translation.translate(txt);
	}
}