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
import javax.swing.Icon;

import oracle.ide.model.DefaultFolder;
import oracle.ide.net.URLPath;

/**
 * Node used to represent the javadoc for a library when there is more than
 * one docpath entry
 * 
 * @author Brian.Duff@oracle.com
 */
public final class LibraryNode extends DefaultFolder
{
  private final String m_libName;

  public LibraryNode(  final String libName, final URLPath path )
  {
    m_libName = libName;
    Iterator pathEntries = path.iterator();
    while ( pathEntries.hasNext() )
    {
      URL u = (URL) pathEntries.next();
      add( new JavadocNode( u, u.toString() ) );
    }
  }

// ----------------------------------------------------------------------------
// DefaultFolder overrides
// ----------------------------------------------------------------------------

  public String getShortLabel()
  {
    return m_libName;
  }

  public Icon getIcon()
  {
    if ( JavadocFolder.s_icon == null )
    {
      return super.getIcon();
    }
    return JavadocFolder.s_icon;
  }
}