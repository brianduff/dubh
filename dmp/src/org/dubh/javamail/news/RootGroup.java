// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: RootGroup.java,v 1.3 2001-02-11 02:52:49 briand Exp $
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
package org.dubh.javamail.news;

import javax.mail.*;
import javax.mail.event.ConnectionEvent;

import java.util.*;

/**
 * A Javamail folder representing a newsgroup in the NNTP protocol.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: RootGroup.java,v 1.3 2001-02-11 02:52:49 briand Exp $
 */
class RootGroup extends Newsgroup
{



   public RootGroup(Store s)
   {
      super("/", s);
   }



   private NewsStore getServer()
   {
      return (NewsStore)getStore();
   }
   /**
    * Close this newsgroup.
    */
   public void close(boolean expunge)
      throws MessagingException
   {
   }


   /**
    * Does this group exist on the server?
    */
   public boolean exists()
      throws MessagingException
   {
      return true;
   }


   /**
    * Get a folder
    */
   public Folder getFolder(String name)
      throws MessagingException
   {
      return ((NewsStore)store).getFolder(name);
   }


   /**
    * Root group doesn't contain messages
    */
   public Message getMessage(int msgno)
      throws MessagingException
   {
      throw new MessagingException("Root group doesn't contain messages");
   }

   /**
    * Get a count of messages in this group
    */
   public int getMessageCount()
      throws MessagingException
   {
      return 0;
   }

   /**
    * Get all messages in this group. Check for new messages
    * since the last time this method was called and retrieve them
    */
   public Message[] getMessages()
      throws MessagingException
   {
      throw new MessagingException("Root doesn't contain messages");
   }


   public char getSeparator()
      throws MessagingException
   {
      return '.';
   }

   /**
    * Type of the root is always HOLDS_FOLDERS
    */
   public int getType()
      throws MessagingException
   {
      return HOLDS_FOLDERS;
   }

   /**
    * Whether there are new messages
    */
   public boolean hasNewMessages()
      throws MessagingException
   {
      throw new MessagingException("Root doesn't contain messages");
   }


   /**
    * This gets all newsgroups which match the specified pattern.
    */
   public Folder[] list(String pattern)
      throws MessagingException
   {
      // TODO: Pay attention to pattern.
      return getServer().getNewsgroups();
   }

   /**
    * This gets all newsgroups which are subscribed.
    */
   public Folder[] listSubscribed(String pattern)
      throws MessagingException
   {
      return getServer().getSubscribedGroups();
   }

   /**
    * Open the newsgroup
    */
   public void open(int mode)
      throws MessagingException
   {
      if (!isOpen())
      {
         m_open = true;
         notifyConnectionListeners(ConnectionEvent.OPENED);
      }
   }
}


//
// $Log: not supported by cvs2svn $
// Revision 1.2  2000/06/14 21:33:02  briand
// Added support for progress monitoring. Numerous fixes & upgrades.
//
// Revision 1.1  2000/02/22 23:49:39  briand
// New news store implementation that sits on top of clients. Initial
// revision.
//
// Revision 1.3  1999/11/11 21:26:39  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.2  1999/10/16 16:47:44  briand
// Provide implementation of list(), so getting the list of all
// groups on a server can be tested now.
//
// Revision 1.1  1999/08/03 19:16:55  briand
// Ummm. Wrote this months ago
//
//
//