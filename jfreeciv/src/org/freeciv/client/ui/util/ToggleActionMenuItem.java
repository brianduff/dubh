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
    super( aca.getName() );
    aca.addPropertyChangeListener( this );
    addActionListener( aca );
    aca.addComponent( this );
  }
  public void propertyChange( PropertyChangeEvent pe )
  {
    update( (AbstractToggleAction) pe.getSource() );
  }
  public void update( AbstractToggleAction aca )
  {
    KeyStroke key = aca.getFirstAccelerator();
    if( key != null )
    {
      setAccelerator( key );
    }
    char mn = aca.getMnemonic();
    if( mn != '\0' )
    {
      setMnemonic( mn );
    }
    setEnabled( aca.isEnabled() );
    setSelected( aca.isToggledOn() );
  }
}
