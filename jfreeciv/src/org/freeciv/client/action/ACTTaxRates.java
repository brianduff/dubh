package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class ACTTaxRates extends AbstractClientAction
{
  public ACTTaxRates()
  {
    super();
    setName( translate( "Tax Rates" ) );
    setMnemonic( 'T' );
    addAccelerator( KeyEvent.VK_T, Event.SHIFT_MASK );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    getClient().getDialogManager().getTaxRatesDialog().display();
  }
}
