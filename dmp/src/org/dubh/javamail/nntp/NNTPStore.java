// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NNTPStore.java,v 1.6 1999-11-11 21:26:39 briand Exp $
//   Copyright (C) 1997, 1999  Brian Duff
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

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.StringTokenizer;
import java.net.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.InternetHeaders;

import org.javalobby.dju.misc.Debug;
import org.javalobby.dju.misc.StringUtils;

/**
 * A JavaMail store for the Network News Transfer Protocol (NNTP), as
 * defined in RFC 977.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NNTPStore.java,v 1.6 1999-11-11 21:26:39 briand Exp $
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
       
       
   private final static int MAX_FETCHSIZE = 1024;    
   
   /**
    * Should be configurable. Not yet implemented.
    */
   private final static int MAX_TIMEOUT_RETRY = 10;
   
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

   /**
    * If you recieve a SERVER_INTERNAL_ERROR, you can call this method
    * to determine whether the server has timed out.
    */
   protected boolean hasServerTimedOut()
   {
      return (m_response.toLowerCase().indexOf(TIMEOUT) > 0);
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
      if (!g.equals(m_currentGroup))
      {
         try
         {
            sendCommand(NNTPCommands.GROUP, g.getName());
            int response = getResponse();
            switch (response)
            {
               case NNTPErrorCodes.GROUP_SELECTED:
                  m_currentGroup = g;
                  // Set the article counts of the group
                  setArticleCounts(g);
                  break;
               case NNTPErrorCodes.NO_SUCH_GROUP:
                  throw new MessagingException(NNTPErrorCodes.ERR_NNTP+m_response);
               case NNTPErrorCodes.SERVER_INTERNAL_ERROR:
                  if (hasServerTimedOut())
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
                     throw new NNTPException(getLastResponse(), response);
                  }
                  break;
               default:
                  throw new NNTPException(getLastResponse(), response);
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
      Newsgroup oldgroup = m_currentGroup;
      
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
    * Retrieve a message from the list of messages. If it doesn't exist,
    * create a new NNTPMessage instance and store it.
    */  
   NNTPMessage getNNTPMessage(Newsgroup newsgroup, String message_id) 
      throws MessagingException 
   {
      NNTPMessage msg = (NNTPMessage)m_messages.get(message_id);
      if (msg==null)
      {
         msg = new NNTPMessage(newsgroup, message_id);
         m_messages.put(message_id, msg);
      }
      return msg;
   }
   
   /**
    * Get all messages in the specified newsgroup.
    * @param g The group to get messages in
    * @return an array of messages
    * @throws javax.mail.MessagingException if something goes wrong.
    */
   NNTPMessage[] getMessages(Newsgroup g)
      throws MessagingException
   {
      // TODO: Support XOVER
      return getNewMessages(g, new Date(0));
   }
   
   /**
    * Get new messages since the specified date.
    * @param g The newsgroup to get new messages for
    * @param since The local date to get new messages since. This will be
    *              converted to a GMT date string before being sent to the server
    * @return An array of new messages in the group since the specified date
    * @throws javax.mail.MessagingException if something goes wrong.
    */
   NNTPMessage[] getNewMessages(Newsgroup g, Date since)
      throws MessagingException
   {
      Vector vecNewMessages = new Vector();
      try
      {
         sendCommand(NNTPCommands.NEWNEWS, g.getName()+" "+getGMTDate(since));
         int response = getResponse();
         
         switch (response)
         {
            case NNTPErrorCodes.LIST_ARTICLES:
               String strCurrentLine=m_in.readLine();
               
               while (strCurrentLine != null && !".".equals(strCurrentLine))
               {
                  NNTPMessage msg = getNNTPMessage(g, strCurrentLine);
                  vecNewMessages.addElement(msg);
               
               
                  strCurrentLine = m_in.readLine();
                  
               }
               break;
            default:
               throw new NNTPException(getLastResponse(), response);
               
         }
      }
      catch (IOException ioe)
      {
         throw new MessagingException(NNTPErrorCodes.ERR_IOEXCEPTION);
      }
      
      NNTPMessage[] msgs = new NNTPMessage[vecNewMessages.size()];
      vecNewMessages.copyInto(msgs);
      return msgs;
   }
   
   /**
    * Given a number < 100, returns a double digit
    * padded version of the number as a string.
    * e.g. 5 -> "05", 10 -> "10"
    */
   private String getZeroPaddedNumeric(int num)
   {
      if (num > 99 || num < 0) 
         throw new IllegalArgumentException("Number for getZeroPaddedNumeric must be < 100");
      return (num < 10) ? "0"+num : Integer.toString(num);
   }
   
   /**
    * Get an NNTP valid GMT date string from a local Date instance.
    * e.g. 1/1/99 14:52 GMT -> "990101 145200 GMT"
    * 12/12/00 01:00 EST -> "001212 060000 GMT"
    */
   String getGMTDate(Date date) 
   {
      final String TIMEZONE="GMT";
   
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
      
      StringBuffer buffer = new StringBuffer();
      int n;
      
      buffer.append(getZeroPaddedNumeric(calendar.get(Calendar.YEAR)%100));
      buffer.append(getZeroPaddedNumeric(calendar.get(Calendar.MONTH)+1)); 
      buffer.append(getZeroPaddedNumeric(calendar.get(Calendar.DAY_OF_MONTH)));
      buffer.append(" ");
      buffer.append(getZeroPaddedNumeric(calendar.get(Calendar.HOUR_OF_DAY)));
      buffer.append(getZeroPaddedNumeric(calendar.get(Calendar.MINUTE)));
      buffer.append(getZeroPaddedNumeric(calendar.get(Calendar.SECOND)));
      buffer.append(" "+TIMEZONE);

      return buffer.toString();
   }   
   
   /**
    * Get all headers for the specified message
    * @param message The message to retrieve headers for
    * @return an InternetHeaders object containing the headers
    * @throws javax.mail.MessagingException if the NNTP server returned an unexpected response code.
    */
   InternetHeaders getHeaders(NNTPMessage message)
      throws MessagingException
   {
      try
      {
         String strMessageID = resolveMessageID(message);
         
         // Get the header
         sendCommand(NNTPCommands.HEAD, strMessageID);
         
         int response = getResponse();
         
         switch (response)
         {
            case NNTPErrorCodes.ARTICLE_RETRIEVED_HEAD:
               return new InternetHeaders(new MessageInputStream(m_in));
            case NNTPErrorCodes.SERVER_INTERNAL_ERROR:
               if (hasServerTimedOut())
               {
                  close();
                  connect();
                  return getHeaders(message);  
               }
               // Fall through to the default case...
            default:
               throw new NNTPException(getLastResponse(), response);
         }
      }
      catch (IOException ioe)
      {
         throw new MessagingException(NNTPErrorCodes.ERR_IOEXCEPTION);
      }
   }
   
   /**
    * Gets a message identifier. This method will first attempt to retrieve
    * the Message-Id field of the message. If this field doesn't exist
    * (hasn't been read in yet), the current group is switched to the containing
    * group of the article and the message number of the message is returned.
    * Either one of these identifiers can be used in NNTP commands such as
    * BODY or HEAD.
    * @return either a Message-Id or a message number converted to a string.
    */
   protected String resolveMessageID(NNTPMessage message)
      throws MessagingException
   {
      String strMessageID = message.getMessageId();
      if (strMessageID == null)
      {
         Newsgroup ngContainer = (Newsgroup)message.getFolder();
         if (m_currentGroup != ngContainer)
            openGroup(ngContainer);
         
         return Integer.toString(message.getMessageNumber());
      }
      return strMessageID;
   }
   
   /**
    * Get the content for the specified message
    * @param message The message you want to retrieve the body for
    * @returns A byte array of the body of the message
    * @throws java.io.IOException if a fundamental IO Error occurred
    * @throws javax.mail.MessagingException if the server returned an unexpected code
    */
   byte[] getContent(NNTPMessage message)
      throws  MessagingException
   {
      try
      {
         String strMessageID = resolveMessageID(message);
         
         // Get the message body
         sendCommand(NNTPCommands.BODY, strMessageID);
         
         int response = getResponse();
         
         switch (response)
         {
            case NNTPErrorCodes.ARTICLE_RETRIEVED_BODY:
               int length;
               byte b[] = new byte[MAX_FETCHSIZE];
               
               MessageInputStream misBody = new MessageInputStream(m_in);
               ByteArrayOutputStream baosStorage = new ByteArrayOutputStream();
               
               while ((length = misBody.read(b, 0, MAX_FETCHSIZE))!=-1)
               baosStorage.write(b, 0, length);
               
               return baosStorage.toByteArray();
            case NNTPErrorCodes.SERVER_INTERNAL_ERROR:
               if (hasServerTimedOut())
               {
                  close();
                  connect();
                  return getContent(message);
               }
               throw new NNTPException(getLastResponse(), response);
            default:
               throw new NNTPException(getLastResponse(), response);
         }
      }
      catch (IOException ioe)
      {
         throw new MessagingException(NNTPErrorCodes.ERR_IOEXCEPTION);
      }
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
      g.setArticleCount(StringUtils.stringToInt(StringUtils.getWord(lastReply, 1)));
      g.setFirstArticle(StringUtils.stringToInt(StringUtils.getWord(lastReply, 2)));
      g.setLastArticle(StringUtils.stringToInt(StringUtils.getWord(lastReply, 3)));
  
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
    * constants in {@link org.javalobby.javamail.nntp.NNTPErrorCodes}. After
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
      Newsgroup g = (Newsgroup) m_groups.get(name);
      
      if (g == null)
      {
         g = new Newsgroup(name, this);
         m_groups.put(name, g);
      }
      
      return g;
   }
   
   
   /**
    * Get all newsgroups available on the server.
    */
   Newsgroup[] getNewsgroups() throws MessagingException 
   {
      Vector vecGroups = new Vector();
      try {
         sendCommand(NNTPCommands.LIST);   // TODO Support extended syntax.
         int response = getResponse();
      
         switch (response) 
         {
            case NNTPErrorCodes.NEWSGROUP_LIST:
               String curLine;
               curLine = m_in.readLine();
               while (curLine != null && !".".equals(curLine))
               {
                  StringTokenizer tok = new StringTokenizer(curLine, " ");
                  // Line is of form 
                  // GroupName FIRST_NUM LAST_NUM POSTING_ALLOWED
                  String strName = tok.nextToken();
                  int nLast = Integer.parseInt(tok.nextToken());
                  int nFirst = Integer.parseInt(tok.nextToken());
                  boolean bPosting = ("y".equals(tok.nextToken().toLowerCase()));
                  
                  Newsgroup grp = (Newsgroup)getFolder(strName);
                  grp.setFirstArticle(nFirst);
                  grp.setLastArticle(nLast);
                  grp.setPostingOK(bPosting);
                  vecGroups.addElement(grp);
                  
                  curLine = m_in.readLine();
               
               }
               break;
            case NNTPErrorCodes.SERVER_INTERNAL_ERROR:
               if (hasServerTimedOut())
               {
                  close();
                  connect();
                  return getNewsgroups();
               }
               // Fall through to default
            default:
               throw new NNTPException(getLastResponse(), response);
         }
      } 
      catch (IOException e) 
      {
         throw new MessagingException(NNTPErrorCodes.ERR_IOEXCEPTION, e);
      } 
      catch (NumberFormatException e) 
      {
         throw new MessagingException("Number format wrong in server response", e);
      }
      
      Newsgroup[] groups = new Newsgroup[vecGroups.size()]; 
      vecGroups.copyInto(groups);
      
      return groups;
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
      System.err.println(m_hostName + (toServer ? "<<" : ">>") + s);
      System.err.flush();
   }
   
   
   
   
   
}


//
// $Log: not supported by cvs2svn $
// Revision 1.5  1999/10/17 17:07:42  briand
// Fix slight indexing bug in getting article counts.
//
// Revision 1.4  1999/10/16 16:47:07  briand
// Add lots more code; can now list groups on the server (woo).
//
// Revision 1.3  1999/08/03 19:15:41  briand
// More work on filling out missing method bodies.
//
// Revision 1.2  1999/06/08 22:45:40  briand
// Add some more methods (with big TODOs in them)
//
// Revision 1.1.1.1  1999/06/06 23:37:38  briand
// Dubh Mail Protocols initial revision.
//
//