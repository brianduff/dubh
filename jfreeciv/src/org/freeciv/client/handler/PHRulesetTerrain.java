package org.freeciv.client.handler;


import org.freeciv.client.*;
import org.freeciv.client.dialog.*;
import org.freeciv.net.*;

import javax.swing.JOptionPane;

/**
 * Ruleset control packet handler.
 */
public class PHRulesetTerrain implements ClientPacketHandler, DlgProgress.ProgressItem
{

  public String getPacketClass()
  {
    return "org.freeciv.net.PktRulesetTerrain";
  }

  /**
   */
  public void handle(Client c, AbstractPacket pkt)
  {
    PktRulesetTerrain prt = (PktRulesetTerrain)pkt;
    c.getDialogManager().getProgressDialog().updateProgress(this);
    c.getRulesetManager().setRulesetTerrain(prt.id, prt);
  }


   public String getProgressString()
   {
      return _("Receiving terrain...");
   }

	// localization
	private static String _(String txt)
	{
		return Localize.translation.translate(txt);
	}
}