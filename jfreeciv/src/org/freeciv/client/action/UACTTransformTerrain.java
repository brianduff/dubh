package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.freeciv.common.CommonConstants;
import org.freeciv.common.Unit;

public class UACTTransformTerrain extends AbstractUnitAction
{
  public UACTTransformTerrain()
  {
    super();
    setName( translate( "Transform Terrain" ) ); // Needs to be able to change
    addAccelerator( KeyEvent.VK_O );
  }

  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null )
    {
      if ( unit.canDoActivity( CommonConstants.ACTIVITY_TRANSFORM ) )
      {
        requestNewUnitActivity( unit, CommonConstants.ACTIVITY_TRANSFORM );
      }
    }
  }

  public boolean isEnabledFor( Unit u )
  {
    return u.canDoActivity( CommonConstants.ACTIVITY_TRANSFORM );
  }

}
