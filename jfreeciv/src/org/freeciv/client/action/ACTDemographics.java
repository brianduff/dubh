package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class ACTDemographics extends AbstractClientAction
{
   public ACTDemographics()
   {
      super();
      putValue(NAME, _("Demographics"));
      setAccelerator(KeyEvent.VK_F11);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}