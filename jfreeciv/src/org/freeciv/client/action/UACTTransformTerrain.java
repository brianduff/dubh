package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.CommonConstants;

public class UACTTransformTerrain extends AbstractUnitAction
{
  public UACTTransformTerrain() 
  {
    super();
    putValue( NAME, _( "Transform Terrain" ) ); // Needs to be able to change
    setAccelerator( KeyEvent.VK_O );
  }
  public void actionPerformed( ActionEvent e )
  {
    
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_TRANSFORM );
  }

}
