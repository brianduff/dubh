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
 * Replace the standard default action of the text pane with one that always
 * jumps to the end of the document before inserting text.
 *
 * @author Brian.Duff@oracle.com
 */
class DefaultConsoleAction extends ConsoleAction
{

  /**
   * Construct an instance of DefaultConsoleAction
   */
  public DefaultConsoleAction()
  {
    super( "Default" );
  }

// ---------------------------------------------------------------------------
// ConsoleAction implementation
// ---------------------------------------------------------------------------

  public void actionPerformed( ActionEvent ae )
  {
    Console con = getConsole( ae );
    int mod = ae.getModifiers();

    // TODO, we should check if we're inside the command area. If so, we want
    // to overwrite rather than append.
    con.setCaretPosition( con.getDocument().getLength() );
    String text = ae.getActionCommand();
    if ( text != null && text.length() > 0 && 
      ((mod & ActionEvent.ALT_MASK ) == (mod & ActionEvent.CTRL_MASK)) )
    {
      char c = text.charAt( 0 );
      if ( ( c >= 0x20 ) && ( c != 0x7F ) )
      {
        con.replaceSelection( text );
      }
    }
  }
  
}