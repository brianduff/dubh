package org.freeciv.client.action;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class ACTFindCity extends AbstractClientAction
{
  public ACTFindCity()
  {
    super();
    setName( translate( "Find City" ) );
    setMnemonic( 'F' );
    addAccelerator( KeyEvent.VK_F, Event.CTRL_MASK );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    getClient().getDialogManager().getFindCityDialog().display();
  }
}
