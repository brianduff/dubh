package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktGenericInteger;
import org.freeciv.common.CommonConstants;

public class ACTDemographics extends AbstractClientAction 
  implements PacketConstants, CommonConstants
{

  private PktGenericInteger m_packet;
  
  public ACTDemographics() 
  {
    super();
    putValue( NAME, _( "Demographics" ) );
    setAccelerator( KeyEvent.VK_F11 );
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
