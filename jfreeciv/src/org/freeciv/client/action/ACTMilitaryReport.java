package org.freeciv.client.action;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
public class ACTMilitaryReport extends AbstractClientAction
{
  public ACTMilitaryReport() 
  {
    super();
    setName( _( "Military Report" ) );
    addAccelerator( KeyEvent.VK_F2 );
    
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    getClient().getDialogManager().getMilitaryReport().display();
  }
}
