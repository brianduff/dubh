package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class UACTConnect extends AbstractUnitAction
{
   public UACTConnect()
   {
      super();
      putValue(NAME, _("Connect"));
      setAccelerator(KeyEvent.VK_C, Event.SHIFT_MASK);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}