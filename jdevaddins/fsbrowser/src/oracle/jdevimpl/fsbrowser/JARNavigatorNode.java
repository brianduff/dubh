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

import oracle.ide.model.DeployableTextNode;
import oracle.ide.model.Locatable;
import oracle.ide.model.NodeFactory;
import oracle.ide.net.URLFileSystem;
import oracle.ide.resource.IdeIcons;

public class JARNavigatorNode extends AbstractLazyElement
  implements Locatable
{
  private URL m_url;

  public JARNavigatorNode( URL u )
  {
    m_url = u;
  }

  public String getShortLabel()
  {
    return URLFileSystem.getFileName( m_url );
  }

  public javax.swing.Icon getIcon()
  {
    return IdeIcons.getIcon( IdeIcons.JAR_ICON );
  }

  protected void loadChildren()
  {
    URL[] children = URLFileSystem.list( m_url );

    for ( int i=0; i < children.length; i++ )
    {
      URL u = children[i];

      if ( URLFileSystem.isDirectoryPath( u ) )
      {
        FileSystemFolderElement newElement = new FileSystemFolderElement( u );
        newElement.setInJar( true );  // use protocol instead?
        add( newElement );
      }
      else
      {
        try
        {
          add( NodeFactory.findOrCreate( u ) );
        }
        catch (Exception e )
        {

        }
      }
    }
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