package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Move right unit action
 */
public class UACTMoveEast extends AbstractUnitAction
{
  public UACTMoveEast() 
  {
    super();
    putValue( NAME, _( "Move East" ) );
    setAccelerator( KeyEvent.VK_KP_RIGHT );
    setEnabled( false );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
