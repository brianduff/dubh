package org.freeciv.client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import org.freeciv.net.*;
import javax.swing.event.ListSelectionListener;
public class HelpPanel extends JPanel implements ListSelectionListener
{
  Client client;
  HashMap /*String,Vector*/ groups = new HashMap();
  Vector /*String*/ groupsVector = new Vector();
  JList groupList;
  JList entriesList;
  JPanel mainPanel = new JPanel();
  public HelpPanel( Client aClient, File helpDataFile )
          throws IOException 
  {
    super();
    client = aClient;
    groups.put( DEFAULT_GROUP, new Vector() );
    groups.put( null, groups.get( DEFAULT_GROUP ) );
    groupsVector.add( DEFAULT_GROUP );
    loadDataFrom( helpDataFile );
    groupList = new JList( groupsVector );
    groupList.getSelectionModel().addListSelectionListener( this );
    groupList.setPrototypeCellValue( "XXXXXXXXXXXXXX" );
    entriesList = new JList( new DefaultListModel() );
    entriesList.getSelectionModel().addListSelectionListener( this );
    entriesList.setPrototypeCellValue( "XXXXXXXXXXXXXXXXX" );
    setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
    add( new JScrollPane( groupList ) );
    add( new JScrollPane( entriesList ) );
    add( mainPanel );
    mainPanel.setLayout( new FlowLayout() );
    setPreferredSize( new Dimension( 600, 400 ) );
  }
  public void showHelp( String category, String item )
  {
    int groupIndex = -1;
    int entryIndex = -1;
    Vector v = (Vector)groups.get( category );
    if( v == null )
    {
      return ;
    }
    if( category == null )
    {
      groupIndex = 0;
    }
    else
    {
      for( int i = 0;i < groupsVector.size();i++ )
      {
        if( category.equals( groupsVector.get( i ) ) )
        {
          groupIndex = i;
          break;
        }
      }
    }
    if( item == null )
    {
      entryIndex = 0;
    }
    else
    {
      for( int i = 0;i < v.size();i++ )
      {
        if( item.equals( ( (HelpItem)v.get( i ) ).getHelpName() ) )
        {
          entryIndex = i;
          break;
        }
      }
    }
    if( entryIndex < 0 )
    {
      return ;
    }
    groupList.setSelectedIndex( groupIndex );
    entriesList.setSelectedIndex( entryIndex );
  }
  public void showHelp( HelpItem hitem )
  {
    String category = hitem.getHelpCategory();
    String item = hitem.getHelpName();
    int groupIndex = -1;
    int entryIndex = -1;
    Vector v = (Vector)groups.get( category );
    if( v == null )
    {
      return ;
    }
    for( int i = 0;i < groupsVector.size();i++ )
    {
      if( category.equals( groupsVector.get( i ) ) )
      {
        groupIndex = i;
        break;
      }
    }
    for( int i = 0;i < v.size();i++ )
    {
      if( hitem.equals( v.get( i ) ) )
      {
        entryIndex = i;
        break;
      }
    }
    if( entryIndex < 0 )
    {
      return ;
    }
    groupList.setSelectedIndex( groupIndex );
    entriesList.setSelectedIndex( entryIndex );
  }
  public Client getClient()
  {
    return client;
  }
  public JComponent getBuildingHelpPanel( PktRulesetBuilding building )
  {
    return new JLabel( building.name );
  }
  public JComponent getUnitHelpPanel( PktRulesetUnit unit )
  {
    return new JLabel( unit.name );
  }
  public JComponent getTechHelpPanel( PktRulesetTech tech )
  {
    return new JLabel( tech.name );
  }
  JPanel textHelpPanel;
  JLabel textHelpName;
  JTextArea textHelpContent;
  public JComponent getTextHelpPanel( String name, String text )
  {
    if( textHelpPanel == null )
    {
      textHelpPanel = new JPanel();
      textHelpPanel.setLayout( new BoxLayout( textHelpPanel, BoxLayout.Y_AXIS ) );
      textHelpName = new JLabel( " " );
      textHelpContent = new JTextArea( 20, 80 );
      textHelpContent.setFont( new Font( "monospaced", Font.BOLD, 12 ) );
      textHelpContent.setEditable( false );
      textHelpPanel.add( textHelpName );
      textHelpPanel.add( new JScrollPane( textHelpContent ) );
    }
    textHelpName.setText( name );
    textHelpContent.setText( text );
    return textHelpPanel;
  }
  public void loadDataFrom( File helpDataFile )
               throws IOException
  {
    /*
    LineNumberReader br = new LineNumberReader( new BufferedReader( new FileReader( helpDataFile ) ) );
    String line;
    String group = DEFAULT_GROUP;
    StringBuffer currentEntry = new StringBuffer( 8000 );
    String currentEntryName = null;
    while( ( line = br.readLine() ) != null )
    {
      if( line.startsWith( "%%" ) )
      {
        //				System.out.println("Skipping comment " + line );
        continue;
      }
      if( line.startsWith( "---" ) )
      {
        if( currentEntryName == null )
        {
          throw new IOException( _( "Entry ends before the start at line " ) + br.getLineNumber() );
        }
        Vector v = (Vector)groups.get( group );
        HelpItem hitem;
        if( group.equals( "UNITS" ) )
        {
          hitem = (HelpItem)client.getRulesetManager().getRulesetUnit( currentEntryName );
        }
        else
        {
          if( group.equals( "IMPROVEMENTS" ) || group.equals( "WONDERS" ) )
          {
            hitem = (HelpItem)client.getRulesetManager().getRulesetBuilding( currentEntryName );
          }
          else
          {
            if( group.equals( "TECHS" ) )
            {
              hitem = (HelpItem)client.getRulesetManager().getRulesetTech( currentEntryName );
            }
            else
            {
              if( group.equals( "TERRAINS" ) )
              {
                hitem = new TerrainHelpItem( currentEntryName );
              }
              else
              {
                hitem = new TextHelpItem( currentEntryName );
              }
            }
          }
        }
        if( hitem == null )
        {
          System.out.println( "Extra help for non-existent " + group + " " + currentEntryName + " ignored" );
        }
        else
        {
          hitem.setHelpText( currentEntry.toString() );
          addSorted( v, hitem );
        }
        currentEntryName = null;
        currentEntry.setLength( 0 );
        continue;
      }
      if( line.startsWith( "#" ) )
      {
        if( currentEntryName != null )
        {
          throw new IOException( _( "Entry startss before the end of previous one at line " ) + br.getLineNumber() );
        }
        currentEntryName = line.substring( 1 ).trim();
        continue;
      }
      if( currentEntryName == null && line.startsWith( "@START_" ) )
      {
        if( group != DEFAULT_GROUP )
        {
          throw new IOException( _( "Nested group at line " ) + br.getLineNumber() );
        }
        group = line.substring( "@START_".length() ).trim();
        Vector v = new Vector();
        groups.put( group, v );
        groupsVector.add( group );
        continue;
      }
      if( line.startsWith( "@END_" ) )
      {
        if( !group.equals( line.substring( "@END_".length() ).trim() ) )
        {
          throw new IOException( _( "Non current group closed at line " ) + br.getLineNumber() );
        }
        group = DEFAULT_GROUP;
        continue;
      }
      currentEntry.append( line );
      currentEntry.append( '\n' );
    }
    */
  }
  private static void addSorted( Vector v, Object obj )
  {
    v.add( obj );
  }
  private static final String DEFAULT_GROUP = "TEXT";
  static class TextHelpItem implements HelpItem
  {
    String name;
    String text;
    public TextHelpItem( String aName ) 
    {
      name = aName;
    }
    public void setHelpText( String txt )
    {
      text = txt;
    }
    public JComponent getRenderer( HelpPanel help )
    {
      return help.getTextHelpPanel( name, text );
    }
    public String getHelpCategory()
    {
      return "TEXT";
    }
    public String getHelpName()
    {
      return name;
    }
    public String toString()
    {
      return name;
    }
  }
  static class TerrainHelpItem extends TextHelpItem
  {
    public TerrainHelpItem( String aName ) 
    {
      super( aName );
    }
    public String getHelpCategory()
    {
      return "TERRAINS";
    }
  }
  public void valueChanged( ListSelectionEvent e )
  {
    if( e.getSource() == groupList.getSelectionModel() )
    {
      DefaultListModel model = (DefaultListModel)entriesList.getModel();
      setValues( model, (Vector)groups.get( (String)groupList.getSelectedValue() ) );
      revalidate();
    }
    else
    {
      displayHelp( (HelpItem)entriesList.getSelectedValue() );
    }
  }
  private void displayHelp( HelpItem hitem )
  {
    mainPanel.removeAll();
    if( hitem != null )
    {
      mainPanel.add( hitem.getRenderer( this ) );
    }
    revalidate();
    mainPanel.repaint();
  }
  private static void setValues( DefaultListModel model, Vector v )
  {
    model.clear();
    for( int i = 0;i < v.size();i++ )
    {
      model.addElement( v.get( i ) );
    }
  }
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
}
