// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ReadOnlyVersion.java,v 1.4 1999-11-11 21:24:34 briand Exp $
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
package org.javalobby.dju.misc;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.MessageFormat;

import java.util.Date;

/**
 * <p>
 * Represents a product's name, version and copyright information.
 * </p>
 * @author <a href=mailto:dubh@btinternet.com>Brian Duff</a>
 * @version 0.0 (DJU 0.0.01) [22/Nov/1998]
 */
public interface ReadOnlyVersion
{
   public int    getMajorVersion();   
   public int    getMinorVersion();   
   public int    getMicroVersion();   
   public int    getBuildNumber();   
   public String getBuildLabel();   
   public String getProductName();   
   public String getProductCopyright();   
   public Date   getReleaseDate();
   public String getVersionDescription(String format);
   public String getShortDescription();
   public String getLongDescription();   
}