package org.freeciv.client.dialog;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.freeciv.common.City;
import org.freeciv.common.Unit;
import org.freeciv.common.UnitType;
import org.freeciv.common.Player;
import org.freeciv.common.CommonConstants;
import org.freeciv.client.*;
import org.freeciv.client.dialog.util.*;
import org.freeciv.client.ui.util.*;

/**
 * Implementation of the Military dialog.  Shows active and in-progress
 * units and their upkeep.
 *
 * @author Ben Mazur
 */
class ImplMilitaryReport extends VerticalFlowPanel
  implements DlgMilitaryReport, CommonConstants
{
  private JLabel m_labMilitary = new JLabel( translate( "Military" ), JLabel.CENTER );
  private JLabel m_labGovt = new JLabel( translate( "<Gov't> of the <Nation>" ), JLabel.CENTER );
  private JLabel m_labYear = new JLabel( translate( "<Title> <Name> : XXXX BC/AD" ), JLabel.CENTER );
  private UnitsTableModel m_unitsModel;
  private JTable m_tabUnits;
  private JPanel m_panButtons = new JPanel();
  private JButton m_butClose = new JButton( translate( "Close" ) );
  private JButton m_butUpgrade = new JButton( translate( "Upgrade" ) );
  private JButton m_butRefresh = new JButton( translate( "Refresh" ) );

  private Client m_client;
  JDialog m_dialog;
  private DialogManager m_dlgManager;

  public ImplMilitaryReport( DialogManager mgr, Client c )
  {
    m_client = c;
    m_dlgManager = mgr;

    addRow( m_labMilitary );
    addRow( m_labGovt );
    addRow( m_labYear );

    setupUnitsTable();
    setupButtonPanel();
  }

  /**
   * for consistancy
   */
  private Client getClient()
  {
    return m_client;
  }

  /**
   * Shortcut to getClient().getGame().getCurrentPlayer()
   */
  private Player getPlayer()
  {
    return getClient().getGame().getCurrentPlayer();
  }

  /**
   * Sets up the units table
   */
  private void setupUnitsTable()
  {
    m_unitsModel = new UnitsTableModel();
    m_tabUnits = new JTable( m_unitsModel );

    m_tabUnits.setRowSelectionAllowed( true );
    m_tabUnits.setColumnSelectionAllowed( false );
    m_tabUnits.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    m_tabUnits.setShowHorizontalLines( false );
    m_tabUnits.setShowVerticalLines( false );

    this.addSpacerRow( new JScrollPane( m_tabUnits ) );
  }

  /**
   * Initialization function; sets up the button panel and adds it to the
   * main dialog panel.
   */
  private void setupButtonPanel()
  {
    m_butClose.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        undisplay();
      }
    } );
    m_butRefresh.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        refresh();
      }
    } );
    m_butUpgrade.setEnabled( false ); //TODO

    m_panButtons.setLayout( new FlowLayout() );
    m_panButtons.add( m_butClose );
    m_panButtons.add( m_butUpgrade );
    m_panButtons.add( m_butRefresh );
    this.addRow( m_panButtons );
  }


  /**
   * Re-populates the combo box and lists, as well as updates the labels
   * with new info from the client
   */
  public void refresh()
  {
    m_labGovt.setText( translate(
            getPlayer().getGovernment().getName()
            + " of the "
            + getPlayer().getNation().getPluralName()
            ) );
    m_labYear.setText( translate(
            getPlayer().getRulerTitle() + " "
            + getPlayer().getName()
            + " : " + getClient().getGame().getYearString()
            ) );

    updateUnits();
    resizeTable( m_tabUnits );
  }

  /**
   * Repopulate the units table with data from the client
   */
  public void updateUnits()
  {
    ArrayList uList = new ArrayList();
    // [ unittype.id ][ { inprod, active, food, shields } ]
    int[][] unitTotals = new int[ CommonConstants.U_LAST ][ 4 ];

    for( Iterator i = getPlayer().getUnits(); i.hasNext(); )
    {
      Unit unit = (Unit)i.next();
      unitTotals[unit.getType()][1]++;
      unitTotals[unit.getType()][2] += unit.getUpkeepFood();
      unitTotals[unit.getType()][3] += unit.getUpkeep();
    }

    for( Iterator i = getPlayer().getCities(); i.hasNext(); )
    {
      City city = (City)i.next();
      if( city.isBuildingUnit() )
      {
         unitTotals[city.getCurrentlyBuildingId()][0]++;
      }
    }

    int totalInprog = 0, totalActive = 0, totalShields = 0, totalFood = 0;
    for( int i = 0; i < unitTotals.length; i++ )
    {
      if( unitTotals[i][0] == 0 && unitTotals[i][1] == 0 )
      {
        continue;
      }
      final UnitType unitType = (UnitType)getClient().getFactories().getUnitTypeFactory().findById( i );
      Object[] datum = new Object[7];

      datum[0] = unitType.getName();
      datum[1] = "?";  //TODO: can_upgrade_unittype() ? "*" : "-"
      datum[2] = new Integer( unitTotals[i][0] );
      datum[3] = new Integer( unitTotals[i][1] );
      datum[4] = new Integer( unitTotals[i][2] );
      datum[5] = new Integer( unitTotals[i][3] );
      datum[6] = new Integer( i );

      totalInprog += unitTotals[i][0];
      totalActive += unitTotals[i][1];
      totalShields += unitTotals[i][2];
      totalFood += unitTotals[i][3];

      uList.add( datum );
    }

    //TODO: make totals stick to the bottom of the table, yet align each
    // value with the appropriate column.

    Object[] datum = new Object[7];
    datum[0] = "Totals:";
    datum[1] = "";
    datum[2] = new Integer( totalInprog );
    datum[3] = new Integer( totalActive );
    datum[4] = new Integer( totalShields );
    datum[5] = new Integer( totalFood );
    datum[6] = new Integer( CommonConstants.U_LAST );
    uList.add( datum );

    Object[][] newData = new Object[uList.size()][7];
    uList.toArray( newData );
    m_unitsModel.data = newData;

  }

  /**
   * Attempt to resize a table's PreferredScrollableViewportSize based on the
   * size of the headers and data within it.  Isn't there some way to do this
   * automatically?  There should be.
   *
   * TableColumn.sizeWidthToFit() seems inadequate.
   *
   * This method might be better off in some utility class.
   */
  private static void resizeTable( JTable table )
  {
    int totalWidth = 0;
    for( int i = 0; i < table.getColumnCount(); i++ )
    {
      final TableColumn column = table.getColumnModel().getColumn( i );
      int width = 0;
      width = Math.max( width, table.getFontMetrics( table.getFont() ).stringWidth( table.getColumnName( i ) ) );
      for( int j = 0; j < table.getRowCount(); j++ )
      {
        width = Math.max( width, table.getFontMetrics( table.getFont() ).stringWidth( table.getValueAt( j, i ).toString() ) );
      }
      // and it seems like we need some padding.
      width += 10;
      column.setPreferredWidth( width );
      totalWidth += width;
    }

    int height = Math.min( 12, table.getRowCount() ) * table.getRowHeight();
    table.setPreferredScrollableViewportSize(
      new Dimension( totalWidth, height )
    );
  }

  public void display()
  {
    JDialog dlg = new JDialog(
      m_client.getMainWindow(), translate( "Military" ), true
    );
    dlg.getContentPane().setLayout( new BorderLayout() );
    dlg.getContentPane().add( ImplMilitaryReport.this, BorderLayout.CENTER );
    m_dialog = dlg;

    refresh();

    m_dlgManager.showDialog( m_dialog );
  }

  public void undisplay()
  {
    m_dlgManager.hideDialog( m_dialog );
  }

  /**
   * A nice, if unoriginal, class representing the columns and data in the
   * units table
   */
  class UnitsTableModel extends AbstractTableModel
  {
    String[] columnNames = { translate( "Unit Type" ), translate( "U" ),
                             translate( "In-Prog" ), translate( "Active" ),
                             translate( "Shields" ), translate( "Food" ) };
    Object[][] data = { { translate("Phalanx"), translate( "-" ),
                          new Integer( 0 ), new Integer( 2 ),
                          new Integer( 2 ), new Integer( 0 ) },
                        { translate("Settlers"), translate( "-" ),
                          new Integer( 2 ), new Integer( 2 ),
                          new Integer( 0 ), new Integer( 2 ) } };

    public int getColumnCount()
    {
      return columnNames.length;
    }
    public int getRowCount()
    {
      return data.length;
    }
    public String getColumnName( int col )
    {
      return columnNames[ col ];
    }
    public Object getValueAt( int row, int col )
    {
      return data[row][col];
    }
    public Class getColumnClass( int col )
    {
      return getValueAt( 0, col ).getClass();
    }
  }

  // localization
  private static String translate( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }

}
