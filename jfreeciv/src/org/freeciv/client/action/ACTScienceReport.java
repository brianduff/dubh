package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class ACTScienceReport extends AbstractClientAction
{
   public ACTScienceReport()
   {
      super();
      putValue(NAME, _("Science Report"));
      setAccelerator(KeyEvent.VK_F6);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}