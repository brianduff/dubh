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


package oracle.jdevimpl.toolmanager;

import oracle.ide.addin.Context;

/**
 * Represents a type of substituted argument available to pass in to the
 * command line of external tools invoked through the ToolManager.
 * <p>
 * The name and description of a ToolArgument are used only for the 
 * purpose of displaying the argument substitution type to the user.
 * <p>
 * When a Tool is invoked, the getValue() method is called for each
 * of its arguments in turn to retrieve each argument for the external
 * process being launched. Each ToolArgument is passed a Context which 
 * can be used to determine the return value
 * <p>
 * ToolArguments have a moniker which is used only by "text" arguments. See
 * the oracle.jdevimpl.toolmanager.argument.TextArgument class for more
 * details.
 * 
 * @see oracle.jdevimpl.toolmanager.argument.TextArgument
 * @author Brian.Duff@oracle.com
 */
public interface ToolArgument 
{
  /**
   * A short human readable description of what this tool
   * argument represents
   *
   * @return a String which indicates what this tool argument is
   */
  String getName();

  /**
   * Get a long description of what this tool argument represents
   * 
   * @return a user readable description of this tool argument
   */
  String getDescription();

  /**
   * Convert a context into an argument instance. 
   *
   * @param context the context a tool is being invoked in
   * @return the value of this argument for the given context
   */
  String getValue( Context context );

  /**
   * Get a user visible moniker for this tool argument. This can be used by the
   * user to represent this argument when the user wants it to be substituted
   * into text.
   * 
   * @return a moniker for this tool argument
   * @see oracle.jdevimpl.toolmanager.argument.TextArgument
   */
  String getMoniker();

  /**
   * Can this tool argument resolve into a directory? If getValue() can
   * return a directory which could be used as the working directory for
   * a process, you should return true from this method. This allows the
   * argument type to be used in the "Run In Directory" field of the tool
   * dialog
   *
   * @return true if getValue() may return a directory path
   */
  boolean isDirectoryArgument();
}