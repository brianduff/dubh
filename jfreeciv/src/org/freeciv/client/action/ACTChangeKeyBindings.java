package org.freeciv.client.action;


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
    setName( translate( "Key Bindings..." ) );

    setEnabled( true );
  }
  public void actionPerformed( java.awt.event.ActionEvent e )
  {
    getClient().getDialogManager().getChangeKeyBindingsDialog().display();
  }
}
