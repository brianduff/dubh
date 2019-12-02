package org.freeciv.client.action;
import java.awt.Event;
import java.awt.event.KeyEvent;
/**
 *
 * @author Brian Duff (bduff@uk.oracle.com)
 */
public class ACTMapGrid extends AbstractToggleAction
{
  public ACTMapGrid()
  {
    super();
    setName( translate( "Map Grid" ) );
    addAccelerator( KeyEvent.VK_G, Event.CTRL_MASK );
  }
  /**
   * The toggle was changed
   */
  protected void toggleStateChanged()
  {

  }
}
