// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ValidatorPanel.java,v 1.6 2001-02-11 02:52:12 briand Exp $
//   Copyright (C) 1997 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1997 - 2001 Brian Duff
//
// This program is free software.
//
// You may redistribute it and/or modify it under the terms of the
// license as described in the LICENSE file included with this
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://www.dubh.org/license'
//
// THIS SOFTWARE IS PROVIDED AS-IS WITHOUT WARRANTY OF ANY KIND,
// NOT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY. THE AUTHOR
// OF THIS SOFTWARE, ASSUMES _NO_ RESPONSIBILITY FOR ANY
// CONSEQUENCE RESULTING FROM THE USE, MODIFICATION, OR
// REDISTRIBUTION OF THIS SOFTWARE.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history

package org.dubh.dju.ui;

// Core Java Imports
import java.awt.*;
import java.util.*;

// Swing Imports
import javax.swing.*;


// Dubh Utils Imports
import org.dubh.dju.event.*;

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
 * @author Brian Duff
 * @version 0.0 (DJU 1.1.00) [12/Dec/1998]
 */
public class ValidatorPanel extends JPanel
{

   private Vector m_validators;
   private Vector m_listeners;
   private Vector m_messageValidators;
   private boolean m_valid;


   public ValidatorPanel()
   {
      super();
      m_validators = new Vector();
      m_listeners  = new Vector();
      m_messageValidators = new Vector();
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
      Enumeration en = m_listeners.elements();
      ValidationChangeEvent e = new ValidationChangeEvent(this, newState);
      while (en.hasMoreElements())
      {
         ((ValidationChangeListener)en.nextElement()).validationStateChanged(e);
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

   public void registerMessageValidator(MessageValidator v)
   {
      m_messageValidators.addElement(v);
   }

   public void unregisterMessageValidator(MessageValidator v)
   {
      m_messageValidators.removeElement(v);
   }

   Vector getMessageValidators()
   {
      return m_messageValidators;
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

   /**
    * Validators should implement this interface. If
    * you return null from getErrorCode, the DubhOkCancelDialog
    * will disable the Ok button until all validators are
    * happy. If you return an error code, your validator
    * is not used to affect the buttons, but if your
    * validator fails when the user clicks Ok, your error
    * message is displayed.
    */
   public interface Validator
   {
      public boolean isValid();
   }

   public interface MessageValidator extends Validator
   {
      public String getErrorMessage();
      public Object[] getSubstitutions();
   }
}