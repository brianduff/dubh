package org.freeciv.client.action;

import org.freeciv.client.Client;
import org.freeciv.net.PktAllocNation;
import org.freeciv.client.dialog.DlgNation;
import java.awt.event.ActionEvent;

public class ACTSendAllocNation extends AbstractClientAction
{

  private PktAllocNation m_packet;
  
  public ACTSendAllocNation() 
  {
    super();
    setName( _( "Choose Nation" ) );
  }
  
  public void actionPerformed( ActionEvent e )
  {
    if( m_packet == null )
    {
      m_packet = new PktAllocNation();
    }
    DlgNation dn = getClient().getDialogManager().getNationDialog();
    m_packet.nation_no = dn.getNation();
    m_packet.name = dn.getLeaderName();
    m_packet.is_male = dn.isLeaderMale();
    m_packet.city_style = dn.getCityStyle();
    getClient().sendToServer( m_packet );
  }
  

}
