// ---------------------------------------------------------------------------
//   Dubh Mail Providers
//   $Id: NewsClient.java,v 1.1 2000-02-22 23:47:35 briand Exp $
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
import java.util.Date;

import java.io.InputStream;

import org.javalobby.javamail.client.StoreClient;

/**
 * A news client represents a client for USENET news. This interface defines the
 * fundamental methods which must be implemented by any client implementation
 * that will be used by org.javalobby.javamail.store.NewsStore.
 *
 * @author <a href="mailto:dubh@btinternet.com">Brian Duff</a>
 * @version $Id: NewsClient.java,v 1.1 2000-02-22 23:47:35 briand Exp $
 */
public interface NewsClient extends StoreClient, NewsStatusCodes
{
   /**
    * When using getListNewsgroups(), an array list of objects
    * implementing this interface are returned.
    */
   public interface GroupInfo
   {
      public String getGroupName();
      public int getLowWaterMark();
      public int getHighWaterMark();
      public boolean isPostingAllowed();
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
      throws NewsClientException;

   /**
    * Determine whether a connection is currently open.
    *
    * @return true if a connection currently exists.
    */
   public boolean isConnected();

   /**
    * Close the connection.
    */
   public void disconnect()
      throws NewsClientException;


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
   public int getStatus();

   /**
    * Get the status text resulting from the last command.
    *
    * @return a human readable message explaining the success or failure
    *    of the last command.
    */
   public String getStatusText();


///////////////////////////////////////////////////////////
// NNTP Commands
///////////////////////////////////////////////////////////

   /**
    * Attempt to authorize using the specified username and
    * password.
    *
    * @param username a user name. Must not be null.
    * @param password a password. Must not be null.
    */
   public void authorize(String username, String password)
      throws NewsClientException;


   /**
    * Retrieve the full article for a particular message id.
    *
    * @param msgId The message id. Can either be a numeric id
    *   or a string message-id
    *
    * @return a stream for the article. This may be null
    *   if the command failed. More information about failure
    *   can be obtained by calling getStatus() and getStatusText().
    */
   public InputStream getArticle(String msgId)
      throws NewsClientException;


   /**
    * Retrieve the body text for a particular message id.
    *
    * @param msgId The message id. Can either be a numeric id
    *   or a string message-id.
    *
    * @return a stream for the body text of a message. This may be null
    *   if the command failed. More information about failure
    *   can be obtained by calling getStatus() and getStatusText().
    */
   public InputStream getBody(String msgId)
      throws NewsClientException;

   /**
    * Find out what date the server thinks it is. This method is optional
    * an is allowed to return null.
    *
    * @return a date object representing the date the server thinks it is.
    */
   public Date getDate()
      throws NewsClientException;

   /**
    * Select a particular newsgroup as the current group.
    *
    * @param groupName The name of the group to select.
    */
   public void setCurrentGroup(String groupName)
      throws NewsClientException;


   /**
    * Retrieve the header text for a particular message id.
    *
    * @param msgId The message id. Can either be a numeric id
    *   or a string message-id.
    *
    * @return a stream for the header of a message. This may be null
    *   if the command failed. More information about failure
    *   can be obtained by calling getStatus() and getStatusText().
    */
   public InputStream getHead(String msgId)
      throws NewsClientException;

   /**
    * Get a list of all newsgroups that this server carries.
    * BD: Pending: need to somehow report progress from this method?
    *
    * @return an arraylist of objects which implement the NewsClient.GroupInfo
    *    interface, representing all available groups. Must never
    *    return null.
    */
   public ArrayList getListNewsgroups()
      throws NewsClientException;

   /**
    * Get the order of fields in the overview database. This is
    * an optional facility, and you can return null.
    *
    * @return an arraylist of header fields (Strings), in the order in which
    *    they will be retrieved by the getOverview() command. Each header in
    *    in the list may or may not end with a colon.
    */
   public ArrayList getOverviewFormat()
      throws NewsClientException;

   /**
    * Get the list of all article numbers in a specified group.
    *
    * @param groupName the name of the group
    * @return an arraylist containing string article ids of all messages
    *   in the specified group.
    *
    */
   public ArrayList getArticleList(String groupName)
      throws NewsClientException;

   /**
    * Get a list of newly created groups since a specified date.
    *
    * @param since the date to get groups created since.
    * @return an arraylist of objects which implement the NewsClient.GroupInfo
    *    interface, representing all new groups. May be empty, but must never
    *    be null.
    */
   public ArrayList getNewGroups(Date since)
      throws NewsClientException;

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
      throws NewsClientException;

   /**
    * Post an article.
    *
    * @param strText the full text of the article
    *
    */
   public void postArticle(String strText)
      throws NewsClientException;

   /**
    * Get a short description for a group. This does not have to be
    * implemented and may return null.
    *
    * @param grpName the name of the group
    * @return a short description of the group
    */
   public String getGroupDescription(String grpName)
      throws NewsClientException;

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
      throws NewsClientException;


}


//
// $Log: not supported by cvs2svn $
//
