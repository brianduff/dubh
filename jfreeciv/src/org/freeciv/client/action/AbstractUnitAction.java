package org.freeciv.client.action;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.freeciv.client.Client;
import org.freeciv.client.Localize;
import org.freeciv.common.Unit;


/**
 *  This is the superclass of all actions which operate on units
 *
 * @author Brian Duff
 */
abstract class AbstractUnitAction extends AbstractClientAction
{
  public AbstractUnitAction() 
  {
    super();
    setEnabled( false );
  }
  public boolean isEnabledFor( Unit u )
  {
    return false;
  }
}
