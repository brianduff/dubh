package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;
public class ACTMilitaryReport extends AbstractClientAction
{
  public ACTMilitaryReport() 
  {
    super();
    putValue( NAME, _( "Military Report" ) );
    setAccelerator( KeyEvent.VK_F2 );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
