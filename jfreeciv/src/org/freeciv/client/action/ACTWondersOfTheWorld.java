package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class ACTWondersOfTheWorld extends AbstractClientAction
{
   public ACTWondersOfTheWorld()
   {
      super();
      putValue(NAME, _("Wonders of the World"));
      setAccelerator(KeyEvent.VK_F7);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}