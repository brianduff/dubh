package org.freeciv.client.action;

import org.freeciv.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class ACTTaxRates extends AbstractClientAction
{
   public ACTTaxRates()
   {
      super();
      putValue(NAME, _("Tax Rates"));
      setMnemonic('T');
      setAccelerator(KeyEvent.VK_T, Event.SHIFT_MASK);
      setEnabled(true);
   }

   public void actionPerformed(ActionEvent e)
   {

   }

}