package org.freeciv.client.panel;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.freeciv.common.Unit;
import org.freeciv.common.TerrainType;
import org.freeciv.client.Client;
import org.freeciv.client.dialog.util.VerticalFlowPanel;

/**
 * The unit info panel shows information about the currently focused unit
 *
 * @author Brian Duff
 */
public class UnitInfoPanel extends VerticalFlowPanel
{
  private Client m_client;
  private JLabel m_labType, m_labMoves, m_labTerrain, m_labHomeCity;

  private static final Color TEXT_COLOR =
    UIManager.getLookAndFeelDefaults().getColor("infoText");

  private static final Color WINDOW_COLOR =
    UIManager.getLookAndFeelDefaults().getColor("info");

  public UnitInfoPanel( Client c )
  {
    m_client = c;
    m_labType = new JLabel();
    m_labType.setForeground( TEXT_COLOR );
    m_labMoves = new JLabel( );
    m_labMoves.setForeground( TEXT_COLOR );
    m_labTerrain = new JLabel( );
    m_labTerrain.setForeground( TEXT_COLOR );
    m_labHomeCity = new JLabel( );
    m_labHomeCity.setForeground( TEXT_COLOR );
    addRow( m_labType );
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
    invalidate();
    validate();
  }

  public void setUnit( Unit unit )
  {
    if ( unit == null )
    {
      clear();
    }
    else
    {

      StringBuffer sb = new StringBuffer();
      sb.append( "Type: " );
      sb.append( unit.getUnitType().getName() );
      sb.append( " (" );
      sb.append( unit.getUnitType().getAttackStrength() );
      sb.append( "/" );
      sb.append( unit.getUnitType().getDefenseStrength() );
      sb.append( "/" );
      sb.append( unit.getUnitType().getMoveRate() );
      sb.append( ")" );
      m_labType.setText( sb.toString() );

      sb = new StringBuffer();
      sb.append( "Moves: ");
      sb.append( unit.getMovesLeft() );
      m_labMoves.setText( sb.toString() );

      sb = new StringBuffer();
      sb.append( "Terrain: ");
      int ttype = m_client.getGame().getMap().getTile(
        unit.getX(), unit.getY() ).getTerrain();

      TerrainType tt = (TerrainType)
        m_client.getGame().getFactories().getTerrainTypeFactory().findById( ttype );
      sb.append( tt.getName() );
      m_labTerrain.setText( sb.toString() );

      sb = new StringBuffer();
      sb.append( "Home City: " );
      org.freeciv.common.City c = unit.getHomeCity();
      if ( c == null )
      {
        sb.append( "None" );
      }
      else
      {
        sb.append( c.getName() );
      }

      m_labHomeCity.setText( sb.toString() );

      invalidate();
      validate();
    }


  }

  private static String translate( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
