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

import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

/**
 * The action which actually invokes the command.
 *
 * @author Brian.Duff@oracle.com
 */
class InvokeCommandAction extends ConsoleAction
{
  private final static String NAME = "Invoke Command";

  /**
   * Construct an instance of InvokeCommandAction
   */
  InvokeCommandAction( )
  {
    super( NAME );
  }

// ---------------------------------------------------------------------------
// TextAction implementation
// ---------------------------------------------------------------------------


  public void actionPerformed( final ActionEvent e )
  {
    Console con = getConsole( e );  
    try
    {

      String command = con.getCurrentCommand();
      con.newline();
      if ( con.getInterpreter() != null )
      {
        con.getInterpreter().interpret( command );
      }
    }
    finally
    {
      con.prompt();
    }
  }

}