// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: TestNewsClient.java,v 1.2 2000-06-14 21:33:01 briand Exp $
//   Copyright (C) 1999, 2000  Brian Duff
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

package org.javalobby.javamail.client.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import org.javalobby.javamail.client.StoreClient;

/**
 * This is a test implementation of NewsClient that pretends to talk
 * to a news server. This is really for testing that the NNTPStore
 * stuff is working properly without making any assumptions about
 * the networking code, but because the NNTPStore is the layer presented
 * to applications using DMP, this test class can equally be used for testing
 * end applications without having to connect to a network, without having
 * to change application code.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: TestNewsClient.java,v 1.2 2000-06-14 21:33:01 briand Exp $
 */
public final class TestNewsClient extends AbstractNewsClient
{
   private boolean m_bConnected;
   private int m_nLastStatus;
   private String m_strLastStatusMessage;
   private String m_curGroup = null;

   private HashMap m_hashGroupStore;

   private DefaultGroupInfo[] dummyGroups = {
      new DefaultGroupInfo("org.javalobby.jfa.javamail.info", 1, 0, true),
      new DefaultGroupInfo("org.javalobby.jfa.javamail.request", 1, 1, true),
      new DefaultGroupInfo("org.javalobby.jfa.javamail.bugs", 1, 1, true),
      new DefaultGroupInfo("comp.lang.java.programmer", 1, 19, false),
      new DefaultGroupInfo("oracle.corp.tools.jdeveloper", 1, 0, true),
      new DefaultGroupInfo("oracle.corp.tools.des2k", 0, 1, false)
   };

   private final String test_head =
      "From: bduff@uk.oracle.com\n"+
      "Subject: Test Message\n"+
      "Newsgroups: org.javalobby.jfa.javamail.info\n"+
      "Message-Id: 001@test.com";

   private final String test_body =
      "This is the body text of my first test message.";



   /**
    * Create a new test news client
    */
   public TestNewsClient()
   {
      say("A test news client has been instantiated");
      m_hashGroupStore = new HashMap();
      for (int i=0; i < dummyGroups.length; i++)
      {
         m_hashGroupStore.put(dummyGroups[i].getGroupName(),
            dummyGroups[i]
         );
      }
   }

   private NewsClient.GroupInfo getGroupInfo(String groupName)
   {
      return (NewsClient.GroupInfo)m_hashGroupStore.get(groupName);
   }

///////////////////////////////////////////////////////////
// Connection
///////////////////////////////////////////////////////////

   /**
    * Open a connection to the specified server and port. This is
    * implementation specific (e.g. you might be implementing a file
    * based client, in which case you will probably just ignore this
    * method).
    *
    * @param hostName the host name to connect to
    * @param portNumber the port number to connect to
    *
    */
   public void connect(String hostName, int portNumber)
      throws NewsClientException
   {
      if (hostName == null || hostName.trim().length() == 0)
      {
         throw new NewsClientException(
            "hostName=null or zero length"
         );
      }
      say("Connected to "+hostName+" "+portNumber);
      m_bConnected = true;
      setStatus(READY_POSTOK, "Hello! Posting is OK");
   }

   /**
    * Determine whether a connection is currently open.
    *
    * @return true if a connection currently exists.
    */
   public boolean isConnected()
   {
      return m_bConnected;
   }

   /**
    * Close the connection.
    */
   public void disconnect()
      throws NewsClientException
   {
      say("Disconnected.");
      m_bConnected = false;
   }


///////////////////////////////////////////////////////////
// General communication
///////////////////////////////////////////////////////////

   /**
    * Get the status code resulting from the last command. You should use
    * one of the constants defined in NewsStatusCodes.
    *
    * @return a numerical status code that indicates the success or
    *   otherwise of the last command.
    */
   public int getStatus()
   {
      return m_nLastStatus;
   }

   /**
    * Get the status text resulting from the last command.
    *
    * @return a human readable message explaining the success or failure
    *    of the last command.
    */
   public String getStatusText()
   {
      return m_strLastStatusMessage;
   }

   private void setStatus(int n, String message)
   {
      m_nLastStatus = n;
      m_strLastStatusMessage = message;
   }

   // A bunch of utilities for checking method parameters

   private void checkbool(boolean b, String message)
      throws NewsClientException
   {
      if (b)
      {
         throw new NewsClientException(message);
      }
   }

   private void checkconn() throws NewsClientException
   {
      checkbool(!isConnected(), "No connection");
   }

   private void checknull(Object param, String varName)
      throws NewsClientException
   {
      checkbool((param == null), varName+" is null");
   }

   private void checkstring(String param, String varName)
      throws NewsClientException
   {
      checknull(param, varName);
      checkbool((param.trim().length()==0),
         varName+" is zero length when trimmed"
      );
   }


///////////////////////////////////////////////////////////
// NNTP Commands
///////////////////////////////////////////////////////////

   /**
    * Attempt to authorize using the specified username and
    * password.       For the test news client, the username
    * and password must always be test and test.
    *
    * @param username a user name. Must not be null.
    * @param password a password. Must not be null.
    */
   public void authorize(String username, String password)
      throws NewsClientException
   {
      checkconn();
      checkstring(username, "user name");
      checkstring(password, "password");

      if ("test".equals(username) && "test".equals(password))
      {
         // BD: Not sure this is the correct status code for
         // authenticate OK
         setStatus(READY_POSTOK, "Authentication success");
         say("Authenticate success");
      }
      else
      {
         setStatus(PERMISSION_DENIED, "Authentication failed");
         say("Authenticate failure");
         disconnect();
      }
   }


   /**
    * Retrieve the full article for a particular message id.
    *
    * @param msgId The message id. Can either be a numeric id
    *   or a string message-id
    *
    * @return the full article. This may be null
    *   if the command failed. More information about failure
    *   can be obtained by calling getStatus() and getStatusText().
    */
   public InputStream getArticle(String msgId)
      throws NewsClientException
   {
      checkconn();
      checkstring(msgId, "msgId");
      byte[] head = test_head.getBytes();
      byte[] body = test_body.getBytes();

      byte[] msg = new byte[head.length+body.length+2];
      String sep = "\n\n";
      System.arraycopy(head, 0, msg, 0, head.length);
      System.arraycopy(sep.getBytes(), 0, msg,
         head.length, sep.length()
      );
      System.arraycopy(body, 0, msg, head.length+sep.length(),
         body.length
      );

      setStatus(ARTICLE_RETRIEVED_FULL, "Article follows");
      say("Article "+msgId+" retrieved in group "+m_curGroup);

      return new ByteArrayInputStream(msg);

   }


   /**
    * Retrieve the body text for a particular message id.
    *
    * @param msgId The message id. Can either be a numeric id
    *   or a string message-id.
    *
    * @return the full body text of a message. This may be null
    *   if the command failed. More information about failure
    *   can be obtained by calling getStatus() and getStatusText().
    */
   public InputStream getBody(String msgId)
      throws NewsClientException
   {
      checkconn();
      checkstring(msgId, "msgId");

      setStatus(ARTICLE_RETRIEVED_BODY, "Body follows");
      say("Article "+msgId+" body retrieved.");

      return new ByteArrayInputStream(test_body.getBytes());


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
      checkstring(groupName, "groupName");

      boolean bNameCheck = false;
      for (int i=0; i < dummyGroups.length; i++)
      {
         if (groupName.equalsIgnoreCase(dummyGroups[i].getGroupName()))
         {
            bNameCheck = true;
            break;
         }
      }
      // ac first last

      if (bNameCheck)
      {
         m_curGroup = groupName;

         NewsClient.GroupInfo gi = getGroupInfo(groupName);

         setStatus(GROUP_SELECTED, ""+gi.getLowWaterMark()+" "+
            gi.getHighWaterMark()+" "+
            (gi.getHighWaterMark()-gi.getLowWaterMark()+1)+" "+
            gi.getGroupName()
         );

         say("Group "+groupName+" selected.");
      }
      else
      {
         setStatus(NO_SUCH_GROUP, "Group "+groupName+" doesn't exist!");
         say("Failed group check for "+groupName);
      }

   }


   /**
    * Retrieve the header text for a particular message id.
    *
    * @param msgId The message id. Can either be a numeric id
    *   or a string message-id.
    *
    * @return the full header of a message. This may be null
    *   if the command failed. More information about failure
    *   can be obtained by calling getStatus() and getStatusText().
    */
   public InputStream getHead(String msgId)
      throws NewsClientException
   {
      checkconn();
      checkstring(msgId, "msgId");

      setStatus(ARTICLE_RETRIEVED_HEAD, "Head follows");
      say("Article "+msgId+" head retrieved.");

      return new ByteArrayInputStream(test_head.getBytes());

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
      checkconn();

      ArrayList al = new ArrayList();

      for (int i=0; i < dummyGroups.length; i++)
      {
         al.add(dummyGroups[i]);
      }

      setStatus(NEWSGROUP_LIST, "Newsgroups in form \"group high low flags\"");
      say("Retrieved group list.");
      return al;
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
      checkconn();

      return null;

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
      checkconn();

      ArrayList al = new ArrayList();

      if ("org.javalobby.jfa.javamail.request".equals(groupName) ||
          "org.javalobby.jfa.javamail.bugs".equals(groupName))
      {
         al.add("1");
      }
      else if ("comp.lang.java.programmer".equals(groupName))
      {
         al.add("19");
      }

      setStatus(LIST_ARTICLES, "Article list follows");
      say("Got article list for "+groupName);
      return al;

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
      checkconn();
      checknull(since, "since");

      say("Got new groups since "+since);
      ArrayList newGroups = new ArrayList();
      setStatus(LIST_NEWGROUPS, "New groups list follows");
      return newGroups;
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
      checkconn();
      checkstring(groupNames, "group name");
      checknull(since, "since");

      say("Got new news since "+since+" in groups "+groupNames);
      ArrayList newNews = new ArrayList();
      setStatus(LIST_ARTICLES, "New news follows");
      return newNews;
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
      checkconn();
      checkstring(strText, "text");

      say("Message posted");
      setStatus(POST_OK, "Article posted");
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
      checkconn();
      checkstring(grpName, "grpName");

      // Make sure caller handles empty string
      if ("comp.lang.java.programmer".equals(grpName))
      {
         return "";
      }

      // test if caller handles null
      if ("oracle.corp.tools.des2k".equals(grpName))
      {
         return null;
      }

      return "This is just a pretend description";
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
      checkbool((lowRange > hiRange), "lowRange > hiRange: "+lowRange+" > "+hiRange);

      return null;
   }



   /**
    * Dump an error message. Should really use debug.
    */
   private void say(String message)
   {
      System.out.println("TestNewsClient: "+message);
   }
}


//
// $Log: not supported by cvs2svn $
// Revision 1.1  2000/02/22 23:47:35  briand
// News client implementation initial revision.
//
//
