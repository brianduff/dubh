// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NewsClientException.java,v 1.1 2000-02-22 23:47:35 briand Exp $
//   Copyright (C) 1999  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh
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

package org.javalobby.javamail.client.news;

/**
 * A news client exception is thrown by a NewsClient implementation
 * when something goes wrong. This is usually <b>not</b> because a
 * protocol command returned an error code (this is handled by the
 * store itself). Instead, this is caused by an underlying failure
 * in the implementation of the client (e.g. an IOException trying
 * to connect.) In future, support will be added to this class for
 * NLS error message retrieval for reporting to the user.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NewsClientException.java,v 1.1 2000-02-22 23:47:35 briand Exp $
 */
public class NewsClientException extends Exception
{
   private Throwable m_thrwBase;

   /**
    * Creates a news client exception based on the specified
    * underlying exception.
    */
   public NewsClientException(String key, Throwable baseThrowable)
   {
      super(key);
      m_thrwBase = baseThrowable;
   }

   public NewsClientException(String key)
   {
      this(key, null);
   }

   /**
    * Get the underlying throwable on which this news client throwable
    * is based.
    */
   public Throwable getBaseThrowable()
   {
      return m_thrwBase;
   }
}


//
// $Log: not supported by cvs2svn $
//