package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class UACTBuildCity extends AbstractUnitAction
{
   public UACTBuildCity()
   {
      super();
      putValue(NAME, _("Build City"));
      setAccelerator(KeyEvent.VK_B);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}