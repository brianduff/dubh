package org.freeciv.client.action;
import java.awt.event.ActionEvent;

import org.freeciv.common.CommonConstants;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktGenericInteger;

public class ACTServerOptInitial extends AbstractClientAction
  implements PacketConstants, CommonConstants
{

  private PktGenericInteger m_packet;

  public ACTServerOptInitial()
  {
    super();
    setName( translate( "Server Opt initial" ) );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    if( m_packet == null )
    {
      m_packet = new PktGenericInteger();
    }
    m_packet.setType( PACKET_REPORT_REQUEST );
    m_packet.value = REPORT_SERVER_OPTIONS1;

    getClient().sendToServer( m_packet );
  }
}
