// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NNTPStore.java,v 1.1.1.1 1999-06-06 23:37:38 briand Exp $
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

import java.net.*;
import java.io.*;
import javax.mail.*;

/**
 * A JavaMail store for the Network News Transfer Protocol (NNTP), as
 * defined in RFC 977.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NNTPStore.java,v 1.1.1.1 1999-06-06 23:37:38 briand Exp $
 */
public class NNTPStore 
{
   // This implementation mostly came from the old dubh.apps.newsagent.nntp.NNTPServer
   // class from NewsAgent.

   private transient Socket m_connection;
   private transient LineNumberReader m_in;    // Input stream      // TODO CRLF
   private transient PrintWriter m_out;        // Output stream
   
   private transient PrintWriter m_debugStream; // Debug stream
   
   private String m_hostName;
   private int    m_port;
   
   private final static int DEFAULT_PORT = 119;
   private final static String TIMEOUT = "timeout";
       
   // The last response to an NNTP command.
   private String m_response;
   private int  m_code;
   
   private boolean m_postingOK;
   
   private Newsgroup m_group;
       
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
         m_out = new PrintWriter(new OutputStreamWriter(m_connection.getOutputStream()));
         m_in = new LineNumberReader(new InputStreamReader(m_connection.getInputStream()));
         
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
            sendMessage(NNTPCommands.AUTHINFO_USER, userName);
            if (getResponse() >= 400)
            {
               return false;
            }
            sendMessage(NNTPCommands.AUTHINFO_PASS, password);
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
         sendMessage(NNTPCommands.QUIT);
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
         sendMessage(NNTPCommands.GROUP, g.getName());
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
               break;
            case NNTPErrorCodes.SERVER_INTERNAL_ERROR:
               if (m_response.toLowerCase().contains(TIMEOUT))
               {
                  if (Debug.TRACE_LEVEL_1)
                  {
                     Debug.trace(1, this, "Server seems to have timed out. Reconnecting");
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
   
   
   /**
    * Using the most recent status line (must be a GROUP_SELECTED
    * message), update the newsgroup object's internal article
    * counts.
    */
   private void setArticleCounts(Newsgroup g)
   {
      // The group reply message contains a string of the form:
      // 211 <count> <first> <last> <newsgroup-name>
      g.setArticleCount(StringUtils.stringToInt(getWord(lastReply, 2)));
      g.setFirstArticle(StringUtils.stringToInt(getWord(lastReply, 3)));
      g.setLastArticle(StringUtils.stringToInt(getWord(lastReply, 4)));
  
   }
   
   

//////////////////////////////////////////////////////////////////////////////
// Utilities for talking to the server
//////////////////////////////////////////////////////////////////////////////

   private void sendMessage(String command, String argument)
      throws IOException
   {
      sendMessage(command + " " + argument);
   }

   /**
    * Sends a message through the socket to the NNTP Server.
    * @param mesg The text of the message to send
    * @throws java.io.IOException An I/O or network error occurred
    */
   private void sendMessage(String mesg) 
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
   private boolean isConnected() 
   {
      return (m_connection != null && m_in != null && m_out != null);
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
//