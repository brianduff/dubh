package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class UACTAutoExplore extends AbstractUnitAction
{
   public UACTAutoExplore()
   {
      super();
      putValue(NAME, _("Auto Explore"));
      setAccelerator(KeyEvent.VK_X);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}