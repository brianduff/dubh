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
