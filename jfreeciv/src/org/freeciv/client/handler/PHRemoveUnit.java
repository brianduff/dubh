package org.freeciv.client.handler;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;
import org.freeciv.common.Unit;
import org.freeciv.net.Packet;
import org.freeciv.net.PktGenericInteger;

/**
 * Handle a remove unit packet
 * 
 * @author Ben Mazur
 */
public class PHRemoveUnit extends AbstractHandler implements Constants
{

  /**
   * Return the class name of the packet that this
   * handler needs
   */
  public Class getPacketClass()
  {
    return PktGenericInteger.class;
  }
  
  /**
   * Your handler should implement this method to actually
   * handle an incoming packet.
   */
  public void handleOnEventThread( final Client c, final Packet pkt )
  {
    PktGenericInteger packet = (PktGenericInteger)pkt;
    
    Unit unit = c.getGame().getCurrentPlayer().getUnit( packet.value );
    c.removeUnit( unit );
  }
}
