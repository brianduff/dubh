package org.freeciv.client.panel;

import java.util.EventListener;

/**
 * This interface is implemented by objects which need to be notified when the
 * user requests a jump via the map overview component
 *
 * When the user requests a jump (usually by clicking), the mapOverviewJumped()
 * method is called with a MapOverviewJumpEvent indicating the tile coordinates
 * to jump to.
 *
 * You can add a listener to the map overview, by calling
 * {@link MapOverview.addJumpListener(MapOverviewJumpListener)}.
 *
 * @see org.freeciv.client.panel.MapOverviewJumpEvent
 * @see org.freeciv.client.panel.MapOverview
 *
 * @author Brian Duff
 */
public interface MapOverviewJumpListener extends EventListener
{
  /**
   * Called when the user requests a jump via the map overview component.
   *
   * @param moje the event contains information about the jump request
   */
  void mapOverviewJumped( MapOverviewJumpEvent moje );
}