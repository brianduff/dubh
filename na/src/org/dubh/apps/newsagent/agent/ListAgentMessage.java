// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: ListAgentMessage.java,v 1.4 1999-11-09 22:34:40 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://wired.st-and.ac.uk/~briand/newsagent/
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
package org.javalobby.apps.newsagent.agent;

/**
 * A reply from a ListAgent.
 * Version History: <UL>
 * <LI>0.1 [28/04/98]: Initial Revision
 *</UL>
 @author Brian Duff
 @version 0.1 [28/04/98]
 */
public class ListAgentMessage {

 /** whether to delete the message from the list of messages */
 protected boolean m_wasDelete;
 /** whether the header changed */
 protected boolean m_headerChanged;
 /** whether the font or colour changed */
 protected boolean m_displayChanged;
 /** whether to display a message to the user */
 protected boolean m_userMessage;
 /** The message */
 protected String m_message;


 /**
  * Construct a ListAgentMessage, in which nothing changed.
  */
 public ListAgentMessage() {
   m_wasDelete = false;
   m_headerChanged = false;
   m_displayChanged = false;
   m_userMessage = false;
   m_message = "";
 }

 /**
  * Construct a ListAgentMessage
  @param didDelete whether the message is to be deleted
  @param didChange whether the contents of the header changed
  @param didChangeDisplay whether the appearance (font or colour) of the message
     was changed
  @param isUserMessage whether this reply should display a message to the user
  @param message the message to display, or an empty string.
  */
 public ListAgentMessage(boolean didDelete, boolean didChange,
             boolean didChangeDisplay, boolean isUserMessage, String message) {
   m_wasDelete = didDelete;
   m_headerChanged = didChange;
   m_displayChanged = didChangeDisplay;
   m_userMessage = isUserMessage;
   m_message = message;
 }

 /**
  * Is this reply a deletion?
  @return true if the header is to be deleted
  */
 public boolean isDelete() {
   return m_wasDelete;
 }

 /**
  * Did the message header contents change?
  */
 public boolean isHeaderChanged() {
   return m_headerChanged;
 }

 /**
  * Did the appearance of the message change?
  */
 public boolean isAppearanceChange() {
   return m_displayChanged;
 }

 /**
  * Is this reply a message to display to the user?
  */
 public boolean isUserMessage() {
   return m_userMessage;
 }

 /**
  * Get the message to display to the user.
  */
 public String getUserMessage() {
   return m_message;
 }

}