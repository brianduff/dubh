// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ValidationChangeEvent.java,v 1.6 2001-02-11 02:52:11 briand Exp $
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


package org.dubh.dju.event;

import java.util.EventObject;
/**
 * ValidationChangeEvents are fired by ValidatorPanels when the valid
 * state of the panel changes.</P>
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: ValidationChangeEvent.java,v 1.6 2001-02-11 02:52:11 briand Exp $
 * @see org.dubh.dju.ui.ValidatorPanel
 */
public class ValidationChangeEvent extends EventObject
{
   private boolean m_valid;

   public ValidationChangeEvent(Object source, boolean state)   {
      super(source);
      m_valid = state;
   }


   public boolean isValid()
   {
      return m_valid;
   }



}

/*
 * <B>Revision History:</B><UL>
 * <LI>0.1 [29/06/98]: Initial Revision
 * </UL>
 */

//
// $Log
//