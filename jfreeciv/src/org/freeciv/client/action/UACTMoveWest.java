package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * This UnitAction moves the currently focused unit one tile left.
 *
 * @author ???
 * @author Julian Rueth
 */
public class UACTMoveWest extends UACTMove
{

  /**
   * Creates an instance of UACTMoveWest.
   * (Called by BasicMoveFactory)
   */
  public UACTMoveWest() 
  {
    super();
    setName( _( "Move West" ) );
    addAccelerator( KeyEvent.VK_KP_LEFT );
    addAccelerator( KeyEvent.VK_NUMPAD4 );
  }
  
  /**
   * Moves the currently focused unit one tile left.
   */
  public void actionPerformed( ActionEvent e )
  {
      move( -1 , 0);
  }
}
