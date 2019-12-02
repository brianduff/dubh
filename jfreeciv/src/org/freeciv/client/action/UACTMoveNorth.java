package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * This UnitAction moves the currently focused unit one tile up.
 *
 * @author ???
 * @author Julian Rueth
 */
public class UACTMoveNorth extends UACTMove
{

  /**
   * Creates an instance of UACTMoveNorth.
   * (Called by BasicMoveFactory)
   */
  public UACTMoveNorth()
  {
    super();
    setName( translate( "Move North" ) );
    addAccelerator( KeyEvent.VK_KP_UP );
    addAccelerator( KeyEvent.VK_NUMPAD8 );
  }

  /**
   * Moves the currently focused unit one tile up.
   */
  public void actionPerformed( ActionEvent e )
  {
      move( 0 , -1 );
  }
}
