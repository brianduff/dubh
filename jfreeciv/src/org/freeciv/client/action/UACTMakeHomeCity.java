package org.freeciv.client.action;


import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class UACTMakeHomeCity extends AbstractUnitAction
{
  public UACTMakeHomeCity() 
  {
    super();
    putValue( NAME, _( "Make Home City" ) );
    setAccelerator( KeyEvent.VK_H );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
