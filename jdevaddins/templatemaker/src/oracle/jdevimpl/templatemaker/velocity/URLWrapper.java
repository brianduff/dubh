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
import oracle.ide.net.URLFileSystem;

/**
 * A wrapper round a URL that provides a useful interface to velocity users
 * to retrieve interesting parts of the URL
 *
 * @author Brian.Duff@oracle.com
 * @version $Revision: 1.2 $
 */
public final class URLWrapper
{
  private final String url;
  private final String path;
  private final String name;
  private final String nameWithoutExtension;
  private String pkg;
  
  URLWrapper( URL u )
  {
    url = u.toString();
    path = URLFileSystem.getPlatformPathName( u );
    name = URLFileSystem.getFileName( u );
    int lastDot = name.lastIndexOf( '.' );
    if ( lastDot >= 0 )
    {
      nameWithoutExtension = name.substring( 0, lastDot );
    }
    else
    {
      nameWithoutExtension = name;
    }
    pkg = null;
  }

  public String getURL()
  {
    return url;
  }

  public String getPath()
  {
    return path;
  }

  public String getName()
  {
    return name;
  }

  public String getNameWithoutExtension()
  {
    return nameWithoutExtension;
  }

  public String getPackageName()
  {
    return pkg;
  }

  void setPackageName( final String thePackage )
  {
    if ( thePackage != null && thePackage.trim().length() == 0 )
    {
      pkg = null;
    }
    else
    {
      pkg = thePackage;
    }
  }
}