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

import java.awt.Component;
import java.util.EventObject;
import javax.swing.JScrollPane;

import oracle.ide.Ide;
import oracle.ide.IdeContext;
import oracle.ide.addin.AbstractPinnable;
import oracle.ide.addin.Context;
import oracle.ide.docking.Dockable;
import oracle.ide.docking.DockableFactory;
import oracle.ide.docking.DockableWindow;
import oracle.ide.docking.DockStation;
import oracle.ide.layout.ViewId;
import oracle.ide.addin.View;
import oracle.ide.addin.ViewAdapter;
import oracle.ide.addin.ViewEvent;

import org.dubh.ui.console.Console;

/**
 * A dockable view in the IDE for the Python console
 *
 * @author Brian.Duff@oracle.com
 */
final class ConsoleDockable extends DockableWindow 
  implements DockableFactory
{
  static final String UNIQUE_NAME =
    "ConsoleDockable";    //NOTRANS

  private final Console m_console;
  private final JScrollPane m_scrollPane;
  
  public ConsoleDockable( final Console console )
  {
    super( Ide.getMainWindow(), UNIQUE_NAME );
    m_console = console;
    m_scrollPane = new JScrollPane( m_console );
  }

// ---------------------------------------------------------------------------
// DockableWindow overrides
// ---------------------------------------------------------------------------

  public Component getGUI()
  {
    return m_scrollPane;
  }

  public Context getContext( EventObject eo )
  {
    return new IdeContext(this, eo);
  }

  public String getTabName()
  {
    return "Python Console";
  }

  public String getTitleName()
  {
    return getTabName();
  }

  public String getUniqueName()
  {
    return UNIQUE_NAME;
  }

  public int getMenuPreferredMnemonic()
  {
    return (int) 'P';
  }

// DockableFactory implementation

  public Dockable getDockable( final ViewId viewId )
  {
    if ( UNIQUE_NAME.equals( viewId.getName() ) )
    {
      return this;
    }
    return null;
  }

  public void install()
  {
    Ide.getDockStation().dock( this, DockStation.SOUTH, true );
  }
  

}