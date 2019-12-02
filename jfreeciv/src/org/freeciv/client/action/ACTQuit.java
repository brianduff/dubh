package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
import javax.swing.JOptionPane;

public class ACTQuit extends AbstractClientAction
{
  public ACTQuit()
  {
    super();
    setName( translate( "Quit" ) );
    addAccelerator( KeyEvent.VK_Q, Event.CTRL_MASK );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    int option = JOptionPane.showConfirmDialog(
      getClient().getMainWindow(),
      "Do you really want to quit?",
      getClient().APP_NAME,
      JOptionPane.YES_NO_OPTION
    );

    if ( option == JOptionPane.YES_OPTION )
    {
      getClient().quit();
    }
  }
}
