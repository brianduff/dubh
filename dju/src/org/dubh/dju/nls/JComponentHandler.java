// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: JComponentHandler.java,v 1.5 2001-02-11 02:52:11 briand Exp $
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


package org.dubh.dju.nls;


/**
 * <p>
 * NLS handler for JComponents
 * </p>
 * @author <a href=mailto:dubh@btinternet.com>Brian Duff</a>
 * @version 0.0 (DJU 1.1.01) [03/Dec/1998]
 * @since DJU 1.1.0
 */
public class JComponentHandler implements ComponentHandler
{
   private static JComponentHandler m_instance = null;

   private static final String[] s_SUPPORTED = new String[] {
      "toolTipText"
   };

   protected JComponentHandler()
   {

   }

   public static JComponentHandler getInstance()
   {
      if (m_instance == null)
         m_instance = new JComponentHandler();
      return m_instance;
   }

   public Class getHandledClass()
   {
      try
      {
         return Class.forName("javax.swing.JComponent");
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