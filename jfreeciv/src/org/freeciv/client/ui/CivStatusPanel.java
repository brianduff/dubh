package org.freeciv.client.ui;
import org.freeciv.client.*;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import javax.swing.*;
import java.awt.*;
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
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
