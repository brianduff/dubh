// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: MessageHeaderFields.java,v 1.4 2001-02-11 02:52:48 briand Exp $
//   Copyright (C) 1999 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1999 - 2001 Brian Duff
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


package org.dubh.javamail;

/**
* String constants for common mail header fields.
*
* @author Brian Duff
* @version $Id: MessageHeaderFields.java,v 1.4 2001-02-11 02:52:48 briand Exp $
*/
public class MessageHeaderFields
{
   public static final String
      FROM                = "From",
      SENDER              = "Sender",
      SUBJECT             = "Subject",
      TO                  = "To",
      DATE                = "Date",
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
      MESSAGE_ID          = "Message-Id",
      CONTENT_TYPE        = "Content-Type",
      CONTENT_DISPOSITION = "Content-Disposition",
      CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding",
      CONTENT_ID          = "Content-Id",
      CONTENT_MD5         = "Content-MD5",
      CONTENT_DESCRIPTION = "Content-Description",
      CONTENT_LANGUAGE    = "Content-Language";


   public static final String[] DEFAULT_VIEWER_HEADERS = new String[]
      {
      FROM,
      SUBJECT,
      DATE,
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
      DATE,
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
      MESSAGE_ID,
      CONTENT_TYPE,
      CONTENT_DISPOSITION,
      CONTENT_TRANSFER_ENCODING,
      CONTENT_ID,
      CONTENT_MD5,
      CONTENT_DESCRIPTION,
      CONTENT_LANGUAGE
      };
}

//
// (was moved from NewsAgent at some point, June 1999)
//
// $Log: not supported by cvs2svn $
// Revision 1.3  1999/11/11 21:26:38  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.2  1999/06/08 22:45:00  briand
// Add Content- headings, change Sent to Date.
//
// Revision 1.1.1.1  1999/06/06 23:37:38  briand
// Dubh Mail Protocols initial revision.
//
//