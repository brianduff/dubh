package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTHelpBuildWonder extends AbstractUnitAction
{
  public UACTHelpBuildWonder() 
  {
    super();
    putValue( NAME, _( "Help Build Wonder" ) );
    setAccelerator( KeyEvent.VK_B, Event.SHIFT_MASK );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
