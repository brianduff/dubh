package org.freeciv.client.panel;

import org.freeciv.client.*;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import javax.swing.*;
import java.awt.*;

/**
 * Panel that displays information about the civilization
 */
public class CivInfoPanel extends VerticalFlowPanel
{
  private Client m_client;
  private JLabel m_labNation, m_labPopulation, m_labYear, m_labGold, m_labTax;

  private static final Color TEXT_COLOR =
    UIManager.getLookAndFeelDefaults().getColor("infoText");

  private static final Color WINDOW_COLOR =
    UIManager.getLookAndFeelDefaults().getColor("info");

  public CivInfoPanel( Client c )
  {
    m_client = c;
    m_labNation = new JLabel( );
    m_labNation.setForeground( TEXT_COLOR );
    m_labPopulation = new JLabel( );
    m_labPopulation.setForeground( TEXT_COLOR );
    m_labYear = new JLabel( );
    m_labYear.setForeground( TEXT_COLOR );
    m_labGold = new JLabel( );
    m_labGold.setForeground( TEXT_COLOR );
    m_labTax = new JLabel( );
    m_labTax.setForeground( TEXT_COLOR );
    addRow( m_labNation );
    addRow( m_labPopulation );
    addRow( m_labYear );
    addRow( m_labGold );
    addRow( m_labTax );
    setBackground( WINDOW_COLOR );

    clear();
  }

  /**
   * Clears the civ info panel to its initially empty state.
   */
  public void clear()
  {
    // We use spaces so that the preferred height of the panel remains
    // correct.
    m_labNation.setText( " " );
    m_labPopulation.setText( " " );
    m_labYear.setText( "Game not started" );
    m_labGold.setText( " " );
    m_labTax.setText( " " );
  }

  public void setGold( int gold )
  {
    setAndValidate( m_labGold, "Gold: " + gold );
  }

  public void setYear( int year )
  {
    StringBuffer yearString = new StringBuffer(8);
    yearString.append( String.valueOf( Math.abs( year ) ) );
    yearString.append( ' ' );
    if ( year < 0 )
    {
      yearString.append( "B.C." );
    }
    else
    {
      yearString.append( "A.D." );
    }

    setAndValidate( m_labYear, yearString.toString() );
  }

  public void setTax( int tax, int science, int luxury )
  {
    setAndValidate( m_labTax, "Tax: " + tax + " Sci: " + science + " Lux: " + luxury );
  }

  public void setPop( int pop )
  {
    setAndValidate( m_labPopulation, "Population: " + String.valueOf( pop ) );
  }

  public void setNationName( String nationName )
  {
    m_labNation.setText( nationName );
    this.validate();
  }

  private void setAndValidate( JLabel lab, String value )
  {
    lab.setText( value );
    this.validate();
  }

  private static String _( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
