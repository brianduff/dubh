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
 * The Original Code is Filesystem Browser addin for Oracle9i JDeveloper
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@oracle.com).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package oracle.jdevimpl.fsbrowser;

import oracle.ide.Ide;
import oracle.ide.IdeAction;

import oracle.ide.addin.Context;
import oracle.ide.addin.View;

import oracle.ide.addin.Controller;

/**
 * Utility class for chaining controllers. 
 */
public class ChainedController implements Controller
{
  private Controller m_controller = null;
  private Controller m_oldController = null;
  private IdeAction m_action = null;

  public ChainedController( IdeAction action, Controller controller )
  {
    m_action = action;
    m_controller = controller;
    m_oldController = action.getController();
    action.setController( controller );
  }

  public Controller supervisor()
  {
    if ( m_oldController != null )
    {
      return m_oldController;
    }
    final View v = Ide.getMainWindow().getLastActiveView();
    if ( v != null )
    {
      return v.getController();
    }
    return Ide.getInstance();
  }

  public boolean handleEvent( IdeAction action, Context context )
  {
    if ( action == m_action )
    {
      if ( m_controller.handleEvent( action, context ) )
      {
        return true;
      }

    }
    Controller supervisor = supervisor();
    if ( supervisor == null )
    {
      return false;
    }
    return supervisor.handleEvent( action, context );    
  }

  public boolean update( IdeAction action, Context context )
  {
    if ( action == m_action )
    {
      if ( m_controller.update( action, context ) )
      {
        return true;
      }

    }
    Controller supervisor = supervisor();
    if ( supervisor == null )
    {
      return false;
    }
    return supervisor.update( action, context );    
  }

  public void checkCommands( Context context, Controller activeController )
  {
    Controller supervisor = supervisor();
    if ( supervisor != null )
    {
      supervisor.checkCommands( context, activeController );
    }
    m_controller.update( m_action, context );
  }
}