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
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import oracle.ide.model.DefaultNode;
import oracle.ide.net.URLFactory;

/**
 * A node in the IDE navigator that represents a set of javadoc
 * 
 * @author Brian.Duff@oracle.com
 */
public final class JavadocNode extends DefaultNode
{
  private final String m_name;
  private final static Icon s_icon;

  static
  {
    ImageIcon icon = new ImageIcon(
      JavadocFolder.class.getResource( "doclink.png" )
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

  public JavadocNode( final URL u, final String name )
  { 
    super( URLFactory.newURL( u, "index.html") );
    m_name = name;
  }

// ----------------------------------------------------------------------------
// DefaultNode overrides
// ----------------------------------------------------------------------------

  public String getShortLabel()
  {
    return m_name;
  }

  public Icon getIcon()
  {
    if ( s_icon != null )
    {
      return s_icon;
    }
    return super.getIcon();
  }
}