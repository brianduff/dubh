package org.freeciv.client.ui.util;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.freeciv.client.action.AbstractClientAction;

/**
 * A menu item that is based on an AbstractClientAction.
 */
public class ActionMenuItem extends JMenuItem implements PropertyChangeListener
{
  public ActionMenuItem( AbstractClientAction aca ) 
  {
    super( aca.getName() );
    update( aca );
    aca.addPropertyChangeListener( this );
    addActionListener( aca );
    aca.addComponent( this );
  }
  public void propertyChange( PropertyChangeEvent pe )
  {
    update( (AbstractClientAction) pe.getSource() );
  }
  public void update( AbstractClientAction aca )
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
  }
}
