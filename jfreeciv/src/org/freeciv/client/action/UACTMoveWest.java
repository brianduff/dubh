package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Move down unit action
 */
public class UACTMoveWest extends AbstractUnitAction
{
  public UACTMoveWest() 
  {
    super();
    putValue( NAME, _( "Move West" ) );
    setAccelerator( KeyEvent.VK_KP_LEFT );
    setEnabled( false );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
