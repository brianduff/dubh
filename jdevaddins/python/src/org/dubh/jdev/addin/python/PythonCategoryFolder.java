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
 * The Original Code is Python Addin for Oracle9i JDeveloper.
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@dubh.org).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */
package org.dubh.jdev.addin.python;

import java.awt.MediaTracker;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import oracle.ide.model.CategoryFolder;
import oracle.ide.net.URLFactory;

import oracle.jdeveloper.model.JProjectFilter;

/**
 * A category folder for the JDeveloper navigator which contains Python
 * scripts.
 *
 * @author Brian.Duff@oracle.com
 */
public class PythonCategoryFolder extends CategoryFolder
{

  static final String PYTHON_SCRIPTS = "Python Scripts";
  static final Icon ICON;

  static final int PYTHON_CATEGORY;

  static
  {
    PYTHON_CATEGORY = JProjectFilter.newCategory();
  
    ImageIcon i = new ImageIcon( 
      PythonCategoryFolder.class.getResource( "pythoncategory.png" )
    );
    if ( i.getImageLoadStatus() == MediaTracker.COMPLETE )
    {
      ICON = i;
    }
    else
    {
      ICON = null;
    }
  }
  
  /**
   * Construct an instance of PythonCategoryFolder
   */
  public PythonCategoryFolder( URL url )
  {
    super( url );
  }

  public PythonCategoryFolder()
  {
    super( URLFactory.newURL( DISPLAY_URL_PROTOCOL, PYTHON_SCRIPTS ) );
  }

// ---------------------------------------------------------------------------
// CategoryFolder overrides
// ---------------------------------------------------------------------------

  public Icon getIcon()
  {
    if ( ICON != null )
    {
      return ICON;
    }
    else
    {
      return super.getIcon();
    }
  }

}