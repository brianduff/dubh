// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: Newsgroup.java,v 1.5 1999-11-11 21:26:39 briand Exp $
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

package org.javalobby.javamail.nntp;

import javax.mail.*;
import javax.mail.event.ConnectionEvent;

import java.util.*;

/**
 * A Javamail folder representing a newsgroup in the NNTP protocol.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: Newsgroup.java,v 1.5 1999-11-11 21:26:39 briand Exp $
 */
class Newsgroup extends Folder
{
   private final static String ERR_RO = "Newsgroups are readonly";
   protected boolean m_open;
   
   protected String m_groupName;
   
   private ArrayList m_messages;
   
   private Date m_datNewNews;
   
   private int m_articleCount;
   private int m_firstArticle;
   private int m_lastArticle;
   
   private boolean m_bPostingOK;
    
   public Newsgroup(String groupName, Store s)
   {
      super(s);
      m_groupName = groupName;
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
   public boolean delete(boolean whatever)
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
         NNTPMessage[] msgs = s.getMessages(this);
         
         m_messages = new ArrayList(msgs.length);
         for (int i=0; i < msgs.length; i++)
         {
            m_messages.add(msgs[i]);
         }
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
      return '?';
      //throw new MessagingException("Newsgroups don't have a separator");
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
   public boolean renameTo(Folder f)
      throws MessagingException
   {
      return false;
   }
   
   void setPostingOK(boolean b)
   {
      m_bPostingOK = b;
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
// Revision 1.4  1999/10/16 16:45:13  briand
// Add support for posting_ok flag.
//
// Get rid of assumtion about return type of NNTPStore.getMessages().
//
// Revision 1.3  1999/08/03 19:16:15  briand
// Changed visibility of some fields. Added Store to constructor.
//
// Revision 1.2  1999/06/08 22:46:12  briand
// First compiling version of the Newsgroup class.
//
// Revision 1.1.1.1  1999/06/06 23:37:38  briand
// Dubh Mail Protocols initial revision.
//
//