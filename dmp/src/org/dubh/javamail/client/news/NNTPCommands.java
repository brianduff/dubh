// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NNTPCommands.java,v 1.1 2000-02-22 23:47:35 briand Exp $
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
 * Command strings for NNTP. These are unlikely to ever change, 
 * but using static constants reduces errors caused by typos
 * in hard coded strings.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NNTPCommands.java,v 1.1 2000-02-22 23:47:35 briand Exp $
 */
interface NNTPCommands
{
   public static final String
      QUIT                  = "QUIT",
      GROUP                 = "GROUP",
      LIST                  = "LIST",
      NEXT                  = "NEXT",
      NEWGROUPS             = "NEWGROUPS",
      HEAD                  = "HEAD",
      BODY                  = "BODY",
      XOVER                 = "XOVER",
      DATE                  = "DATE",
      POST                  = "POST",
      NEWNEWS               = "NEWNEWS",
      AUTHINFO_USER         = "AUTHINFO USER",
      AUTHINFO_PASS         = "AUTHINFO PASS",
      LIST_NEWSGROUPS       = "LIST NEWSGROUPS",
      XGTITLE               = "XGTITLE",
      LISTGROUP             = "LISTGROUP",
      ARTICLE               = "ARTICLE";
}


//
// $Log: not supported by cvs2svn $
// Revision 1.2  1999/11/11 21:26:39  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.1.1.1  1999/06/06 23:37:38  briand
// Dubh Mail Protocols initial revision.
//
//