package org.freeciv.client.dialog;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.Collator;
import org.freeciv.client.*;
import org.freeciv.client.dialog.util.*;
import org.freeciv.client.action.*;
import org.freeciv.client.ui.util.*;

import org.freeciv.common.CityStyle;
import org.freeciv.common.Nation;

//
// TODO:
//
// The text field on the combo needs to be more intelligent about
// whether the user has typed something in or whether to default
// from items in the list. Theres an aborted attempt at getting it
// to work in here, this needs improving.
/**
 * The main interface to the nation dialog
 */
class ImplNation extends VerticalFlowPanel implements DlgNation,Constants
{
  private final static int NATION_COLUMNS = 5;
  private VerticalFlowPanel m_panNation = new VerticalFlowPanel();
  private JPanel m_panNationList = new JPanel();
  private JPanel m_panSex = new JPanel();
  private JPanel m_panCityStyle = new JPanel();
  private JComboBox m_cmbLeaderName = new JComboBox();
  private JPanel m_panButtons = new JPanel();
  private JButton m_butOK = new JButton( _( "OK" ) );
  private ActionButton m_butDisconnect;
  private ActionButton m_butQuit;
  private ArrayList m_alNationButtons = new ArrayList();
  private JRadioButton m_rbMale = new JRadioButton( _( "Male" ) ), 
    m_rbFemale = new JRadioButton( _( "Female" ) );
  private JList m_nations;
  private JRadioButton[] m_arbCityStyles;
  private ButtonGroup m_bgSex = new ButtonGroup();
  private ButtonGroup m_bgCityStyle = new ButtonGroup();
  private boolean bFullyInitialized;
  private Client m_client;
  JDialog m_dialog;
  private DialogManager m_dlgManager;
  private boolean m_bNameAltered;
  private boolean m_bJustSelectedALeader;
  private NationLeaderPopulator m_nlp = new NationLeaderPopulator();
    
  public ImplNation( DialogManager mgr, Client c ) 
  {
    m_client = c;
    bFullyInitialized = false;
    setupNationPanel();
    setupSexPanel();
    setupCityStylePanel();
    setupButtonPanel();
    m_dlgManager = mgr;
  }

  private Client getClient()
  {
    return m_client;
  }
  
  private void setupButtonPanel()
  {
    m_butDisconnect = new ActionButton( m_client.getAction( "ACTDisconnect" ) );
    m_butQuit = new ActionButton( m_client.getAction( "ACTQuit" ) );
    m_panButtons.setLayout( new FlowLayout() );
    m_panButtons.add( m_butOK );
    m_panButtons.add( m_butDisconnect );
    m_panButtons.add( m_butQuit );
    this.addRow( m_panButtons );
  }
  
  private void setupNationPanel()
  {
    m_panNation.setBorder( BorderFactory.createTitledBorder( _( "Select nation and name" ) ) );
    m_nations = new JList();
    m_panNation.addSpacerRow( new JScrollPane( m_nations ) );
    m_nations.addListSelectionListener( new NationLeaderPopulator() );
    m_nations.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    m_nations.setCellRenderer( new NationListCellRenderer() );
    m_panNation.addRow( m_cmbLeaderName );
    m_cmbLeaderName.setEditable( true );
    m_cmbLeaderName.addActionListener( new LeaderNameComboListener() );
    ( (JTextField)m_cmbLeaderName.getEditor().getEditorComponent() ).getDocument().addDocumentListener( new DocumentListener() 
    {
      public void changedUpdate( DocumentEvent e )
      {
        m_bNameAltered = !m_bJustSelectedALeader && true;
        m_bJustSelectedALeader = false;
      }
      public void insertUpdate( DocumentEvent e )
      {
        m_bNameAltered = !m_bJustSelectedALeader && true;
        m_bJustSelectedALeader = false;
      }
      public void removeUpdate( DocumentEvent e )
      {
        m_bNameAltered = !m_bJustSelectedALeader && true;
        m_bJustSelectedALeader = false;
      }
    } );
    this.addSpacerRow( m_panNation );
  }
  
  private void setupSexPanel()
  {
    m_panSex.setBorder( BorderFactory.createTitledBorder( _( "Select your sex" ) ) );
    m_bgSex.add( m_rbMale );
    m_bgSex.add( m_rbFemale );
    m_panSex.setLayout( new BorderLayout() );
    m_panSex.add( m_rbMale, BorderLayout.WEST );
    m_panSex.add( m_rbFemale, BorderLayout.CENTER );
    m_rbMale.setSelected( true );
    this.addRow( m_panSex );
  }
  
  private void setupCityStylePanel()
  {
    m_panCityStyle.setBorder( BorderFactory.createTitledBorder( _( "Select your city style" ) ) );
    this.addRow( m_panCityStyle );
  }
  
  private void addNations()
  {
    m_nations.setModel( new NationListModel() );
    selectInitialNation();
  }
  
  private void addCityStyles()
  {
    m_panCityStyle.setLayout( new GridLayout() );
    int b_s_num;
    ArrayList alCityStyles = new ArrayList();
    for( int i = 0; i < m_client.getGame().getCityStyleCount(); i++ )
    {
      //System.out.println("Tech req of city style "+i+" is "+
      //   m_rs.getRulesetCity(i).techreq+" require "+A_NONE
      //);
      CityStyle cs = (CityStyle)
        getClient().getFactories().getCityStyleFactory().findById( i );
        
      if( !cs.isAdvanceRequired() )
      {
        alCityStyles.add( cs.getName() );
      }
    }
    m_arbCityStyles = new JRadioButton[ alCityStyles.size() ];
    for( int i = 0;i < alCityStyles.size();i++ )
    {
      JRadioButton rb = new JRadioButton( (String)alCityStyles.get( i ) );
      m_arbCityStyles[ i ] = rb;
      if( i == 0 )
      {
        rb.setSelected( true );
      }
      m_bgCityStyle.add( rb );
      m_panCityStyle.add( rb );
    }
  }
  
  private void init()
  {
    if( !bFullyInitialized )
    {
      addNations();
      addCityStyles();
      bFullyInitialized = true;
    }
  }
  
  /**
   * Selects the specified nation
   *
   * @param num the id of the nation to select
   */
  private void selectNation( int num )
  {
    for( int i = 0;i < m_nations.getModel().getSize();i++ )
    {
      Nation n = (Nation)m_nations.getModel().getElementAt( i );
      if( num == n.getId() )
      {
        m_nations.setSelectedIndex( i );
        return ;
      }
    }
    throw new IllegalArgumentException( "Tried to select nation with id " + num + " but it's not in the list" );
  }
  
  private void selectInitialNation()
  {
    // Some suggestion on freeciv-dev that this should be random.
    // We just select the first enabled one for now.
    // BDUFF: In 1.12, I think it is random now, probably the server tells
    // us which one to choose. Will fix this one day.
    m_nations.setSelectedIndex( 0 );
  }
  
  /**
   * Get the id of the selected nation
   */
  private int getSelectedNation()
  {
    Nation nation = (Nation)m_nations.getSelectedValue();
    if( nation == null )
    {
      return -1;
    }
    return nation.getId();
  }
  
  private int getSelectedCityStyle()
  {
    for( int i = 0;i < m_arbCityStyles.length;i++ )
    {
      if( m_arbCityStyles[ i ].isSelected() )
      {
        return i;
      }
    }
    return -1;
  }

  

  class NationLeaderPopulator implements ListSelectionListener
  {
    public void valueChanged( ListSelectionEvent e )
    {
      // If the user typed something into the text field, then
      // don't change it.
      if( m_bNameAltered )
      {
        return ;
      }
      // Now get the leader names for the selected nation.
      Nation nation = (Nation)m_nations.getSelectedValue();
      if( nation == null )
      {
        return ;
      }
      int leaderCount = nation.getLeaderCount();
      m_cmbLeaderName.removeAllItems();
      for( int i = 0;i < leaderCount;i++ )
      {
        LeaderItem li = new LeaderItem();
        li.name = nation.getLeaderName( i );
        li.is_male = nation.isLeaderMale( i );
        m_cmbLeaderName.addItem( li );
      }
      m_bNameAltered = false;
    }
  }
  
  class LeaderItem
  {
    public String name;
    public boolean is_male;
    public String toString()
    {
      return name;
    }
  }
  
  public class LeaderNameComboListener implements ActionListener
  {
    public void actionPerformed( ActionEvent e )
    {
      Object sel = m_cmbLeaderName.getSelectedItem();
      if( sel instanceof LeaderItem )
      {
        boolean male = ( (LeaderItem)sel ).is_male;
        m_rbMale.setSelected( male );
        m_rbFemale.setSelected( !male );
        // Forget about whether the user typed in a name, they've
        // selected one from the list now and anything they typed
        // is forgotten forever :)
        m_bNameAltered = false;
        m_bJustSelectedALeader = true;
      }
    }
  }
  
  // DlgNation interface
  class TogglerRunnable implements Runnable
  {
    private int bits1, bits2;
    public TogglerRunnable( int bits1, int bits2 ) 
    {
      this.bits1 = bits1;
      this.bits2 = bits2;
    }
    public void run()
    {
      int i, selected, mybits;
      mybits = bits1;
      for( i = 0;i < m_client.getGame().getPlayableNationCount() && i < 32;i++ )
      {
        boolean enabled = ( ( mybits & 1 ) == 0 );
        ( (NationListModel)m_nations.getModel() ).setNationEnabled( i, enabled );
        mybits >>= 1;
      }
      mybits = bits2;
      for( i = 32; i < m_client.getGame().getPlayableNationCount(); i++ )
      {
        boolean enabled = ( ( mybits & 1 ) == 0 );
        ( (NationListModel)m_nations.getModel() ).setNationEnabled( i, enabled );
        mybits >>= 1;
      }
      if( ( selected = getSelectedNation() ) == -1 )
      {
        return ;
      }
      if( ( bits1 & ( 1 << selected ) ) != 0 || ( selected > 32 && ( bits2 & ( 1 << ( selected - 32 ) ) ) != 0 ) )
      {
        selectInitialNation();
      }
    }
  }
  
  public void toggleAvailableRaces( int bits1, int bits2 )
  {
    SwingUtilities.invokeLater( new TogglerRunnable( bits1, bits2 ) );
  }
  
  public void display()
  {
    init();
    JDialog dlg = new JDialog( m_client, _( "Choose Nation" ), true );
    dlg.getContentPane().setLayout( new BorderLayout() );
    dlg.getContentPane().add( ImplNation.this, BorderLayout.CENTER );
    m_dialog = dlg;
    m_butOK.addActionListener( ACTSendAllocNation.getInstance( m_client ) );
    m_dlgManager.showDialog( m_dialog );
  }
  
  public void undisplay()
  {
    m_dlgManager.hideDialog( m_dialog );
  }
  
  public int getNation()
  {
    return getSelectedNation();
  }
  
  public String getLeaderName()
  {
    return m_cmbLeaderName.getSelectedItem().toString();
  }
  
  public boolean isLeaderMale()
  {
    return m_rbMale.isSelected();
  }
  
  public int getCityStyle()
  {
    return getSelectedCityStyle();
  }
  
  /**
   * The datamodel for the list of nations. This alphabeticizes the list
   */
  private class NationListModel extends AbstractListModel
  {
    private ArrayList m_sortedList;
    public NationListModel() 
    {
      m_sortedList = new ArrayList();
      for( int i = 0; i < getClient().getGame().getPlayableNationCount(); i++ )
      {
        m_sortedList.add( 
          getClient().getFactories().getNationFactory().findById(i)
        );
      }
      Collections.sort( m_sortedList, new NationComparator() );
    }
    public Object getElementAt( int index )
    {
      return m_sortedList.get( index );
    }
    public int getSize()
    {
      return m_sortedList.size();
    }
    
    /**
     * Enables or disables a nation. Actually physically removes or re-inserts
     * the nation into the list.
     *
     * @param nationId the id of the nation to enable or disable
     * @param isEnabled whether to enable or disable the nation
     */
    public void setNationEnabled( int nationId, boolean isEnabled )
    {
      Nation n = (Nation)
        getClient().getFactories().getNationFactory().findById(nationId);

      boolean alreadyThere = m_sortedList.contains( n );
      if( isEnabled )
      {
        if( !alreadyThere )
        {
          m_sortedList.add( n );
          Collections.sort( m_sortedList );
          // Although we just resorted the list, the only element which should
          // have moved will be the nation we just added. Get it's index
          // and fire an insertion event.
          int idx = getNationIndex( n );
          fireIntervalAdded( this, idx, idx );
        }
      }
      else
      {
        if( alreadyThere )
        {
          int idx = getNationIndex( n );
          m_sortedList.remove( n );
          fireIntervalRemoved( this, idx, idx );
        }
      }
    }
    
    private int getNationIndex( Nation n )
    {
      for( int i = 0;i < m_sortedList.size();i++ )
      {
        if( m_sortedList.get( i ) == n )
        {
          return i;
        }
      }
      throw new IllegalArgumentException( "Nation " + n + " is not in the list" );
    }
  }
  
  /**
   * Used to sort the list of nations alphabetically
   */
  private class NationComparator implements Comparator
  {
    public int compare( Object o1, Object o2 )
    {
      return Collator.getInstance().compare( ( (Nation)o1 ).getName(), ( (Nation)o2 ).getName() );
    }
    
    public boolean equals( Object obj )
    {
      return ( obj instanceof NationComparator );
    }
  }
  
  /**
   * Renders the cells in the nation list
   */
  private class NationListCellRenderer extends DefaultListCellRenderer
  {
    public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
    {
      Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
      Nation n  = (Nation)value;
      setText(  n.getName() );
      Icon i = getClient().getTileSpec().lookupSpriteTagAlt(
        n.getFlagGraphicStr(), n.getFlagGraphicAlt(), false, 
        "flag", n.getName()
      );

      if (i != null)
      {
        setIcon(i);
      }
      
      return c;
    }
  }
  
  // localization
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
