package org.freeciv.client.action;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.freeciv.client.Client;
import org.freeciv.common.CommonConstants;
import org.freeciv.common.Unit;

import org.freeciv.client.handler.PHUnitInfo;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktUnitInfo;


/**
 *  This is the superclass of all actions which operate on units
 *
 * @author Brian Duff
 */
public abstract class AbstractUnitAction extends AbstractClientAction
{
  public AbstractUnitAction()
  {
    super();
  }

  /**
   * Is this action performable by the specified unit?
   * 
   * @param u the unit to check
   */
  public boolean isEnabledFor( Unit u )
  {
    return false;
  }
  
  /**
   * Sends a packet to the server requesting that the unit change activities.
   */
  protected void requestNewUnitActivity( Unit unit, int activity ) 
  {
    requestNewUnitActivity( unit, activity, 0 );
  }
  
  /**
   * Sends a packet to the server requesting that the unit change activities.
   * Targetted version.
   * 
   * This could go somewhere else in the code if anything else used it, but where?
   */
  protected void requestNewUnitActivity( Unit unit, int activity, int target ) 
  {
    PktUnitInfo pkt = new PktUnitInfo();
    
    pkt.id = unit.getId();
    pkt.owner = unit.getOwner().getId();
    pkt.x = unit.getX();
    pkt.y = unit.getY();
    pkt.homecity = unit.getHomeCityId();
    pkt.veteran = unit.isVeteran();
    pkt.type = unit.getType();
    pkt.movesleft = unit.getMovesLeft();
    pkt.activity = activity; // this is being changed
    pkt.activity_target = target; // this too
    pkt.select_it = false;
    pkt.packet_use = PHUnitInfo.UNIT_INFO_IDENTITY;
    
    getClient().sendToServer(pkt);
  }
}
