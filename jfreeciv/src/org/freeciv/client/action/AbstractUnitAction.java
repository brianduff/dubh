package org.freeciv.client.action;

import org.freeciv.client.Unit;
import org.freeciv.client.Client;
import org.freeciv.client.Localize;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 * Actions on units
 */
abstract class AbstractUnitAction extends AbstractClientAction
{

   public AbstractUnitAction()
   {
      super();
   }

   public boolean isEnabledFor(Unit u)
   {
      return false;
   }
}