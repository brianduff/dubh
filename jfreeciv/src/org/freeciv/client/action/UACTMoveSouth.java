package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Move down unit action
 */
public class UACTMoveSouth extends AbstractUnitAction
{
  public UACTMoveSouth() 
  {
    super();
    putValue( NAME, _( "Move South" ) );
    setAccelerator( KeyEvent.VK_KP_DOWN );
    setEnabled( false );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
