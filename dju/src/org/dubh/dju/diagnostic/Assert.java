// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: Assert.java,v 1.3 2001-02-11 15:37:07 briand Exp $
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


package org.dubh.dju.diagnostic;
/** * This class provides an assert capability. Assertions are used to validate * states or preconditions. It is usually an internal (programming) error if * an assertion fails. The Assert class allows you to make assertions, and * throws an AssertionFailedException if an assertion fails. This exception * subclasses from RuntimeException, so does not have to be declared. If * thrown, however, the thread which was executing is killed. * * You should surround all calls to Assert methods with a conditional check * on Assert.ENABLED. eg.: * <pre> *    if (Assert.ENABLED) *    { *        Assert.that((i > 5), "i must be greater than 5); *    } *</pre> * @author Brian Duff (bduff@uk.oracle.com) * @version $Id: Assert.java,v 1.3 2001-02-11 15:37:07 briand Exp $ */public class Assert{   ///////////////////////////////////////////////////////////////////////////   // Constants   //////////////////////////////////////////////////////////////////////////   /**    * Whether or not assertions are currently enabled. For this to work, you    * must surround all calls to methods in this class with a check on this    * constant. The constant can be changed at compile time to disable    * assertions for production code.    */   public static final boolean ENABLED = true;
   ///////////////////////////////////////////////////////////////////////////   // Constructors   ///////////////////////////////////////////////////////////////////////////

   ///////////////////////////////////////////////////////////////////////////   // Static Methods   ///////////////////////////////////////////////////////////////////////////
   /**    * Make an assertion.    * @param condition The condition to assert. If this is false, the    * assertion has failed.    *    * @param failMessage The message to display if the assertion fails. May    *   be null.    *    * @throws org.dubh.dju.diagnostic.AssertionFailedException if the    *   assertion fails.    */   public static void that(boolean condition, String failMessage)   {      if (!condition)      {         throw new AssertionFailedException(failMessage);      }   }}


//
// $Log: not supported by cvs2svn $
// Revision 1.2  2001/02/11 02:52:10  briand
// Repackaged from org.javalobby to org.dubh
//
// Revision 1.1  2000/06/14 21:28:35  briand
// Initial Revision.
//
//