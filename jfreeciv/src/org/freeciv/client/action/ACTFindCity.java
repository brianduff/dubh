package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class ACTFindCity extends AbstractClientAction
{
   public ACTFindCity()
   {
      super();
      putValue(NAME, _("Find City"));
      setMnemonic('F');
      setAccelerator(KeyEvent.VK_F, Event.CTRL_MASK);
      setEnabled(true);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}