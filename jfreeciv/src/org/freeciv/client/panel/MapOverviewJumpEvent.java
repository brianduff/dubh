package org.freeciv.client.panel;

import java.util.EventObject;

import org.freeciv.common.MapPosition;

/**
 * Event fired when the user requests a jump in the map overview component
 *
 * @see org.freeciv.client.panel.MapOverviewJumpListener
 * @see org.freeciv.client.panel.MapOverview
 *
 * @author Brian Duff
 */
public final class MapOverviewJumpEvent extends EventObject 
{
  private MapPosition m_pos;

  /**
   * Construct a map overview jump event
   * 
   * @param source the source of the event
   * @param tilex the x-coordinate of the tile to jump to
   * @param tiley the y-coordinate of the tile to jump to
   */
  MapOverviewJumpEvent(Object source, int tilex, int tiley)
  {
    super( source );
    m_pos = new MapPosition( tilex, tiley );
  }

  /**
   * Get the map position of the jump event
   *
   * @return a map position object
   */
  public MapPosition getPosition()
  {
    return m_pos;
  }
}