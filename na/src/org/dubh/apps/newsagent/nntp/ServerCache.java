// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ServerCache.java,v 1.4 1999-11-09 22:34:42 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
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

package org.javalobby.apps.newsagent.nntp;

import javax.swing.tree.*;
import java.util.*;
import java.io.*;
import org.javalobby.apps.newsagent.GlobalState;
import org.javalobby.apps.newsagent.dialog.ErrorReporter;
import org.javalobby.dju.misc.StringUtils;
import org.javalobby.apps.newsagent.agent.AgentManager;

/**
 * Represents cached headers and bodies for an NNTPServer.
 *
 * Version History: <UL>
 * <LI>0.1 [05/04/98]: Initial Revision
 * <LI>0.2 [06/04/98]: Got the References line the wrong way round (the last
 *   item on the References line is the message this one is a reply to, rather
 *   than the first).. Fixed now.
 * <LI>0.3 [28/04/98]: Added support for List Agents.
 * <LI>0.4 [06/06/98]: Added dubh utils import for StringUtils
 *</UL>
 @author Brian Duff
 @version 0.4 [06/06/98]
 */
public class ServerCache implements Serializable {

// Private instance variables
  /**
   * Use a Hashtable to map Message-Id to DefaultMutableTreeNode. This makes
   * threading and lookup easier. This member is transient, so it is not
   * serialised when this class is (otherwise, we'd have two copies of
   * all the message headers). 
   */
  private transient Hashtable m_headers;
  /**
   * And another Hashtable mapping Message-Id to MessageBody. Again, for
   * quick lookup.
   */
  private Hashtable m_bodies;
  /**
   * And the tree itself. A DefaultTreeModel with DefaultMutableTreeNodes.
   */
  private DefaultTreeModel m_headerTree;
  /**
   * A utility variable that keeps track of whether the cache needs to be
   * serialised. This makes life more efficient when the storage manager
   * wants to save all the caches.
   */
  private transient boolean m_didChange;
  /**
   * The last time headers were added to the cache, in NNTP Server date format
   * (yymmddhhmmss)
   */
  private String m_lastUpdate;

  public ServerCache() {
   DefaultMutableTreeNode root = new DefaultMutableTreeNode(null);
   m_headerTree = new DefaultTreeModel(root);
   m_headers = new Hashtable();
   m_bodies = new Hashtable();
   m_didChange = false;
   m_lastUpdate = null;
  }

  /**
   * Determine whether the cache contains the header corresponding to a given
   * message ID.
   @param messageID The unique Message-Id header to look up
   */
  public boolean hasHeader(String messageID) {
   return (m_headers.containsKey(messageID));
  }

  /**
   * Retrieves a header with the specified messageID from the cache.
   @param messageID the unique Message-Id header to look up
   @return a MessageHeader object if the header is cached, null otherwise
   */
  public MessageHeader getHeader(String messageID) {
   return ((MessageHeader)
     ((DefaultMutableTreeNode)m_headers.get(messageID)).getUserObject());
  }

  /**
   * Determine whether the cache contains the body of a given header.
   @param header A MessageHeader object to look up.
   */
  public boolean hasBody(MessageHeader header) {
   return (m_bodies.containsKey(header.getFieldValue("Message-Id")));
  }

  /**
   * Retrieve a message body from the cache.
   @param header The header of the message body to look up
   @return a MessageBody object
   */
  public MessageBody getBody(MessageHeader header) {
   return ((MessageBody)m_bodies.get(header.getFieldValue("Message-Id")));
  }

  /**
   * Gets the entire tree of cached message headers.
   @return a Swing TreeModel containing all the message headers in the cache.
   */
  public TreeModel getHeaders() {
   return m_headerTree;
  }

  /**
   * Adds a set of headers to the cache. The headers will be threaded into the
   * existing cache message thread tree. List Agents are called on the message
   * headers in this routine. 
   @param headers a Vector consisting of MessageHeader objects to add to the
     cache
   @param now The date according to the server in server date format
     (yymmddhhmmss)
   */
  public void addHeaders(Vector headers, String now) {
   Enumeration enum = headers.elements();
   while (enum.hasMoreElements()) {
     MessageHeader thisHeader = (MessageHeader)enum.nextElement();
     // Call the list agents, and add the message only if it isn't rejected
     // by any of the agents.
     if (GlobalState.getAgentManager().callListAgents(thisHeader))
       insertHeader(thisHeader);
   }

   m_lastUpdate = now;

  }

  /**
   * Adds a body to the cache. Its header should already exist in the cache.
   @param header the header of the message
   @param body the body of the message to cache
   */
  public void addBody(MessageHeader header, MessageBody body) {
   String headerID = header.getFieldValue("Message-Id");
   if (m_headers.containsKey(headerID)) {
     m_bodies.put(headerID, body);
     m_didChange = true;
   }
  }

  /**
   * Determine whether this Cache object has changed since it was last
   * deserialised.
   @return true if the ServerCache needs to be serialised.
   */
  public boolean needsSaved() {
     return m_didChange;
  }

  /**
   * Determine whether any headers have ever been added to this cache.
   */
  public boolean hasEverDownloaded() {
   return (m_lastUpdate != null);
  }

  /**
   * Get the last date the cache had headers added to it, in NNTP server
   * date format (yymmddhhmmss)
   */
  public String getLastUpdate() {
   return m_lastUpdate;
  }

// Private methods

 /**
  * Add a header, making it a child of the first message identified on its
  * references header. If that can't be found, all ids in the references header
  * are tried. If no id can be found, or the message is not a reply, the message
  * is added to the root node.
  @param newHeader the new message header
  */
 private void insertHeader(MessageHeader newHeader) {
   DefaultMutableTreeNode node = new DefaultMutableTreeNode(newHeader);
   // Attempt to add it into the tree.
   String parent = null;
   if (newHeader.hasField("References"))
     parent = getParentID(newHeader.getFieldValue("References"));
   // if parent is null, add the node at the root.
   if (parent == null) {
     ((DefaultMutableTreeNode)m_headerTree.getRoot()).add(node);

   }else
     // look up the parent (which getParentID assures us exists), and add
     ((DefaultMutableTreeNode)m_headers.get(parent)).add(node);
   // Finally, add the header to the hashtable.
   m_headers.put(newHeader.getFieldValue("Message-Id"), node);
   m_didChange = true;
 }

 /**
  * Get the message id that this message refers to, if it exists in the cache.
  * If not, return null.
  @param references The References header of the message
  @return a message ID that the message refers to, or null if none exists
  */
 private String getParentID(String references) {
   String[] refs = StringUtils.getWords(references);
   if (refs.length == 0) return null;
   /*
    * According to Byte, this is a more efficient way of doing array loops:
    * it eliminates the condition test on each loop cycle
    */
   try {
     /*
      * Return the last message id in the references that exists in the cache
      */
     for (int i=refs.length-1;;i--) {
       if (hasHeader(refs[i])) return refs[i];
     }
   } catch (ArrayIndexOutOfBoundsException e) {}
   return null;  // none found in cache.
 }

 /**
  * Post processing serialization method. This routine recreates the message-id
  * hashtable from the message tree.
  */
 private void readObject(ObjectInputStream in) throws IOException,
     ClassNotFoundException {
   in.defaultReadObject();         // Read normally
   recreateHashtable();
   m_didChange = false;            // Nothing has changed yet.
 }

 /**
  * Recreates the hashtable from the tree of headers.
  */
 private void recreateHashtable() {
   m_headers = new Hashtable();
   hashNode((DefaultMutableTreeNode)m_headerTree.getRoot());
 }

 /**
  * Recursive routine which hashes a tree node and its children.
  */
 private void hashNode(DefaultMutableTreeNode node) {
   Object obj = node.getUserObject();
   if (obj != null)
     m_headers.put(((MessageHeader)obj).getFieldValue("Message-Id"), node);
   if (node.getChildCount() > 0) {
     Enumeration enum = node.children();
     while (enum.hasMoreElements()) {
       hashNode((DefaultMutableTreeNode)enum.nextElement());
     }
   }

 }

 /**
  * The all-singing & dancing test routine.
  */
 public void doTest() {
   // First, create some headers.
   Vector vec = new Vector();
   MessageHeader head = new MessageHeader();
   head.setField("Message-Id", "1@test.com");
   head.setField("Subject", "Bananas are cool");
   vec.addElement(head);
   head = new MessageHeader();
   head.setField("Message-Id", "33@wiredsoc.ml.org");
   head.setField("Subject", "Swing Rules!");
   vec.addElement(head);
   head = new MessageHeader();
   // A reply about bananas.
   head.setField("Message-Id", "42@wiredsoc.ml.org");
   head.setField("Subject", "Re: Bananas are cool");
   head.setField("References", "1@test.com");
   vec.addElement(head);
   // A reply about that reply.
   head = new MessageHeader();
   head.setField("Message-Id", "99@test.com");
   head.setField("Subject", "Re: Bananas are cool");
   head.setField("References", "42@wiredsoc.ml.org 1@test.com");
   vec.addElement(head);
   // add the headers
   addHeaders(vec, "010101010101");
   // dump the tree
   dumpTreeNode((DefaultMutableTreeNode)m_headerTree.getRoot());

 }

 /**
  * Dumps a tree node and all its children
  */
 private void dumpTreeNode(DefaultMutableTreeNode node) {
   Object obj = node.getUserObject();
   if (obj == null)
     System.out.println("ROOT NODE");
   else
     System.out.println(obj.toString());
   if (node.getChildCount() > 0) {
     System.out.println("CHILDREN:");
     Enumeration enum = node.children();
     while (enum.hasMoreElements()) {
       dumpTreeNode((DefaultMutableTreeNode)enum.nextElement());
     }
     System.out.println("---");
   }
 }



}