package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class ACTWorklists extends AbstractClientAction
{
   public ACTWorklists()
   {
      super();
      putValue(NAME, _("Worklists"));
      setMnemonic('l');
      setAccelerator(KeyEvent.VK_W, Event.SHIFT_MASK);
      setEnabled(true);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}