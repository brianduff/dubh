package oracle.jdevimpl.openfiles;

import java.awt.Component;
import java.util.EventObject;

import oracle.ide.Ide;
import oracle.ide.IdeContext;
import oracle.ide.addin.AbstractPinnable;
import oracle.ide.addin.Context;
import oracle.ide.docking.Dockable;
import oracle.ide.docking.DockableFactory;
import oracle.ide.docking.DockableWindow;
import oracle.ide.docking.DockStation;
import oracle.ide.layout.ViewId;
import oracle.ide.addin.View;
import oracle.ide.addin.ViewAdapter;
import oracle.ide.addin.ViewEvent;

/**
 * A dockable window that displays nodes 4for all open files in the IDE
 *
 * @author Brian.Duff@oracle.com
 */
final class OpenFilesDockable extends DockableWindow 
  implements DockableFactory
{
  static final String UNIQUE_NAME =
    "OpenFilesDockable";    //NOTRANS

  private OpenFiles m_openFiles;
  
  public OpenFilesDockable( final OpenFiles openFiles )
  {
    super( Ide.getMainWindow(), UNIQUE_NAME );
    m_openFiles = openFiles;

    // This is only used to achieve the "greyed-selection" color in the list
    // when the dockable loses focus
    this.addViewListener(new ViewAdapter()
    {
      public final void viewActivated(ViewEvent e)
      {
        m_openFiles.setActive( true );
        getGUI().repaint();
      }

      public final void viewDeactivated(ViewEvent e)
      {    
        m_openFiles.setActive( false );
        getGUI().repaint();
      }

    });    
  }

// DockableWindow overrides



  public Component getGUI()
  {
    Component c = m_openFiles.getUI();
    
    return c;
  }

  public Context getContext( final EventObject e )
  {
    Context baseContext = m_openFiles.getContext();
    IdeContext ctx;    
    if ( baseContext == null )
    {
      ctx = new IdeContext( this, e );
    }
    else
    {
      ctx = new IdeContext( baseContext );
    }
    ctx.setView( this );
    return ctx;
  }

  public String getTabName()
  {
    return "Open Files";
  }

  public String getTitleName()
  {
    return getTabName();
  }

  public String getUniqueName()
  {
    return UNIQUE_NAME;
  }

  public int getMenuPreferredMnemonic()
  {
    return (int) 'F';
  }

// DockableFactory implementation

  public Dockable getDockable( final ViewId viewId )
  {
    if ( UNIQUE_NAME.equals( viewId.getName() ) )
    {
      return this;
    }
    return null;
  }

  public void install()
  {
    Ide.getDockStation().dock( this, DockStation.EAST, true );
  }
  

}