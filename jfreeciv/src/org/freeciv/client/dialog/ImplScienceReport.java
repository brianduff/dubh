package org.freeciv.client.dialog;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.freeciv.common.Advance;
import org.freeciv.common.Player;
import org.freeciv.common.CommonConstants;
import org.freeciv.client.*;
import org.freeciv.client.dialog.util.*;
import org.freeciv.client.ui.util.*;
import org.freeciv.net.PktPlayerRequest;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.WorkList;

/**
 * Implementation of the science dialog.  Used to view/change current
 * research or reserach goal.
 * 
 * @author Ben Mazur
 */
class ImplScienceReport extends VerticalFlowPanel 
  implements DlgScienceReport, CommonConstants, ItemListener
{
  // top-level
  private JLabel m_labScience = new JLabel( _( "Science" ), JLabel.CENTER );
  private JLabel m_labGovt = new JLabel( _( "<Gov't> of the <Nation>" ), JLabel.CENTER );
  private JLabel m_labYear = new JLabel( _( "<Title> <Name> : XXXX BC/AD" ), JLabel.CENTER );
  private JLabel m_labTurns = new JLabel( _( "(X turns/advance)" ), JLabel.CENTER );
  private JPanel m_panResearch = new JPanel();
  private JPanel m_panGoal = new JPanel();
  private JPanel m_panKnown = new JPanel();
  private JButton m_butClose = new JButton( _( "Close" ) );
  // current research
  private JComboBox m_cmbResearch;
  private JProgressBar m_pbrResearch= new JProgressBar();
  private JCheckBox m_chkHelp = new JCheckBox( _( "Help" ) );
  // research goal
  private JComboBox m_cmbGoal;
  private JLabel m_labGoalSteps = new JLabel( _( "(X steps)" ) );
  // known techs
  private JList m_lstKnownTechs;
  
  private Client m_client;
  JDialog m_dialog;
  private DialogManager m_dlgManager;
    
  public ImplScienceReport( DialogManager mgr, Client c ) 
  {
    m_client = c;
    m_dlgManager = mgr;
    
    addRow( m_labScience );
    addRow( m_labGovt );
    addRow( m_labYear );
    addRow( m_labTurns );
    
    setupResearchPanel();
    setupGoalPanel();
    setupKnownTechs();
    
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
   * shortcut function
   */
  private Player getPlayer()
  {
    return getClient().getGame().getCurrentPlayer();
  }

  /**
   * Sets up the research panel
   */
  private void setupResearchPanel()
  {
    m_cmbResearch = new JComboBox();
    m_cmbResearch.addItemListener( this );
    
    m_pbrResearch.setStringPainted( true );
    m_pbrResearch.setString( "X / X" );
    
    m_chkHelp.setEnabled( false );
    
    m_panResearch.setLayout( new FlowLayout() );
    m_panResearch.add( m_cmbResearch );
    m_panResearch.add( m_pbrResearch );
    m_panResearch.add( m_chkHelp );
    
    this.addSpacerRow( m_panResearch );
  }
  
  /**
   * Sets up the research goal panel
   */
  private void setupGoalPanel()
  {
    m_cmbGoal = new JComboBox();
    m_cmbGoal.addItemListener( this );

    m_panGoal.setLayout( new FlowLayout() );
    m_panGoal.add( m_cmbGoal );
    m_panGoal.add( m_labGoalSteps );
    
    this.addSpacerRow( m_panGoal );
  }

  /**
   * Sets up the known advances panels
   * 
   * Note: uses 1.4 method JList.setLayoutOrientation()
   */
  private void setupKnownTechs()
  {
    m_lstKnownTechs = new JList();
    
    m_lstKnownTechs.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    m_lstKnownTechs.setLayoutOrientation( JList.HORIZONTAL_WRAP );
    m_lstKnownTechs.setEnabled( false );
    
    m_panKnown.setLayout( new BorderLayout() );
    m_panKnown.add( new JScrollPane( m_lstKnownTechs ), BorderLayout.CENTER );
    this.addSpacerRow( m_panKnown );
  }
  
  /**
   * Re-populates the combo box and lists, as well as updates the labels
   * with new info from the client
   */
  public void updateAll()
  {
    m_labGovt.setText( _( 
            getPlayer().getGovernment().getName()
            + " of the "
            + getPlayer().getNation().getPluralName()
            ) );
    m_labYear.setText( _( 
            getPlayer().getRulerTitle()
            + getPlayer().getName()
            + " : " + getClient().getGame().getYearString()
            ) );
    m_labTurns.setText( _( "("
                          + getPlayer().getTurnsToAdvance()
                           + " turns/advance)" ) );
   
    updateResearchList();
    updateResearchBar();
    updateGoalList();
    updateGoalLabel();
    updateKnownTechs();
  }
  
  /**
   * Refresh the research combo box.  This contains a stupid workaround to the
   * problem where when you clear a combo box and start adding new items, the
   * first added item becomes selected, triggering the item listener.
   */
  private void updateResearchList()
  {
    m_cmbResearch.removeItemListener( this );  // XXX
    m_cmbResearch.removeAllItems();
    for( int i = A_FIRST; 
         i < getClient().getGame().getNumberOfTechnologyTypes(); 
         i++ )
    {
      if( getClient().getGame().advanceExists( i ) 
         && !getPlayer().getResearch().hasInvention( i ) )
      {
        final Advance adv = (Advance)getClient().getGame().getFactories().getAdvanceFactory().findById( i );
        boolean reachable = true;
        for( Iterator iter = adv.getRequiredAdvances();
             iter.hasNext(); )
        {
          reachable &= getPlayer().getResearch().hasInvention( (Advance)iter.next() );
        }
        if( reachable )
        {
          AdvanceListItem item = new AdvanceListItem( adv );
          m_cmbResearch.addItem( item );
          if( getPlayer().getResearch().getCurrentlyResearching().getId() == i )
          {
            m_cmbResearch.setSelectedItem( item );
          }
        }
      }
    }
    m_cmbResearch.addItemListener( this );  // XXX
  }
    
  /**
   * Refresh the research progress bar
   */
  private void updateResearchBar()
  {
    int done = getPlayer().getResearch().getResearched();
    int cost = getPlayer().getResearch().getResearchPointCost();
    m_pbrResearch.setMinimum( 0 );
    m_pbrResearch.setMaximum( cost );
    m_pbrResearch.setValue( done );
    m_pbrResearch.setString( done + " / " + cost );
  }

  /**
   * Refresh the goal combo box.  This contains a stupid workaround to the
   * problem where when you clear a combo box and start adding new items, the
   * first added item becomes selected, triggering the item listener.
   */
  private void updateGoalList()
  {
    m_cmbGoal.removeItemListener( this );  // XXX
    m_cmbGoal.removeAllItems();
    for( int i = A_FIRST; 
         i < getClient().getGame().getNumberOfTechnologyTypes(); 
         i++ )
    {
      if( getClient().getGame().advanceExists( i ) 
         && !getPlayer().getResearch().hasInvention( i )
         && getPlayer().getResearch().getTechGoalSteps( i ) < 11 )
      {
        final Advance adv = (Advance)getClient().getGame().getFactories().getAdvanceFactory().findById( i );
        AdvanceListItem item = new AdvanceListItem( adv );
        m_cmbGoal.addItem( item );
        if( getPlayer().getAI().getTechGoal() == i )
        {
          m_cmbGoal.setSelectedItem( item );
        }
      }
    }
    m_cmbGoal.addItemListener( this );  // XXX
  }
  
  /**
   * Refresh the goal steps label
   */
  private void updateGoalLabel()
  {
    int steps = getPlayer().getResearch().getTechGoalSteps( getGoal() );
    
    m_labGoalSteps.setText( _( "(" + steps + " steps)" ) );
  }

  /**
   * Refresh the known techs list
   */
  private void updateKnownTechs()
  {
    ArrayList kList = new ArrayList();
    for( int i = A_FIRST; 
         i < getClient().getGame().getNumberOfTechnologyTypes(); 
         i++ )
    {
      if( getClient().getGame().advanceExists( i ) 
         && getPlayer().getResearch().hasInvention( i ) )
      {
        final Advance adv = (Advance)getClient().getGame().getFactories().getAdvanceFactory().findById( i );
        kList.add( new AdvanceListItem( adv ) );
      }
    }
    m_lstKnownTechs.setListData( kList.toArray() );
    m_lstKnownTechs.setVisibleRowCount( (int)Math.ceil( kList.size() / 4.0f ) );
  }
  
  /**
   * Changes the goal to whatever's displayed on the goal list
   */
  private void changeGoal()
  {
    if( getGoal() != getPlayer().getAI().getTechGoal() )
    {
      updateGoalLabel();
      requestChangeGoal( getGoal() );
    }
  }
  
  /**
   * Changes the current research to whatever's displayed on the research list
   */
  private void changeResearch()
  {
    if( getResearch()
        != getPlayer().getResearch().getCurrentlyResearching().getId() )
    {
      updateResearchBar();
      requestChangeResearch( getResearch() );
    }
  }
  
  /**
   * Sends a packet indicating that the player should change its current
   * research to the specified one
   */
  private void requestChangeResearch( int research ) 
  {
    PktPlayerRequest packet = new PktPlayerRequest();
    packet.setType( PacketConstants.PACKET_PLAYER_RESEARCH );
    packet.tech = research;
    getClient().sendToServer( packet );
  }
  
  /**
   * Sends a packet indicating that the player should change its current tech
   * goal to the specified one
   */
  private void requestChangeGoal( int goal ) 
  {
    PktPlayerRequest packet = new PktPlayerRequest();
    packet.setType( PacketConstants.PACKET_PLAYER_TECH_GOAL );
    packet.tech = goal;
    getClient().sendToServer( packet );
  }
  
  /**
   * Returns the goal selected on the goal list
   */
  public int getGoal()
  {
    return ( (AdvanceListItem)m_cmbGoal.getSelectedItem() ).m_advance.getId();
  }

  
  /**
   * Returns the research selected on the research list
   */
  public int getResearch()
  {
    return ( (AdvanceListItem)m_cmbResearch.getSelectedItem() ).m_advance.getId();
  }

  public void display()
  {
    JDialog dlg = new JDialog(
      m_client.getMainWindow(), _( "Science" ), true 
    );
    dlg.getContentPane().setLayout( new BorderLayout() );
    dlg.getContentPane().add( ImplScienceReport.this, BorderLayout.CENTER );
    m_dialog = dlg;
    
    updateAll();
    
    m_dlgManager.showDialog( m_dialog );
  }
  
  public void undisplay()
  {
    m_dlgManager.hideDialog( m_dialog );
  }
  
  /**
   * A class to hold techs within the confines of a list item
   */
  private class AdvanceListItem
  {
    public Advance m_advance;
    
    public AdvanceListItem( Advance adv )
    {
      m_advance = adv;
    }
    
    public String toString()
    {
      return m_advance.getName();
    }
  }
 
  // localization
  private static String _( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
  
  //
  // ItemListener
  //
  public void itemStateChanged( ItemEvent e )
  {
    if( e.getStateChange() == ItemEvent.SELECTED
       && e.getSource().equals( m_cmbGoal ) )
    {
      changeGoal();
    }
    if( e.getStateChange() == ItemEvent.SELECTED
       && e.getSource().equals( m_cmbResearch ) )
    {
      changeResearch();
    }
  }
}
