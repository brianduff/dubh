// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: RootGroup.java,v 1.1 1999-08-03 19:16:55 briand Exp $
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
import javax.mail.event.ConnectionEvent;

import java.util.*;

/**
 * A Javamail folder representing a newsgroup in the NNTP protocol.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: RootGroup.java,v 1.1 1999-08-03 19:16:55 briand Exp $
 */
class RootGroup extends Newsgroup
{
    
   public RootGroup(Store s)
   {
      super("/", s);
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
      return ((NNTPStore)store).getFolder(name);
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
         m_open = true;
         notifyConnectionListeners(ConnectionEvent.OPENED);
      }
   }
}


//
// $Log: not supported by cvs2svn $
//
//