package org.freeciv.client;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.freeciv.net.*;
import org.freeciv.tile.Coords;
import org.freeciv.tile.FlashingIcon;
import org.freeciv.tile.TileMap;
import org.gjt.abies.VectorListModel;
public class City extends PktCityInfo implements FlashingIcon
{
  boolean visible = true;
  Client client;
  Icon icon;
  CityDialog cityDialog;
  Icon flag;
  public City() 
  {
    
  }
  public City( Client c ) 
  {
    super();
    client = c;
  }
  public City( Client c, InStream in ) 
  {
    super();
    client = c;
    receive( in );
  //icon = client.getCityIcon(); 
  //flag = client.getFlagForPlayer(owner);
  }
  public void receive( InStream in )
  {
    super.receive( in );
    if( cityDialog != null )
    {
      cityDialog.civUpdate();
    }
  }
  public void setVisible( boolean aVisible )
  {
    visible = aVisible;
  }
  public void paintIcon( Component c, Graphics g, int x, int y )
  {
    if( visible )
    {
      flag.paintIcon( c, g, x, y );
      icon.paintIcon( c, g, x, y );
      g.setColor( Color.white );
      g.drawString( name, x, y + 10 ); // hack
    }
  }
  public int getIconWidth()
  {
    return icon.getIconWidth();
  }
  public int getIconHeight()
  {
    return icon.getIconHeight();
  }
  public void openCityDialog()
  {
    if( cityDialog == null )
    {
      cityDialog = new CityDialog( client, name );
      cityDialog.civUpdate();
    }
    cityDialog.show(); // bring to front
  }
  public void closeDialogs()
  {
    if( cityDialog != null )
    {
      cityDialog.setVisible( false );
      cityDialog.dispose();
      cityDialog = null;
    }
  }
  public boolean canBuild( PktRulesetUnit unit )
  {
    return false;
    /*
    if( unit.tech_requirement == Constants.A_LAST )
    {
      return false;
    }
    if( !client.getCurrentPlayer().inventions[ unit.tech_requirement ] )
    {
      return false;
    }
    return true;
    */
  }
  public boolean canBuild( PktRulesetBuilding bl )
  {
    /*
    if ( bl.tech_requirement == Constants.A_LAST )
    return false;
    if ( !client.getCurrentPlayer().inventions[bl.tech_requirement] )
    return false;
    if ( improvements[bl.id] )
    return false;
    if ( bl.is_wonder && (client.getGameInfo().global_wonders[bl.id] != 0))
    return false;
    return true;
    */
    return false; // fixme
  }
  class CityDialog extends JInternalFrame implements InternalFrameListener
  {
    TileMap tm = new TileMap( (short)5, (short)5, getIconWidth(), getIconHeight(), false, false );
    CivMap bigMap;
    JList choiceList;
    JList alreadyBuiltList;
    VectorListModel alreadyBuiltModel = new VectorListModel();
    VectorListModel buildListModel = new VectorListModel();
    JToggleButton a, u, b, w;
    Object prevButton;
    JLabel currentProd = new JLabel( "None" );
    JPanel cityData = new JPanel();
    JLabel foodData = new JLabel( "None" );
    PopulationDisplay population;
    public CityDialog( Client c, String name ) 
    {
      super( name, false, true, false, true );
      setContentPane( new JPanel() );
      reshape( 200, 200, 1, 1 ); // later will be packed
      //c.desktop.add(this,c.CITY_DIALOG_LAYER);
      bigMap = null; // TODOc.getMap();
      setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
      for( int x = 0;x < 5;x++ )
      {
        for( int y = 0;y < 5;y++ )
        {
          ArrayList l = new ArrayList( C_MAX_LAYER );
          for( int i = 0;i < C_MAX_LAYER;i++ )
          {
            l.add( null );
          }
          tm.setTileList( x, y, l );
        }
      }
      tm.setMinimumSize( new Dimension( 5 * getIconWidth(), 5 * getIconHeight() ) );
      tm.setPreferredSize( new Dimension( 5 * getIconWidth(), 5 * getIconHeight() ) );
      tm.setMaximumSize( new Dimension( 5 * getIconWidth(), 5 * getIconHeight() ) );
      tm.setBackground( null );
      tm.setVoidColor( null );
      tm.setGridlineColor( null );
      tm.addMouseListener( new MouseAdapter() 
      {
        public void mouseClicked( MouseEvent e )
        {
          
        

        //Coords p = tm.toTileCoordinates(e.getX(),e.getY());
        //if ( city_map[p.x+p.y*5] == Constants.C_TILE_WORKER )
        //	client.commandMakeSpecialist(City.this,p.x,p.y);
        //else if ( city_map[p.x+p.y*5] == Constants.C_TILE_EMPTY )
        //	client.commandMakeWorker(City.this,p.x,p.y);
        }
      } );
      getContentPane().setLayout( new BorderLayout() );
      getContentPane().add( BorderLayout.CENTER, tm );
      JPanel right = new JPanel();
      right.setLayout( new BorderLayout() );
      JPanel kindButtonsPanel = new JPanel();
      kindButtonsPanel.setLayout( new GridLayout( 1, 4, 4, 4 ) );
      ButtonGroup buttons = new ButtonGroup();
      a = new JToggleButton( "A" );
      u = new JToggleButton( "U" );
      b = new JToggleButton( "B" );
      w = new JToggleButton( "W" );
      buttons.add( a );
      buttons.add( u );
      buttons.add( b );
      buttons.add( w );
      switch( defaultBL )
      {
        case BL_UNIT:
          u.setSelected( true );
          break;
        
        case BL_BUILDING:
          b.setSelected( true );
          break;
        
        case BL_WONDER:
          w.setSelected( true );
          break;
        
        default:
          a.setSelected( true );
          break;
      }
      a.addActionListener( new ActionListener() 
      {
        public void actionPerformed( ActionEvent e )
        {
          updateBuildList( BL_ALL );
          prevButton = a;
        }
      } );
      u.addActionListener( new ActionListener() 
      {
        public void actionPerformed( ActionEvent e )
        {
          if( prevButton == u )
          {
            a.doClick();
          }
          else
          {
            updateBuildList( BL_UNIT );
            prevButton = u;
          }
        }
      } );
      b.addActionListener( new ActionListener() 
      {
        public void actionPerformed( ActionEvent e )
        {
          if( prevButton == b )
          {
            a.doClick();
          }
          else
          {
            updateBuildList( BL_BUILDING );
            prevButton = b;
          }
        }
      } );
      w.addActionListener( new ActionListener() 
      {
        public void actionPerformed( ActionEvent e )
        {
          if( prevButton == w )
          {
            a.doClick();
          }
          else
          {
            updateBuildList( BL_WONDER );
            prevButton = w;
          }
        }
      } );
      kindButtonsPanel.add( u );
      kindButtonsPanel.add( b );
      kindButtonsPanel.add( w );
      right.add( BorderLayout.NORTH, kindButtonsPanel );
      choiceList = new JList( buildListModel );
      choiceList.setPrototypeCellValue( "99/99/99 XXXXXXXXXXX 999" );
      choiceList.addMouseListener( new MouseAdapter() 
      {
        public void mouseClicked( MouseEvent e )
        {
          if( e.getClickCount() == 2 )
          {
            Object obj = choiceList.getSelectedValue();
            if( obj == null )
            {
              return ;
            }
            if( shield_stock > 0 )
            {
              boolean scrap = false;
              if( obj instanceof PktRulesetUnit && !is_building_unit )
              {
                scrap = true;
              }
              else
              {
                if( obj instanceof PktRulesetBuilding )
                {
                  if( is_building_unit )
                  {
                    scrap = true;
                  }
                //else  if (((PktRulesetBuilding)obj).is_wonder !=
                //	client.rulesetBuildingArr[currently_building].is_wonder)
                //{
                //	scrap = true;
                //}
                }
              }
              if( scrap )
              {
                int result = JOptionPane.showInternalConfirmDialog( CityDialog.this, _( "You will be penalized by cutting prod in half\nDo you want to change ?" ), _( "Scrap production" ), JOptionPane.YES_NO_OPTION );
                // request focus to avoid strange togglebutton behaviour
                CityDialog.this.show();
                if( result != JOptionPane.YES_OPTION )
                {
                  return ;
                }
              }
            }
          //if ( obj instanceof PktRulesetUnit )
          //	client.commandChangeCityProduction(City.this,(PktRulesetUnit)obj);
          //else
          //	client.commandChangeCityProduction(City.this,(PktRulesetBuilding)obj);
          }
        }
      } );
      choiceList.setCellRenderer( choiceListRenderer );
      alreadyBuiltList = new JList( alreadyBuiltModel );
      alreadyBuiltList.setPrototypeCellValue( "XXXXXXXXXXXXXXX 999" );
      alreadyBuiltList.setCellRenderer( alreadyBuiltRenderer );
      JScrollPane jsc = new JScrollPane( choiceList );
      right.add( BorderLayout.CENTER, jsc );
      right.add( BorderLayout.SOUTH, currentProd );
      population = new PopulationDisplay( client );
      population.setCitySize( size );
      cityData.setLayout( new BoxLayout( cityData, BoxLayout.Y_AXIS ) );
      cityData.add( population );
      cityData.add( foodData );
      getContentPane().add( BorderLayout.EAST, right );
      getContentPane().add( BorderLayout.WEST, new JScrollPane( alreadyBuiltList ) );
      getContentPane().add( BorderLayout.NORTH, cityData );
      updateBuildList( defaultBL );
      addInternalFrameListener( this );
      pack();
    }
    public void civUpdate()
    {
      for( int ax = 0;ax < 5;ax++ )
      {
        int starty = 0;
        int endy = 5;
        if( ( ax == 0 ) || ( ax == 4 ) )
        {
          starty = 1;
          endy = 4;
        }
        for( int ay = starty;ay < endy;ay++ )
        {
          List to = tm.getTileList( ax, ay );
          List from = bigMap.getTileList( x + ax - 2, y + ay - 2 );
          to.set( C_TERRAIN_LAYER, from.get( bigMap.TERRAIN_LAYER ) );
          to.set( C_TERRAIN_OVERLAY_LAYER, from.get( bigMap.TERRAIN_OVERLAY_LAYER ) );
          to.set( C_RESOURCE_LAYER, from.get( bigMap.RESOURCE_LAYER ) );
          if( ( ax == 2 ) && ( ay == 2 ) )
          {
            to.set( C_WORKER_LAYER, City.this );
          }
          //else if ( city_map[ax+ay*5] == Constants.C_TILE_WORKER )
          //	to.set(C_WORKER_LAYER,client.getWorkerIcon()); //toChange
          else
          {
            to.set( C_WORKER_LAYER, null );
          }
        }
      }
      tm.repaint();
      if( is_building_unit )
      {
        
      

      //PktRulesetUnit unit = client.rulesetUnitArr[currently_building];
      //currentProd.setText(_("Prod: ") + unit.name + " " +shield_stock + "/" + unit.build_cost);
      }
      else
      {
        
      

      //PktRulesetBuilding bld = client.rulesetBuildingArr[currently_building];
      //currentProd.setText(_("Prod: ") + bld.name + " " +shield_stock + "/" + bld.build_cost);
      }
      ArrayList al = new ArrayList( 100 );
      for( int i = 0;i < Constants.B_LAST;i++ )
      {
        
      

      //	if (improvements[i])
      //		al.add(client.rulesetBuildingArr[i]);
      }
      Object[] arr = al.toArray();
      java.util.Arrays.sort( arr, new Comparator() 
      {
        public int compare( Object o1, Object o2 )
        {
          //-1 o1 < o2
          PktRulesetBuilding b1 = (PktRulesetBuilding)o1;
          PktRulesetBuilding b2 = (PktRulesetBuilding)o2;
          if( b1.is_wonder && !b2.is_wonder )
          {
            return 1;
          }
          else
          {
            if( !b1.is_wonder && b2.is_wonder )
            {
              return -1;
            }
          }
          return b1.name.compareTo( b2.name );
        }
      } );
      alreadyBuiltModel.compareAndReplace( java.util.Arrays.asList( arr ) );
      foodData.setText( _( "Food prod:" ) + food_prod + _( ", surp:" ) + food_surplus + _( ", stock:" ) + food_stock );
      //population.setPeople(size, ppl_happy,ppl_content,ppl_unhappy, ppl_elvis,
      //	ppl_scientist, ppl_taxman);
      updateBuildList( defaultBL );
    }
    public void updateBuildList( int mode )
    {
      defaultBL = mode;
      ArrayList v = new ArrayList( 100 );
      if( ( mode & ( BL_WONDER | BL_BUILDING ) ) != 0 )
      {
        for( int i = 0;i < Constants.B_LAST;i++ )
        {
          
        

        //	PktRulesetBuilding building = client.rulesetBuildingArr[i];
        //	if ( canBuild(building) )
        //	{
        //		if ( building.is_wonder && ((mode&BL_WONDER) != 0) )
        //			v.add(building);
        //		else if ( !building.is_wonder && ((mode&BL_BUILDING) != 0) )
        //			v.add(building);
        //	}
        }
      }
      if( ( mode & BL_UNIT ) != 0 )
      {
        for( int i = 0;i < Constants.U_LAST;i++ )
        {
          
        

        //		PktRulesetUnit unit = client.rulesetUnitArr[i];
        //		if ( canBuild(unit) )
        //		{
        //			v.add(unit);
        //		}
        }
      }
      Object[] arr = v.toArray();
      java.util.Arrays.sort( arr, new Comparator() 
      {
        public int compare( Object o1, Object o2 )
        {
          //-1 o1 < o2
          boolean b1Unit = o1 instanceof PktRulesetUnit;
          boolean b2Unit = o2 instanceof PktRulesetUnit;
          if( b1Unit && !b2Unit )
          {
            return 1;
          }
          else
          {
            if( !b1Unit && b2Unit )
            {
              return -1;
            }
          }
          if( b1Unit && b2Unit )
          {
            PktRulesetUnit u1 = (PktRulesetUnit)o1;
            PktRulesetUnit u2 = (PktRulesetUnit)o2;
            int dif = u1.build_cost - u2.build_cost;
            if( dif != 0 )
            {
              return dif;
            }
            return u1.name.compareTo( u2.name );
          }
          else
          {
            PktRulesetBuilding b1 = (PktRulesetBuilding)o1;
            PktRulesetBuilding b2 = (PktRulesetBuilding)o2;
            if( b1.is_wonder && !b2.is_wonder )
            {
              return 1;
            }
            else
            {
              if( !b1.is_wonder && b2.is_wonder )
              {
                return -1;
              }
            }
            int dif = b1.build_cost - b2.build_cost;
            if( dif != 0 )
            {
              return dif;
            }
            return b1.name.compareTo( b2.name );
          }
        }
      } );
      buildListModel.compareAndReplace( java.util.Arrays.asList( arr ) );
    }
    public void internalFrameOpened( InternalFrameEvent e )
    {
      
    }
    public void internalFrameClosing( InternalFrameEvent e )
    {
      
    }
    public void internalFrameClosed( InternalFrameEvent e )
    {
      cityDialog = null;
      //		client.desktop.remove(this); // is this needed ?
      //client.getMap().requestFocus();
    }
    public void internalFrameIconified( InternalFrameEvent e )
    {
      
    }
    public void internalFrameDeiconified( InternalFrameEvent e )
    {
      
    }
    public void internalFrameActivated( InternalFrameEvent e )
    {
      
    }
    public void internalFrameDeactivated( InternalFrameEvent e )
    {
      
    }
  }
  public static final int C_TERRAIN_LAYER = 0;
  public static final int C_TERRAIN_OVERLAY_LAYER = 1;
  public static final int C_RESOURCE_LAYER = 2;
  public static final int C_WORKER_LAYER = 3;
  public static final int C_MAX_LAYER = 4;
  public static final int BL_UNIT = 1;
  public static final int BL_BUILDING = 2;
  public static final int BL_WONDER = 4;
  public static final int BL_ALL = BL_UNIT | BL_BUILDING | BL_WONDER;
  public static int defaultBL = BL_ALL;
  private static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }
  public static ListCellRenderer alreadyBuiltRenderer = new AlreadyBuiltRenderer();
  public static Font plainFont = new Font( "sanserif", Font.PLAIN, 12 );
  public static Font boldFont = new Font( "sanserif", Font.BOLD, 12 );
  static class AlreadyBuiltRenderer extends DefaultListCellRenderer
  {
    public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
    {
      if( value instanceof String )
      {
        return super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
      }
      PktRulesetBuilding bld = (PktRulesetBuilding)value;
      /*			if ( bld.is_wonder )
      setFont(boldFont);
      else
      setFont(plainFont);
      */
      return super.getListCellRendererComponent( list, bld.name + " " + bld.upkeep, index, isSelected, cellHasFocus );
    }
  }
  public static ListCellRenderer choiceListRenderer = new ChoiceListRenderer();
  static class ChoiceListRenderer extends DefaultListCellRenderer
  {
    public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
    {
      if( value instanceof String )
      {
        return super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
      }
      else
      {
        if( value instanceof PktRulesetBuilding )
        {
          PktRulesetBuilding bld = (PktRulesetBuilding)value;
          return super.getListCellRendererComponent( list, bld.name + " " + bld.build_cost, index, isSelected, cellHasFocus );
        }
        else // unit
        {
          PktRulesetUnit unit = (PktRulesetUnit)value;
          return super.getListCellRendererComponent( list, "" + unit.attack_strength + "/" + unit.defense_strength + "/" + ( unit.move_rate / 3 ) + " " + unit.name + "   " + unit.build_cost, index, isSelected, cellHasFocus );
        }
      }
    }
  }
  static class PopulationDisplay extends JComponent
  {
    Client client;
    int size;
    ArrayList icons = new ArrayList();
    ArrayList types = new ArrayList();
    final static int N = 0;
    final static int E = 1;
    final static int T = 2;
    final static int S = 3;
    final static Integer Ni = new Integer( N );
    final static Integer Ei = new Integer( E );
    final static Integer Ti = new Integer( T );
    final static Integer Si = new Integer( S );
    public PopulationDisplay( Client c ) 
    {
      client = c;
      addMouseListener( new MouseAdapter() 
      {
        public void mouseClicked( MouseEvent e )
        {
          if( icons.size() < 1 )
          {
            return ;
          }
          int i = ( e.getX() - getInsets().left ) / ( (Icon)icons.get( 0 ) ).getIconWidth();
          if( i >= types.size() )
          {
            return ;
          }
          int type = ( (Integer)types.get( i ) ).intValue();
        }
      } );
    }
    public void setCitySize( int aSize )
    {
      size = aSize;
    }
    public void setPeople( int aSize, int happy, int content, int angry, int elvis, int sciencist, int taxmen )
    {
      boolean needValidate = false;
      if( size != aSize )
      {
        size = aSize;
        needValidate = true;
      }
      icons.clear();
      types.clear();
      while( happy-- > 0 )
      {
        //	icons.add(client.getHappyCitizenIcon(happy%2!=0));
        types.add( Ni );
      }
      while( content-- > 0 )
      {
        //	icons.add(client.getNormalCitizenIcon(content%2!=0));
        types.add( Ni );
      }
      while( angry-- > 0 )
      {
        //	icons.add(client.getAngryCitizenIcon(angry%2!=0));
        types.add( Ni );
      }
      while( elvis-- > 0 )
      {
        //	icons.add(client.getElvisIcon());
        types.add( Ei );
      }
      while( sciencist-- > 0 )
      {
        //	icons.add(client.getSciencistIcon());
        types.add( Si );
      }
      while( taxmen-- > 0 )
      {
        //	icons.add(client.getTaxmanIcon());
        types.add( Ti );
      }
      if( needValidate )
      {
        revalidate();
      }
      repaint();
    }
    protected void paintComponent( Graphics g )
    {
      Insets b = getInsets();
      int x = b.left;
      for( int i = 0;i < icons.size();i++ )
      {
        Icon icon = (Icon)icons.get( i );
        icon.paintIcon( null, g, x, b.top );
        x += icon.getIconWidth();
      }
    }
    public Dimension getMinimumSize()
    {
      return new Dimension( 100, 100 ); // ??
    //	Insets b = getInsets();
    //	Icon icon = client.getNormalCitizenIcon(false);
    //	return new Dimension(b.left+b.right+size*icon.getIconWidth(),
    //		b.top+b.bottom+icon.getIconHeight());
    }
    public Dimension getPreferredSize()
    {
      return getMinimumSize();
    }
    public Dimension getMaximumSize()
    {
      return getMinimumSize();
    }
  }
}
