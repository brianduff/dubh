// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NNTPMessage.java,v 1.2 1999-06-08 22:45:58 briand Exp $
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

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Date;
import java.io.IOException;

/**
 * A USENET news article for JavaMail.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NNTPMessage.java,v 1.2 1999-06-08 22:45:58 briand Exp $
 */
class NNTPMessage extends MimeMessage
{
   private boolean m_bXOver;
   private String m_strMessageID;

   NNTPMessage(Session s)
   {
      super(s);
   }

   /**
    * The NNTPStore uses this to set the headers of this message if
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
   
   
   /**
    * Fetch article headers from the server if necessary. Not required
    * if X-Over headers have already been fetched, but only if the
    * server supports the specified header as an XOver header. You
    * can pass null as the header to force all headers to be read in.
    */
   private void loadHeader(String header)
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
   private NNTPStore getServer()
   {
      return ((NNTPStore)folder.getStore());
   }
}


//
// $Log: not supported by cvs2svn $
// Revision 1.1.1.1  1999/06/06 23:37:38  briand
// Dubh Mail Protocols initial revision.
//
//