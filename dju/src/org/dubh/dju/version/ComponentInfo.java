// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ComponentInfo.java,v 1.1 2001-02-11 15:38:22 briand Exp $
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
 * This interface is used to provide the version information for a particular
 * component.
 * <P>
 * @author Brian Duff
 */
public interface ComponentInfo
{
   /**
    * Get the name of the component. This property is mandatory.
    *
    * @return a string containing the name of the component
    */
   public String getName();

   /**
    * Get the short version description of the component. This is usually in
    * the form x.y. This property is mandatory.
    *
    * @return a string containing the short version description of the
    *    component
    */
   public String getShortVersion();

   /**
    * Get the long version description of the component. This might include
    * a build number e.g. x.y.z build 123. This property is optional.
    *
    * @return a string containing the long version description of the component.
    *    May be null.
    */
   public String getLongVersion();

   /**
    * Get the copyright message for the component. This property is optional.
    *
    * @return a copyright message. May be null.
    */
   public String getCopyrightMessage();

   /**
    * Get the vendor of the component. This property is optional.
    *
    * @return the vendor of the component. May be null
    */
   public String getVendor();
}

 