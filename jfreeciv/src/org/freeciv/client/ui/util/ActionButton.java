package org.freeciv.client.ui.util;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;

import org.freeciv.client.action.AbstractClientAction;
/**
 * A menu item that is based on an AbstractClientAction.
 */
public class ActionButton extends JButton implements PropertyChangeListener
{
  public ActionButton( AbstractClientAction aca, String surrogateText ) 
  {
    this( aca );
    setText( surrogateText );
    setMnemonic( (char)0 );
  }
  public ActionButton( AbstractClientAction aca ) 
  {
    super( aca.getName() );
    update( aca );
    aca.addPropertyChangeListener( this );
    addActionListener( aca );
    aca.addComponent( this );
  }
  public void update( AbstractClientAction aca ){
    char mn = aca.getMnemonic();
    if( mn != '\0' )
    {
      setMnemonic( mn );
    }
    setEnabled( aca.isEnabled() );
    setText( aca.getName() );      
  }
  public void propertyChange( PropertyChangeEvent pe )
  {
    update( (AbstractClientAction)pe.getSource() );
  }
}
