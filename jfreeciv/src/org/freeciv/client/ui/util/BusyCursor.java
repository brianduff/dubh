package org.freeciv.client.ui.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

/**
 * If you attach this to a root pane for example a JInternalFrame, JDialog,
 * JFrame, JWindow etc. then call show(), the busy cursor will be
 * displayed after a certain amount of time, and the glasspane will be
 * made visible, which will block any mouse events from the components in the
 * root pane until the component stops being busy.
 *
 * Use this if you are doing lots of processing. Do the processing on the UI
 * thread and ensure that the user can't interact with the UI using this class.
 * For longer operations, use a progress watcher.
 *
 */
public class BusyCursor 
{
  /**
   *  The default number of milliseconds to delay before showing the
   *  wait cursor.
   */
  private static final long DEFAULT_DELAY = 500;

  private static final Timer _timer = new Timer();

  private ArrayList _glassPanes = new ArrayList( 1 ); 
  private int _waitCount=0;
  private TimerTask _timerTask;
  private final MouseAdapter _beeper = new MouseAdapter()
  {
    public void mousePressed( MouseEvent e )
    {
      Toolkit.getDefaultToolkit().beep();
    }
  };

  /**
   *  Attaches WaitCursor to the {@link RootPaneContainer} of the
   *  specified {@link Component}.
   */
  public BusyCursor( Component component )
  {
    if ( component != null )
    {
      final RootPaneContainer rpc =
        (RootPaneContainer) SwingUtilities.getAncestorOfClass( 
          RootPaneContainer.class, component );
      attach( rpc );
    }
  }


  /**
   * Installs the manager on the specified {@link RootPaneContainer}.
   */
  public void attach( RootPaneContainer rootPaneContainer )
  {
    if ( rootPaneContainer != null )
    {
      final Component glassPane = rootPaneContainer.getGlassPane();
      if ( glassPane != null )
      {
        _glassPanes.add( glassPane );
      }
    }
  }

  /**
   * De-installs the manager from the specified {@link RootPaneContainer}.
   */
  public void detach( RootPaneContainer rootPaneContainer )
  {
    if ( rootPaneContainer != null )
    {
      final Component glassPane = rootPaneContainer.getGlassPane();
      if ( _glassPanes.contains( glassPane ) )
      {
        setWaitCursor( glassPane, false );
        _glassPanes.remove( glassPane );
      }
    }
  }

  /**
   * Shows the wait cursor after {@link #DEFAULT_DELAY} number of
   * milliseconds has elapsed.
   */
  public void show()
  {
    show( DEFAULT_DELAY );
  }

  /**
   * Schedules the wait cursor to be shown after the specified number
   * of milliseconds has elapsed.  If {@link WaitCursor#hide()} is
   * called before the delay has elapsed, then the
   * <code>WaitCursor</code> is not shown.
   *
   * @param delay the number of milliseconds to delay before showing
   * the wait cursor.
   */
  public void show( long delay )
  {
    synchronized( this )
    {
      if ( _waitCount++ == 0 )
      {
        if ( delay > 0 )
        {
          _timerTask = new TimerTask()
          {
            public void run()
            {
              synchronized( BusyCursor.this )
              {
                // Check that _timerTask is not null, since the hide() method
                // may have already canceled the wait cursor while we were
                // waiting for the lock
                if ( _timerTask != null )
                {
                  setWaitCursor( true );
                  _timerTask = null;
                }
              }
            }
          };
          _timer.schedule( _timerTask, delay );
        }
        else
        {
          setWaitCursor( true );
        }
      }
    }
  }

  /**
   *  Hides the wait cursor if the counter reaches 0; does
   *  nothing if the wait count is already 0.
   */
  public void hide()
  {
    synchronized( this )
    {
      if ( _waitCount > 0 )
      {
        _waitCount--;
        if ( _timerTask != null )
        {
          _timerTask.cancel();
          _timerTask = null;
        }
        else
        {
          setWaitCursor( false );
        }
      }
    }
  }

  private void setWaitCursor( boolean bWait )
  {
    final int n = _glassPanes.size();
    final Component[] glasses = new Component[n];
    _glassPanes.toArray( glasses );
    for( int i = 0; i < n; i++ )
    {
      setWaitCursor( glasses[i], bWait );
    }
  }

  private void setWaitCursor( Component glass, boolean bWait )
  {
    final int cursorType = bWait ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR;
    final Cursor c = Cursor.getPredefinedCursor( cursorType );
    glass.setCursor( c );
    glass.setVisible( bWait );

    if ( bWait )
    {
      glass.addMouseListener( _beeper );
    }
    else
    {
      glass.removeMouseListener( _beeper );
    }
  }
}
