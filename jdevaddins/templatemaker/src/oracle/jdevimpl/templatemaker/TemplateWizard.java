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
 * The Original Code is TemplateMaker addin for Oracle9i JDeveloper
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package oracle.jdevimpl.templatemaker;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;

import oracle.ide.Ide;
import oracle.ide.addin.Context;
import oracle.ide.addin.UpdateMessage;
import oracle.ide.addin.VetoableMessage;
import oracle.ide.addin.Wizard;
import oracle.ide.controls.JEWTMessageDialog;
import oracle.ide.exception.ChangeVetoException;
import oracle.ide.model.Locatable;
import oracle.ide.model.Node;
import oracle.ide.model.NodeFactory;
import oracle.ide.model.PackageFolder;
import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;
import oracle.ide.net.URLPath;
import oracle.ide.resource.IdeIcons;
import oracle.ide.util.MenuSpec;

import oracle.jdeveloper.model.JProject;

/**
 * The "wizard" (i.e. gallery dialog) that is used to instantiate a template
 *
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.3 $
 */
class TemplateWizard implements Wizard
{
  private final URL m_url;
  private final String m_name;
  private TemplateCaster m_caster;

  TemplateWizard( final URL u, final String name )
  {
    m_url = u;
    m_name = name;
  }

  URL getURL()
  {
    return m_url;
  }

  /**
   * Set the object responsible for casting a template into a new file
   *
   * @param caster the caster
   */
  void setTemplateCaster( final TemplateCaster caster )
  {
    m_caster = caster;
  }

  /**
   * Utility to display an alert in the neat OLAF style
   */
  private void alert( final String message )
  {
    JEWTMessageDialog md = JEWTMessageDialog.createMessageDialog(
      Ide.getMainWindow(), Ide.getProgramName(), 
      JEWTMessageDialog.TYPE_ALERT
    );
    md.setButtonMask( md.getButtonMask() ^ md.BUTTON_HELP );
    md.setMessageText( message );
    md.runDialog();
  }  

  /**
   * Open a URL into the IDE, creating a node for it in the navigator and
   * optionally adding it to the project in the specified context.
   *
   * @param url the URL to open
   * @param context the context to use to retrieve the project.
   * @param addToProject if true, the node will be added to the project
   *  in the context
   *
   * @return the created node if any.
   */
  private Node openIntoIDE( final URL url, final Context context,
    final boolean addToProject )
  {
    Node node = NodeFactory.find( url );
    if ( node == null )
    {
      // It's a new node
      try
      {
        node = NodeFactory.findOrCreate( url );
      }
      catch (IllegalAccessException ille )
      {
        alert( "Failed to open "+url );
      }
      catch (InstantiationException ie )
      {
        alert( "Failed to open "+url );
      }
      if ( node.isNew() )
      {
        node.markDirty( true );
      }

      if ( addToProject && context.getProject() != null &&
        context.getProject().canAdd( node ) )
      {
        List addedItems = new ArrayList( 1 );
        addedItems.add( node );
        try
        {
          VetoableMessage.fireCanAddChildren( context.getProject(), addedItems );
          context.getProject().addToProject( addedItems );
          if ( addedItems.size() > 0 )
          {
            UpdateMessage.fireChildrenAdded( context.getProject(), addedItems );
          }
        }
        catch (ChangeVetoException cve)
        {
          // Someone vetoed the add. So don't do it.
        }
      }

      // Update the File->Reopen menu
      Ide.getFileOpenHistory().updateFileHistory( node.getURL() );

      // Open the file in an editor window.
      if ( addToProject )
      {
        Ide.getEditorManager().openDefaultEditorInFrame( node );
      }
      else
      {
        Ide.getEditorManager().openDefaultEditorInFrameExternal( node.getURL() );
      }
    }
    return node;    
  }

  /**
   * Get the name of the package the specified file is in, if any
   *
   * @param u the URL of a file
   * @param prj a project
   * @return the package this url is in, or an empty string.
   */
  private String getPackage( final URL u, final JProject prj )
  {
    Iterator i = prj.getSourcePath().iterator();
    URL parent = URLFileSystem.getParent( u );
    while ( i.hasNext() )
    {
      URL thisURL = (URL) i.next();

      if ( URLFileSystem.isBaseURLFor( thisURL, parent ) )
      {
        String pkg = URLFileSystem.toRelativeSpec( parent, thisURL );
        return pkg.replace( '/', '.' );
      }
    }

    return "";
    
  }

// ---------------------------------------------------------------------------
// Wizard implementation
// ---------------------------------------------------------------------------

  public String getName()
  {
    return m_name;
  }

  public String getMenuLabel()
  {
    return null;
  }

  public MenuSpec getMenuSpecification()
  {
    return null;
  }

  public boolean isAvailable(final Context context)
  {
    return true;
  }

  public String toString()
  {
    return getName();
  }

  public Icon getIcon()
  {
    // Hmm. Would be nice if we could get the type icon here.
    return IdeIcons.getIcon( IdeIcons.FILE_NODE_ICON );
  }

  public boolean invoke( final Context ctx, final String[] args )
  {
    if ( m_caster == null )
    {
      alert( "Internal error: no template caster has been set" );
      return true;
    }

    UseTemplateDialog utd = new UseTemplateDialog();
    utd.setFileName( URLFileSystem.getFileName( m_url ) );
    utd.setTemplateName( m_name );

    // If the file's extension is .java and the context project is a JProject,
    // invoke the utd in package mode.

    utd.setPackageMode( 
      ".java".equals( URLFileSystem.getSuffix( m_url ) ) &&
      ctx.getProject() instanceof JProject
    );

    if ( ctx.getProject() != null )
    {
      utd.setProjectName(
        URLFileSystem.getFileName( ctx.getProject().getURL() )
      );
      utd.setAddToProject( true );
    }

    // Try to guess a sensible path or package for the file.


    // If the context is a package node, we use that
    if ( ctx.getElement() != null && ctx.getElement() instanceof PackageFolder )
    {
      if ( utd.isPackageMode() )
      {
        utd.setPackageName( ctx.getElement().getLongLabel() );  // Check this
      }
      else
      {
        utd.setDirectoryName(
          URLFileSystem.getPlatformPathName( 
            ((PackageFolder) ctx.getElement()).getURL()
          )
        );
      }
    }
    // Use the same directory as an existing element...
    else if ( ctx.getElement() != null && ctx.getElement() instanceof Locatable )
    {
      if ( utd.isPackageMode() )
      {
        utd.setPackageName( getPackage( 
          ((Locatable)ctx.getElement()).getURL(),
          (JProject)ctx.getProject()
        ));
      }
      else if ( !utd.isPackageMode() )
      {
        utd.setDirectoryName(
          URLFileSystem.getPlatformPathName(
            URLFileSystem.getParent(
              ((Locatable) ctx.getElement()).getURL()
            )
          )
        );
      }
    }
    // Try the first component of the project source path
    else if ( ctx.getProject() != null )
    {
      if ( ctx.getProject() instanceof JProject && !utd.isPackageMode() )
      {
        utd.setDirectoryName(
          URLFileSystem.getPlatformPathName(
            ((JProject)ctx.getProject()).getSourcePath().getEntries()[0]
          )
        );
      }
    }
    // Try the workspace directory
    else if ( ctx.getWorkspace() != null && !utd.isPackageMode() )
    {
      utd.setDirectoryName(
        URLFileSystem.getPlatformPathName(
          URLFileSystem.getParent(
            ctx.getWorkspace().getURL()
          )
        )
      );
    }
    // Failing all that, use the IDE mywork directory
    else if ( !utd.isPackageMode() )
    {
      utd.setDirectoryName(
        Ide.getWorkDirectory()
      );
    }

    // Still not set the package? Ah well, let's try the project default..
    if ( utd.isPackageMode() && utd.getPackageName().trim().length() == 0 )
    {
      utd.setPackageName( ((JProject)ctx.getProject()).getDefaultPackage() );
    }

    if ( utd.runDialog( Ide.getMainWindow() ) )
    {
      URL directoryURL;
      if ( utd.isPackageMode() )
      {
        directoryURL = URLFactory.newDirURL(
          ((JProject)ctx.getProject()).getSourcePath().getEntries()[0],
          utd.getPackageName().replace( '.', '/' )
        );
      }
      else
      {
        directoryURL = URLFactory.newURL( utd.getDirectoryName() );
      }
      // If the user missed the extension off the filename, we can default
      // it based on the extension of the template.
      String fileName = utd.getFileName();
      int lastDot = fileName.lastIndexOf( '.' );
      if ( lastDot == -1 )
      {
        fileName = fileName + URLFileSystem.getSuffix( m_url );
      }
      URL u = URLFactory.newURL( directoryURL, fileName );

      // We should really do this validation in response to the click on the
      // ok button rather than after the dialog is gone...
      if ( URLFileSystem.exists( u ) )
      {
        alert(
          "A file already exists in the specified location."
        );
        return true;
      }

      // Use the caster to cast the template
      try
      {
        m_caster.castTemplate( ctx, m_url, u );      
        openIntoIDE( u, ctx, utd.isAddToProject() );

      }
      catch ( TemplateCastFailedException tcf)
      {
        alert( tcf.getMessage() + 
          ((tcf.getBaseCause() != null) ? "\n"+tcf.getBaseCause().getMessage() : ""));
      }
    }

    return true;
  }
}