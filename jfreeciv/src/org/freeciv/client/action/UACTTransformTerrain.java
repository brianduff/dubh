package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTTransformTerrain extends AbstractUnitAction
{
  public UACTTransformTerrain() 
  {
    super();
    putValue( NAME, _( "Transform to Hills" ) ); // Needs to be able to change
    setAccelerator( KeyEvent.VK_O );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
