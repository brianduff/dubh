// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: AbstractButtonHandler.java,v 1.4 1999-11-11 21:24:35 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
// Copyright (c) 1998 by the Java Lobby
// <mailto:jfa@javalobby.org>  <http://www.javalobby.org>
// 
// This program is free software.
// 
// You may redistribute it and/or modify it under the terms of the JFA
// license as described in the LICENSE file included with this 
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://javalobby.org/jfa/license.html'
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
package org.javalobby.dju.nls;


/**
 * <p>
 * NLS handler for abstract buttons
 * </p>
 * @author <a href=mailto:dubh@btinternet.com>Brian Duff</a>
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