package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.freeciv.common.Unit;
import org.freeciv.net.PktMoveUnit;


/**
 * An abstract UnitAction to move units.
 *
 * @author Julian Rueth
 */
abstract public class UACTMove extends AbstractUnitAction
{

  /**
   * Creates an instance of UCATMove.
   * All instances of UACTMove are currently created by the BasicMoveFactory.
   */
  public UACTMove()
  {
    super();
    setEnabled(true);
  }

  /**
   * Move the currently focused unit.
   *
   * @param dx change the x-coordinate by dx units
   * @param dy change the y-coordinate by dy units
   */
  public void move( int dx , int dy )
  {
    Unit u = getClient().getUnitInFocus();
    if( u == null )
      //the focused tile is empty
      return;

    int ox = u.getX();
    int nx = ox+dx;
    int width = getClient().getGame().getMap().getWidth();
    //Leaving the map in the east
    if( nx >= width ){
	nx = dx - ( width - ox );
    }
    //Leaving the map in the west.
    if( nx < 0 ){
	nx = width - dx + ox;
    }
    int ny = u.getY()+dy;

    if ( ny >= 0 && ny < getClient().getGame().getMap().getHeight() )
    {
      // Send a PktMoveUnit off to the server
      PktMoveUnit packet=new PktMoveUnit();
      packet.unitId = u.getId();
      packet.nx = nx;
      packet.ny = ny;
      getClient().sendToServer( packet );
    }
  } 

  /**
   * There isn't any unit that isn't able to move.
   * (Illegal moves will be caught by the server) 
   */
  public boolean isEnabledFor( Unit u )
  {
    return true;
  }
}
