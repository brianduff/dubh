package org.freeciv.client.action;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import org.freeciv.client.Client;
import org.freeciv.client.Localize;
import org.freeciv.common.Assert;
import org.freeciv.common.Logger;

/**
 * This is the superclass of all client actions.
 */
public abstract class AbstractClientAction extends AbstractAction
{
  private Client m_client;
  public static final String ACCELERATOR = "accelerator";
  public static final String MNEMONIC = "mnemonic";
  public static final String VISIBLE = "visibile";

  private static boolean s_numpadLog = true;
  
  public AbstractClientAction() 
  {
    setEnabled( false );
  }

  public AbstractClientAction( Client c ) 
  {
    m_client = c;
  }
  
  void setClient( Client c )
  {
    m_client = c;
  }
  

  
  protected final void setMnemonic( char c )
  {
    putValue( MNEMONIC, "" + c );
  }
  
  protected final void setAccelerator( int vcode, int mod )
  {
    putValue( ACCELERATOR, KeyStroke.getKeyStroke( vcode, mod ) );
  }
  
  protected final void setAccelerator( int vcode )
  {
    setAccelerator( vcode, 0 );
  }
  
  protected final Client getClient()
  {
    return m_client;
  }
  
  public boolean isVisible()
  {
    return ( ( (Boolean)getValue( VISIBLE ) ).booleanValue() );
  }
  
  public void setVisible( boolean b )
  {
    putValue( VISIBLE, b ? Boolean.TRUE : Boolean.FALSE );
  }
  
  protected static String _( String txt )
  {
    return Localize.translation.translate( txt );
  }


  /**
   * Utility method that provides the modifier for the numeric keyboard. Because
   * support for this was not added until java 1.4, this method returns 0 if
   * the modifier is not present in the JVM. Using this method saves having
   * this workaround code in every action that needs this modifier
   */
  protected int getNumberKeypadModifier()
  {
    Class keyEventClass = KeyEvent.class;
    try
    {
      Field numpadField = 
        keyEventClass.getDeclaredField( "VK_LOCATION_NUMPAD" );
      Integer i = (Integer)numpadField.get( null );
      return i.intValue();
    }
    catch (NoSuchFieldException nfe)
    {
      // Only moan once
      if (s_numpadLog)
      {
        Logger.log( Logger.LOG_NORMAL, 
          "This version of Java does not support VK_LOCATION_NUMPAD. Unable to "+
          "distinguish key events on the numeric keyboard. Upgrading to 1.4 or "+
          "later is recommended."
        );
        s_numpadLog = false;
      }
      return 0;
    }
    catch (SecurityException se)
    {
      Assert.fail( "Unexpected security exception", se );
    }
    catch (IllegalAccessException ille)
    {
      Assert.fail( "Unexpected illegal access exception", ille );
    }
    return 0;
  }
}
