package org.freeciv.common;

/**
 * A map position iterator is used on several methods on Map. You pass an
 * object implementing this interface into the map iterator methods on
 * Map, and it calls the iteratePosition() method for each iteration.
 */
public abstract class MapPositionIterator 
{
  private boolean m_finished = false;

  /**
   * Your implementation should carry out whatever action it needs to on the
   * provided map position
   *
   * @param mp the map position
   */
  public abstract void iteratePosition( MapPosition mp );

  /**
   * Call this to stop any further calls to iteratePosition
   */
  protected void setFinished( boolean finished )
  {
    m_finished = finished;
  }

  boolean isFinished()
  {
    return m_finished;
  }

}