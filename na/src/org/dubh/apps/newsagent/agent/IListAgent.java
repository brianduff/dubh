// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: IListAgent.java,v 1.4 1999-11-09 22:34:40 briand Exp $
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

import javax.swing.Icon;
import javax.swing.JPanel;
import org.javalobby.apps.newsagent.nntp.MessageHeader;
import java.util.Properties;

/**
 * The interface which must be implemented by all classes which are list agents.
 * Version History: <UL>
 * <LI>0.1 [28/04/98]: Initial Revision
 *</UL>
 @author Brian Duff
 @version 0.1 [28/04/98]
 */
public interface IListAgent {

 /**
  * Get the name of this agent. This should be a short (1-3 word) description
  * of the agent.
  @return a String name for the agent
  */
 String getName();

 /**
  * Get an icon for the agent.
  @return a Swing Icon object, or null if no icon is to be used for this agent.
  */
 Icon getIcon();

 /**
  * Get a longer description of this agent.
  @return a string describing the agent in a few short sentences.
  */
 String getDescription();

 /**
  * Return a panel which can be used to configure this agent in some way.
  @return a JPanel which the user will be able to configure your agent with
  */
 JPanel getConfigurationPanel();

 /**
  * Called when the user applies changes to a configuration panel.
  @param panConfig the configuration panel you returned in
     getConfigurationPanel(). You probably want to use a subclass of JPanel
     so you can get and set component values through an interface.
  */
 void applyConfiguration(JPanel panConfig);

 /**
  * Called by NewsAgent before it lists a message. Your agent should process
  * the message and return a ListAgentMessage object describing what
  * NewsAgent should do next.<P>
  * NewsAgent calls its agents in a <B>user specified</B> order, calling
  * checkMessage(), checking the return code. Your agent may alter the header
  * in any way you like, since the header passed to this method is the actual
  * header object that will be used. You should take care to notify NewsAgent
  * using the ListAgentMessage if you have decided to delete the message.
  @param msgHead the head of the message to be processed
  @return a ListAgentMessage object describing what the agent thought of the
     message it recieved
  */
 ListAgentMessage checkMessage(MessageHeader msgHead);

 /**
  * Get the properties of this agent, so that they can be serialised. These
  * are usually the configurations you allow the user to change with the
  * config panel.
  */
 Properties getProperties();

 /**
  * Set the properties
  */
 void setProperties(Properties p);


}