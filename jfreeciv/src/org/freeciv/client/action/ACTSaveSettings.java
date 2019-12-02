package org.freeciv.client.action;
import java.awt.event.ActionEvent;
public class ACTSaveSettings extends AbstractClientAction
{
  public ACTSaveSettings()
  {
    super();
    setName( translate( "Save Settings" ) );
    setEnabled( false );
  /**
   c.addStateChangeListener(new ClientStateChangeListener() {
   public clientStateChanged(ClientStateChangeEvent e)
   {
   setEnabled(e.getState() == ?);
   }   // maybe better not to use anon inner c to stop excessive
   // instantiation.
   });
   */
  }
  public void actionPerformed( ActionEvent e )
  {



  // NYI
  }
}
