package org.freeciv.client.ui.util;
import org.freeciv.client.action.AbstractToggleAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.JCheckBoxMenuItem;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
/**
 * A menu item that is based on an AbstractClientAction.
 */
public class ToggleActionMenuItem extends JCheckBoxMenuItem implements PropertyChangeListener
{
  public ToggleActionMenuItem( AbstractToggleAction aca ) 
  {
    super( (String)aca.getValue( Action.NAME ) );
    KeyStroke key = (KeyStroke)aca.getValue( AbstractToggleAction.ACCELERATOR );
    if( key != null )
    {
      setAccelerator( key );
    }
    String mn = (String)aca.getValue( AbstractToggleAction.MNEMONIC );
    if( mn != null && mn.length() > 0 )
    {
      setMnemonic( mn.charAt( 0 ) );
    }
    setEnabled( aca.isEnabled() );
    aca.addPropertyChangeListener( this );
    addActionListener( aca );
    setSelected( aca.isToggledOn() );
  }
  public void propertyChange( PropertyChangeEvent pe )
  {
    if( "enabled".equals( pe.getPropertyName() ) )
    {
      setEnabled( ( (Boolean)pe.getNewValue() ).booleanValue() );
    }
    else
    {
      if( AbstractToggleAction.VISIBLE.equals( pe.getPropertyName() ) )
      {
        setVisible( ( (Boolean)pe.getNewValue() ).booleanValue() );
      }
      else
      {
        if( Action.NAME.equals( pe.getPropertyName() ) )
        {
          setText( (String)pe.getNewValue() );
        }
        else
        {
          if( AbstractToggleAction.TOGGLE_STATE.equals( pe.getPropertyName() ) )
          {
            setSelected( ( (Boolean)pe.getNewValue() ).booleanValue() );
          }
        }
      }
    }
  }
}
