package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * This UnitAction moves the currently focused unit one tile down and right.
 *
 * @author ???
 * @author Julian Rueth
 */
public class UACTMoveSouthEast extends UACTMove
{

  /**
   * Creates an instance of UACTMoveSouthEast.
   * (Called by BasicMoveFactory)
   */
  public UACTMoveSouthEast() 
  {
    super();
    setName( _( "Move SouthEast" ) );
    addAccelerator( KeyEvent.VK_PAGE_DOWN );
    addAccelerator( KeyEvent.VK_NUMPAD3 );
  }
  
  /**
   * Moves the currently focused unit one tile down and right.
   */
  public void actionPerformed( ActionEvent e )
  {
      move( 1 , 1 );
  }
}
