package org.freeciv.client.action;

import org.freeciv.client.MainWindow;
import org.freeciv.client.MenuDefinitions;
import org.freeciv.client.Client;
import org.freeciv.client.ui.util.ToggleActionMenuItem;
import org.freeciv.client.ui.util.ActionMenuItem;
import org.freeciv.client.action.ActionManager;
import org.freeciv.util.Localize;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.Action;

/**
 *
 *
 * @author Julian Rueth
 */
public final class MenuFactory{
  /**
   * Creates all the menus defined in MenuDefinitions
   *
   * @param client the current Client object
   * @param mainWindow the destination of the MenuBar
   */
  public static void createMenus(Client client, JFrame mainWindow)
  {
    JMenuBar jmb = new JMenuBar();
    for( int i = 0;i < MenuDefinitions.MENUS.length;i++ )
    {
      JMenu menu = new JMenu( _( (String) MenuDefinitions.MENUS[ i ][ 0 ] ) );
      for( int j = 1;j < MenuDefinitions.MENUS[ i ].length;j++ )
	{
	  if( MenuDefinitions.MENUS[ i ][ j ] == null )
	  {
	    menu.addSeparator();
	  }
	  else
	  {
	    AbstractClientAction act = client.getAction( (Class) MenuDefinitions.MENUS[ i ][ j ] );
	    JMenuItem jmi;
	    if( act instanceof AbstractToggleAction )
	    {
	      jmi = new ToggleActionMenuItem( (AbstractToggleAction)act );
	    }
	    else
	    {
	      jmi= new ActionMenuItem( (AbstractClientAction)act );
	    }
	    menu.add(jmi);
	  }
	}
      jmb.add( menu );
    }
    mainWindow.setJMenuBar( jmb );
  }
  private MenuFactory(){}
  private static String _( String txt )
  {
    return Localize.translate( txt );
  }
}
