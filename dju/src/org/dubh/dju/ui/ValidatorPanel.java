/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Please note that this software is not in any way endorsed by
 * Oracle Corporation
 * Version History:
 *  FV   DUV    Date          Who    What
 *  ======================================================================
 *  0.0  1.1.00 [12/Dec/1998] BD     Initial Revision
 *
 */

package dubh.utils.ui;

// Core Java Imports
import java.awt.*;
import java.util.*;

// Swing Imports
import javax.swing.*;


// Dubh Utils Imports
import dubh.utils.event.*;

// FESI Imports

/**
 * <p>
 * Any panel that has to be validated before it can be oked. You should
 * register any number of validator objects using the registerValidator
 * method. Then call checkValid() if the validation state may have changed.
 * this will force the validator panel to poll all its validators. If
 * all validators return true, the panel is considered to be in a 
 * valid state. You can add event listeners for changes in the valid
 * state of this panel (e.g. the ok cancel dialog adds listeners
 * to any ValidatorPanels it contains to enable or disable the ok button
 * depending on the validity of the panel.)
 * </p>
 * @author <a href=mailto:bduff@uk.oracle.com>Brian Duff</a>
 * @version 0.0 (DJU 1.1.00) [12/Dec/1998]
 */
public class ValidatorPanel extends JPanel
{
   
   private Vector m_validators;
   private Vector m_listeners;
   private boolean m_valid;

   
   public ValidatorPanel()
   {
      super();
      m_validators = new Vector();
      m_listeners  = new Vector();
      m_valid = true;
   }

   public void addValidationChangeListener(ValidationChangeListener l)
   {
      m_listeners.addElement(l);
   }
   
   public void removeValidationChangeListener(ValidationChangeListener l)
   {
      m_listeners.removeElement(l);
   }
   
   private void fireValidationChange(boolean newState)
   {
      Enumeration enum = m_listeners.elements();
      ValidationChangeEvent e = new ValidationChangeEvent(this, newState);
      while (enum.hasMoreElements())
      {
         ((ValidationChangeListener)enum.nextElement()).validationStateChanged(e);
      }
   }
   
   public void registerValidator(Validator v)
   {
      m_validators.addElement(v);
   }
   
   public void unregisterValidator(Validator v)
   {
      m_validators.removeElement(v);
   }
   
   public void checkValid()
   {
      m_valid = true;
      Enumeration v = m_validators.elements();
      while (v.hasMoreElements())
      {
         if (!((Validator)v.nextElement()).isValid())
         {
            m_valid = false;
            fireValidationChange(false);
            return;
         }
      }
      fireValidationChange(true);
   }
   
   public boolean isValid()
   {   
      return m_valid;
   }   

   
   public interface Validator 
   {
      public boolean isValid();
   }
}