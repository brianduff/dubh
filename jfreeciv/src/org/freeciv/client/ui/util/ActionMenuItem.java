package org.freeciv.client.ui.util;
import org.freeciv.client.action.AbstractClientAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.JMenuItem;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
/**
 * A menu item that is based on an AbstractClientAction.
 */
public class ActionMenuItem extends JMenuItem implements PropertyChangeListener
{
  public ActionMenuItem( AbstractClientAction aca ) 
  {
    super( (String)aca.getValue( Action.NAME ) );
    KeyStroke key = (KeyStroke)aca.getValue( AbstractClientAction.ACCELERATOR );
    if( key != null )
    {
      setAccelerator( key );
    }
    String mn = (String)aca.getValue( AbstractClientAction.MNEMONIC );
    if( mn != null && mn.length() > 0 )
    {
      setMnemonic( mn.charAt( 0 ) );
    }
    setEnabled( aca.isEnabled() );
    aca.addPropertyChangeListener( this );
    addActionListener( aca );
  }
  public void propertyChange( PropertyChangeEvent pe )
  {
    if( "enabled".equals( pe.getPropertyName() ) )
    {
      setEnabled( ( (Boolean)pe.getNewValue() ).booleanValue() );
    }
    else
    {
      if( AbstractClientAction.VISIBLE.equals( pe.getPropertyName() ) )
      {
        setVisible( ( (Boolean)pe.getNewValue() ).booleanValue() );
      }
      else
      {
        if( Action.NAME.equals( pe.getPropertyName() ) )
        {
          setText( (String)pe.getNewValue() );
        }
      }
    }
  }
}
