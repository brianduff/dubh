package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
/**
 *
 * @author Brian Duff (bduff@uk.oracle.com)
 */
public class ACTMapGrid extends AbstractToggleAction
{
  public ACTMapGrid() 
  {
    super();
    setName( _( "Map Grid" ) );
    addAccelerator( KeyEvent.VK_G, Event.CTRL_MASK );
  }
  /**
   * The toggle was changed
   */
  protected void toggleStateChanged()
  {
    
  }
}
