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
 * The Original Code is The Dubh.Org Console Control.
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@dubh.org).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package org.dubh.ui.console;

import java.awt.event.ActionEvent;

/**
 * Clears all text in the console window. After this action completes, the 
 * only text in the console window is a single new prompt.
 *
 * @author Brian.Duff@oracle.com
 */
class ClearAction extends ConsoleAction
{
  private Console m_console;
  
  ClearAction( Console con )
  {
    super( "Clear" );
    m_console = con;
  }

// ---------------------------------------------------------------------------
// ConsoleAction implementation
// ---------------------------------------------------------------------------

  protected Console getConsole( ActionEvent ae )
  {
    return m_console;
  }

  public void actionPerformed( ActionEvent ae )
  {
    Console c = getConsole( ae );
    c.clear();
    c.prompt();
  }

}