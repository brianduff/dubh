package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class ACTServerOptInitial extends AbstractClientAction
{
   public ACTServerOptInitial()
   {
      super();
      putValue(NAME, _("Server Opt initial"));
      setEnabled(true);
      /**
      c.addStateChangeListener(new ClientStateChangeListener() {
         public clientStateChanged(ClientStateChangeEvent e)
         {
            setEnabled(e.getState() == ?);
         }   // maybe better not to use anon inner c to stop excessive
             // instantiation.
      });
      */
   }

   public void actionPerformed(ActionEvent e)
   {
      // NYI
   }

}