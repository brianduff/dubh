package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * This UnitAction moves the currently focused unit one tile up and left.
 *
 * @author ???
 * @author Julian Rueth
 */
public class UACTMoveNorthWest extends UACTMove
{

  /**
   * Creates an instance of UACTMoveNorthWest.
   * (Called by BasicMoveFactory)
   */
  public UACTMoveNorthWest() 
  {
    super();
    setName( _( "Move NorthWest" ) );
    addAccelerator( KeyEvent.VK_HOME );
    addAccelerator( KeyEvent.VK_NUMPAD7 );
  }
  
  /**
   * Moves the currently focused unit one tile up and left.
   */
  public void actionPerformed( ActionEvent e )
  {
      move( -1 , -1 );
  }
}
