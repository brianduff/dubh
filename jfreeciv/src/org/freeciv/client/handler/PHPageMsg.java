package org.freeciv.client.handler;


import org.freeciv.client.Constants;
import org.freeciv.client.Client;
import org.freeciv.client.Options;
import org.freeciv.net.Packet;
import org.freeciv.net.PktPageMsg;
import org.freeciv.client.dialog.DlgNotifyGoto;

/**
 * "Page" message handler for certain reports
 * 
 * TODO: page messages are also used by a historian notification, which maybe
 * TODO: are modal, or maybe not.  anyhow, figure that out.
 * 
 * @author Ben Mazur
 */
public class PHPageMsg extends AbstractHandler implements Constants
{
  /**
   * Return the class name of the packet that this
   * handler needs
   */
  public Class getPacketClass()
  {
    return PktPageMsg.class;
  }
  /**
   * Parses out the seperate parts of the message and pops up an appropriate dialog
   */
  void handleOnEventThread( Client c, Packet pkt )
  {
    PktPageMsg msg = (PktPageMsg)pkt;
    // pop up dialog
    c.getDialogManager().getNotifyDialog().display( msg.caption, msg.headline, msg.lines );
  }
}
