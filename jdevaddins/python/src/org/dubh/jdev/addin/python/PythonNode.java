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
import javax.swing.Icon;
import javax.swing.ImageIcon;

import oracle.ide.model.DeployableTextNode;
import oracle.ide.model.ElementAttributes;

/**
 * A node in the JDeveloper navigator for python (.py) source files.
 *
 * @author Brian.Duff@oracle.com
 */
public class PythonNode extends DeployableTextNode
{

  /**
   * Construct a python node
   */
  public PythonNode()
  {
    super();
  }

// ---------------------------------------------------------------------------
// DeployableTextNode overrides
// ---------------------------------------------------------------------------

  public Icon getIcon()
  {
    if ( PythonAddin.PYTHON_FILE_ICON != null )
    {
      return PythonAddin.PYTHON_FILE_ICON;
    }
    else
    {
      return super.getIcon();
    }
  }

  public int getCategory()
  {
    return PythonCategoryFolder.PYTHON_CATEGORY;
  }

}