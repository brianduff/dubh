package org.freeciv.client.action;

import java.awt.Component;

import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.freeciv.client.Client;
import org.freeciv.common.Logger;
import org.freeciv.util.Localize;

/**
 * This is the superclass of all client actions.
 */
public abstract class AbstractClientAction extends AbstractAction
{
  /*
   * Creates an ACAction.
   */
  public AbstractClientAction(Client client)
  {
    this.client = client;
  }

  /*
   * Creates an ACAction.
   */
  public AbstractClientAction()
  {
    this.client = null;
  }

  /*
   * The current Client object.
   */
  private Client client;
  protected final void setClient(Client client)
  {
    this.client = client;
  }
  protected final Client getClient()
  {
    return client;
  }

  /*
   * A Set of KeyStrokes which are accelerators for this
   * ACAction - there could be several accelerators for one
   * ACAction, but only the one in the ACCELERATOR_KEY field is
   * shown at the MenuItems.
   */
  Set accelerators = new HashSet();

  /*
   * Adds an accelerator for this ACAction.
   */
  public final void addAccelerator( int vcode )
  {
    addAccelerator( vcode , 0 );
  }

  /*
   * Adds an accelerator for this ACAction.
   */
  public final void addAccelerator( int vcode, int mod )
  {
    accelerators.add( KeyStroke.getKeyStroke( vcode , mod ) );
    //If there's nothing in the ACCELERATOR_KEY field this
    // accelerator is put in there.
    if( getFirstAccelerator() == null )
	setFirstAccelerator( vcode , mod );
    ActionManager.actionManager.add(this);
  }

  /*
   * Puts an accelerator in the ACCELERATOR_KEY field and adds it
   * to the Set of accelerators.
   */
  public final void setFirstAccelerator( int vcode )
  {
    setFirstAccelerator( vcode , 0 );
  }

  /*
   * Puts an accelerator in the ACCELERATOR_KEY field and adds it
   * to the Set of accelerators.
   */
  public final void setFirstAccelerator( int vcode , int mod )
  {
    KeyStroke newValue = KeyStroke.getKeyStroke( vcode , mod );
    putValue( ACCELERATOR_KEY , newValue );
    addAccelerator( vcode , mod );
  }

  /*
   * Returns the ACCELERATOR_KEY field.
   */
  public final KeyStroke getFirstAccelerator()
  {
      return (KeyStroke)getValue( ACCELERATOR_KEY );
  }

  /*
   * Sets the NAME of this ACAction. The name is shown on
   * the Button, MenuItems... that represent this ACAction.
   */
  public final void setName(String string)
  {
    putValue( NAME , string );
  }

  /*
   * Returns the NAME of this ACAction.
   */
  public final String getName()
  {
    return (String)getValue(NAME);
  }

  /*
   * Sets the MNEMONIC_KEY of this ACAction.
   */
  public final void setMnemonic(char c)
  {
    Character newValue = new Character( c );
    putValue( MNEMONIC_KEY , newValue );
  }

  /*
   * Returns the MNEMONIC_KEY of this ACAction.
   */
  public final char getMnemonic()
  {
    try
    {
	return ( (Character)getValue( MNEMONIC_KEY ) ).charValue();
    }
    catch( Exception e )
    {
	//MNEMONIC_KEY wasn't set.
	return '\0';
    }
  }

  /*
   * This set contains every component that is connected to this
   * ACAction. This is needed to enable accelerators while the map
   * is focused / accelerated ACActions which are not in a menu.
   */
  private Set components=new HashSet();
  public final void addComponent(Component c)
  {
    components.add(c);
    ActionManager.actionManager.add(this);
  }
  public final Set getComponents()
  {
    return components;
  }
  public final void removeComponent(Component c)
  {
      Logger.log( Logger.LOG_ERROR ,
		  "AbstractClientAction.removeComponent() - "+
		  "not yet implemented" );
  }

  private boolean visible;

  /*
   * Are the Components which are representing this ACAction visible?
   */
  public boolean isVisible()
  {
      return visible;
  }

  /*
   * Set the visibility of the Components which are representing this
   * ACAction.
   */
  public void setVisible( boolean b )
  {
    Boolean oldValue = new Boolean( visible );
    Boolean newValue = new Boolean( b );
    visible = b;
    firePropertyChange( "visible" , oldValue , newValue );
  }

  protected static String translate( String txt )
  {
    return Localize.translate( txt );
  }
}
