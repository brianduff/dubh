// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: AbstractButtonHandler.java,v 1.3 1999-03-22 23:37:18 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
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
package dubh.utils.nls;


/**
 * <p>
 * NLS handler for abstract buttons
 * </p>
 * @author <a href=mailto:bduff@uk.oracle.com>Brian Duff</a>
 * @version 0.0 (DJU 1.1.01) [03/Dec/1998]
 * @since DJU 1.1.0
 */
public class AbstractButtonHandler implements ComponentHandler
{
   private static AbstractButtonHandler m_instance = null;
   
   private static final String[] s_SUPPORTED = new String[] {
      "text", "mnemonic", "icon"
   };
   
   protected AbstractButtonHandler()
   {

   }
   
   public static AbstractButtonHandler getInstance()
   {
      if (m_instance == null)
         m_instance = new AbstractButtonHandler();
      return m_instance;
   }

   public Class getHandledClass()
   {
      try
      {
         return Class.forName("javax.swing.AbstractButton");
      } 
      catch (ClassNotFoundException e)
      {
         return null;
      }
   }
   public String[] getSupportedProperties()
   {
      return s_SUPPORTED;
   }
}