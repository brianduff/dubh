// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NetworkNewsClient.java,v 1.4 2001-02-11 02:52:48 briand Exp $
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TimeZone;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;


import java.net.Socket;
import java.net.UnknownHostException;

import java.text.MessageFormat;

import org.dubh.dju.progress.ProgressMonitor;
import org.dubh.dju.progress.ProgressMonitorSupport;


/**
 * This is an implementation of NewsClient that is based on the NNTP protocol
 * over a TCP/IP socket connection; i.e. it talks to a "real" news server over
 * the internet.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NetworkNewsClient.java,v 1.4 2001-02-11 02:52:48 briand Exp $
 */
public class NetworkNewsClient extends AbstractNewsClient
   implements NewsStatusCodes, NNTPCommands, ProgressMonitorSupport
{
   public static final String
      ERR_BADPORT               = "BadPortNumber",
      ERR_BADHOST               = "BadHostname",
      ERR_CONNECTION_LOST       = "ConnectionLost",
      ERR_MALFORMED_COMMAND     = "MalformedCommand",
      ERR_MALFORMED_STATUS      = "MalformedStatus",
      ERR_IO_FAILURE            = "IOFailure",
      ERR_CONNECTION_REFUSED    = "ConnectionRefused",
      ERR_UNABLE_TO_CONNECT     = "UnableToConnect",
      ERR_UNKNOWN_HOST          = "UnknownHost",
      ERR_FAILURE_TO_CLOSE      = "FailureToCloseConnection",
      ERR_BAD_USERPASS          = "BadUserNameOrPassword",
      ERR_BAD_GROUPNAME         = "BadGroupName",
      ERR_INVALID_DATE          = "InvalidDate",
      ERR_BAD_MESSAGE           = "BadMessage",
      ERR_BAD_MSGID             = "BadMessageId";

   private int m_nLastStatus;
   private String m_strLastStatusMessage;

   private Socket m_sckConnection;
   private CRLFInputStream m_isIn;
   private CRLFOutputStream m_osOut;

   private String m_strHostName;

   private String m_curGroup = null;

   private HashMap m_hashGroupDescriptions;

   private boolean m_bPostingOK;

   private ProgressMonitor m_progressMonitor;

   /**
    * Create the network news client.
    */
   public NetworkNewsClient()
   {
      m_hashGroupDescriptions = new HashMap();
   }

   /**
    * Set the progress monitor on this client. The monitor will be notified
    * of changes in progress.
    */
   public void setProgressMonitor(ProgressMonitor pm)
   {
      m_progressMonitor = pm;
   }

   private ProgressMonitor getProgressMonitor()
   {
      return m_progressMonitor;
   }

///////////////////////////////////////////////////////////
// Error Handling Utilities
///////////////////////////////////////////////////////////

   // A bunch of utilities for checking method parameters

   private void doError(String msgId)
      throws NewsClientException
   {
      throw new NewsClientException(msgId);
   }

   private void checkbool(boolean b, String message)
      throws NewsClientException
   {
      if (b)
      {
         doError(message);
      }
   }

   private void checknull(Object o, String message)
      throws NewsClientException
   {
      checkbool((o==null), message);
   }

   private void checkconn() throws NewsClientException
   {
      checkbool(!isConnected(), ERR_CONNECTION_LOST);
   }

   private void checkstring(String param, String message)
      throws NewsClientException
   {
      checkbool((param == null), message);
      checkbool((param.trim().length()==0), message);
   }

///////////////////////////////////////////////////////////
// Server communication utilities
///////////////////////////////////////////////////////////

   protected Socket getConnection()
   {
      return m_sckConnection;
   }

   /**
    * Get the input stream
    */
   protected CRLFInputStream getInStream()
   {
      return m_isIn;
   }

   /**
    * Get the output stream
    */
   protected CRLFOutputStream getOutStream()
   {
      return m_osOut;
   }

   /**
    * Send a command to the server. Sends the specified command and
    * reads the status return code
    * @param command The NNTP command to send.
    * @param arguments any arguments you want to pass to the command
    *   separated by spaces.
    *
    * @throws NewsClientException see {@link #sendCommand(String)} for
    *   circumstances.
    */
   private void sendCommand(String command, String arguments)
      throws NewsClientException
   {
      sendCommand(command + " " + arguments);
   }

   /**
    * Send a command to the server. Sends the specified command and
    * reads the status return code.
    * @param command a full command including any arguments.
    *
    * @throws NewsClientException if an IO error occurs while reading to
    *   or writing from the server, if the command is null or zero length,
    *   if no connection is present, or if the news server returns a status
    *   line that is garbled.
    */
   private void sendCommand(String command)
      throws NewsClientException
   {
      try
      {
         // TODO: Check for timeout
         checkconn();
         checkstring(command, ERR_MALFORMED_COMMAND);

         getOutStream().println(command);
         getOutStream().flush();

         echoCommand(command);

         readStatus();
      }
      catch (IOException ioe)
      {
         doError(ERR_IO_FAILURE);
      }

   }

   /**
    * Read the status line. Don't normally call this method directly. Calling
    * {@link #sendCommand(String)} causes the status line to be retrieved.
    * Thereafter, you can call {@link #getLastStatus()} or {@link
    * #getLastStatusMessage()} to get the retrieved message.
    */
   private void readStatus()
      throws IOException, NewsClientException
   {
      String strResponse = getInStream().readLine();

      try
      {
         String strCode = strResponse.substring(0, 3);
         String strMsg  = strResponse.substring(4);

         m_nLastStatus = Integer.parseInt(strCode);
         m_strLastStatusMessage = strMsg;

         echoResponse();
      }
      catch (Exception e)
      {
         doError(ERR_MALFORMED_STATUS);
      }

   }

   /**
    * Get the last status message retrieved from the server. This is a
    * status message without its corresponding number, which you can
    * retrieve by calling {@link #getLastStatus()}.
    *
    * @return a message from the server about the last command.
    */
   public String getStatusText()
   {
      return m_strLastStatusMessage;
   }

   /**
    * Get the last status code retrieved from the server.
    *
    * @return one of the NewsStatusCode constants.
    */
   public int getStatus()
   {
      return m_nLastStatus;
   }

///////////////////////////////////////////////////////////
// Connection & Disconnection
///////////////////////////////////////////////////////////

   /**
    * Open a physical connection to the specified server and port.
    *
    * @param hostName the host name to connect to. Must not be null
    *   or zero length.
    * @param portNumber the port number to connect to. Must be > 0
    *
    * @throws NewsClientException if the host is null, zero length or
    *   is unknown, if the port number is <=0, if an IO error occurs
    *   while trying to connect or if the server returns a
    *   status code other than READY_POSTOK or READY_NOPOSTING
    *   after connection.
    *
    */
   public void connect(String hostName, int portNumber)
      throws NewsClientException
   {
      if (getProgressMonitor() != null)
      {
         getProgressMonitor().setFixedLengthTask(false);
         getProgressMonitor().setMessage("Attempting to connect to "+hostName+"...");
      }
      checkstring(hostName, ERR_BADHOST);
      checkbool((portNumber <= 0), ERR_BADPORT);

      try
      {
         // Do all the physical networking stuff
         m_sckConnection = new Socket(hostName, portNumber);
         m_osOut = new CRLFOutputStream(m_sckConnection.getOutputStream());
         m_isIn = new CRLFInputStream(m_sckConnection.getInputStream());

         // Verify the connection was opened successfully
         checkconn();

         m_strHostName = hostName;

         // Get the "welcome" server response.
         readStatus();

         int nStatus = getStatus();

         if (getProgressMonitor() != null)
         {
            getProgressMonitor().setMessage("Connection to "+hostName+" established");
         }
         // Use the welcome status to figure out whether posting is OK
         // on the server.
         if (nStatus == READY_NOPOSTING)
         {
            m_bPostingOK = false;
         }
         else if (nStatus == READY_POSTOK)
         {
            m_bPostingOK = true;
         }
         else
         {
            m_strHostName = null;
            doError(ERR_CONNECTION_REFUSED);
         }
      }
      catch (UnknownHostException uhe)
      {
         m_strHostName = null;
         doError(ERR_UNKNOWN_HOST);
      }
      catch (IOException ioe)
      {
         m_strHostName = null;
         doError(ERR_UNABLE_TO_CONNECT);

      }
      finally
      {
         if (getProgressMonitor() != null)
         {
            // Not sure if we should do this here.
            getProgressMonitor().setFinished(true);
         }
      }

   }

   /**
    * Get the hostname for the current or previous connection.
    */
   protected String getHostName()
   {
      return m_strHostName;
   }

   /**
    * Determine whether a connection is currently open.
    *
    * @return true if a connection currently exists.
    */
   public boolean isConnected()
   {
      // Provided the protocol is followed properly, this should always
      // be the case.
      return (m_sckConnection != null);
   }

   /**
    * Close the connection.
    *
    * @throws NewsClientException if the physical closing of network
    *   connections failed for some reason.
    */
   public void disconnect()
      throws NewsClientException
   {
      // We don't really give a rat's arse if the server fails during the
      // quit command, so we'll squash any exceptions.
      try
      {
         sendCommand(QUIT);
      }
      catch (NewsClientException nce)
      {
         // Squashed
      }

      // But if we fail to close the actual physical connections something
      // dodgy is going on, so we'll catch that and report it.
      try
      {
         getInStream().close();
         getOutStream().close();
         getConnection().close();
         m_isIn = null;
         m_osOut = null;
         m_sckConnection = null;
      }
      catch (IOException ioe)
      {
         doError(ERR_FAILURE_TO_CLOSE);
      }
   }

///////////////////////////////////////////////////////////
// NNTP Commands
///////////////////////////////////////////////////////////

   /**
    * Attempt to authorize using the specified username and
    * password. This only presently supports the authinfo
    * type of authentication.
    *
    * @param username a user name. Must not be null or zero
    *   length.
    * @param password a password. Must not be null or zero
    *   length.
    *
    */
   public void authorize(String username, String password)
      throws NewsClientException
   {
      checkconn();
      checkstring(username, ERR_BAD_USERPASS);
      checkstring(password, ERR_BAD_USERPASS);

      sendCommand(AUTHINFO_USER, username);
      sendCommand(AUTHINFO_PASS, password);

      // If the connection hasn't been lost, lose it now. Most
      // news servers do close the connection if authorization fails,
      // so we make it consistent.
      if (getStatus() == PERMISSION_DENIED)
      {
         // ISSUE: BD not sure if this is the right thing to do; this
         // might change the status code and confuse NewsStore.
         disconnect();
      }
   }


   /**
    * Retrieve the full article for a particular message id.
    *
    * @param msgId The message id. Can either be a numeric id
    *   or a string message-id
    *
    * @return an input stream. You should check the status before
    *   attempting to read the message from the stream.
    */
   public InputStream getArticle(String msgId)
      throws NewsClientException
   {
      checkconn();
      checkstring(msgId, ERR_BAD_MSGID);

      sendCommand(ARTICLE, msgId);

      return new MessageInputStream(getInStream());
   }


   /**
    * Retrieve the body text for a particular message id.
    *
    * @param msgId The message id. Can either be a numeric id
    *   or a string message-id.
    *
    * @return an input stream for the message body. Check the
    *   status before using the stream.
    */
   public InputStream getBody(String msgId)
      throws NewsClientException
   {
      checkconn();
      checkstring(msgId, ERR_BAD_MSGID);

      sendCommand(BODY, msgId);

      return new MessageInputStream(getInStream());
   }

   /**
    * Select a particular newsgroup as the current group.
    *
    * @param groupName The name of the group to select.
    */
   public void setCurrentGroup(String groupName)
      throws NewsClientException
   {
      checkconn();
      checkstring(groupName, ERR_BAD_GROUPNAME);

      sendCommand(GROUP, groupName);
   }


   /**
    * Retrieve the header text for a particular message id.
    *
    * @param msgId The message id. Can either be a numeric id
    *   or a string message-id.
    *
    * @return a stream for the header. Check the status code before
    *   using this stream.
    */
   public InputStream getHead(String msgId)
      throws NewsClientException
   {
      checkconn();
      checkstring(msgId, ERR_BAD_MSGID);

      sendCommand(HEAD, msgId);

      return new MessageInputStream(getInStream());

   }

   /**
    * Get a list of all newsgroups that this server carries.
    * BD: Pending: need to somehow report progress from this method?
    *
    * @return an arraylist of objects which implement the NewsClient.GroupInfo
    *    interface, representing all available groups. Must never
    *    return null.
    */
   public ArrayList getListNewsgroups()
      throws NewsClientException
   {
      if (getProgressMonitor() != null)
      {
         getProgressMonitor().setFixedLengthTask(false);
      }
      checkconn();

      sendCommand(LIST);

      ArrayList l =  readGroupList("Received {0} groups");

      if (getProgressMonitor() != null)
      {
         getProgressMonitor().setFinished(true);
      }
      return l;
   }

   private GroupListVisitor m_glvShared = new GroupListVisitor();

   protected ServerListVisitor getGroupListVisitor(ArrayList destination,
      String progressMessage)
   {
      m_glvShared.setDestinationList(destination);
      m_glvShared.setProgressMessage(progressMessage);
      return m_glvShared;
   }

   protected class GroupListVisitor implements ServerListVisitor
   {
      private ArrayList m_destList;
      private String m_progressMessage;
      private final int PROGRESS_UPDATE_INTERVAL = 10;

      public GroupListVisitor()
      {
      }

      protected void setDestinationList(ArrayList list)
      {
         m_destList = list;
      }

      protected void setProgressMessage(String progressMessage)
      {
         m_progressMessage = progressMessage;
      }

      /**
       * For each retrieved group, we construct a GroupInfo implemenation
       * and parse the group line to determine the low & high message nos
       * and whether posting is OK
       */
      public boolean visit(String item)
      {
         StringTokenizer stParser = new StringTokenizer(item, " ");

         String strName = stParser.nextToken();
         int nHigh = Integer.parseInt(stParser.nextToken());
         int nLow  = Integer.parseInt(stParser.nextToken());
         boolean bPosting = ("y".equalsIgnoreCase(stParser.nextToken()));

         DefaultGroupInfo dgi = new DefaultGroupInfo(
            strName, nLow, nHigh, bPosting
         );

         // Every PROGRESS_UPDATE_INTERVALth group, change the status
         // message.
         if (getProgressMonitor() != null &&
             m_destList.size() % PROGRESS_UPDATE_INTERVAL == 0)
         {
            getProgressMonitor().setMessage(
               MessageFormat.format(m_progressMessage, new Object[] {
                  new Integer(m_destList.size())
               }
            ));
         }
         m_destList.add(dgi);

         return true;
      }
   }

   /**
    * Attempt to read a list of newsgroups from the server in the form
    * group high low flags. You should have already sent a list command
    * of some sort to the server.
    */
   protected ArrayList readGroupList(String progressMessage)
      throws NewsClientException
   {
      ArrayList alGroups = new ArrayList();
      if (getStatus() == NEWSGROUP_LIST)
      {
         try
         {
            getServerList().accept(getGroupListVisitor(alGroups,
               progressMessage));
         }
         catch (IOException ioe)
         {
            doError(ERR_IO_FAILURE);
         }
      }
      return alGroups;
   }

   /**
    * Get the order of fields in the overview database. This is
    * an optional facility, and you can return null.
    *
    * @return an arraylist of header fields (Strings), in the order in which
    *    they will be retrieved by the getOverview() command. Each header in
    *    in the list may or may not end with a colon.
    */
   public ArrayList getOverviewFormat()
      throws NewsClientException
   {
      if (getProgressMonitor() != null)
      {
         getProgressMonitor().setFixedLengthTask(false);
      }
      checkconn();

      // The only way to see if xover is supported is to actually try using
      // the extended commands.
      sendCommand(LIST, "overview.fmt");

      ArrayList stringList;
      // INN returns NEWSGROUP_LIST
      if (getStatus() == NEWSGROUP_LIST)
      {
         stringList =  getStringList("Received {0} items");
      }
      else
      {
         stringList = null;
      }

      if (getProgressMonitor() != null)
      {
         getProgressMonitor().setFinished(true);
      }
      return stringList;
   }

   /**
    * Get the list of all article numbers in a specified group.
    *
    * @param groupName the name of the group
    * @return an arraylist containing string article ids of all messages
    *   in the specified group.
    *
    */
   public ArrayList getArticleList(String groupName)
      throws NewsClientException
   {
      if (getProgressMonitor() != null)
      {
         getProgressMonitor().setFixedLengthTask(false);
      }
      checkconn();
      checkstring(groupName, ERR_BAD_GROUPNAME);

      sendCommand(LISTGROUP, groupName);

      ArrayList articleList;
      if (getStatus() == LIST_ARTICLES || getStatus() == GROUP_SELECTED)
      {
         articleList =  getStringList("Received {0} messages");
      }
      else
      {
         articleList = new ArrayList();
      }

      if (getProgressMonitor() != null)
      {
         getProgressMonitor().setFinished(true);
      }
      return articleList;
   }

   /**
    * Get a list of newly created groups since a specified date.
    *
    * @param since the date to get groups created since.
    * @return an arraylist of objects which implement the NewsClient.GroupInfo
    *    interface, representing all new groups. May be empty, but must never
    *    be null.
    */
   public ArrayList getNewGroups(Date since)
      throws NewsClientException
   {
      if (getProgressMonitor() != null)
      {
         getProgressMonitor().setFixedLengthTask(false);
      }
      checkconn();
      checknull(since, ERR_INVALID_DATE);

      sendCommand(NEWGROUPS, getNNTPDate(since));

      ArrayList groupList;
      if (getStatus() == LIST_NEWGROUPS)
      {
         groupList =  readGroupList("Received {0} new groups");
      }
      else
      {
         groupList = new ArrayList();
      }

      if (getProgressMonitor() != null)
      {
         getProgressMonitor().setFinished(true);
      }
      return groupList;
   }

   /**
    * Get a list of new news in a specified group since the specified
    * date
    *
    * @param groupName the name of the group
    * @param date the date from which to check for new articles
    * @return an arraylist of message numbers. May be empty, but never null.
    *
    */
   public ArrayList getNewNews(String groupNames, Date since)
      throws NewsClientException
   {
      if (getProgressMonitor() != null)
      {
         getProgressMonitor().setFixedLengthTask(false);
      }
      checkconn();
      checkstring(groupNames, ERR_BAD_GROUPNAME);
      checknull(since, ERR_INVALID_DATE);

      sendCommand(NEWNEWS, getNNTPDate(since));

      ArrayList newsList;
      if (getStatus() == LIST_ARTICLES)
      {
         newsList =  getStringList("Received {0} new messages");
      }
      else
      {
         newsList = new ArrayList();
      }
      if (getProgressMonitor() != null)
      {
         getProgressMonitor().setFinished(true);
      }
      return newsList;
   }

   /**
    * Post an article.
    *
    * @param strText the full text of the article
    *
    */
   public void postArticle(String strText)
      throws NewsClientException
   {
   // Funny feeling the impl of this method may change.
      checkconn();
      checkstring(strText, ERR_BAD_MESSAGE);

      sendCommand(POST);

      if (getStatus() == POST_OK)
      {
         try
         {
            getOutStream().println(strText);
            getOutStream().println(".");
            getOutStream().flush();
            readStatus();
         }
         catch (IOException ioe)
         {
            doError(ERR_IO_FAILURE);
         }
      }
   }

   /**
    * Get a short description for a group. This does not have to be
    * implemented and may return null.
    *
    * @param grpName the name of the group
    * @return a short description of the group
    */
   public String getGroupDescription(String grpName)
      throws NewsClientException
   {
      // BD: This is inefficient if we want to get lots of group
      // descriptions at a time. maybe groupinfo should include the
      // description? might double the time that list takes tho'.


      // Also: might want to remember the fact that the server doesn't
      // support XGTITLE or LIST NEWSGROUPS and not use those commands;
      // would need to forget this whenever the connection is closed
      // to support connection sharing.

      checkconn();
      checkstring(grpName, ERR_BAD_GROUPNAME);

      // First try getting the group from local storage
      String grpDescription = (String)m_hashGroupDescriptions.get(grpName);

      if (grpDescription == null)
      {
         // OK, let's ask the server.

         // First try using the xgtitle extended syntax
         sendCommand(XGTITLE, grpName);

         if (getStatus() == COMMAND_NOT_RECOGNIZED)
         {
            // Right-o, try using list newsgroups
            sendCommand(LIST_NEWSGROUPS, grpName);
         }

         if (getStatus() == LIST_FOLLOWS || getStatus() == NEWSGROUP_LIST)
         {
            ArrayList alGroups = getStringList(null);

            if (alGroups.size() == 1)
            {
               String grpLine = (String)alGroups.get(0);
               int spacePos = grpLine.indexOf(' ');
               grpDescription = grpLine.substring(spacePos).trim();
            }
            else
            {
               grpDescription = "";
            }
            // Remember it for future use
            m_hashGroupDescriptions.put(grpName, grpDescription);
         }
      }
      if (grpDescription == null)
      {
         grpDescription = "";
      }

      return grpDescription;
   }

   /**
    * Get an overview of messages in the current group. This method
    * is allowed to return null, indicating that overview is not
    * supported.
    *
    * @param lowRange the first message to get an overview of
    * @param hiRange the last message to get an overview of
    * @return an arraylist of strings. Each item corresponds to one
    *   article. The strings contain a sequence of header text values
    *   separated by vertical bar (|) characters. The header text keys
    *   and order can be obtained using the getOverviewFormat() command.
    */
   public ArrayList getOverview(long lowRange, long hiRange)
      throws NewsClientException
   {
      // TODO: Implement xover one day.
      return null;
   }


/////////////////////////////////////////////////////////////////////
// Utils converting dates to the correct format
/////////////////////////////////////////////////////////////////////

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
    * Get an NNTP date string from a local Date instance.
    * e.g. 1/1/99 14:52 GMT -> "990101 145200 GMT"
    * 12/12/00 01:00 EST -> "001212 060000 GMT"
    */
   String getNNTPDate(Date date)
   {
      // Note: This will work in other timezones, it will adjust the date and
      // convert it to GMT before sending it to the server. That way, the
      // server always knows what time we are talking about, regardless of the
      // country.
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

/////////////////////////////////////////////////////////////////////
// Utils for attaching debug streams
/////////////////////////////////////////////////////////////////////

   private static ArrayList s_alosAttachedStreams = new ArrayList();

   /**
    * You can attach an output stream to NetworkNewsClient. Whenever a
    * command is sent to the server or a status response is recieved,
    * the text will be sent to the stream. Note that all attached streams
    * are shared by all instances of NetworkNewsClient, so the same stream
    * may recieve output from several instances.
    *
    * @param os an output stream to attach.
    */
   public static void attachStream(OutputStream os)
   {
      s_alosAttachedStreams.add(new PrintWriter(new OutputStreamWriter(os)));
   }

   /**
    * Print a string and a newline on all attached streams.
    */
   protected static void sendToAttachedStreams(String s)
   {
      for (int i=0; i < s_alosAttachedStreams.size(); i++)
      {
         PrintWriter pw = (PrintWriter)s_alosAttachedStreams.get(i);
         pw.println(s);
         pw.flush();
      }
   }

   /**
    * Echo a command out to all attached streams.
    */
   protected void echoCommand(String command)
   {
      String strHostName = getHostName();
      StringBuffer sbMessage = new StringBuffer(strHostName.length() +
         command.length() + 4);

      sbMessage.append(getHostName());
      sbMessage.append(" << ");
      sbMessage.append(command);

      sendToAttachedStreams(sbMessage.toString());
   }

   /**
    * Echo a response out to all attached streams.
    */
   protected void echoResponse()
   {
      String strHostName = getHostName();
      String strStatusText = getStatusText();
      // 4 for status no and space + status text length + hostname length +
      // 4 for " >> ".
      StringBuffer sbMessage = new StringBuffer(
         8 + strStatusText.length() + strHostName.length()
      );

      sbMessage.append(getHostName());
      sbMessage.append(" >> ");
      sbMessage.append(getStatus());
      sbMessage.append(" ");
      sbMessage.append(strStatusText);

      sendToAttachedStreams(sbMessage.toString());
   }



/////////////////////////////////////////////////////////////////////
// Utils for handling lists from the server
/////////////////////////////////////////////////////////////////////

   /**
    * Read in a number of lines terminated by a single dot on its own
    * from the server and return an arraylist of strings for all the
    * returned items. If you want to do processing on each item, consider
    * using a visitor (see ServerList).
    */
   protected ArrayList getStringList(String progressMessage)
      throws NewsClientException
   {
      ArrayList alNewList = new ArrayList();
      try
      {
         getServerList().accept(getStringListVisitor(alNewList,
            progressMessage));
      }
      catch (IOException ioe)
      {
         doError(ERR_IO_FAILURE);
      }

      return alNewList;
   }

   private StringListVisitor m_slvShared = new StringListVisitor();

   protected ServerListVisitor getStringListVisitor(ArrayList dest,
      String progressMessage)
   {
      m_slvShared.setDestinationList(dest);
      m_slvShared.setProgressMessage(progressMessage);
      return m_slvShared;
   }

   protected class StringListVisitor implements ServerListVisitor
   {
      private ArrayList m_alDestination;
      private String m_progressMessage;
      private final int PROGRESS_UPDATE_INTERVAL = 10;

      public StringListVisitor() {}

      public void setDestinationList(ArrayList al)
      {
         m_alDestination = al;
      }

      public void setProgressMessage(String progressMessage)
      {
         m_progressMessage = progressMessage;
      }

      public boolean visit(String item)
      {
         m_alDestination.add(item);
         if (getProgressMonitor() != null &&
             m_alDestination.size() % PROGRESS_UPDATE_INTERVAL == 0
             && m_progressMessage != null)
         {
            getProgressMonitor().setMessage(MessageFormat.format(
               m_progressMessage, new Object[] { new Integer(m_alDestination.size()) }
            ));
         }
         return true;
      }
   }

   /**
    * The interface that must be implemented by visitors to lists from the
    * server.
    */
   protected interface ServerListVisitor
   {
      boolean visit(String item);
   }

   /**
    * Get a shared instance of an object that is capable of iterating through
    * a list as it is being retrieved from the server.
    */
   protected ServerList getServerList()
   {
      return m_sl;
   }

   private ServerList m_sl = new ServerList();

   /**
    * This allows us to retrieve a list of "stuff" which ends with a dot
    * character and do different things with each returned item in an efficient
    * and centralized way. Rather than instantiating this class, use
    * getServerList() to retrieve a shared instance.
    *
    */
   protected class ServerList
   {
      ServerList()
      {

      }

      /**
       * Call this method to actually go through the list. For each item in
       * the list, the visit() method on your visitor will be called.
       */
      public void accept(ServerListVisitor slv)
         throws IOException
      {
         String strCurLine;
         strCurLine = getInStream().readLine();
         while (strCurLine != null && (!".".equals(strCurLine)))
         {
            if (!slv.visit(strCurLine)) break;
            strCurLine = getInStream().readLine();
         }
      }
   }
}


//
// $Log: not supported by cvs2svn $
// Revision 1.3  2000/08/19 21:28:36  briand
// Check for null progress monitor.
//
// Revision 1.2  2000/06/14 21:33:01  briand
// Added support for progress monitoring. Numerous fixes & upgrades.
//
// Revision 1.1  2000/02/22 23:47:35  briand
// News client implementation initial revision.
//
//
