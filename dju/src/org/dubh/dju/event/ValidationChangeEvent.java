// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ValidationChangeEvent.java,v 1.4 1999-11-02 19:52:46 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 2 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software
//   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history

package dubh.utils.event;

import java.util.EventObject;
/**
 * ValidationChangeEvents are fired by ValidatorPanels when the valid
 * state of the panel changes.</P>
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: ValidationChangeEvent.java,v 1.4 1999-11-02 19:52:46 briand Exp $
 * @see dubh.utils.ui.ValidatorPanel
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