// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NNTPErrorCodes.java,v 1.1.1.1 1999-06-06 23:37:38 briand Exp $
//   Copyright (C) 1999  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh
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

package dubh.mail.nntp;

/**
 * Error code constants for the NNTP protocol. All messages
 * returned from NNTP commands start with one of these three
 * digit codes. See RFC 977 for full details.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NNTPErrorCodes.java,v 1.1.1.1 1999-06-06 23:37:38 briand Exp $
 */
class NNTPErrorCodes
{

   //
   // 100 series messages:
   // help
   //
   public static final int 
            HELP                     = 100,
            DEBUG_OUT                = 199;
            
   //
   // 200 series messages:
   // positive status information
   //
   public static final int
            READY_POSTOK             = 200,
            READY_NOPOSTING          = 201,
            SLAVE_STATUS             = 202,
            CLOSING                  = 205,
            GROUP_SELECTED           = 211,
            NEWSGROUP_LIST           = 215,
            ARTICLE_RETRIEVED_FULL   = 220,
            ARTICLE_RETRIEVED_HEAD   = 221,
            ARTICLE_RETRIEVED_BODY   = 222,
            ARTICLE_RETRIEVED        = 223,
            LIST_OVERVIEW            = 224,
            LIST_ARTICLES            = 230,
            LIST_NEWGROUPS           = 231,
            POST_OK                  = 240;
            

   //
   // 300 series messages:
   // requests for further data
   //
   public static final int
            POST_REQUEST             = 340;
            

   //
   // 400 series messages:
   // error messages
   //
   public static final int
            SERVICE_DISCONTINUED     = 400,
            NO_SUCH_GROUP            = 411,
            NO_CURRENT_GROUP         = 412,
            NO_CURRENT_ARTICLE       = 420,
            NO_ARTICLE_IN_GROUP      = 423,
            NO_ARTICLE               = 430,
            POSTING_NOT_ALLOWED      = 440,
            POSTING_FAILED           = 441;

   //
   // 500 series messages:
   // protocol errors / internal errors
   //
   public static final int
            COMMAND_NOT_RECOGNIZED   = 500,
            COMMAND_SYNTAX           = 501,
            PERMISSION_DENIED        = 502,
            SERVER_INTERNAL_ERROR    = 503;
            
            
            
   // Pseudo errors for internal use
   
   public static final String
            ERR_UNKNOWN_HOST = "Unknown Host",
            ERR_IOEXCEPTION  = "IO Exception",
            ERR_NNTP         = "NNTP Exception: ";
}


//
// $Log: not supported by cvs2svn $
//