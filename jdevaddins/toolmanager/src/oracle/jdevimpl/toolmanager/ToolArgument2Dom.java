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

import org.w3c.dom.Element;

import oracle.ide.marshal.xml.Object2Dom;
import oracle.ide.marshal.xml.ObjectWrapper;
import oracle.ide.marshal.xml.ToDomConverter;

import oracle.jdevimpl.toolmanager.argument.TextArgument;

/**
 * A ToDomConverter for ToolArguments.
 *
 * @author Brian.Duff@oracle.com
 */
class ToolArgument2Dom implements ToDomConverter
{
  private ToolArgumentRegistry m_registry;

  ToolArgument2Dom( ToolArgumentRegistry registry )
  {
    m_registry = registry;
  }

  public boolean toObject( ObjectWrapper wrapper, Element e, Class clazz, 
    Object2Dom o2d )
  {
    if ( ToolArgument.class.isAssignableFrom( clazz ) &&
      clazz != TextArgument.class )
    {
      // Return the single instance from the registry.
      wrapper.setObject( m_registry.get( clazz ) );
      return true;
    }
    return false;
  }

  public boolean toElement( ObjectWrapper wrapper, Element e, Class clazz,
    Object2Dom od2 )
  {
    return false;
  }
}