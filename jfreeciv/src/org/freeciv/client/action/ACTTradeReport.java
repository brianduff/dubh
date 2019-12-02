package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class ACTTradeReport extends AbstractClientAction
{
  public ACTTradeReport()
  {
    super();
    setName( translate( "Trade Report" ) );
    addAccelerator( KeyEvent.VK_F5 );

    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    getClient().getDialogManager().getTradeReportDialog().display();
  }
}
