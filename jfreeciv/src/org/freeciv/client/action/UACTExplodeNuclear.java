package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class UACTExplodeNuclear extends AbstractUnitAction
{
   public UACTExplodeNuclear()
   {
      super();
      putValue(NAME, _("Explode Nuclear"));
      setAccelerator(KeyEvent.VK_N, Event.SHIFT_MASK);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}