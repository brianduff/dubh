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

import oracle.jdevimpl.toolmanager.ToolManager;
import oracle.jdevimpl.toolmanager.ToolArgument;
import oracle.ide.addin.Context;

/**
 * Text argument is a special type of ToolArgument that can be used to pass
 * plain text arguments into tools. 
 * <p>
 * Unlike all other arguments, TextArgument is not registered with the
 * argument registry (it's persisted different, and several instances of this
 * class may exist at any one time). TextArgument does not have a
 * useful name, description or moniker.
 * <p>
 * TextArgument has a text attribute which can be accessed using setText()/
 * getText(). This text is returned from the getValue() method. The text
 * attribute may contain substitution variables. These are represented by
 * the moniker for a ToolArugment registered with the ToolArgumentRegistry
 * surrounded by brackets.
 * <p>
 * TextArguments can be used to create complex arguments which require 
 * several substitutions per argument or the inclusion of text and a 
 * substition in the same argument, for example a text attribute of:
 * <pre>
 *  -Dsome.property={outputDirectory}
 * </pre>
 * might expand to:
 * <pre>
 *  -Dsome.property=c:\some\output\directory
 * </pre>
 * assuming there is a registered ToolArgument with the ToolArgumentRegistry
 * with the "outputDirectory" moniker which retrieves the output directory
 * from the Context.
 * <p>
 * If the text attribute contains a badly formed or unrecognized 
 * substitution variable, it is returned unmodified. 
 *
 * @author Brian.Duff@oracle.com
 */
public class TextArgument implements ToolArgument
{
  private String m_text;


  public String toString()
  {
    return getText();
  }
  
  /**
   * A short human readable description of what this tool
   * argument represents
   *
   * @return a String which indicates what this tool argument is
   */
  public String getName()
  {
    return "";    // NOTRANS
  }

  /**
   * Get a long description of what this tool argument represents
   */
  public String getDescription()
  {
    return "";    // NOTRANS
  }
  
  /**
   * Convert a context into an argument instance. 
   *
   * @param context the context a tool is being invoked in
   * @return the value of this argument for the given context
   */
  public String getValue( Context context )
  {
    return ToolManager.getArgumentRegistry().expandMonikers( m_text, context );
  }

  public void setText( String text )
  {
    m_text = text;
  }

  public String getText()
  {
    return m_text;
  }

  public String getMoniker()
  {
    return "";   // NOTRANS
  }   

  public boolean isDirectoryArgument()
  {
    return false;
  }

  
}