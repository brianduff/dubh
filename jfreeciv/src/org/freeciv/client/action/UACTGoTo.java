package org.freeciv.client.action;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
public class UACTGoTo extends AbstractUnitAction
{
  public UACTGoTo()
  {
    super();
    setName( translate( "Go To" ) );
    addAccelerator( KeyEvent.VK_G );
  }
  public void actionPerformed( ActionEvent e )
  {

  }
}
