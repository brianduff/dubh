package org.freeciv.client.action;
import java.awt.event.ActionEvent;

import org.freeciv.common.CommonConstants;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktGenericInteger;

public class ACTServerOptOngoing extends AbstractClientAction 
  implements PacketConstants, CommonConstants
{

  private PktGenericInteger m_packet;
  
  public ACTServerOptOngoing() 
  {
    super();
    setName( _( "Server Opt ongoing" ) );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    if( m_packet == null )
    {
      m_packet = new PktGenericInteger();
    }
    m_packet.setType( PACKET_REPORT_REQUEST );
    m_packet.value = REPORT_SERVER_OPTIONS2;
    
    getClient().sendToServer( m_packet );
  }
}
