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
 * The Original Code is Oracle9i JDeveloper release 9.0.3
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */


package oracle.jdevimpl.toolmanager.argument;

import oracle.ide.Ide;
import oracle.ide.addin.Context;

import oracle.jdevimpl.toolmanager.ToolArgument;
import oracle.jdevimpl.toolmanager.ToolManagerArb;

/**
 * A tool argument that represents the IDE install directory.
 *
 * @author Brian.Duff@oracle.com
 */
public final class IDEInstallDir implements ToolArgument
{

  public String toString()
  {
    return getName();
  }

  public String getName()
  {
    return
      ToolManagerArb.getString( ToolManagerArb.IDE_INSTALL_DIR_NAME );
  }

  public String getDescription()
  {
    return
      ToolManagerArb.getString( ToolManagerArb.IDE_INSTALL_DIR_DESCRIPTION );
  }

  public String getValue( Context context )
  {
    return Ide.getHomeDirectory();
  }

  public String getMoniker()
  {
    return "ideInstallDir";   // NOTRANS
  }

  public boolean isDirectoryArgument()
  {
    return true;
  }
}