package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTBuildAirbase extends AbstractUnitAction
{
  public UACTBuildAirbase() 
  {
    super();
    putValue( NAME, _( "Build Airbase" ) );
    setAccelerator( KeyEvent.VK_E );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
