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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.collections.ExtendedProperties;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

/**
 * Velocity ResourceLoader that uses the IDE's URLFileSystem to load template
 * resources. Unlike the ResourceLoaders that are used by Velocity normally,
 * this ResourceLoader has no concept of a path. Therefore, all usage of
 * #include in templates must use the full URL to work.
 *
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.2 $
 */
public class URLResourceLoader extends ResourceLoader
{

// ----------------------------------------------------------------------------
// ResourceLoader implementation / overrides
// ----------------------------------------------------------------------------

  public void init( ExtendedProperties prop )
  {
    // NOOP
  }

  public InputStream getResourceStream( final String source )
    throws ResourceNotFoundException
  {
    final URL u = URLFactory.newURL( source );

    if ( u == null )
    {
      throw new ResourceNotFoundException(
        source+" is not a valid resource for the URLResourceLoader"
      );
    }
    
    if ( !URLFileSystem.exists( u ) )
    {
      throw new ResourceNotFoundException(
        "Unable to find "+u.toString()
      );
    }
    try
    {
      return URLFileSystem.openInputStream( u );
    }
    catch ( IOException ioe )
    {
      throw new ResourceNotFoundException(
        "IO Exception opening stream for "+u.toString()
      );
    }
  }

  public long getLastModified( final Resource resource )
  {
    final URL u = URLFactory.newURL( resource.getName() );
    return URLFileSystem.lastModified( u );
  }

  public boolean isSourceModified( final Resource resource )
  {
    final URL u = URLFactory.newURL( resource.getName() );
    
    if ( URLFileSystem.canRead( u ) )
    {
      return (URLFileSystem.lastModified( u ) != resource.getLastModified());           
    }
    return true;
  }

}