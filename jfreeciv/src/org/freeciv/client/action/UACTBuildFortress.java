package org.freeciv.client.action;


import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

public class UACTBuildFortress extends AbstractUnitAction
{
  public UACTBuildFortress() 
  {
    super();
    putValue( NAME, _( "Build Fortress" ) ); // Needs to be able to change
    setAccelerator( KeyEvent.VK_F, Event.SHIFT_MASK );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
}
