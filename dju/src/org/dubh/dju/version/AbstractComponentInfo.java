// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: AbstractComponentInfo.java,v 1.1 2001-02-11 15:38:22 briand Exp $
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
package org.dubh.dju.version;

/**
 * A superclass abstract implementation of ComponentInfo that takes care
 * of the trivialities of storing the various bits of information, leaving the
 * subclass responsible for retreiving them from a source. The subclass
 * should retrieve all required information (probably in the constructor)
 * and call the various setter methods on this class.
 *
 * @author Brian Duff
 */
abstract class AbstractComponentInfo implements ComponentInfo
{
   private String m_name;
   private String m_shortVersion;
   private String m_longVersion;
   private String m_copyrightMessage;
   private String m_vendor;

   /**
    * Get the name of the component. This property is mandatory.
    *
    * @return a string containing the name of the component
    */
   public final String getName()
   {
      return m_name;
   }

   /**
    * Set the name of the component
    */
   protected final void setName(String name)
   {
      m_name = name;
   }

   /**
    * Get the short version description of the component. This is usually in
    * the form x.y. This property is mandatory.
    *
    * @return a string containing the short version description of the
    *    component
    */
   public final String getShortVersion()
   {
      return m_shortVersion;
   }

   /**
    * Set the short version
    */
   protected final void setShortVersion(String shortVersion)
   {
      m_shortVersion = shortVersion;
   }

   /**
    * Get the long version description of the component. This might include
    * a build number e.g. x.y.z build 123. This property is optional.
    *
    * @return a string containing the long version description of the component.
    *    May be null.
    */
   public final String getLongVersion()
   {
      return m_longVersion;
   }

   protected final void setLongVersion(String longVersion)
   {
      m_longVersion = longVersion;
   }

   /**
    * Get the copyright message for the component. This property is optional.
    *
    * @return a copyright message. May be null.
    */
   public final String getCopyrightMessage()
   {
      return m_copyrightMessage;
   }

   protected final void setCopyrightMessage(String copyrightMessage)
   {
      m_copyrightMessage = copyrightMessage;
   }


   /**
    * Get the vendor of the component. This property is optional.
    *
    * @return the vendor of the component. May be null
    */
   public final String getVendor()
   {
      return m_vendor;
   }

   protected final void setVendor(String vendor)
   {
      m_vendor = vendor;
   }

}


