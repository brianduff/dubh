package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * This UnitAction moves the currently focused unit one tile up and right.
 *
 * @author ???
 * @author Julian Rueth
 */
public class UACTMoveNorthEast extends UACTMove
{

  /**
   * Creates an instance of UACTMoveNorthEast.
   * (Called by BasicMoveFactory)
   */
  public UACTMoveNorthEast() 
  {
    super();
    setName( _( "Move NorthEast" ) );
    addAccelerator( KeyEvent.VK_PAGE_UP );
    addAccelerator( KeyEvent.VK_NUMPAD9 );
  }
  
  /**
   * Moves the currently focused unit one tile up and right.
   */
  public void actionPerformed( ActionEvent e )
  {
      move( 1 , -1 );
  }
}
