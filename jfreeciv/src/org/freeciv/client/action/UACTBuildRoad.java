package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class UACTBuildRoad extends AbstractUnitAction
{
   public UACTBuildRoad()
   {
      super();
      putValue(NAME, _("Build Road"));
      setAccelerator(KeyEvent.VK_R);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}