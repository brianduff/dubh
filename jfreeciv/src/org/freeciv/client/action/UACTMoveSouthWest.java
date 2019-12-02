package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * This UnitAction moves the currently focused unit one tile down and left.
 *
 * @author ???
 * @author Julian Rueth
 */
public class UACTMoveSouthWest extends UACTMove
{

  /**
   * Creates an instance of UACTMoveSouthWest.
   * (Called by BasicMoveFactory)
   */
  public UACTMoveSouthWest()
  {
    super();
    putValue( NAME, translate( "Move SouthWest" ) );
    addAccelerator( KeyEvent.VK_END );
    addAccelerator( KeyEvent.VK_NUMPAD1 );
  }

  /**
   * Moves the currently focused unit one tile down and left.
   */
  public void actionPerformed( ActionEvent e ){
    move( -1 , 1 );
  }
}
