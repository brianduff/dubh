// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: MessageHeaderFields.java,v 1.2 1999-11-09 22:34:40 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://wired.st-and.ac.uk/~briand/newsagent/
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

** Copyright (c) 1998 by the Java Lobby
** <mailto:jfa@javalobby.org>  <http://www.javalobby.org>
** 
** This program is free software.
** 
** You may redistribute it and/or modify it under the terms of the JFA
** license as described in the LICENSE file included with this 
** distribution.  If the license is not included with this distribution,
** you may find a copy on the web at 'http://javalobby.org/jfa/license.html'
**
** THIS SOFTWARE IS PROVIDED AS-IS WITHOUT WARRANTY OF ANY KIND,
** NOT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY. THE AUTHOR
** OF THIS SOFTWARE, ASSUMES _NO_ RESPONSIBILITY FOR ANY
** CONSEQUENCE RESULTING FROM THE USE, MODIFICATION, OR
** REDISTRIBUTION OF THIS SOFTWARE. 

package org.javalobby.apps.newsagent;

/**
* String constants for common NNTP header fields.
*  
* @author Brian Duff
* @since NewsAgent 1.1.0
* @version $Id: MessageHeaderFields.java,v 1.2 1999-11-09 22:34:40 briand Exp $
*/
public class MessageHeaderFields
{
   public static final String 
      FROM                = "From",
      SENDER              = "Sender",
      SUBJECT             = "Subject",
      TO                  = "To",
      SENT                = "Sent",
      REPLY_TO            = "Reply-To",
      NEWSGROUPS          = "Newsgroups",
      FOLLOWUP_TO         = "Followup-To",
      EXPIRES             = "Expires",
      DISTRIBUTION        = "Distribution",
      ORGANIZATION        = "Organization",
      CC                  = "Cc",
      BCC                 = "Bcc",
      FCC                 = "Fcc",
      REFERENCES          = "References",
      X_MAILER            = "X-Mailer",
      X_MAILER_URL        = "X-Mailer-URL",
      MESSAGE_ID          = "Message-Id";
      

   public static final String[] DEFAULT_VIEWER_HEADERS = new String[] 
      {
      FROM,
      SUBJECT,
      SENT,
      NEWSGROUPS
      };
   
   public static final String[] DEFAULT_COMPOSER_HEADERS = new String[]
      {
      NEWSGROUPS,
      SUBJECT
      };
      
   public static final String[] ALL_HEADERS = new String[]
      {
      FROM,
      SENDER,
      SUBJECT,
      TO,
      SENT,
      REPLY_TO,
      NEWSGROUPS,
      FOLLOWUP_TO,
      EXPIRES,
      DISTRIBUTION,
      ORGANIZATION,
      CC,
      BCC,
      FCC,
      REFERENCES,
      X_MAILER,
      X_MAILER_URL,
      MESSAGE_ID      
      };
}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/06/01 17:58:51  briand
// Static constants for message header fields. The message
// composer will need to be upgraded to use these soon (it currently
// uses hard coded strings).
//
//
//