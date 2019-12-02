package org.freeciv.client.panel;

import javax.swing.JLabel;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import org.freeciv.util.Localize;


public class CivStatusPanel extends VerticalFlowPanel
{
  private Client m_client;
  private JLabel m_labTemp;
  public CivStatusPanel( Client c )
  {
    m_client = c;
    m_labTemp = new JLabel( "Temp" );
    addRow( m_labTemp );
  }
  // TODO: Add listeners to client for changes in pop, year, gold and tax. (and
  // game state)
  private static String translate( String txt )
  {
    return Localize.translate( txt );
  }
}
