// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: Newsgroup.java,v 1.1.1.1 1999-06-06 23:37:38 briand Exp $
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

import javax.mail.Folder;

import java.util;

/**
 * A Javamail folder representing a newsgroup in the NNTP protocol.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: Newsgroup.java,v 1.1.1.1 1999-06-06 23:37:38 briand Exp $
 */
class Newsgroup extends Folder
{
   private final static String ERR_RO = "Newsgroups are readonly";
   private boolean m_open;
   
   private String m_groupName;
   
   private ArrayList m_messages;
   
   private Date m_datNewNews;
   
   private int m_articleCount;
   private int m_firstArticle;
   private int m_lastArticle;
    
   public Newsgroup(Store s)
   {
      super(s);
   }
   
   /**
    * Can't append to newsgroups
    */
   public void appendMessages(Message[] msgs)
      throws MessagingException
   {
      throw new MessagingException(ERR_RO);
   }
   
   /**
    * Close this newsgroup.
    */
   public void close(boolean expunge)
      throws MessagingException
   {
      if (isOpen())
      {
         ((NNTPStore)getStore()).closeGroup(this);
         m_open = false;
         notifyConnectionListeners(ConnectionEvent.CLOSED);
      }
      else
      {
         throw new MessagingException("Group already closed");
      }
   }
   
   /**
    * Cant create newsgroups
    */
   public boolean create(int type)
      throws MessagingException
   {
      return false;
   }
   
   /**
    * Can't delete newsgroups
    */
   public boolean delete()
      throws MessagingException
   {
      return false;
   }
   
   /**
    * Does this group exist on the server?
    */
   public boolean exists()
      throws MessagingException
   {
      if (isOpen()) return true;
      
      return ((NNTPStore)getStore()).groupExists(this);
   }
   
   /**
    * Can't expunge news messages
    */
   public Message[] expunge()
      throws MessagingException
   {
      throw new MessagingException(ERR_RO);
   }
   
   /**
    * Newsgroups don't have subfolders
    */
   public Folder getFolder(String name)
      throws MessagingException
   {
      throw new MessagingException("Newsgroups don't have subfolders");
   }
   
   /**
    * Get the name of this newsgroup
    */
   public String getFullName()
   {
      return m_groupName;
   }
   
   /**
    * Get a message from the newsgroup
    */
   public Message getMessage(int msgno)
      throws MessagingException
   {
      // message array is zero based, msgno is 1 based.
      return getMessages()[msgno-1];
   }
   
   /**
    * Get a count of messages in this group
    */
   public int getMessageCount()
      throws MessagingException
   {
      return getMessages().length;
   }
   
   /**
    * Get all messages in this group. Check for new messages
    * since the last time this method was called and retrieve them
    */
   public Message[] getMessages()
      throws MessagingException
   {
      if (!isOpen()) throw new MessagingException("Not open");
      
      NNTPStore s = (NNTPStore)store;
      
      if (m_messages == null)
      {
         m_messages = s.getMessages(this);
      }
      else 
      {
         NNTPMessage[] nm = s.getNewMessages(this, m_datNewNews);
         for (int i=0; i < nm.length; i++)
         {
            m_messages.add(nm[i]);
         }
         
         // Store the current date/time
         m_datNewNews = new Date();
      }
      
      return (Message[]) m_messages.toArray(new Message[0]);
      
   }
   
   /**
    * Get the name of this group
    */
   public String getName()
   {
      return m_groupName;
   }
   
   /**
    * Newsgroups don't have parents
    */
   public Folder getParent()
      throws MessagingException
   {
      throw new MessagingException("Newsgroups don't have parents");
   }
   
   /**
    * No flags for newsgroups
    */
   public Flags getPermanentFlags()
   {
      return new Flags();
   }
   
   /**
    * No separator for newsgroups; not hierarchical
    */
   public char getSeparator()
      throws MessagingException
   {
      throw new MessagingException("Newsgroups don't have a separator");
   }
   
   /**
    * Type of newsgroups is always HOLDS_MESSAGES
    */
   public int getType()
      throws MessagingException
   {
      return HOLDS_MESSAGES;
   }

   /**
    * Whether there are new messages
    */
   public boolean hasNewMessages()
      throws MessagingException
   {
      return getNewMessageCount() > 0;
   }
   
   /**
    * Is the group open?
    */
   public boolean isOpen()
   {
      return m_open;
   }

   /**
    * Can't have subfolders in a newsgroup, this throws an exception
    */
   public Folder[] list(String pattern)
      throws MessagingException
   {
      throw new MessagingException("Can't have subfolders in a newsgroup");
   }
   
   /**
    * Open the newsgroup
    */
   public void open(int mode)
      throws MessagingException
   {
      if (!isOpen())
      {
         ((NNTPStore)getStore()).openGroup(this);
         m_open = true;
         notifyConnectionListeners(ConnectionEvent.OPENED);
      }
   }
   
   /**
    * Can't rename newsgroups
    */
   public boolean renameTo(String s)
      throws MessagingException
   {
      return false;
   }
   
   
   void setArticleCount(int count)
   {
      m_articleCount = count;   
   }
   
   void setFirstArticle(int number)
   {
      m_firstArticle = number;
   }
   
   void setLastArticle(int number)
   {
      m_lastArticle = number;
   }

}


//
// $Log: not supported by cvs2svn $
//