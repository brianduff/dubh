package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class ACTClearLog extends AbstractClientAction
{
   public ACTClearLog()
   {
      super();
      putValue(NAME, _("Clear Log"));
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