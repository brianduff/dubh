// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: MessageHeaderFields.java,v 1.4 2001-02-11 02:50:58 briand Exp $
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


package org.dubh.apps.newsagent;

/**
* String constants for common NNTP header fields.
*
* @author Brian Duff
* @since NewsAgent 1.1.0
* @version $Id: MessageHeaderFields.java,v 1.4 2001-02-11 02:50:58 briand Exp $
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
// Revision 1.3  1999/12/12 01:47:12  briand
// Fix compilation problems caused by removal of FolderTreePanel and move to
// javalobby.
//
// Revision 1.2  1999/11/09 22:34:40  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.1  1999/06/01 17:58:51  briand
// Static constants for message header fields. The message
// composer will need to be upgraded to use these soon (it currently
// uses hard coded strings).
//
//
//