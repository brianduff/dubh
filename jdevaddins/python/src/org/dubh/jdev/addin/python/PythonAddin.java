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

import java.io.InputStream;
import java.io.IOException;

import oracle.ide.ContextMenu;
import oracle.ide.Ide;
import oracle.ide.IdeAction;
import oracle.ide.addin.Addin;
import oracle.ide.addin.BaseController;
import oracle.ide.addin.Context;
import oracle.ide.addin.ContextMenuListener;
import oracle.ide.config.IdeSettings;
import oracle.ide.docking.DockStation;
import oracle.ide.net.URLFileSystem;
import oracle.ide.net.URLRecognizer;
import oracle.ide.panels.Navigable;

import oracle.jdeveloper.model.JProjectFilter;

import org.python.util.PythonInterpreter;

import org.dubh.jdev.addin.python.PythonNode;
import org.dubh.jdev.language.python.PythonLanguageModule;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;



/**
 * Add support to JDeveloper for Python. The specific integrations are:<p>
 * <ul>
 *  <li>A lexer to syntax highlight .py files</li>
 *  <li>A Python custom node type</li>
 *  <li>A Python Scripts category in the IDE navigator</li>
 *  <li>The ability to invoke the python interpreter on a python file in the
 *      navigator</li>
 *  <li>A python console, which can be used to type in interactive commands
 *      which are interpreted by jython</li>
 *  <li>A preferences page for the console</li>
 * </ul>
 * 
 * @author Brian.Duff@oracle.com
 */
public final class PythonAddin extends BaseController
  implements Addin, ContextMenuListener
{

  private PythonLog m_log;
  private PythonIDEInterpreter m_interpreter;
  private ConsoleDockable m_consoleDockable;

  private IdeAction m_interpretAction;
  private IdeAction m_viewConsoleAction;

  static final String CONSOLE_SETTINGS_KEY = "org.dubh.jdev.addin.python.PythonConsoleOptions";

  /**
   * Is the specified context a valid context for operations on python files?
   * 
   * @param ctx the context
   * @return true if this is a python context.
   */
  private static boolean isPythonContext( final Context ctx )
  {
    return ( ctx != null && ctx.getElement() instanceof PythonNode );
  }

  /**
   * Create IdeAction instances for this addin
   */
  private void createActions()
  {
    m_interpretAction = IdeAction.get(
      Ide.newCmd( "PythonAddin.Interpret" ), "Interpret with Jython", 
      new Integer( 0 )
    );
    m_interpretAction.setController(this);
    Ide.getMenubar().add(
      Ide.getMenubar().createMenuItem( m_interpretAction ),
      Ide.getRunner().getRunMenu()
    );


    m_viewConsoleAction = IdeAction.get(
      Ide.newCmd( "PythonAddin.ViewConsole" ), "Python Console",
      new Integer( 0 )
    );
    m_viewConsoleAction.setController( this );
    m_viewConsoleAction.putValue( IdeAction.TOGGLES, Boolean.TRUE );
    Ide.getMenubar().insert(
      Ide.getMenubar().createMenuItem( m_viewConsoleAction ),
      Ide.getMainWindow().View,
      Ide.getMainWindow().Toolbar
    );
    
    
  }

  private PythonIDEInterpreter getInterpreter()
  {
    if ( m_interpreter == null )
    {
      m_interpreter = new PythonIDEInterpreter( m_log );
    }
    return m_interpreter;
  }

  private void interpret( PythonNode node )
  {
    getInterpreter().interpret( node );
  }



// ---------------------------------------------------------------------------
// Addin implementation
// ---------------------------------------------------------------------------

  public void initialize()
  {
    // All we have to do is construct the language module for python. It 
    // registers itself with the IDE editor framework.
    new PythonLanguageModule();
    URLRecognizer.mapExtensionToClass( ".py", PythonNode.class );
    JProjectFilter.registerCategory(
      PythonCategoryFolder.PYTHON_CATEGORY, PythonCategoryFolder.class, 
      PythonCategoryFolder.PYTHON_SCRIPTS, PythonCategoryFolder.ICON,
      0
    );

    createActions();
    m_log = new PythonLog();
    Ide.getNavigatorManager().addContextMenuListener( this, PythonNode.class );


    // Register the console settings preference page in IDE preferences
    IdeSettings settings = Ide.getSettings();
    PythonConsoleOptions options = new PythonConsoleOptions();
    PythonConsoleOptionsPanel.setConsole( getInterpreter().getConsole() );
    options = (PythonConsoleOptions)settings.getData( CONSOLE_SETTINGS_KEY );
    if ( options == null )
    {
      options = new PythonConsoleOptions();
      settings.putData( CONSOLE_SETTINGS_KEY, options );
    }
    getInterpreter().getConsole().setSettings( options.toConsoleSettings() );
    getInterpreter().getConsole().reset();
    Navigable editorNav = new Navigable( "Python Console", 
      PythonConsoleOptionsPanel.class );
    IdeSettings.registerUI( editorNav );

    // Create the dockable console window
    m_consoleDockable = new ConsoleDockable( getInterpreter().getConsole() );
    Ide.getDockStation().registerDockableFactory(
      ConsoleDockable.UNIQUE_NAME, 
      m_consoleDockable
    );
    Ide.getDockStation().dock( m_consoleDockable, DockStation.SOUTH, true );    
    Ide.getDockStation().setDockableVisible(
      m_consoleDockable, true
    );


  }

  public void shutdown()
  {
    
  }

  public boolean canShutdown()
  {
    return true;
  }

  public float version()
  {
    return 0.1f;
  }

  public float ideVersion()
  {
    return Ide.getVersion();
  }

// ---------------------------------------------------------------------------
// BaseController overrides
// ---------------------------------------------------------------------------

  public boolean handleEvent( final IdeAction action, final Context ctx )
  {
    if ( isPythonContext( ctx ) )
    {
      if ( m_interpretAction == action )
      {
        interpret( (PythonNode) ctx.getElement() );
        return true;
      }
    }
    
    if ( m_viewConsoleAction == action )
    {
      if ( m_consoleDockable.isVisible() )
      {
        Ide.getDockStation().setDockableVisible( m_consoleDockable, false );
      }
      else
      {
        Ide.getDockStation().dock( m_consoleDockable, DockStation.SOUTH, false );
        Ide.getDockStation().setDockableVisible( m_consoleDockable, true );
      }
      return true;        
    }
    
    return super.handleEvent( action, ctx );
  }

  public boolean update( IdeAction action, Context context )
  {
    if ( action == m_viewConsoleAction )
    {
      action.setState( m_consoleDockable.isVisible() );
      return true;
    }
    return false;
  }

// ---------------------------------------------------------------------------
// ContextMenuListener implementation
// ---------------------------------------------------------------------------

  public boolean handleDefaultAction( final Context ctx )
  {
    return false;
  }

  public void poppingUp( final ContextMenu menu )
  {
    if ( isPythonContext( menu.getContext() ) )
    {
      menu.add( menu.createMenuItem( m_interpretAction ) );
    }
  }

  public void poppingDown( final ContextMenu menu )
  {
    // NOOP
  }

}