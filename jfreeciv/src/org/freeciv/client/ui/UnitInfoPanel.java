package org.freeciv.client.ui;
import org.freeciv.client.*;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import javax.swing.*;
import java.awt.*;


public class UnitInfoPanel extends VerticalFlowPanel
{
  private Client m_client;
  private JLabel m_labMoves, m_labTerrain, m_labHomeCity;

  private static final Color TEXT_COLOR = 
    UIManager.getLookAndFeelDefaults().getColor("infoText");

  private static final Color WINDOW_COLOR =
    UIManager.getLookAndFeelDefaults().getColor("info");
  
  public UnitInfoPanel( Client c ) 
  {
    m_client = c;
    m_labMoves = new JLabel( );
    m_labMoves.setForeground( TEXT_COLOR );
    m_labTerrain = new JLabel( );
    m_labTerrain.setForeground( TEXT_COLOR );    
    m_labHomeCity = new JLabel( );
    m_labHomeCity.setForeground( TEXT_COLOR );    
    addRow( m_labMoves );
    addRow( m_labTerrain );
    addRow( m_labHomeCity );

    setBackground( WINDOW_COLOR );

    clear();
  }

  public void clear()
  {
    m_labMoves.setText(" ");
    m_labTerrain.setText("No unit selected");
    m_labHomeCity.setText(" ");
  }

  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
