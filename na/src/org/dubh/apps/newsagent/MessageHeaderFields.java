// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: MessageHeaderFields.java,v 1.1 1999-06-01 17:58:51 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
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

package dubh.apps.newsagent;

/**
* String constants for common NNTP header fields.
*  
* @author Brian Duff
* @since NewsAgent 1.1.0
* @version $Id: MessageHeaderFields.java,v 1.1 1999-06-01 17:58:51 briand Exp $
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
//
//