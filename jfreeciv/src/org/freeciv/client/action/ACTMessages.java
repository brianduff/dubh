package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class ACTMessages extends AbstractClientAction
{
   public ACTMessages()
   {
      super();
      putValue(NAME, _("Messages"));
      setAccelerator(KeyEvent.VK_F10, 0);
      setEnabled(false);
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