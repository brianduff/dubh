package org.freeciv.client.action;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.freeciv.common.CommonConstants;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktGenericInteger;

public class ACTDemographics extends AbstractClientAction 
  implements PacketConstants, CommonConstants
{

  private PktGenericInteger m_packet;
  
  public ACTDemographics() 
  {
    super();
    setName( _( "Demographics" ) );
    addAccelerator( KeyEvent.VK_F11 );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    if( m_packet == null )
    {
      m_packet = new PktGenericInteger();
    }
    m_packet.setType( PACKET_REPORT_REQUEST );
    m_packet.value = REPORT_DEMOGRAPHIC;
    
    getClient().sendToServer( m_packet );
  }
}
