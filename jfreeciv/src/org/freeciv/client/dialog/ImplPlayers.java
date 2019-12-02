package org.freeciv.client.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import org.freeciv.common.DiplomacyState;
import org.freeciv.common.Player;

/**
 * Implementation of the players dialog.  This has some nice buttons
 * for diplomacy & stuff that sure aren't implemented yet.
 *
 * @author Ben Mazur
 */
class ImplPlayers extends VerticalFlowPanel implements DlgPlayers
{
  private PlayerTableModel m_playerTableModel;
  private JTable m_tabPlayers;
  private JPanel m_panButtons = new JPanel();
  private JButton m_butClose = new JButton( translate( "Close" ) );
  private JButton m_butIntelligence = new JButton( translate( "Intelligence" ) );
  private JButton m_butMeet = new JButton( translate( "Meet" ) );
  private JButton m_butCancelTreaty = new JButton( translate( "Cancel Treaty" ) );
  private JButton m_butWithdrawVision = new JButton( translate( "Withdraw Vision" ) );
  private JButton m_butSpaceship = new JButton( translate( "Spaceship" ) );

  private Client m_client;
  JDialog m_dialog;
  private DialogManager m_dlgManager;

  public ImplPlayers( DialogManager mgr, Client c )
  {
    m_client = c;
    m_dlgManager = mgr;

    setupPlayerTable();
    setupButtonPanel();

    m_butClose.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        undisplay();
      }
    } );
    addRow( m_butClose );
  }

  private Client getClient()
  {
    return m_client;
  }

  /**
   * Sets up the player table with the inital (dummy) data in PlayerTableModel
   */
  private void setupPlayerTable()
  {
    m_playerTableModel = new PlayerTableModel();
    m_tabPlayers = new JTable( m_playerTableModel );

    m_tabPlayers.setRowSelectionAllowed( true );
    m_tabPlayers.setColumnSelectionAllowed( false );
    m_tabPlayers.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    m_tabPlayers.setShowHorizontalLines( false );
    m_tabPlayers.setShowVerticalLines( false );

    this.addSpacerRow( new JScrollPane( m_tabPlayers ) );
  }

  /**
   * Sets up the button panel.
   *
   * TODO: these buttons need to do things!  important things!
   */
  private void setupButtonPanel()
  {
    m_butIntelligence.setEnabled( false );
    m_butMeet.setEnabled( false );
    m_butCancelTreaty.setEnabled( false );
    m_butWithdrawVision.setEnabled( false );
    m_butSpaceship.setEnabled( false );

    m_panButtons.add( m_butIntelligence );
    m_panButtons.add( m_butMeet );
    m_panButtons.add( m_butCancelTreaty );
    m_panButtons.add( m_butWithdrawVision );
    m_panButtons.add( m_butSpaceship );

    this.addRow( m_panButtons );
  }

  /**
   * Returns a (non-translated) string indicating the embassy
   * status of me and them
   *
   * climisc.c:get_embassy_status()
   */
  private String getEmbassyStatus( Player me, Player them )
  {
    if( me.hasEmbassyWith( them ) )
    {
      if( them.hasEmbassyWith( me ) )
      {
        return "Both";
      }
      else
      {
        return "Yes";
      }
    }
    else
    {
      if( them.hasEmbassyWith( me ) )
      {
        return "With us";
      }
      else
      {
        return "";
      }
    }
  }

  /**
   * Returns a (non-translated) string indicating the shared
   * vision status of me and them
   *
   * climisc.c:get_vision_status()
   */
  private String getVisionStatus( Player me, Player them )
  {
    if( ( me.getGivesSharedVision() & ( 1 << them.getId() ) ) != 0 )
    {
      if( ( them.getGivesSharedVision() & ( 1 << me.getId() ) ) != 0 )
      {
        return "Both";
      }
      else
      {
        return "Yes";
      }
    }
    else
    {
      if( ( them.getGivesSharedVision() & ( 1 << me.getId() ) ) != 0 )
      {
        return "With us";
      }
      else
      {
        return "";
      }
    }
  }

  /**
   * Clear and re-populate the player table with fresh data from the client.
   */
  public void refresh()
  {
    ArrayList pList = new ArrayList();
    final int myID = getClient().getGame().getCurrentPlayer().getId();

    for( int i = 0; i < getClient().getGame().getNumberOfPlayers(); i++ ) {
      final Player player = getClient().getGame().getPlayer( i );
      // skip barbarians
      if( player.getAI().isBarbarian() )
      {
        continue;
      }
      String[] datum = new String[9];
      // text for name, plus AI marker
      datum[0] = translate( ( player.getAI().isControlled() ? "*" : "" ) + player.getName() );
      // text for nation
      datum[1] = translate( player.getNation().getName() );
      // text for embassy
      datum[2] = translate( getEmbassyStatus( getClient().getGame().getCurrentPlayer(), player ) );
      // text for diplomacy state and turns, ignoring me.
      if( i == myID )
      {
        datum[3] = translate( "-" );
      }
      else
      {
        final DiplomacyState pds = getClient().getGame().getCurrentPlayer().getDiplomacyState( i );
        if( pds.getType() == pds.DS_CEASEFIRE )
        {
          datum[3] = translate( pds.getName() + " (" + pds.getTurnsLeft() + ")" );
        }
        else
        {
          datum[3] = translate( pds.getName() );
        }
      }
      // text for shared vision
      datum[4] = translate( getVisionStatus( getClient().getGame().getCurrentPlayer(), player ) );
      // test for reputation
      datum[5] = translate( player.getReputationName() );
      // text for state
      if( player.isAlive() )
      {
        if( player.isConnected() )
        {
          if( player.isTurnDone() )
          {
            datum[6] = translate( "done" );
          }
          else
          {
            datum[6] = translate( "moving" );
          }
        }
        else
        {
          datum[6] = translate( "" );
        }
      }
      else
      {
        datum[6] = translate( "R.I.P." );
      }
      // text for conn. address
      datum[7] = translate( "?" );
      // text for idleness
      if( player.getNumberOfIdleTurns() > 3 )
      {
        datum[8] = translate( "(idle " + ( player.getNumberOfIdleTurns() - 1 ) + " turns)" );
      }
      else
      {
        datum[8] = "";
      }

      pList.add( datum );
    }

    String[][] newData = new String[pList.size()][9];
    pList.toArray( newData );
    m_playerTableModel.data = newData;

    resizeTable( m_tabPlayers );
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

    table.setPreferredScrollableViewportSize(
      new Dimension( totalWidth, table.getRowCount() * table.getRowHeight() )
    );
  }

  public void display()
  {
    JDialog dlg = new JDialog(
      m_client.getMainWindow(), translate( "Players" ), true
    );
    dlg.getContentPane().setLayout( new BorderLayout() );
    dlg.getContentPane().add( ImplPlayers.this, BorderLayout.CENTER );
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
   * player table
   */
  class PlayerTableModel extends AbstractTableModel
  {
    String[] columnNames = { translate( "Name" ), translate( "Nation" ), translate( "Embassy" ),
                             translate( "Dipl. State" ), translate( "Vision" ), translate( "Reputation" ),
                             translate( "State" ), translate( "Host" ), translate( "Idle" ) };
    String[][] data = { { translate("Robert the Bruce"), translate("Scottish"), translate("?"),
                          translate( "No Contact" ), translate( "?" ), translate( "Spotless" ),
                          translate( "moving" ), translate( "localhost" ), translate( "(idle 103 turns" ) },
                        { translate("Clint Eastwood the Long Named"), translate("Kalamazooean"), translate("embassy:Maybe"),
                          translate( "No Contact" ), translate( "vision:Perhaps" ), translate( "Dastardly" ),
                          translate( "moving" ), translate( "345.134.126.432" ), translate( "(idle 2 turns" ) } };

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
  }

  // localization
  private static String translate( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
