package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * This UnitAction moves the currently focused unit one tile down.
 *
 * @author ???
 * @author Julian Rueth
 */
public class UACTMoveSouth extends UACTMove
{

  /**
   * Creates an instance of UACTMoveSouth.
   * (Called by BasicMoveFactory)
   */
  public UACTMoveSouth()
  {
    super();
    setName( translate( "Move South" ) );
    addAccelerator( KeyEvent.VK_KP_DOWN );
    addAccelerator( KeyEvent.VK_NUMPAD2 );
    setEnabled( false );
  }

  /**
   * Moves the currently focused unit one tile down.
   */
  public void actionPerformed( ActionEvent e )
  {
      move( 0 , 1 );
  }
}
