package org.freeciv.client.handler;


import org.freeciv.client.dialog.DlgProgress;
import org.freeciv.client.dialog.ProgressItem;
import org.freeciv.client.*;
import org.freeciv.net.*;

import javax.swing.JOptionPane;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetControl implements ClientPacketHandler, ProgressItem
{
   private final static int NUM_RULESETS = 8;

   public String getPacketClass()
   {
     return "org.freeciv.net.PktRulesetControl";
   }

   /**
    */
   public void handle(Client c, AbstractPacket pkt)
   {
      c.getDialogManager().getProgressDialog().display(
         _("Game has been started"), NUM_RULESETS);
      c.getDialogManager().getProgressDialog().updateProgress(this);
      c.getRulesetManager().setRulesetControl((PktRulesetControl)pkt);
   }

   public String getProgressString()
   {
      return _("Receiving overall game rules...");
   }

	// localization
	private static String _(String txt)
	{
		return Localize.translation.translate(txt);
	}
}