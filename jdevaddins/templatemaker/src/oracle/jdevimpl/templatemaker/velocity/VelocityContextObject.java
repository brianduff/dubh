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

package oracle.jdevimpl.templatemaker.velocity;

import java.net.URL;
import java.util.Iterator;

import oracle.ide.addin.Context;
import oracle.ide.net.URLFileSystem;
import oracle.ide.net.URLPath;

import oracle.jdeveloper.model.JProject;

/**
 * The velocity context object provides access to information that a 
 * template can substitute into the file
 *
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.2 $
 */
public class VelocityContextObject 
{
  private URLWrapper m_destination;
  private URLWrapper m_template;

  VelocityContextObject( Context context, URL templateURL, URL destinationURL  )
  {
    m_destination = new URLWrapper( destinationURL );
    m_template = new URLWrapper( templateURL );

    // Figure out the package of the destinationURL
    if ( context.getProject() != null )
    {
      if ( context.getProject() instanceof JProject )
      {
        URL destinationPath = URLFileSystem.getParent( destinationURL );
        URLPath sourcePath = ((JProject)context.getProject()).getSourcePath();
        Iterator i = sourcePath.iterator();
        while ( i.hasNext() )
        {
          URL thisEntry = (URL) i.next();
          if ( URLFileSystem.isBaseURLFor( thisEntry, destinationPath ) )
          {
            String relative = URLFileSystem.toRelativeSpec( 
              destinationPath, thisEntry
            );
            if ( relative.endsWith( "/" ) )
            {
              relative = relative.substring( 0, relative.length()-1 );
            }
            m_destination.setPackageName( relative.replace( '/', '.' ) );
            break;
          }
        }
      }
    }
  }

  public URLWrapper getDestination()
  {
    return m_destination;
  }

  public URLWrapper getTemplate()
  {
    return m_template;
  }



}