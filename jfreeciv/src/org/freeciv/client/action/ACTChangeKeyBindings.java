package org.freeciv.client.action;

import org.freeciv.client.Client;


/**
 * This ACTAction invokes change key bindings dialog.
 *
 * @author Justin
 */
public class ACTChangeKeyBindings extends AbstractClientAction
{
  public ACTChangeKeyBindings()
  {
    super();
    setName( _( "Change Key Bindings" ) );
    
    setEnabled( true );
  }
  public void actionPerformed( java.awt.event.ActionEvent e )
  {
    getClient().getDialogManager().getChangeKeyBindingsDialog().display();
  }
}
