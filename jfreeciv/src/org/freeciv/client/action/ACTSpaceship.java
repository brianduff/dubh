package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class ACTSpaceship extends AbstractClientAction
{
   public ACTSpaceship()
   {
      super();
      putValue(NAME, _("Spaceship"));
      setAccelerator(KeyEvent.VK_F12);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}