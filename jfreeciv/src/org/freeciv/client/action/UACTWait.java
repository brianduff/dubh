package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class UACTWait extends AbstractUnitAction
{
   public UACTWait()
   {
      super();
      putValue(NAME, _("Wait"));
      setAccelerator(KeyEvent.VK_W);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}