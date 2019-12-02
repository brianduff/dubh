package org.freeciv.client.action;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
public class ACTMessages extends AbstractClientAction
{
  public ACTMessages()
  {
    super();
    setName( translate( "Messages" ) );
    addAccelerator( KeyEvent.VK_F10, 0 );
    setEnabled( false );
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
  public void actionPerformed( ActionEvent e )
  {



  // NYI
  }
}
