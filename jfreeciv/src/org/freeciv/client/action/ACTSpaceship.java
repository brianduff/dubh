package org.freeciv.client.action;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
public class ACTSpaceship extends AbstractClientAction
{
  public ACTSpaceship()
  {
    super();
    setName( translate( "Spaceship" ) );
    addAccelerator( KeyEvent.VK_F12 );
  }
  public void actionPerformed( ActionEvent e )
  {

  }
}
