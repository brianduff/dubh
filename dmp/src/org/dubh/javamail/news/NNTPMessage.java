// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NNTPMessage.java,v 1.1 2000-02-22 23:49:39 briand Exp $
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

package org.javalobby.javamail.news;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Date;
import java.util.Enumeration;
import java.io.IOException;

/**
 * A USENET news article for JavaMail.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NNTPMessage.java,v 1.1 2000-02-22 23:49:39 briand Exp $
 */
class NNTPMessage extends MimeMessage
{
   private boolean m_bXOver;
   private String m_strMessageID;

   NNTPMessage(Newsgroup g, int messageNum)
   {
      super(g, messageNum);
   }
   
   NNTPMessage(Newsgroup g, String messageId)
   {
      super(g, 0);
      m_strMessageID = messageId;
   }

   /**
    * The NewsStore uses this to set the headers of this message if
    * the server supports the XOVER extension
    */
   void setXOverHeaders(InternetHeaders head)
   {
      headers = head;
      m_bXOver = true;
   }

   /**
    * Get the body of the message.
    */
   public Object getContent()
      throws MessagingException,
             IOException
   {
      if (content == null)
      {
         content = getServer().getContent(this);
      }

      return super.getContent();
   }


   public void setRecipients(RecipientType type, Address[] addresses)
      throws MessagingException
   {
      throw new IllegalStateException("Folder is read only");
   }

   public void addRecipients(RecipientType type, Address[] addresses)
      throws MessagingException
   {
      throw new IllegalStateException("Folder is read only");
   }


   public void setReplyTo()
      throws MessagingException
   {
      throw new IllegalStateException("Folder is read only");
   }

   public void setSubject(String s)
      throws MessagingException
   {
      throw new IllegalStateException("Folder is read only");
   }

   public void setSubject(String s, String charset)
      throws MessagingException
   {
      throw new IllegalStateException("Folder is read only");
   }


   public void setSentDate(Date d)
      throws MessagingException
   {
      throw new IllegalStateException("Folder is read only");
   }

   public void setReceivedDate(Date d)
   {
      throw new IllegalStateException("Folder is read only");
   }

   /**
    * Get the Message-Id of this NNTP message.
    */
   public String getMessageId()
   {
      return m_strMessageID; // need this?
   }

   /**
    * Override this to force XOver header to be used, or full
    * headers to be read in if the header doesn't exist.
    */
   public String[] getHeader(String s)
      throws MessagingException
   {
      loadHeader(s);
      return super.getHeader(s);
   }

   /**
    * Override this to force XOver header to be used, or full
    * headers to be read in if the header doesn't exist.
    */
   public String getHeader(String s, String delimiter)
      throws MessagingException
   {
      loadHeader(s);
      return super.getHeader(s, delimiter);
   }


   public Enumeration getAllHeaderLines()
      throws MessagingException
   {
      loadHeader(null);
      return super.getAllHeaderLines();
   }

   /**
    * Fetch article headers from the server if necessary. Not required
    * if X-Over headers have already been fetched, but only if the
    * server supports the specified header as an XOver header. You
    * can pass null as the header to force all headers to be read in.
    */
   private void loadHeader(String header)
      throws MessagingException
   {
      if (header != null && m_bXOver && !getServer().supportsXOverHeader(header))
      {
         headers = null;
      }
      if (headers == null)
      {
         headers = getServer().getHeaders(this);
         m_bXOver = false;
      }
   }

   /**
    * Get the store for this article as an NNTPServer.
    */
   private NewsStore getServer()
   {
      return ((NewsStore)folder.getStore());
   }
}


//
// $Log: not supported by cvs2svn $
// Revision 1.5  1999/11/11 21:26:39  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.4  1999/10/17 17:07:04  briand
// Override getAllHeaderLines().
//
// Revision 1.3  1999/10/16 16:46:17  briand
// Sort out constructors.
//
// Revision 1.2  1999/06/08 22:45:58  briand
// First compiling version of the message class.
//
// Revision 1.1.1.1  1999/06/06 23:37:38  briand
// Dubh Mail Protocols initial revision.
//
//