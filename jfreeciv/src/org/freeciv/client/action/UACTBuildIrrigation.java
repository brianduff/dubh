package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTBuildIrrigation extends AbstractUnitAction
{
  public UACTBuildIrrigation() 
  {
    super();
    putValue( NAME, _( "Build Irrigation" ) );
    setAccelerator( KeyEvent.VK_I );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
