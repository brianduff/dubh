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

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import oracle.ide.Ide;
import oracle.ide.controls.JEWTMessageDialog;
import oracle.ide.controls.JWrappedLabel;
import oracle.ide.net.URLFileSystem;
import oracle.ide.net.URLRecognizer;

import oracle.bali.ewt.busyBar.BusyBar;
import oracle.bali.ewt.dialog.JEWTDialog;

import org.python.core.imp;
import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyModule;
import org.python.core.PyString;
import org.python.util.InteractiveConsole;

import org.dubh.ui.console.Console;
import org.dubh.ui.console.Interpreter;


/**
 * PythonIDEInterpreter is a subclass of PythonInterpreter that is integrated
 * into the IDE.
 *
 * @author Brian.Duff@oracle.com
 */
public class PythonIDEInterpreter
  implements Interpreter
{
  private final PythonLog m_logger;
  private Console m_console;

  private InteractiveConsole m_pythonInterpreter;

  private boolean m_isLoaded = false;
  private JEWTDialog m_busyDialog = null;
  private boolean m_cancelFlag;
  private boolean m_noJython = false;
  
  private static final String PYTHON_PROMPT = "Python>";

  private static final String NO_PYTHON_ERROR = 
    "To interpret python in JDeveloper, Jython needs to be installed "+
    "into the IDE. \n\n"+
    "To install Jython, download it from http://www.jython.org. Copy the "+
    "jython.jar file and the Lib directory from under the jython installation path "+
    "into the JDeveloper lib/ext directory and restart JDeveloper.";
  /**
   * Construct an instance of PythonIDEInterpreter
   */
  public PythonIDEInterpreter( PythonLog log )
  {
    m_logger = log;
    m_console = new Console( PYTHON_PROMPT );
    m_console.setInterpreter( this );

    //
    // Jython scans the classpath on startup and creates a cache. This would
    // cause the IDE to hang on startup until it is finished. Unfortunately,
    // it takes some time, because the IDE has a very large classpath. 
    //
    // We construct the interpreter on a thread, and control access to it
    // until it has loaded.
    Thread interpLoader = new Thread( new Runnable() 
    {
      public void run()
      {
        loadInterpreter();
      }
    });

    interpLoader.setPriority( interpLoader.MIN_PRIORITY );
    interpLoader.start();
    
  }

  private boolean isLoaded()
  {
    return m_isLoaded;
  }

  private void setLoaded( boolean loaded )
  {
    m_isLoaded = loaded;
  }
  private boolean isJythonInstalled()
  {
    return !m_noJython;
  }

  private synchronized void loadInterpreter()
  {
    try
    {
      m_pythonInterpreter = new InteractiveConsole();
      m_pythonInterpreter.setErr( m_logger.getWriter() );
      m_pythonInterpreter.setOut( m_logger.getWriter() );  
      PyModule mod = imp.addModule("__main__");
      m_pythonInterpreter.setLocals(mod.__dict__);
      m_logger.getWriter().println( m_pythonInterpreter.getDefaultBanner() );    
      setLoaded( true );
    }
    catch (NoClassDefFoundError t )
    {
      setLoaded( true );
      m_noJython = true;
      // Moan, complain, generally wax lyrical.
      SwingUtilities.invokeLater( new Runnable() 
      {
        public void run()
        {
          m_logger.getWriter().println( NO_PYTHON_ERROR );
          m_console.setPrompt( "Python (Jython is NOT installed)>" );  
          m_console.reset();
        }
      });

    }
  }

  /**
   * You should call waitForInterpreter() before calling this, otherwise
   * it may be null.
   */
  private synchronized InteractiveConsole getInterpreter()
  {
    return m_pythonInterpreter;
  }


  /**
   * Wait until the interpreter has loaded. returns immediately if the 
   * interpreter is already loaded. Otherwise, displays a progress dialog
   * and waits until the user cancels (returns false) or the interpreter
   * has finished loading ( returns true )
   */
  private boolean waitForInterpreter()
  {
    if ( isLoaded() ) return true;

    // OK, the interpreter hasn't finished loading yet. We display a progress
    // UI and poll the interpreter every few ms to see if it has loaded.

    Thread poller = new Thread(
      new Runnable()
      {
        public void run()
        {
          while( true )
          {
            if ( isLoaded() )
            {
              hideBusyDialog();
              return;
            }
            try
            {
              Thread.sleep( 500 );
            }
            catch (Exception e ) {}
          }
        }
      }
    );

    poller.start();
    showBusyDialog();

    return !m_cancelFlag;
  }

  /**
   * Get the console for this interpreter. The console can be used to 
   * interactively enter commands
   */
  Console getConsole()
  {
    return m_console;
  }

  /**
   * Interpret a python script through Jython.
   * 
   * @param node the python node
   */
  void interpret( final PythonNode node )
  {
    try
    {
      if ( "file".equals( node.getURL().getProtocol() ) )
      {
        String fileName = URLFileSystem.getPlatformPathName( node.getURL() );
        String parent = new File( fileName ).getParent();

        if ( waitForInterpreter() )
        {
          PyString parentPy = new PyString( parent );
          Py.getSystemState().path.insert( 0, parentPy );

          getInterpreter().execfile( fileName );
          Py.getSystemState().path.remove( parentPy );          
        }


 
      }
      else
      {
        // So, we support running py scripts on webdav etc., but sometimes
        // the interpreter can't find imported modules in the same path...
        InputStream ins = URLFileSystem.openInputStream( node.getURL() );
        String name = URLFileSystem.getName( node.getURL() );

        if ( waitForInterpreter() )
        {
          getInterpreter().execfile( ins, name );
        }
      }
    }
    catch (IOException ioe )
    {
      alert( "An I/O error occurred running the script" );
    }
    catch ( NoClassDefFoundError cnfe )
    {
      noJython();
    }
    catch (Throwable t )
    {
      m_logger.getWriter().println( t.getMessage() );
    }
  }

  private void noJython()
  {
    alert( NO_PYTHON_ERROR );  
  }

  /**
   * Utility to display an alert in the neat OLAF style
   */
  private void alert( final String message )
  {
    JEWTMessageDialog md = JEWTMessageDialog.createMessageDialog(
      Ide.getMainWindow(), Ide.getProgramName(), 
      JEWTMessageDialog.TYPE_ALERT
    );
    md.setButtonMask( md.getButtonMask() ^ md.BUTTON_HELP );
    md.setMessageText( message );
    md.runDialog();
  }

  private void showBusyDialog()
  {
    //
    // Eugh.
    // There's a utility coming for this kind of progress dialog in o.i.controls in
    // 9.0.3, but sadly it's not on the 9.0.2 branch.
    m_cancelFlag = false;
    JPanel pan = new JPanel();
    pan.setLayout( new GridBagLayout() );
    
    JWrappedLabel lab = new JWrappedLabel(
      "The Jython interpreter is loading. The first time you run the IDE with Jython, it takes some time to initialize. Subsequent startup times will be much faster.\n\nPlease wait, or click Cancel and try again later."
    );
    BusyBar bar = new BusyBar();
    JButton but = new JButton( "Cancel" );

    java.awt.Insets i = new java.awt.Insets( 2, 2, 2, 2 );
    pan.add( lab, new GridBagConstraints(
      0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, i, 0, 0 ));
    pan.add( bar, new GridBagConstraints(
      0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL, i, 0, 0 ));
    pan.add( but, new GridBagConstraints(
      1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, i, 0, 0 ));

 
    but.addActionListener( new ActionListener() 
    {
      public void actionPerformed( ActionEvent ae )
      {
        m_cancelFlag = true;
        hideBusyDialog();
      }
    });

    m_busyDialog = JEWTDialog.createDialog( Ide.getMainWindow(), "Jython", 0 );
    m_busyDialog.setContent( pan );
    bar.start();
    m_busyDialog.setPreferredSize( 400, 200 );
    m_busyDialog.runDialog();
    
  }

  private void hideBusyDialog()
  {
    if ( m_busyDialog != null ) 
    {
      m_busyDialog.setVisible( false );
      m_busyDialog.dispose();
      m_busyDialog = null;
    }
  }

// ---------------------------------------------------------------------------
// Interpreter implementation
// ---------------------------------------------------------------------------

  public void interpret( String str )
  {

  
    if ( m_console != null )
    {

      if ( !isLoaded() )
      {
        m_console.getErrorWriter().println( 
          "The Jython interpreter is loading.\n\nThis initialization will take a while the first time you run the Python addin. On subsequent startup, it will initialize much faster.\n\nPlease try again later..."
        );
        return;
      }

      if ( !isJythonInstalled() )
      {
        m_console.getErrorWriter().println( NO_PYTHON_ERROR );
        return;
        
      }
    
      getInterpreter().setErr( m_console.getErrorWriter() );
      getInterpreter().setOut( m_console.getOutputWriter() );

      try
      {
        getInterpreter().exec( str );
      }
      catch (NoClassDefFoundError cnfe)
      {
        m_console.getErrorWriter().println( NO_PYTHON_ERROR );
      }
      catch (PyException pyex)
      {
        m_console.getErrorWriter().println( pyex );
      }
      finally
      {
        getInterpreter().setErr( m_logger.getWriter() );
        getInterpreter().setOut( m_logger.getWriter() );
      }
      
    }
  }


}