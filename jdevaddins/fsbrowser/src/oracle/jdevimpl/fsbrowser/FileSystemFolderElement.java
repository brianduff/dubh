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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.lang.ref.SoftReference;
import javax.swing.Icon;
import javax.swing.UIManager;


import oracle.ide.Ide;
import oracle.ide.addin.UpdateMessage;
import oracle.ide.controls.JEWTMessageDialog;
import oracle.ide.model.Element;
import oracle.ide.model.NodeFactory;
import oracle.ide.model.Locatable;
import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

/**
 * This element represents a folder on a file system
 *
 * @author Brian.Duff@oracle.com
 */
public class FileSystemFolderElement extends AbstractLazyElement
  implements Locatable
{
  private URL m_url;
  private boolean m_isTopLevel;
  private boolean m_isInJar;

  private static SoftReference s_refFolderPrompt = 
    new SoftReference( new NewFolderPrompt() );

  public FileSystemFolderElement( URL dirURL )
  {
    m_url = dirURL;
  }

  protected FileSystemFolderElement()
  {
    m_url = null;
  }

  private final void setTopLevel( final boolean isTopLevel )
  {
    m_isTopLevel = isTopLevel;
  }

  final void setInJar( final boolean isInJar )
  {
    m_isInJar = isInJar;
  }

  void createNewFolder()
  {
    if ( s_refFolderPrompt.get() == null )
    {
      s_refFolderPrompt = new SoftReference( new NewFolderPrompt() );
    }

    NewFolderPrompt p = (NewFolderPrompt)s_refFolderPrompt.get();
    if ( p.showDialog() )
    {
      String folderName = p.getText();

      // Check for file separators and url separators
      if ( folderName.indexOf( '/' ) >= 0 || 
           folderName.indexOf( File.separatorChar ) >= 0 )
      {
        alert(
          "The directory name must not contain file or URL separator characters"
        );
        return;
      }


      StringTokenizer tokenizer = new StringTokenizer( folderName, "." );
      URL theURL = m_url;
      String parentName = getShortLabel();
      ArrayList urls = new ArrayList();
      while ( tokenizer.hasMoreTokens() )
      {
        String thisToken = tokenizer.nextToken();
        theURL = URLFactory.newURL( theURL, thisToken );

        if ( URLFileSystem.exists( theURL ) && !tokenizer.hasMoreTokens() )
        {
          if ( URLFileSystem.isDirectory( theURL ) )
          {
            alert(
              "A subdirectory named \""+thisToken+"\" already exists in the \""+parentName+"\" directory."
            );
          }
          else
          {
            alert(
              "A file named \""+thisToken+"\" already exists in the \""+parentName+"\" directory."
            );
          }
          return;
        }
        urls.add( theURL );
        parentName = thisToken;
      }

      // If we get here, we can go ahead and mkdirs theURL...

      if ( URLFileSystem.mkdirs( theURL ) )
      {
        // OK, we have new folders, create the nodes. We could just force
        // the node to reload, but we want to expand the whole package for
        // nice feedback...
        Iterator urlIterator = urls.iterator();
        FileSystemFolderElement currentElement = this;
        while ( urlIterator.hasNext() )
        { 
          URL u = (URL) urlIterator.next();
          FileSystemFolderElement newChild = new FileSystemFolderElement( u );
          currentElement.add( newChild );
          UpdateMessage.fireChildAdded( currentElement, newChild );

          currentElement = newChild;
        }
        
      }
      else
      {
        alert( 
          "Failed to create the specified package or directory."
        );
      }
    }
  }

  /**
   * Utility to display an alert in the neat OLAF style
   */
  private void alert( String message )
  {
    JEWTMessageDialog md = JEWTMessageDialog.createMessageDialog(
      Ide.getMainWindow(), Ide.getProgramName(), 
      JEWTMessageDialog.TYPE_ALERT
    );
    md.setButtonMask( md.getButtonMask() ^ md.BUTTON_HELP );
    md.setMessageText( message );
    md.runDialog();
  }

// AbstractLazyElement implementation

  protected void loadChildren()
  {
    URL[] u;
    if ( m_url == null )
    {
      u = URLFileSystem.listRoots();
    }
    else
    {
      u = URLFileSystem.list( m_url );
    }
    if ( u == null ) 
    {
      // OK. that was unexpected.
      return;
    }
    
    for ( int i=0; i < u.length; i++ )
    {
      URL thisURL = u[i];

      if ( URLFileSystem.isDirectoryPath( thisURL ) )
      {
        // If the url is a folder, we create a special node to represent it...      
        FileSystemFolderElement el = new FileSystemFolderElement( thisURL );
        el.setTopLevel( (m_url == null) );
        add( el );
      }
      else
      {
        try
        {
          String urlSuffix = URLFileSystem.getSuffix( thisURL );

          // Can't process jars inside other jar files (limitation of java)
          if ( !m_isInJar && 
               (".zip".equals( urlSuffix ) || ".jar".equals( urlSuffix ) ||
               ".war".equals( urlSuffix ) || ".ear".equals( urlSuffix ) ))
          {

            thisURL = URLFactory.newJarURL( thisURL, "" );

            add( new JARNavigatorNode( thisURL ) );
            
          }
          else
          {
            add( NodeFactory.findOrCreate( thisURL ) );
          }

        }
        catch ( IllegalAccessException illegal )
        {
          // Umm.
        }
        catch ( InstantiationException ine )
        {

        }
      }
    }
  }

// Displayable interface

  public String getShortLabel()
  {
    return URLFileSystem.getFileName( m_url );
  }

  public Icon getIcon()
  {
    if ( m_isTopLevel )
    {
      // Check for windoze?
      return UIManager.getIcon( "FileView.hardDriveIcon" );
    }

    return super.getIcon();
  }

  public String getToolTipText()
  {
    if ( m_url != null )
    {
      return m_url.toString();
    }

    return super.getToolTipText();
  }

// ----------------------------------------------------------------------------
// Locatable implementation
// ----------------------------------------------------------------------------

  public URL getURL()
  {
    return m_url;
  }

  public void setURL( final URL u )
  {
    m_url = u;
    refresh();
  }  
}