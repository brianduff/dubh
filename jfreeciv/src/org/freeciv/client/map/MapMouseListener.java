package org.freeciv.client.map;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.Iterator;

import org.freeciv.common.Map;
import org.freeciv.common.Tile;
import org.freeciv.common.City;
import org.freeciv.common.Unit;
import org.freeciv.client.Client;
import org.freeciv.client.dialog.DialogManager;

/**
 * This MouseListener handles all MouseEvents on the MapComponent.
 *
 * @author Julian Rueth
 */
public class MapMouseListener extends MouseAdapter
{
  /**
   * Currently supported:
   * - opening city dialogs
   * - focusing units
   *
   * @param e a MouseEvent
   */
  public void mousePressed(MouseEvent e)
  {
    MapViewManager mvm = MapViewManager.getMapViewManager();
    DialogManager dm = DialogManager.getDialogManager();
    Tile tile = mvm.getMainMapView().getTileAtCanvasPos( e.getX(), e.getY() );
    int playerId = Client.getClient().getGame().getCurrentPlayer().getNation().getId();

    //check if there's a city on this tile
    if( tile.hasCity() )
    {
      City city = tile.getCity();
      //is this city owned by this player
      if( city.getOwner().getNation().getId() == playerId )
      {
	//open city dialog
	mvm.centerOnTile( city.getX() , city.getY() );
	dm.getCityViewDialog().display( city );
      }
    }

    //check if there are any units on this tile
    Iterator it = tile.getUnits();
    if( !it.hasNext() )
    {
      return;
    }
    Unit u = (Unit) it.next();
    mvm.centerOnTile( u.getX() , u.getY() );
    //if any unit on this tile is owned by the current player,
    // focus it
    do
    {
      if( u.getOwner().getNation().getId() == playerId )
      {
	Client.getClient().setUnitFocusAndSelect( u ); 
	return;
      }
    }
    while( it.hasNext() );
  }
}
