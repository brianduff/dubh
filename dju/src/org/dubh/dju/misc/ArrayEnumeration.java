// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ArrayEnumeration.java,v 1.2 2001-02-11 02:52:11 briand Exp $
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

package org.dubh.dju.misc;

import java.util.Enumeration;

/**
 * A simple implementation of the Enumeration interface for arrays of objects.
 * <P>
 * @author Brian Duff
 */
public class ArrayEnumeration implements Enumeration
{
   private int m_index = 0;
   private Object[] m_items;

   /**
    * Create an array enumeration for the given array of items
    */
   public ArrayEnumeration(Object[] items)
   {
      m_index = 0;
      m_items = items;
   }

   /**
    * Determine if there are more elements on the enumeration.
    */
   public boolean hasMoreElements()
   {
      return (m_index < m_items.length);
   }

   /**
    * Get the next element in the enum
    */
   public Object nextElement()
   {
      return m_items[m_index++];
   }

   /**
    * You can use this to reset the enumeration. Because arrays don't change
    * size, it would be wasteful to construct new ArrayEnumerations for the
    * same array. This reset method allows you to use a single instance
    * of ArrayEnumeration for each array you want to enumerate, and just
    * reset it each time you use it (or return it from a method etc.)
    */
   public void reset()
   {
      m_index = 0;
   }


}

