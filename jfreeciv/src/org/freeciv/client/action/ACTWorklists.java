package org.freeciv.client.action;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class ACTWorklists extends AbstractClientAction
{
  public ACTWorklists()
  {
    super();
    setName( translate( "Worklists" ) );
    setMnemonic( 'l' );
    addAccelerator( KeyEvent.VK_W, Event.SHIFT_MASK );
    setEnabled( false );
  }
  public void actionPerformed( ActionEvent e )
  {

  }
}
