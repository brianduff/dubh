package org.freeciv.client.action;


import java.awt.event.ActionEvent;

public class ACTRevolution extends AbstractClientAction
{
  public ACTRevolution() 
  {
    super();
    putValue( NAME, _( "Revolution" ) );
    setEnabled( false );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
