package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * Client action for End Turn
 */
public class ACTEndTurn extends AbstractClientAction
{
  public ACTEndTurn() 
  {
    super();
    putValue( NAME, _( "End Turn" ) );
    setEnabled( true );

    URL imgURL = ACTEndTurn.class.getResource( 
      "res/endturn.png"
    );
    putValue( SMALL_ICON, new ImageIcon( imgURL ) );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    // TODO
  }
}