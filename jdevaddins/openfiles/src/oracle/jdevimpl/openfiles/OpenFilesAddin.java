package oracle.jdevimpl.openfiles;

import oracle.ide.Ide;
import oracle.ide.IdeAction;
import oracle.ide.MainWindow;
import oracle.ide.addin.Addin;
import oracle.ide.addin.BaseController;
import oracle.ide.addin.Context;
import oracle.ide.addin.Controller;
import oracle.ide.docking.DockStation;
import oracle.ide.resource.IdeIcons;

/**
 * The OpenFiles addin creates a dockable window in the IDE which can be used
 * for quick access to files which are open in an editor window.
 *
 * @author Brian.Duff@oracle.com
 */
public class OpenFilesAddin extends BaseController implements Addin
{
  private OpenFiles m_openFiles;
  private OpenFilesDockable m_dockable;
  private IdeAction m_viewAction;

  private void createMenu()
  {
    // Register a menu item for the View menu.
    m_viewAction = IdeAction.get(
      Ide.newCmd( "Open Files Window" ),
      null,
      "Open Files Window",
      MainWindow.ACTION_CATEGORY_VIEW,
      new Integer('O'),
      IdeIcons.getIcon( IdeIcons.BLANK_ICON ),
      null,
      true
    );

    m_viewAction.putValue( IdeAction.TOGGLES, Boolean.TRUE );
    m_viewAction.setController( this );

    Ide.getMenubar().insert(
      Ide.getMenubar().createMenuItem( m_viewAction ),
      MainWindow.View,
      MainWindow.Toolbar
    );
  }

// Controller implementation

  public Controller supervisor()
  {
    return null;
  }

  public boolean handleEvent( IdeAction action, Context context )
  {
    if ( action == m_viewAction )
    {
      if ( m_dockable.isVisible() )
      {
        Ide.getDockStation().setDockableVisible( m_dockable, false );
      }
      else
      {
        Ide.getDockStation().dock( m_dockable, DockStation.EAST, false );
        Ide.getDockStation().setDockableVisible( m_dockable, true );
      }
      return true;
    }

    return false;
  }

  public boolean update( IdeAction action, Context context )
  {
    if ( action == m_viewAction )
    {
      action.setState( m_dockable.isVisible() );
      return true;
    }
    return false;
  }

  public void checkCommands( Context context, Controller controller )
  {
    // NOOP
  }
  


// Addin implementation

  public boolean canShutdown()
  {
    return true;
  }

  public float ideVersion()
  {
    return Ide.getVersion();
  }

  public float version()
  {
    return 1.0f;
  }

  public void initialize()
  {
    m_openFiles = new OpenFiles();

    // TODO: We should only add this while the dockable is on screen
    Ide.getEditorManager().addEditorListener( 
      m_openFiles
    );
    m_dockable = new OpenFilesDockable( m_openFiles );
    Ide.getDockStation().registerDockableFactory(
      OpenFilesDockable.UNIQUE_NAME, 
      m_dockable
    );
    Ide.getDockStation().dock( m_dockable, DockStation.EAST, true );    
    Ide.getDockStation().setDockableVisible(
      m_dockable, true
    );

    createMenu();

  }

  public void shutdown()
  {
    // Good practise to clean up everything we did in
    // initialize() and free up references
    Ide.getEditorManager().removeEditorListener(
      m_openFiles
    );
    m_openFiles = null;
    m_dockable = null;
  }
}