package org.freeciv.client.action;


import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

import org.freeciv.common.Unit;
import org.freeciv.client.map.MapViewManager;

/**
 * This ACTAction centers the main MapView on the currently focused unit.
 *
 * @author ???
 * @author Julian Rueth
 */
public class ACTCenterView extends AbstractClientAction
{
  /**
   * Creates a new ACTCenterView instance.
   *
   */
  public ACTCenterView() 
  {
    super();
    setName( _( "Center View" ) );
    addAccelerator( KeyEvent.VK_C, 0 );
    setEnabled( true );
  }
  /**
   * Centers the main MapView on the currently focused unit.
   *
   * @param e not used
   */
  public void actionPerformed( ActionEvent e )
  {
    Unit u = getClient().getUnitInFocus();
    if( u == null )
    {
      //the focused tile is empty
      return;
    }
    MapViewManager.getMapViewManager().centerOnTile( u.getX(), u.getY() );
  }
}
