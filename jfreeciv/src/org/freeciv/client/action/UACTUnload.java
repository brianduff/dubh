package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class UACTUnload extends AbstractUnitAction
{
  public UACTUnload() 
  {
    super();
    putValue( NAME, _( "Unload" ) );
    setAccelerator( KeyEvent.VK_U );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
