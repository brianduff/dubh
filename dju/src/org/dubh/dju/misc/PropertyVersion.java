// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: PropertyVersion.java,v 1.4 1999-11-11 21:24:34 briand Exp $
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
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.MessageFormat;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.Calendar;
import java.util.Locale;

/**
 * <p>
 * A version that is based on a ResourceBundle (rather than a serialised
 * class). Needed, because our build process is currently running on a
 * linux box, but there is still no Java 1.2 support for Linux (we're
 * cheating by using IBM Jikes with the Java 1.2 libraries)
 * </p>
 * @author <a href=mailto:dubh@btinternet.com>Brian Duff</a>
 * @since 1.1.0
 */
public class PropertyVersion extends Version
{
   protected final static String
      KEY_MAJOR = "dubh.version.Major",
      KEY_MINOR = "dubh.version.Minor",
      KEY_MICRO = "dubh.version.Micro",
      KEY_BUILD = "dubh.version.BuildNumber",
      KEY_LABEL = "dubh.version.BuildLabel",
      KEY_NAME  = "dubh.version.ProductName",
      KEY_COPY  = "dubh.version.Copyright",
      KEY_DATE  = "dubh.version.ReleaseDate";
   
   protected ResourceBundle m_bundle;
   
   public PropertyVersion(ResourceBundle b)
   {
      m_bundle = b;
      initFromBundle();
   }
   
   protected void initFromBundle()
   {
      try
      {  
         //
         // Date is stored in UK format.
         // (bug reported by Thanh.Ma@East.Sun.COM)
         //    
         SimpleDateFormat sdfVersionFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
         
         setMajorVersion(Integer.parseInt(m_bundle.getString(KEY_MAJOR)));
         setMinorVersion(Integer.parseInt(m_bundle.getString(KEY_MINOR)));
         setMicroVersion(Integer.parseInt(m_bundle.getString(KEY_MICRO)));
         setBuildNumber(Integer.parseInt(m_bundle.getString(KEY_BUILD)));
         setBuildLabel(m_bundle.getString(KEY_LABEL));
         setProductName(m_bundle.getString(KEY_NAME));
         setProductCopyright(m_bundle.getString(KEY_COPY));
         String dat = m_bundle.getString(KEY_DATE);
         setReleaseDate(sdfVersionFormat.parse(dat));

      }
      catch (Throwable t)
      {
         if (Debug.TRACE_LEVEL_1)
         {
            Debug.println(1, this, "Failed to initialise version from bundle.");
            Debug.printException(1, this, t);
         }
      }
   }
   
}
//
// $Log: not supported by cvs2svn $
// Revision 1.3  1999/06/03 23:07:46  briand
// Fix bug reported by Thanh.Ma@East.Sun.COM:
// date in version file is in UK format;
// DateFormat was throwing exceptions in
// other locales.
//
//