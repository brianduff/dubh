// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NewsStatusCodes.java,v 1.2 2001-02-11 02:52:48 briand Exp $
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


package org.dubh.javamail.client.news;

/**
 * Status code constants for the NNTP protocol. All messages
 * returned from NNTP commands start with one of these three
 * digit codes. See RFC 977 for full details.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NewsStatusCodes.java,v 1.2 2001-02-11 02:52:48 briand Exp $
 */
public interface NewsStatusCodes
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
            POST_OK                  = 240,
            LIST_FOLLOWS             = 282;


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
// Revision 1.1  2000/02/22 23:47:35  briand
// News client implementation initial revision.
//
//