// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: AssertionFailedException.java,v 1.1 2000-06-14 21:28:35 briand Exp $
//   Copyright (C) 1997-2000  Brian Duff
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

package org.javalobby.dju.diagnostic;


/**
 * This is the exception thrown when an assertion fails.
 *
 * @see org.javalobby.dju.diagnostic.Assert
 * @author Brian Duff (bduff@uk.oracle.com)
 * @version $Id: AssertionFailedException.java,v 1.1 2000-06-14 21:28:35 briand Exp $
 */
public final class AssertionFailedException extends RuntimeException
{
   ///////////////////////////////////////////////////////////////////////////
   // Constructors
   ///////////////////////////////////////////////////////////////////////////

   /**
    * Construct the exception.
    *
    * @param message a message to display. May be null.
    */
   public AssertionFailedException(String message)
   {
      super(message);
   }
}


//
// $Log: not supported by cvs2svn $
//