// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NewsStore.java,v 1.3 2001-02-11 02:52:48 briand Exp $
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

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.StringTokenizer;
import java.net.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.InternetHeaders;

import org.dubh.dju.misc.Debug;
import org.dubh.dju.misc.StringUtils;
import org.dubh.dju.diagnostic.Assert;
import org.dubh.dju.progress.ProgressMonitor;
import org.dubh.dju.progress.ProgressMonitorSupport;

import org.dubh.javamail.client.ClientRegistry;
import org.dubh.javamail.client.news.NewsClient;
import org.dubh.javamail.client.news.NewsClientException;
import org.dubh.javamail.client.news.NewsStatusCodes;



/**
 * This is a Javamail store that provides access to network news. It sits on
 * top of a real protocol implementation that must implement the
 * org.dubh.javamail.client.news.NewsClient interface.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NewsStore.java,v 1.3 2001-02-11 02:52:48 briand Exp $
 */
public class NewsStore extends Store implements NewsStatusCodes,
   ProgressMonitorSupport
{
   /** The protocol name: "news" */
   public static final String PROTOCOL_NAME="news";

   private static final String NEWSRC_LOCATION =
      System.getProperty("user.home")+File.separator+".newsrc.";

   private Hashtable m_messages;

   private Hashtable m_groups;

   private String m_hostName;
   private int    m_port;

   private final static int DEFAULT_PORT = 119;

   private boolean m_postingOK;

   private Newsgroup m_currentGroup;
   private NewsClient m_ncClient = null;
   private NewsResource m_newsrc = null;

   private ProgressMonitor m_progressMonitor;

   private final static int MAX_FETCHSIZE = 1024;

   /**
    * Should be configurable. Not yet implemented.
    */
   private final static int MAX_TIMEOUT_RETRY = 10;

   /**
    * Construct a News Store.
    */
   public NewsStore(Session session, URLName urlname)
   {
      super(session, urlname);
      m_groups = new Hashtable();
      m_messages = new Hashtable();
   }

   /**
    * Get the client protocol implementation.
    */
   private NewsClient getClient()
   {
      if (m_ncClient == null)
      {
         m_ncClient = (NewsClient)ClientRegistry.getStoreClient(
            PROTOCOL_NAME
         );

         if (Assert.ENABLED)
         {
            Assert.that((m_ncClient != null), "Client has not been set");
         }
      }
      return m_ncClient;
   }

   /**
    * Set the progress monitor for operations on this store. The monitor will
    * be notified of changes in progress in operations on the client.
    */
   public void setProgressMonitor(ProgressMonitor pm)
   {
      m_progressMonitor = pm;
      NewsClient nc = getClient();
      if (nc instanceof ProgressMonitorSupport)
      {
         ((ProgressMonitorSupport)nc).setProgressMonitor(pm);
      }
   }

   /**
    * Connect to an NNTP server. Calls connect() on the client. If the
    * status of this command is not READY_POSTOK or READY_NOPOSTING,
    * returns false. Otherwise, if a username and password are both provided,
    * calls authorize() on the client. If authorize() returns status
    * PERMISSION_DENIED, an AuthenticationFailedException is thrown.
    * Otherwise, connection has succeeded and the method returns true.
    *
    * @param host The host name to connect to
    * @param port The port number. If the supplied port is < 1, the
    *   default port number is used.
    * @param userName The user name to authenticate with. can be null.
    * @param password The password to authenticate with. Can be null.
    *
    * @throws javax.mail.MessagingException if the client calls to
    *   connect() or authorize() throw exceptions.
    * @throws javax.mail.AuthenticationFailedException if the client
    *   call to authorize() returns a PERMISSION_DENIED status code.
    *

    */
   protected boolean protocolConnect(String host, int port, String userName, String password)
      throws MessagingException
   {
      if (Assert.ENABLED)
      {
         Assert.that((host != null && host.trim().length() != 0),
            "Must specify a host name"
         );
      }


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
         NewsClient client = getClient();
         client.connect(m_hostName, m_port);


         // Status code should be READY_POSTOK or READY_NOPOSTING.
         int status = client.getStatus();

         if (status == READY_POSTOK)
         {
            m_postingOK = true;
         }
         else if (status == READY_NOPOSTING)
         {
            m_postingOK = false;
         }
         else
         {
            // Connection failed. We ought to propagate the server error
            // message from getClient().getStatusText() here somehow.
            return false;
         }

         System.out.println("Username is "+userName+" password is "+password);
         // Authenticate if we have been given a username and password.
         if (userName != null && password != null &&
             userName.trim().length() != 0 && password.trim().length() != 0)
         {
            System.out.println("Authorizing");
            client.authorize(userName, password);
            if (client.getStatus() == PERMISSION_DENIED)
            {
               // Disconnect. Might not be necessary; INN disconnects you
               // immediately if authinfo fails.
               close();
               return false;
            }
         }

         return true;
      }
      catch (NewsClientException nce)
      {
         clientException(nce);
      }
      return false;
   }

   /**
    * Close the connection to the NNTP server. Calls disconnect()
    * on the client. Ignores any returned status code.
    * @throws javax.mail.MessagingException if the client throws
    *   a NewsClientException.
    */
   public void close()
      throws MessagingException
   {
      try
      {
         getClient().disconnect();
      }
      catch (NewsClientException nce)
      {
         clientException(nce);
      }

   }

   /**
    * Close the specified newsgroup.
    */
   void closeGroup(Newsgroup g)
      throws MessagingException
   {
      // do nothing for now.
   }

   /**
    * Open the specified newsgroup. Calls setCurrentGroup() on the
    * client and expects a GROUP_SELECTED or NO_SUCH_GROUP response.
    * If GROUP_SELECTED is the response, the response text should
    * contain the article count, lowest article number, highest
    * article number and group name separated by spaces.
    *
    * @param g A newsgroup to set as the current group.
    * @throws javax.mail.MessagingException if an unexpected response
    *   code is returned from setCurrentGroup(), or if the client
    *   throws an exception.
    */
   void openGroup(Newsgroup g)
      throws MessagingException
   {
      if (!g.equals(m_currentGroup))
      {
         try
         {
            getClient().setCurrentGroup(g.getName());
            int response = getClient().getStatus();
            switch (response)
            {
               case GROUP_SELECTED:
                  m_currentGroup = g;
                  // Set the article counts of the group
                  setArticleCounts(g);
                  break;
               case NO_SUCH_GROUP:
                  throw new MessagingException(getClient().getStatusText());
               default:
                  unexpectedResponse();
            }
         }
         catch (NewsClientException nce)
         {
            clientException(nce);
         }
      }
   }

   /**
    * Get a list of all subscribed groups.
    *
    * @return a list of subscribed newsgroups.
    */
   Newsgroup[] getSubscribedGroups()
      throws MessagingException
   {
      int subSize = getNewsResource().getSubscribedGroupCount();
      Newsgroup[] subGroups = new Newsgroup[subSize];
      if (subSize == 0)
      {
         return subGroups;
      }

      Iterator groupIter = getNewsResource().getSubscribedGroups();
      int i = 0;
      while(groupIter.hasNext())
      {
         subGroups[i++] = getNewsgroup((String)groupIter.next());
      }
      return subGroups;
   }

   /**
    * Determine whether a group has been subscribed to. This uses the
    * news resource.
    *
    * @param g The group to check.
    * @return true if the group has been subscribed to. False otherwise.
    */
   boolean isGroupSubscribed(Newsgroup g)
   {
      return getNewsResource().isSubscribed(g.getName());
   }

   /**
    * Set the subscription of a group. This will be set in the news resource.
    *
    * @param g The group to subscribe to or unsubscribe
    * @param isSubscribed True if the group is subscribed, false otherwise.
    */
   void setGroupSubscribed(Newsgroup g, boolean isSubscribed)
   {
      getNewsResource().setSubscribed(g.getName(), isSubscribed);
   }

   /**
    * Verify that a group actually exists on the server. Does this by
    * calling {@link #openGroup(Newsgroup)} and checking to see
    * whether the response code is GROUP_SELECTED. Always sets the
    * current group back to whatever it was before being called.
    *
    * @param g The group to check.
    * @return true if the group exists and is selectable in the client.
    *
    * @throws javax.mail.MessagingException if trying to re-select
    *   the old group through openGroup() throws a MessagingException
    */
   boolean groupExists(Newsgroup g)
      throws MessagingException
   {
      // Remember the old group so we can restore it later
      Newsgroup oldgroup = m_currentGroup;

      try
      {
         openGroup(g);
      }
      catch (MessagingException me)
      {
         // If the client said the group doesn't exist
         if (getClient().getStatus() == NO_SUCH_GROUP)
         {
            // Restore the old group and return false
            if (oldgroup != null) openGroup(oldgroup);
            return false;

         }
         else
         {
            // Otherwise, something more fundamental went wrong
            // calling openGroup. Try to recover by choosing
            // the old group. If something has really gone drastically
            // wrong, this will throw a MessagingException back out.
            if (oldgroup != null) openGroup(oldgroup);
         }
      }
      // Group must exist. Select the old group.
      if (oldgroup != null) openGroup(oldgroup);
      return true;
   }


   /**
    * Retrieve a message from the list of messages. If it doesn't exist,
    * create a new NNTPMessage instance and store it.
    *
    * @param newsgroup the group from which to retrieve the message
    * @param message_id an identifier for the message
    * @return an NNTPMessage instance.
    *
    * @throws javax.mail.MessagingException if
    */
   NNTPMessage getNNTPMessage(Newsgroup newsgroup, String message_id)
      throws MessagingException
   {
      // Try to retrieve the message from our local storage
      NNTPMessage msg = (NNTPMessage)m_messages.get(message_id);

      // If we don't have it locally
      if (msg==null)
      {
         // Construct a new message and store it
         msg = new NNTPMessage(newsgroup, message_id);
         m_messages.put(message_id, msg);
      }
      return msg;
   }

   /**
    * Get all messages in the specified newsgroup.
    *
    * @param g The group to get messages in
    * @return an array of messages
    * @throws javax.mail.MessagingException if getNewMessages throws
    *   it.
    */
   NNTPMessage[] getMessages(Newsgroup g)
      throws MessagingException
   {
      try
      {
         // Ask the client for new news
         ArrayList newIds = getClient().getArticleList(g.getName());
         NNTPMessage[] new_msg = new NNTPMessage[newIds.size()];
         int response = getClient().getStatus();

         switch (response)
         {
            case LIST_ARTICLES:
            case GROUP_SELECTED: // INN amongst others
               for (int i=0; i < newIds.size(); i++)
               {
                  NNTPMessage msg = getNNTPMessage(g, (String)newIds.get(i));
                  new_msg[i] = msg;
               }
               break;
            default:
               unexpectedResponse();

         }
         return new_msg;
      }
      catch (NewsClientException nce)
      {
         clientException(nce);
      }

      return null;
   }

   /**
    * Get new messages since the specified date.
    *
    * Calls {@link #org.dubh.javamail.client.news.NewsClient.getNewNews(String, Date)}
    * on the client. The expected return status code is LIST_ARTICLES.
    *
    * @param g The newsgroup to get new messages for
    * @param since The local date to get new messages since. This will be
    *              converted to a GMT date string before being sent to the server
    * @return An array of new messages in the group since the specified date
    *
    * @throws javax.mail.MessagingException if the client call to
    *   getNewNews() throws a NewsClientException, or if an unexpected status
    *   code is returned.
    */
   NNTPMessage[] getNewMessages(Newsgroup g, Date since)
      throws MessagingException
   {
      try
      {
         // Ask the client for new news
         ArrayList newIds = getClient().getNewNews(g.getName(), since);
         NNTPMessage[] new_msg = new NNTPMessage[newIds.size()];
         int response = getClient().getStatus();

         switch (response)
         {
            case LIST_ARTICLES:
               for (int i=0; i < newIds.size(); i++)
               {
                  NNTPMessage msg = getNNTPMessage(g, (String)newIds.get(i));
                  new_msg[i] = msg;
               }
               break;
            default:
               unexpectedResponse();

         }
         return new_msg;
      }
      catch (NewsClientException nce)
      {
         clientException(nce);
      }

      return null;


   }

   /**                   OBSOLETE?
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
    * Get all headers for the specified message.
    *
    * Calls getHead() on the client. The expected status code on return is
    * ARTICLE_RETRIEVED_HEAD.
    *
    * @param message The message to retrieve headers for
    * @return an InternetHeaders object containing the headers
    *
    * @throws javax.mail.MessagingException if the NNTP server returned an
    *   unexpected response code, or if the client call to getHead() threw
    *   a NewsClientException.
    */
   InternetHeaders getHeaders(NNTPMessage message)
      throws MessagingException
   {
      try
      {
         String strMessageID = resolveMessageID(message);

         // Get the header
         InputStream instrHead = getClient().getHead(strMessageID);

         int response = getClient().getStatus();

         switch (response)
         {
            case ARTICLE_RETRIEVED_HEAD:
               return new InternetHeaders(instrHead);
            default:
               unexpectedResponse();
         }
      }
      catch (NewsClientException nce)
      {
         clientException(nce);
      }
      return null;
   }

   /**
    * Gets a message identifier. This method will first attempt to retrieve
    * the Message-Id field of the message. If this field doesn't exist
    * (hasn't been read in yet), the current group is switched to the containing
    * group of the article and the message number of the message is returned.
    * Either one of these identifiers can be used in NNTP commands such as
    * BODY or HEAD.
    *
    * @return either a Message-Id or a message number converted to a string.
    *
    * @throws javax.mail.MessagingException if {@link #openGroup(Newsgroup)}
    *   throws one.
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
    * Get the content for the specified message.
    *
    * Calls getBody() on the client. The expected status return code is
    * ARTICLE_RETRIEVED_BODY.
    *
    * @param message The message you want to retrieve the body for
    * @returns A byte array of the body of the message

    * @throws javax.mail.MessagingException if the server returned an unexpected code
    */
   byte[] getContent(NNTPMessage message)
      throws  MessagingException
   {
      try
      {
         String strMessageID = resolveMessageID(message);

         // Get the message body
         InputStream isBody = getClient().getBody(strMessageID);

         int response = getClient().getStatus();

         switch (response)
         {
            case ARTICLE_RETRIEVED_BODY:
               int length;
               // TODO: Get rid of this monstrosity.
               byte b[] = new byte[MAX_FETCHSIZE];

               ByteArrayOutputStream baosStorage = new ByteArrayOutputStream();

               while ((length = isBody.read(b, 0, MAX_FETCHSIZE))!=-1)
               baosStorage.write(b, 0, length);

               return baosStorage.toByteArray();
            default:
               unexpectedResponse();
         }
      }
      catch (NewsClientException nce)
      {
         clientException(nce);
      }
      catch (IOException ioe)
      {
         ioException(ioe);
      }
      return null;
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
      // <count> <first> <last> <newsgroup-name>
      String lastReply = getClient().getStatusText();
      g.setArticleCount(StringUtils.stringToInt(StringUtils.getWord(lastReply, 0)));
      g.setFirstArticle(StringUtils.stringToInt(StringUtils.getWord(lastReply, 1)));
      g.setLastArticle(StringUtils.stringToInt(StringUtils.getWord(lastReply, 2)));

   }


   /**
    * Determines whether the socket connection is currently open. Just
    * delegates to isConnected() on the client.
    *
    * @returns a boolean value indicating whether the connection is open
    */
   public boolean isConnected()
   {
      return getClient().isConnected();
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
    * Get a named newsgroup. Just calls {@link #getNewsgroup(String)}.
    *
    * @return a javamail folder representing the specified newsgroup.
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
    * </pre>
    *
    * @param urlName the url name of the group.
    * @return a folder object for the url.
    * @throws java.lang.IllegalArgumentException if the URL is not for the
    *   correct protocol.
    */
   public Folder getFolder(URLName urlName)
      throws MessagingException
   {
      if (PROTOCOL_NAME.equalsIgnoreCase(urlName.getProtocol()))
      {
         return getNewsgroup(urlName.getFile());
      }
      throw new IllegalArgumentException("URL must be for protocol '"+
         PROTOCOL_NAME+"'");
   }

   /**
    * Get the specified newsgroup if it exists. Otherwise, create a new
    * Newsgroup object for it.
    *
    * @param name the name of the group
    * @return a Newsgroup object.
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
    * Get new newsgroups since the specified date.
    *
    * @param since the date the list of newsgroups was last updated.
    * @return an array of Newsgroup objects for all groups created since
    *   the specified date.
    * @throws javax.mail.MessagingException if the client returns
    *   an unexpected response from getNewGroups().
    */
   private Newsgroup[] getNewNewsgroups(Date since) throws MessagingException
   {
      try
      {
         ArrayList newGroups = getClient().getNewGroups(since);

         Newsgroup[] groupArray = new Newsgroup[newGroups.size()];

         int response = getClient().getStatus();

         if (groupArray.length == 0)
         {
            return groupArray;
         }

         switch (response)
         {
            case NEWSGROUP_LIST:
               for (int i=0; i < newGroups.size(); i++)
               {
                  NewsClient.GroupInfo ncgiGroup =
                     (NewsClient.GroupInfo)newGroups.get(i);
                  Newsgroup ngNew = (Newsgroup)getFolder(
                     ncgiGroup.getGroupName()
                  );
                  ngNew.setFirstArticle(ncgiGroup.getLowWaterMark());
                  ngNew.setLastArticle(ncgiGroup.getHighWaterMark());
                  ngNew.setPostingOK(ncgiGroup.isPostingAllowed());
                  groupArray[i] = ngNew;
                  getNewsResource().addNewGroup(ncgiGroup.getGroupName());
               }
               break;
            default:
               unexpectedResponse();
         }

         return groupArray;

      }
      catch (NewsClientException nce)
      {
         clientException(nce);
      }

      return null;
   }

   /**
    * Get all newsgroups available on the server.
    *
    * Calls getListNewsgroups() on the client. The expected response is
    * NEWSGROUP_LIST.
    *
    * @return an array of Newsgroup objects.
    * @throws javax.mail.MessagingException if the client returns an
    *   unexpected response from getListNewsgroups(), or if the client
    *   throws a NewsClientException.
    *
    */
   Newsgroup[] getNewsgroups() throws MessagingException
   {
      // Before we ask the client, see if we can get the list from the newsrc
      NewsResource res = getNewsResource();

      int resGroupCount = res.getGroupCount();

      if (resGroupCount > 0)
      {
         Newsgroup[] groups = new Newsgroup[resGroupCount];
         Iterator allGroups = res.getAllGroups();
         int i=0;
         while (allGroups.hasNext())
         {
            groups[i++] = getNewsgroup((String)allGroups.next());
         }

         // Now, decide whether to get the list of new groups. Some kind of
         // notification / event mechanism ought to be used here so that the
         // user can decided whether or not to retrieve new groups.

         Newsgroup[] newGroups = getNewNewsgroups(res.getLastGroupUpdate());
         res.setLastGroupUpdateNow();
         if (newGroups != null && newGroups.length > 0)
         {
            Newsgroup[] oldAndNew =
               new Newsgroup[groups.length+newGroups.length];

            System.arraycopy(groups, 0, oldAndNew, 0, groups.length);
            System.arraycopy(
               newGroups, 0, oldAndNew, groups.length, newGroups.length
            );

            // Save the news resource
            try
            {
               // Save the newsrc
               res.saveFile(getNewsResourceFilename());
            }
            catch (IOException ioe)
            {
               throw new MessagingException(
                  "Failed to save Newsrc "+getNewsResourceFilename(),
                  ioe
               );
            }

            return oldAndNew;
         }

         return groups;

      }

      // Otherwise, we have to go off and ask the client...


      try
      {
         ArrayList alGroups = getClient().getListNewsgroups();

         if (Debug.TRACE_LEVEL_3)
         {
            Debug.println(3, this,
               "Got "+alGroups.size()+" newsgroups from the server");
         }

         Newsgroup[] anGroups = new Newsgroup[alGroups.size()];
         int response = getClient().getStatus();


         switch (response)
         {
            case NEWSGROUP_LIST:
               for (int i=0; i < alGroups.size(); i++)
               {
                  NewsClient.GroupInfo ncgiGroup =
                     (NewsClient.GroupInfo)alGroups.get(i);
                  Newsgroup ngNew = (Newsgroup)getFolder(
                     ncgiGroup.getGroupName()
                  );
                  ngNew.setFirstArticle(ncgiGroup.getLowWaterMark());
                  ngNew.setLastArticle(ncgiGroup.getHighWaterMark());
                  ngNew.setPostingOK(ncgiGroup.isPostingAllowed());
                  anGroups[i] = ngNew;
                  res.addNewGroup(ncgiGroup.getGroupName());
               }
               break;
            default:
               unexpectedResponse();
         }

         try
         {
            // Save the newsrc
            res.setLastGroupUpdateNow();
            res.saveFile(getNewsResourceFilename());
         }
         catch (IOException ioe)
         {
            throw new MessagingException(
               "Failed to save Newsrc "+getNewsResourceFilename(),
               ioe
            );
         }
         return anGroups;
      }
      catch (NewsClientException nce)
      {
         clientException(nce);
      }

      return null;

   }

   private String getNewsResourceFilename()
   {
      return NEWSRC_LOCATION+m_hostName;
   }

   /**
    * Get the news resource; this contains information about subscribed
    * groups etc.
    */
   private NewsResource getNewsResource()
   {
      if (m_newsrc == null)
      {
         loadNewsrc();
      }
      return m_newsrc;
   }

   /**
    * Load the news resource for this server. If none exists, sets the m_newsrc
    * field to an empty news resource
    */
   private void loadNewsrc()
   {
      m_newsrc = new NewsResource();
      try
      {
         m_newsrc.loadFile(getNewsResourceFilename());
      }
      catch (IOException ioe)
      {
         // We don't care, we just use an empty resource.
      }
   }

   /**
    * Utility method to centralise exception handling for when
    * an unexpected response code is recieved from the client.
    */
   private void unexpectedResponse()
      throws MessagingException
   {
      System.err.println("Unexpected response "+getClient().getStatus());
      throw new MessagingException("Unexpected Response "+
         getClient().getStatus()+" "+getClient().getStatusText()
      );
   }

   /**
    * Utility method to centralise exception handling for when
    * the client throws a NewsClientException and we want to encapsulate
    * it in a MessagingException and throw the MessagingException.
    */
   private void clientException(NewsClientException nce)
      throws MessagingException
   {
      System.err.println("Client Exception: "+nce);
      throw new MessagingException("Client Exception ", nce);
   }

   private void ioException(IOException ioe)
      throws MessagingException
   {
      throw new MessagingException("IOException", ioe);
   }

}


//
// $Log: not supported by cvs2svn $
// Revision 1.2  2000/06/14 21:33:01  briand
// Added support for progress monitoring. Numerous fixes & upgrades.
//
// Revision 1.1  2000/02/22 23:49:39  briand
// New news store implementation that sits on top of clients. Initial
// revision.
//
//
//