package org.freeciv.common;

import java.awt.Component;
import javax.swing.JOptionPane;

import org.freeciv.util.Localize;

/**
 * The error handler abstracts away the mechanism by which errors are reported
 * by the application. At startup, the server or client registers a subclass
 * of ErrorHandler.
 *
 * @author Brian Duff
 */
public abstract class ErrorHandler 
{
  private static ErrorHandler s_handler;

  /**
   * Signfies an internal error. The handler may consider internal errors
   * to be fatal, in which case the runtime might be terminated
   *
   * @param errorMessage the error message
   */
  public abstract void internalError( String errorMessage );

  /**
   * Signifies an internal error caused by an unexpected exception. 
   *
   * @param t the exception
   */
  public abstract void internalError( Throwable t );


  /**
   * Report an error.
   *
   * @param parent a parent component for any error UI the handler chooses
   *    to use
   *
   * @param message the actual message to display
   */
  public abstract void error( Component parent, String message );

  /**
   * Get the registered handler instance.
   *
   * @return an ErrorHandler for this application context.
   */
  public static final ErrorHandler getHandler()
  {
    if ( s_handler == null )
    {
      s_handler = new DefaultHandler();
    }
    return s_handler;
  }

  /**
   * Register a handler. This replaces any existing handler. 
   *
   * @param h a handler. If null, the defual handler will be used 
   */
  public static final void registerHandler( ErrorHandler h )
  {
    s_handler = h;
  }

  /**
   * The default handler just dumps out to stderr
   */
  private static class DefaultHandler extends ErrorHandler
  {
    public void internalError( String errorMessage )
    {
      System.err.println( "INTERNAL ERROR!" );
      System.err.println( errorMessage );
    }

    public void internalError( Throwable t )
    {
      System.err.println( "INTERNAL ERROR!" );    
      t.printStackTrace();
    }

    public void error( Component c, String message )
    {
      JOptionPane.showMessageDialog( c, message, 
        Localize.translate( "JFreeciv" ), JOptionPane.WARNING_MESSAGE );
    }
  }
}