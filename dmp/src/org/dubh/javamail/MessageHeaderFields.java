// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: MessageHeaderFields.java,v 1.2 1999-06-08 22:45:00 briand Exp $
//   Copyright (C) 1999  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/mail
// ---------------------------------------------------------------------------
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 2 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software
//   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history

package dubh.mail;

/**
* String constants for common mail header fields.
*  
* @author Brian Duff
* @version $Id: MessageHeaderFields.java,v 1.2 1999-06-08 22:45:00 briand Exp $
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
// Revision 1.1.1.1  1999/06/06 23:37:38  briand
// Dubh Mail Protocols initial revision.
//
//