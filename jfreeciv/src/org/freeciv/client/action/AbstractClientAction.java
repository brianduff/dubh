package org.freeciv.client.action;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import org.freeciv.client.Client;
import org.freeciv.client.Localize;

/**
 * This is the superclass of all client actions.
 */
public abstract class AbstractClientAction extends AbstractAction
{
  private Client m_client;
  public static final String ACCELERATOR = "accelerator";
  public static final String MNEMONIC = "mnemonic";
  public static final String VISIBLE = "visibile";
  
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
  
}
