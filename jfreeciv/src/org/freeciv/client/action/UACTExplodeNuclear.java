package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Event;

import org.freeciv.client.Client;
import org.freeciv.common.Unit;
import org.freeciv.common.City;
import org.freeciv.common.CommonConstants;

public class UACTExplodeNuclear extends AbstractUnitAction
{
  public UACTExplodeNuclear() 
  {
    super();
    putValue( NAME, _( "Explode Nuclear" ) );
    setAccelerator( KeyEvent.VK_N, Event.SHIFT_MASK );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    Unit unit = getClient().getUnitInFocus();
    if ( unit != null && isEnabledFor( unit ) )
    {
      if( unit.getMovesLeft() == 0 )
      {
        //do_unit_nuke(punit);
      }
      else
      {
        //set_hover_state(punit, HOVER_NUKE);
        //update_unit_info_label(punit);
      }
    }
  }
  
  public boolean isEnabledFor( Unit u )
  {
    return u.isFlagSet( CommonConstants.F_NUCLEAR );
  }

}
