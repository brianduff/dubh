package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * This UnitAction moves the currently focused unit one tile right.
 *
 * @author ???
 * @author Julian Rueth
 */
public class UACTMoveEast extends UACTMove
{

  /**
   * Creates an instance of UACTMoveEast.
   * (Called by BasicMoveFactory)
   */
  public UACTMoveEast() 
  {
    super();
    setName( _( "Move East" ) );
    addAccelerator( KeyEvent.VK_KP_RIGHT );
    addAccelerator( KeyEvent.VK_NUMPAD6 );
  }

  /**
   * Moves the currently focused unit one tile right.
   */
  public void actionPerformed( ActionEvent e )
  {
      move( 1 , 0);
  }
}
