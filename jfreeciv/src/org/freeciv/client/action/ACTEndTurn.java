package org.freeciv.client.action;

import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

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
  }
  
  public void actionPerformed( ActionEvent e )
  {
    // TODO
  }
}