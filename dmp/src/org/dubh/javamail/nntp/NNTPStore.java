// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NNTPStore.java,v 1.3 1999-08-03 19:15:41 briand Exp $
//   Copyright (C) 1997, 1999  Brian Duff
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

import java.util.ArrayList;
import java.util.Date;
import java.net.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.InternetHeaders;

import dubh.utils.misc.Debug;
import dubh.utils.misc.StringUtils;

/**
 * A JavaMail store for the Network News Transfer Protocol (NNTP), as
 * defined in RFC 977.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NNTPStore.java,v 1.3 1999-08-03 19:15:41 briand Exp $
 */
public class NNTPStore extends Store
{
   // This implementation mostly came from the old dubh.apps.newsagent.nntp.NNTPServer
   // class from NewsAgent.

   private Hashtable m_messages;
   
   private Hashtable m_groups;

   private transient Socket m_connection;
   private transient CRLFInputStream m_in;    // Input stream  
   private transient CRLFOutputStream m_out;        // Output stream
   
   private transient PrintWriter m_debugStream; // Debug stream
   
   private String m_hostName;
   private int    m_port;
   
   private final static int DEFAULT_PORT = 119;
   private final static String TIMEOUT = "timeout";
       
   // The last response to an NNTP command.
   private String m_response;
   private int  m_code;
   
   private boolean m_postingOK;
   
   private Newsgroup m_currentGroup;
       
   /**
    * Construct an NNTP Store.
    */    
   public NNTPStore(Session session, URLName urlname) 
   {
      super(session, urlname);
      m_groups = new Hashtable();
      m_messages = new Hashtable();
   }
       
       
   /**
    * Connect to an NNTP server.
    */
   protected boolean protocolConnect(String host, int port, String userName, String password)
      throws MessagingException
   {
      // Default the port if necessary
      if (port < 1)
      {
         m_port = DEFAULT_PORT;
      }
      else
      {
         m_port = port;
      }
      
      m_hostName = host;
      
      try
      {
         // Create a socket to the host
         m_connection = new Socket(m_hostName, m_port);
         m_out = new CRLFOutputStream(m_connection.getOutputStream());
         m_in = new CRLFInputStream(m_connection.getInputStream());
         
         if (isConnected())
         {
            int response = getResponse();
            
            if (response == NNTPErrorCodes.READY_NOPOSTING)
            {
               m_postingOK = false;
            }
            else if (response == NNTPErrorCodes.READY_POSTOK)
            {
               m_postingOK = true;
            }
            else 
            {
               // Connection failed
               return false;
            }
         }
         
         // Authenticate if necessary
         if (userName != null && password != null)
         {
            sendCommand(NNTPCommands.AUTHINFO_USER, userName);
            if (getResponse() >= 400)
            {
               return false;
            }
            sendCommand(NNTPCommands.AUTHINFO_PASS, password);
            getResponse();
            // TODO : These should check properly
         }
         
         return true;
      }
      catch (UnknownHostException uhe)
      {
         throw new MessagingException(NNTPErrorCodes.ERR_UNKNOWN_HOST);
      }
      catch (IOException ioe)
      {
         throw new MessagingException(NNTPErrorCodes.ERR_IOEXCEPTION);
      }
      
   
   }

   /**
    * Close the connection to the NNTP server
    */
   public void close()
      throws MessagingException
   {
      try
      {
         sendCommand(NNTPCommands.QUIT);
         getResponse();
         m_in.close();
         m_out.close();
         m_connection.close();
         m_in = null;
         m_out = null;
         m_connection = null;
      }
      catch (IOException ioe)
      {
         throw new MessagingException(NNTPErrorCodes.ERR_IOEXCEPTION);
      }
 
   }

//////////////////////////////////////////////////////////////////////////////
// NNTP Commands
//////////////////////////////////////////////////////////////////////////////

   /**
    * Close the specified newsgroup.
    */
   void closeGroup(Newsgroup g)
      throws MessagingException
   {
      // do nothing for now.
   }
   
   /**
    * Open the specified newsgroup.
    */
   void openGroup(Newsgroup g)
      throws MessagingException
   {
      if (g == null) throw new MessagingException("Attempt to select a null group");
      if (!g.equals(m_group))
      {
         try
         {
            sendCommand(NNTPCommands.GROUP, g.getName());
            int response = getResponse();
            switch (response)
            {
               case NNTPErrorCodes.GROUP_SELECTED:
                  m_group = g;
                  // Set the article counts of the group
                  setArticleCounts(g);
                  break;
               case NNTPErrorCodes.NO_SUCH_GROUP:
                  throw new MessagingException(NNTPErrorCodes.ERR_NNTP+m_response);
               case NNTPErrorCodes.SERVER_INTERNAL_ERROR:
                  if (m_response.toLowerCase().indexOf(TIMEOUT) > 0)
                  {
                     if (Debug.TRACE_LEVEL_1)
                     {
                        Debug.println(1, this, "Server seems to have timed out. Reconnecting");
                     }
                     close();
                     connect();
                     openGroup(g);
                  }
                  else
                  {
                     throw new MessagingException(NNTPErrorCodes.ERR_NNTP+m_response);
                  }
                  break;
               default:
                  throw new MessagingException(NNTPErrorCodes.ERR_NNTP+m_response);
            }
         }
         catch (IOException ioe)
         {
            if (Debug.TRACE_LEVEL_1)
            {
               Debug.println(1, this, "IO Exception opening newsgroup.");
               Debug.printException(1, this, ioe);
            }
         }
      }
   }
   
   /**
    * Verify that a group actually exists on the server
    */
   boolean groupExists(Newsgroup g)
      throws MessagingException
   {
      Newsgroup oldgroup = m_group;
      
      try
      {
         openGroup(g);
      }
      catch (MessagingException me)
      {
         if (getLastResponseCode() == NNTPErrorCodes.NO_SUCH_GROUP)
         {
            if (oldgroup != null) openGroup(oldgroup);
            return false;
         
         }
         else
         {
            if (oldgroup != null) openGroup(oldgroup);
            throw me;
         }
      }
      if (oldgroup != null) openGroup(oldgroup);
      return true;
   }
   
   ArrayList getMessages(Newsgroup g)
   {
      return null; // TODO
   }
   
   NNTPMessage[] getNewMessages(Newsgroup g, Date since)
   {
      return null; // TODO
   }
   
   /**
    * Get all headers for the specified message
    */
   InternetHeaders getHeaders(NNTPMessage message)
   {
      return null; // TODO
   }
   
   /**
    * Get the content for the specified message
    */
   byte[] getContent(NNTPMessage message)
   {
      return null; // TODO
   }
   
   /**
    * Does this server support the specified XOver header?
    */
   boolean supportsXOverHeader(String header)
   {
      return false; // TODO
   }
   
   /**
    * Using the most recent status line (must be a GROUP_SELECTED
    * message), update the newsgroup object's internal article
    * counts.
    */
   private void setArticleCounts(Newsgroup g)
   {
      // The group reply message contains a string of the form:
      // 211 <count> <first> <last> <newsgroup-name>
      String lastReply = getLastResponse();
      g.setArticleCount(StringUtils.stringToInt(StringUtils.getWord(lastReply, 2)));
      g.setFirstArticle(StringUtils.stringToInt(StringUtils.getWord(lastReply, 3)));
      g.setLastArticle(StringUtils.stringToInt(StringUtils.getWord(lastReply, 4)));
  
   }
   
   

//////////////////////////////////////////////////////////////////////////////
// Utilities for talking to the server
//////////////////////////////////////////////////////////////////////////////

   private void sendCommand(String command, String argument)
      throws IOException
   {
      sendCommand(command + " " + argument);
   }

   /**
    * Sends a message through the socket to the NNTP Server.
    * @param mesg The text of the message to send
    * @throws java.io.IOException An I/O or network error occurred
    */
   private void sendCommand(String mesg) 
      throws IOException 
   {
      if (isConnected()) 
      {
        echo(true, mesg);
        m_out.println(mesg);
        m_out.flush();
      }
   }
   
   /**
    * Get the code from the last response. Returns -1
    * if the last code is unparseable.
    */
   private int getLastResponseCode()
   {
      return m_code;
   }
   
   private String getLastResponse()
   {
      return m_response;
   }
   
   /**
    * Gets a simple NNTP response code. This should be one of the 
    * constants in {@link dubh.mail.nntp.NNTPErrorCodes}. After
    * calling this method, the class variable m_response contains
    * the full response text.
    */
   private int getResponse()
      throws IOException
   {
      m_response = m_in.readLine();
      echo(false, m_response);
      //
      // Get the status code
      //
      int code;
      try
      {
         m_code = Integer.parseInt(m_response.substring(0, 3));
         return m_code;
      }
      catch (Throwable t)
      {
         throw new ProtocolException("Corrupted status line: "+m_response);
      }
      
   }


   /**
    * Determines whether the socket connection is currently open
    * @returns a boolean value indicating whether the connection is open
    */
   public boolean isConnected() 
   {
      return (m_connection != null && m_in != null && m_out != null);
   }


   /**
    * Get the root folder. This is a placeholder and shouldn't be displayed
    */
   public Folder getDefaultFolder()
      throws MessagingException
   {
      return new RootGroup(this);
   }
   
   /**
    * Get a named newsgroup.
    */
   public Folder getFolder(String name)
      throws MessagingException
   {
      return getNewsgroup(name);
   }
   
   /**
    * Get a newsgroup. The URL should be of the form
    * <pre>
    *   news://news.server.com/group.name
    */
   public Folder getFolder(URLName urlName)
      throws MessagingException
   {
      if (urlName.getProtocol().equals("news"))
      {
         return getNewsgroup(urlName.getFile());
      }
      throw new IllegalArgumentException("URL must be for protocol 'news'");
   }
   
   /**
    * Get the specified newsgroup if it exists. Otherwise, create a new
    * Newsgroup object for it.
    */
   private Newsgroup getNewsgroup(String name)
   {
      Newsgroup g = (Newsgroup) m_newsgroups.get(name);
      
      if (g == null)
      {
         g = new Newsgroup(name, this);
         m_newsgroups.put(name, g);
      }
   }

//////////////////////////////////////////////////////////////////////////////
// Attached streams
//////////////////////////////////////////////////////////////////////////////

   /**
    * Attach a printwriter, which recieves an echo of all NNTP commands and
    * replies. There can only every be one attached stream per store.
    */
   public void attachDebugStream(PrintWriter p)
   {
      m_debugStream = p;
   }

   /**
    * Sends a message to an attached debug stream
    * @param s A string to send to any debug stream attached to the store.
    */
   private void echo(boolean toServer, String s) 
   {
      if (m_debugStream != null) 
      {
         String display = m_hostName + (toServer ? "<<" : ">>") + s;
         m_debugStream.println(display);
      }
   }
}


//
// $Log: not supported by cvs2svn $
// Revision 1.2  1999/06/08 22:45:40  briand
// Add some more methods (with big TODOs in them)
//
// Revision 1.1.1.1  1999/06/06 23:37:38  briand
// Dubh Mail Protocols initial revision.
//
//