/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Filesystem Browser addin for Oracle9i JDeveloper
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package oracle.jdevimpl.fsbrowser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import oracle.ide.Ide;
import oracle.ide.IdeAction;
import oracle.ide.ContextMenu;
import oracle.ide.addin.Addin;
import oracle.ide.addin.BaseController;
import oracle.ide.addin.Controller;
import oracle.ide.addin.Context;
import oracle.ide.addin.ContextMenuListener;
import oracle.ide.addin.UpdateMessage;
import oracle.ide.model.Element;
import oracle.ide.model.Locatable;
import oracle.ide.net.URLFileSystem;

/**
 * An addin that creates a node in the IDE navigator that can be used to 
 * browse the filesystem and perform file system operations.
 *
 * @author Brian.Duff@oracle.com
 */
public class FileSystemBrowser extends BaseController 
  implements Addin, ContextMenuListener
{
  private static final String NEW_FOLDER_ID = "FileSystemBrowser.NewFolder";
  private IdeAction m_newFolderAction;

  private static final String BROWSE_TO_DIRECTORY = "FileSystemBrowser.BrowseToDirectory";
  private IdeAction m_browseToDirectoryAction;

  private FileSystemElement m_fse;


  private void registerFileSystemNode()
  {
    // The IDE persists any top level nodes, which is a bit weird. Before we
    // construct our node and add it, we need to check to see if it already 
    // exists.
    Iterator systemKids = Ide.getSystem().getChildren();
    while ( systemKids.hasNext() )
    {
      Object o = systemKids.next();
      if ( o instanceof FileSystemElement )
      {
        m_fse = (FileSystemElement) o;
        return;
      }
    }
    // Ok, this is the first time the addin has ever been initialized. 
    // construct a new FileSystemElement and add it to system.
    m_fse = new FileSystemElement();
    Ide.getSystem().add( m_fse );
    UpdateMessage.fireChildAdded( Ide.getSystem(), m_fse );
  }

  private void createMenus()
  {
    m_newFolderAction = IdeAction.get(
      Ide.newCmd( NEW_FOLDER_ID ), "New Package or Directory...", new Integer( 0 )
    );
    m_newFolderAction.setController( this );

    m_browseToDirectoryAction = IdeAction.get(
      Ide.newCmd( BROWSE_TO_DIRECTORY ), "Browse to Directory", new Integer( 0 )
    );
    m_browseToDirectoryAction.setController( this );

    // Listen for context menu popups over nodes we're interested in
    Ide.getNavigatorManager().addContextMenuListener(
      this, FileSystemFolderElement.class
    );

    // Override the View->Refresh menu item. The chained controller 
    // installs a new controller into the action, but keeps a reference to
    // the old controller so that it can be used if this controller doesn't
    // handle the context.
    IdeAction refreshAction = IdeAction.find( Ide.REFRESH_CMD_ID );
    if ( refreshAction != null )
    {
      refreshAction.setController( new ChainedController( refreshAction, this ) );
    }
  }
  
// Addin interface

  public void initialize()
  {
    registerFileSystemNode();
    createMenus();
  }

  public boolean canShutdown()
  {
    return true;
  }

  public void shutdown()
  {
  }

  public float ideVersion()
  {
    return Ide.getVersion();
  }

  public float version()
  {
    return 0.2f;
  }


// ContextMenuListener interface

  /**
   * Called just before the context menu is popping up.
   *
   * @param context the current view context.  
   */
  public void poppingUp(ContextMenu popup)
  {
    if ( popup.getContext() != null)
    {
      Element e = popup.getContext().getElement();
      if ( e instanceof Locatable)
      {
        popup.add( popup.createMenuItem( m_browseToDirectoryAction ) );
      }
    
      if ( popup.getContext().getElement() instanceof FileSystemFolderElement &&
        !(popup.getContext().getElement() instanceof FileSystemElement) )
      {
        popup.add( popup.createMenuItem( m_newFolderAction ) );
        popup.addSeparator();
      }

      if ( popup.getContext().getElement() instanceof FileSystemElement ||
           popup.getContext().getElement() instanceof FileSystemFolderElement )
      {
        IdeAction refresh = IdeAction.find( Ide.REFRESH_CMD_ID );
        refresh.setEnabled( true );
        popup.add( popup.createMenuItem( refresh ) );
        
      }
    }
  }

  public void poppingDown(ContextMenu popup)
  {

  }
  
  /**
   * Called when the user double clicks on an item that has a popup menu.
   * Only one listener should return true from this menu.
   *
   * @param context the current context
   */
  public boolean handleDefaultAction(Context context)
  {
    return false;
  }

// Overrides from BaseController

  public boolean handleEvent( IdeAction action, Context ctx )
  {
    if ( action == m_newFolderAction )
    {
      if ( ctx != null && ctx.getElement() instanceof FileSystemFolderElement )
      {
        ((FileSystemFolderElement) ctx.getElement()).createNewFolder();
        return true;
      }
    }
    else if ( action ==  m_browseToDirectoryAction )
    {
      if ( ctx != null && ctx.getElement() instanceof Locatable )
      {
        URL u = URLFileSystem.getParent(((Locatable)ctx.getElement()).getURL());
        ArrayList entries = new ArrayList();
        while ( true )
        {
          entries.add( u );
          u = URLFileSystem.getParent( u );
          if ( u == null )
          {
            break;
          }
        }
        // we now have a list of urls to traverse, the first should identify
        // a top level node in the filesystem tree. ** this may not work
        // for JAR files **
        FileSystemFolderElement current = m_fse;
        while ( entries.size() > 0 )
        {
          URL theURL = (URL) entries.get( 0 );
          Iterator kids = current.getChildren();
          while ( kids.hasNext() )
          {
            FileSystemFolderElement child = (FileSystemFolderElement)kids.next();

            if ( child.getURL().equals( theURL ) )
            {
              current = child;
              entries.remove( 0 );
            }
          }
        }
        
      }
    }
    else if ( action == IdeAction.find( Ide.REFRESH_CMD_ID ) )
    {
      if ( ctx != null && ctx.getElement() instanceof AbstractLazyElement )
      {
        ((AbstractLazyElement)ctx.getElement()).refresh();
        return true;
      }
    }
    return super.handleEvent( action, ctx );
  }

  public boolean update( IdeAction action, Context ctx )
  {
    if ( ctx != null )
    {

      if ( ctx.getElement() instanceof FileSystemFolderElement )
      {
        if ( action == IdeAction.find( Ide.REFRESH_CMD_ID ) )
        {
          action.setEnabled( true );
          return true;
        }
      }
      else if ( ctx.getElement() instanceof Locatable )
      {
        action.setEnabled( 
          "file".equals(((Locatable)ctx.getElement()).getURL().getProtocol()) &&
          !(ctx.getElement() instanceof AbstractLazyElement)
        );
        return true;
      }
    }

    return super.update( action, ctx );
  }
}