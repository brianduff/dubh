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

import java.awt.MediaTracker;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import oracle.ide.Ide;
import oracle.ide.model.DefaultFolder;
import oracle.ide.net.URLPath;

import oracle.ide.model.ElementAttributes;

import oracle.jdeveloper.library.JDK;
import oracle.jdeveloper.library.JLibraryManager;
import oracle.jdeveloper.library.JLibrary;
import oracle.jdeveloper.library.JPaths;


/**
 * The JavadocFolder is a defaultfolder subclass that 
 */
public final class JavadocFolder extends DefaultFolder
{
  private boolean m_expanded = false;
  static final Icon s_icon;

  static
  {
    ImageIcon icon = new ImageIcon(
      JavadocFolder.class.getResource( "javadocfolder.png" )
    );

    if ( icon.getImageLoadStatus() == MediaTracker.COMPLETE )
    {
      s_icon = icon;
    }
    else
    {
      s_icon = null;
    }
  }

  public JavadocFolder()
  {
    getAttributes().set( ElementAttributes.NAVIGABLE );


  }

  private void addChild( final URLPath docPath, String name )
  {

    if ( docPath != null && docPath.size() == 1 )
    {
      add( new JavadocNode( docPath.getEntries()[0], name ) );
    }
    else if ( docPath != null && docPath.size() > 1 )
    {
      add( new LibraryNode( name, docPath ) );
    }
  }

  private void populate()
  {
    // Initialize the contents of the folder based on the docpath of all
    // libraries and JDKs.
    Iterator allLibs = JLibraryManager.getLibraries().iterator();
    while ( allLibs.hasNext() )
    {
      JLibrary lib = (JLibrary) allLibs.next();
      addChild( lib.getDefaultDocPath(), lib.getName() );
    }

    // Add nodes for defined JDKs.
    Iterator jdkIterator = JLibraryManager.getJDKs().iterator();
    while ( jdkIterator.hasNext() )
    {
      JDK jdk = (JDK) jdkIterator.next();
      addChild( jdk.getDocPath(), "Java Version "+jdk.getName() );
    }
    
  }

// ----------------------------------------------------------------------------
// DefaultFolder overrides
// ----------------------------------------------------------------------------

  public Iterator getChildren()
  {
    // Delay population of the children of this node until it has been expande
    // by the user.
    if ( !m_expanded )
    {
      populate();
      m_expanded = true;
    }
    return super.getChildren();
  }

  public String getShortLabel()
  {
    return "Javadoc";
  }

  public Icon getIcon()
  {
    if ( s_icon == null )
    {
      return super.getIcon();
    }
    return s_icon;
  }
}