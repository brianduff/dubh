package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class ACTRevolution extends AbstractClientAction
{
   public ACTRevolution()
   {
      super();
      putValue(NAME, _("Revolution"));
      setEnabled(true);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}