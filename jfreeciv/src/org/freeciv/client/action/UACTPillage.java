package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class UACTPillage extends AbstractUnitAction
{
   public UACTPillage()
   {
      super();
      putValue(NAME, _("Pillage"));
      setAccelerator(KeyEvent.VK_P, Event.SHIFT_MASK);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}