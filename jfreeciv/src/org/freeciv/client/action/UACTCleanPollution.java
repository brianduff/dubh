package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class UACTCleanPollution extends AbstractUnitAction
{
   public UACTCleanPollution()
   {
      super();
      putValue(NAME, _("Clean Pollution"));
      setAccelerator(KeyEvent.VK_P);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}