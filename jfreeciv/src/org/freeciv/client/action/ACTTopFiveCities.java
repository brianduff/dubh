package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktGenericInteger;
import org.freeciv.common.CommonConstants;

public class ACTTopFiveCities extends AbstractClientAction 
  implements PacketConstants, CommonConstants
{

  private PktGenericInteger m_packet;
  
  public ACTTopFiveCities() 
  {
    super();
    setName( _( "Top Five Cities" ) );
    addAccelerator( KeyEvent.VK_F8 );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    if( m_packet == null )
    {
      m_packet = new PktGenericInteger();
    }
    m_packet.setType( PACKET_REPORT_REQUEST );
    m_packet.value = REPORT_TOP_5_CITIES;
    
    getClient().sendToServer( m_packet );
  }
}
