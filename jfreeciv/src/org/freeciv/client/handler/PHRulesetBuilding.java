package org.freeciv.client.handler;


import org.freeciv.client.*;
import org.freeciv.client.dialog.*;
import org.freeciv.net.*;

import javax.swing.JOptionPane;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetBuilding implements ClientPacketHandler, ProgressItem
{

  public String getPacketClass()
  {
    return "org.freeciv.net.PktRulesetBuilding";
  }

  /**
   */
  public void handle(Client c, AbstractPacket pkt)
  {
    PktRulesetBuilding prt = (PktRulesetBuilding)pkt;
    c.getDialogManager().getProgressDialog().updateProgress(this);
    c.getRulesetManager().setRulesetBuilding(prt.id, prt);
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