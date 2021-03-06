package org.freeciv.client.action;

import java.awt.event.ActionEvent;

/**
 * The Copying help action
 */
public final class ACTHelpCopying extends AbstractClientAction
{
  private static final String COPYING_TOPIC = "help_copying";


  public ACTHelpCopying()
  {
    super();
    setName( translate( "Copying" ) );
    setEnabled( true );
  }

  public void actionPerformed(ActionEvent e)
  {
    getClient().getMainWindow().getHelpSystem().showStaticTopic(
      COPYING_TOPIC
    );
  }
}
