package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class ACTTradeReport extends AbstractClientAction
{
  public ACTTradeReport() 
  {
    super();
    putValue( NAME, _( "Trade Report" ) );
    setAccelerator( KeyEvent.VK_F5 );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
