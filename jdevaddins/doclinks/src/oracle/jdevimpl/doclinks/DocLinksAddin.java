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
 * The Original Code is Doclinks addin for Oracle9i JDeveloper
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package oracle.jdevimpl.doclinks;

import java.net.URL;
import java.util.Iterator;

import oracle.ide.ContextMenu;
import oracle.ide.Ide;
import oracle.ide.IdeAction;
import oracle.ide.IdeContext;
import oracle.ide.addin.Addin;
import oracle.ide.addin.BaseController;
import oracle.ide.addin.Context;
import oracle.ide.addin.ContextMenuListener;
import oracle.ide.addin.UpdateMessage;
import oracle.ide.model.Element;
import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

// Naughty import
import oracle.jdevimpl.webapp.html.HtmlEditor;
// End naughty import

/**
 * IDE addin that provides convenient access to all javadoc defined in the 
 * docpath of libraries.
 * 
 * @author Brian.Duff@oracle.com
 */
public final class DocLinksAddin extends BaseController
  implements Addin, ContextMenuListener
{
  private JavadocFolder m_javadocFolder;
  private static final String SHOW_JAVADOC_ID = "ShowJavadoc";
  private IdeAction m_showJavadocAction;

  /**
   * Add the top level javadoc folder to the IDE
   */
  private void addJavadocFolder()
  {
    // The IDE persists any top level nodes, which is a bit weird. Before we
    // construct our node and add it, we need to check to see if it already 
    // exists.
    Iterator systemKids = Ide.getSystem().getChildren();
    while ( systemKids.hasNext() )
    {
      Element e = (Element) systemKids.next();
      if ( e instanceof JavadocFolder )
      {
        m_javadocFolder = (JavadocFolder) e;
        // No need to do anything.
        return;
      }
    }
    m_javadocFolder = new JavadocFolder();
    // Ok, this is the first time the addin has ever been initialized. 
    // construct a new FileSystemElement and add it to system.
    Ide.getSystem().add( m_javadocFolder );
    UpdateMessage.fireChildAdded( Ide.getSystem(), m_javadocFolder );    
  }

  private void createMenus()
  {
    m_showJavadocAction = IdeAction.get(
      Ide.newCmd( SHOW_JAVADOC_ID ), "Show Javadoc", new Integer( 0 )
    );
    m_showJavadocAction.setController( this );

    // Listen for context menu popups over nodes we're interested in
    Ide.getNavigatorManager().addContextMenuListener(
      this, JavadocNode.class
    );
  }

// ----------------------------------------------------------------------------
// Addin interface
// ----------------------------------------------------------------------------

  public void initialize()
  {
    addJavadocFolder();
    createMenus();
  }

  public float version()
  {
    return 0.1f;
  }

  public float ideVersion()
  {
    return Ide.getVersion();
  }

  public boolean canShutdown() 
  {
    return true;
  }

  public void shutdown()
  {
    
  }

// ----------------------------------------------------------------------------
// BaseController Overrides
// ----------------------------------------------------------------------------

  public boolean handleEvent( final IdeAction action, final Context context )
  {
    if ( action == m_showJavadocAction && context != null && 
         context.getElement() instanceof JavadocNode )
    {

      Ide.getEditorManager().openEditorInFrame(
        HtmlEditor.class, context
      );
          

      return true;
    }
    return super.handleEvent( action, context );
  }

  public boolean update( final IdeAction action, final Context context )
  {
    if ( action == m_showJavadocAction )
    {
      action.setEnabled( context != null && context.getElement() instanceof JavadocNode );
      return true;
    }
    return super.update( action, context );
  }

// ----------------------------------------------------------------------------
// ContextMenuListener implementation
// ----------------------------------------------------------------------------

  public boolean handleDefaultAction( final Context context )
  {
    if ( context != null && context.getElement() instanceof JavadocNode )
    {
      handleEvent( m_showJavadocAction, context );
      return true;
    }
    return false;
  }

  public void poppingUp( final ContextMenu menu )
  {
    
  }


  public void poppingDown( final ContextMenu menu )
  {
    
  }
}