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

import java.awt.BorderLayout;

import oracle.ide.panels.DefaultTraversablePanel;
import oracle.ide.panels.TraversableContext;

import org.dubh.ui.console.Console;
import org.dubh.ui.console.ConsoleSettingsPanel;

/**
 * Panel for configuring options for the Python console in IDE Preferences.
 *
 * @author Brian.Duff@oracle.com
 */
public class PythonConsoleOptionsPanel extends DefaultTraversablePanel
{
  private ConsoleSettingsPanel m_panel;

  private static Console m_console;

  /**
   * Construct an instance of PythonConsoleOptionsPanel
   */
  public PythonConsoleOptionsPanel()
  {
    m_panel = new ConsoleSettingsPanel();
    setLayout( new BorderLayout() );
    add( m_panel, BorderLayout.CENTER );
  }

  static void setConsole( Console con )
  {
    m_console = con;
  }

// ---------------------------------------------------------------------------
// DefaultTraversablePanel overrides
// ---------------------------------------------------------------------------


  public void onEntry( TraversableContext tc )
  {
    final PythonConsoleOptions options = 
      (PythonConsoleOptions) tc.find( PythonAddin.CONSOLE_SETTINGS_KEY );
    m_panel.setConsoleSettings( options.toConsoleSettings() );
  }

  public void onExit( TraversableContext tc )
  {
    System.out.println( "setting console settings" );
    final PythonConsoleOptions options = 
      (PythonConsoleOptions) tc.find( PythonAddin.CONSOLE_SETTINGS_KEY );
    options.fromConsoleSettings( m_panel.getConsoleSettings() );
    m_console.setSettings( options.toConsoleSettings() );
  }

}