// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: DemoListAgent.java,v 1.5 2001-02-11 02:50:59 briand Exp $
//   Copyright (C) 1997 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1997 - 2001 Brian Duff
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

package org.dubh.apps.newsagent.agent;

import java.awt.*;
import java.awt.event.*;
import org.dubh.dju.ui.GridBagConstraints2;
import javax.swing.*;
import org.dubh.apps.newsagent.nntp.MessageHeader;
import org.dubh.apps.newsagent.nntp.MessageBody;
import org.dubh.apps.newsagent.agent.ISendAgent;
import java.util.Properties;

/**
 * A very simple ListAgent, meant to serve as an example to users of how to
 * write their own agents.
 * Version History: <UL>
 * <LI>0.1 [28/04/98]: Initial Revision
 *</UL>
 @author Brian Duff
 @version 0.1 [28/04/98]
 */
public class DemoListAgent implements IListAgent {

   private static final String m_name = "Demo List Agent";
   private static final String m_desc =
   "This agent marks messages from anyone called Brian in red. It's really just a demo to show how Agents work.";
   private Properties m_properties;

  public DemoListAgent() {
   m_properties = new Properties();
  }

 /**
  * Get the name of this agent. This should be a short (1-3 word) description
  * of the agent.
  @return a String name for the agent
  */
 public String getName() {
   return m_name;
 }

 /**
  * Get an icon for the agent.
  @return a Swing Icon object, or null if no icon is to be used for this agent.
  */
 public Icon getIcon() {
   return null;
 }

 /**
  * Get a longer description of this agent.
  @return a string describing the agent in a few short sentences.
  */
 public String getDescription() {
   return m_desc;
 }

 /**
  * Return a panel which can be used to configure this agent in some way.
  @return a JPanel which the user will be able to configure your agent with
  */
 public JPanel getConfigurationPanel() {
   return new JPanel();
 }

 /**
  * Called when the user applies changes to a configuration panel.
  @param panConfig the configuration panel you returned in
     getConfigurationPanel(). You probably want to use a subclass of JPanel
     so you can get and set component values through an interface.
  */
 public void applyConfiguration(JPanel panConfig){
   ;
 }

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
 public ListAgentMessage checkMessage(MessageHeader msgHead) {
   /*
    * If the message is from "Brian", mark it in red.
    */
   String realName = msgHead.getRealName();
   if (realName.indexOf("Brian") >= 0) {
     // Change the appearance of the message
     msgHead.setForeground(Color.red);
     return new ListAgentMessage(false, false, true, false, "");
   } else
     return new ListAgentMessage();  // Nothing changed

 }

 /**
  * Get the properties of this agent, so that they can be serialised. These
  * are usually the configurations you allow the user to change with the
  * config panel.
  */
 public Properties getProperties() {
   return m_properties;

 }

 /**
  * Set the properties
  */
 public void setProperties(Properties p) {
   m_properties = p;

 }





}