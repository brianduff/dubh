package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.client.action.ACTEndTurn;
import org.freeciv.common.Assert;
import org.freeciv.common.City;
import org.freeciv.common.Game;
import org.freeciv.common.Map;
import org.freeciv.common.MapPosition;
import org.freeciv.common.MapPositionIterator;
import org.freeciv.common.Tile;
import org.freeciv.common.Unit;
import org.freeciv.net.Packet;
import org.freeciv.net.PktGenericEmpty;

/**
 * Handle the packet sent to notify us that a new year is coming
 */
public class PHBeforeNewYear extends AbstractHandler implements Constants
{

  /**
   * Return the class name of the packet that this
   * handler needs
   */
  public Class getPacketClass()
  {
    return PktGenericEmpty.class;
  }
  
  /**
   * Your handler should implement this method to actually
   * handle an incoming packet.
   */
  public void handleOnEventThread( final Client c, final Packet pkt )
  {
    //c.getDialogManager().getNotifyDialog().undisplay();
    /*
    plrdlg_update_delay_on();
    report_update_delay_on();
    meswin_update_delay_on();    */
  }
}