package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class UACTAutoSettler extends AbstractUnitAction
{
  public UACTAutoSettler() 
  {
    super();
    putValue( NAME, _( "Auto Settler" ) );
    setAccelerator( KeyEvent.VK_A );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
