package org.freeciv.client.action;
import org.freeciv.client.Client;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktGenericInteger;
import org.freeciv.common.CommonConstants;

public class ACTWondersOfTheWorld extends AbstractClientAction 
  implements PacketConstants, CommonConstants
{

  private PktGenericInteger m_packet;
  
  public ACTWondersOfTheWorld() 
  {
    super();
    setName( _( "Wonders of the World" ) );
    addAccelerator( KeyEvent.VK_F7 );
    setEnabled( true );
  }
  public void actionPerformed( ActionEvent e )
  {
    if( m_packet == null )
    {
      m_packet = new PktGenericInteger();
    }
    m_packet.setType( PACKET_REPORT_REQUEST );
    m_packet.value = REPORT_WONDERS_OF_THE_WORLD;
    
    getClient().sendToServer( m_packet );
  }
}
