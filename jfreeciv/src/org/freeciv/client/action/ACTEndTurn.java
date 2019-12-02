package org.freeciv.client.action;

import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.ImageIcon;

import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktGenericMessage;

/**
 * Client action for End Turn
 */
public class ACTEndTurn extends AbstractClientAction
{
  public ACTEndTurn()
  {
    super();
    setName( translate( "End Turn" ) );
    setEnabled( false );

    URL imgURL = ACTEndTurn.class.getResource(
      "res/endturn.png"
    );
    putValue( SMALL_ICON, new ImageIcon( imgURL ) );
  }

  public void actionPerformed( ActionEvent e )
  {
    setEnabled( false );
    PktGenericMessage pkt = new PktGenericMessage();
    pkt.setType( PacketConstants.PACKET_TURN_DONE );
    getClient().sendToServer( pkt );
  }
}
