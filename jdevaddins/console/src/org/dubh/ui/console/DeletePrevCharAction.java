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
import java.awt.Toolkit;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

/**
 * Overridden delete prev char action for the console. Only allows deletion
 * inside the command area.
 *
 * @author Brian.Duff@oracle.com
 */
class DeletePrevCharAction extends ConsoleAction
{

  /**
   * Construct an instance of DeletePrevCharAction
   */
  public DeletePrevCharAction()
  {
    super("DeletePrevChar");
  }

// ---------------------------------------------------------------------------
// ConsoleAction overrides
// ---------------------------------------------------------------------------

  public void actionPerformed( ActionEvent ae )
  {
    Console con = getConsole( ae );

    int dot = con.getCaret().getDot();
    int mark = con.getCaret().getMark();

    try
    {
      if ( dot == mark )
      {
        // Allow deletion only if dot > promptpos
        if ( dot > con.getPromptPosition() )
        {
          con.getDocument().remove( dot -1, 1 );
        }
        else
        {
          Toolkit.getDefaultToolkit().beep();
        }
      }
      else
      {
        // Allow deletion only if Math.min( dot,  mark ) is >= promptpos
        int startSel = Math.min( dot, mark );
        if ( startSel >= con.getPromptPosition() )
        {
          con.getDocument().remove( startSel, Math.abs( dot - mark ) );
        }
        else
        {
          Toolkit.getDefaultToolkit().beep();
        }
      }
    }
    catch (BadLocationException ble ) {}
    
  }

}