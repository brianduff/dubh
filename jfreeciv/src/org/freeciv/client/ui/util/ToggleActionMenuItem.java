package org.freeciv.client.ui.util;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.KeyStroke;

import org.freeciv.client.action.AbstractToggleAction;
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
