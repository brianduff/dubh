package org.freeciv.client.action;


import java.awt.event.ActionEvent;

public class ACTRevolution extends AbstractClientAction
{
  public ACTRevolution()
  {
    super();
    setName( translate( "Revolution" ) );
    setEnabled( false );
  }
  public void actionPerformed( ActionEvent e )
  {

  }
}
