// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: AgentManager.java,v 1.7 2001-02-11 15:39:41 briand Exp $
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

import java.util.*;
import java.io.*;
import org.dubh.apps.newsagent.PreferenceKeys;
import org.dubh.apps.newsagent.GlobalState;
import org.dubh.apps.newsagent.agent.ISendAgent;
import org.dubh.apps.newsagent.agent.IListAgent;
import org.dubh.apps.newsagent.agent.SendAgentPreviewDialog;
import org.dubh.apps.newsagent.dialog.ErrorReporter;
import org.dubh.apps.newsagent.nntp.MessageHeader;
import org.dubh.apps.newsagent.nntp.MessageBody;
import org.dubh.dju.misc.StringUtils;
import org.dubh.dju.misc.Debug;
import org.dubh.dju.misc.UserPreferences;


import java.awt.*;
/**
 * Agent manager
 *
 * @author Brian Duff
 * @version $Id: AgentManager.java,v 1.7 2001-02-11 15:39:41 briand Exp $
 */
public class AgentManager {
   protected Vector m_activeSendAgents = new Vector();
   protected Hashtable m_allSendAgents = new Hashtable();
   protected Vector m_activeListAgents = new Vector();
   protected Hashtable m_allListAgents = new Hashtable();

   /**
    * Construct and initialise the AgentManager. Should be constructed <b>
    * after</b> properties have been initialised.
    */
  public AgentManager() {
   initSendAgents();
   initListAgents();
  }

  /**
   * get the names of all enabled send agents, in the correct order.
   */
  public Vector getEnabledSendAgentNames() {
   return m_activeSendAgents;
  }

  /**
   * get the names of all send agents, as an enumeration.
   */
  public Enumeration getAllSendAgentNames() {
   return m_allSendAgents.keys();
  }

  /**
   * get the names of all enabled list agents, in the correct order.
   */
  public Vector getEnabledListAgentNames() {
   return m_activeListAgents;
  }

  /**
   * get the names of all list agents, as an enumeration.
   */
  public Enumeration getAllListAgentNames() {
   return m_allListAgents.keys();
  }


  /**
   * Set the active send agents. This should be a vector containing the names of
   * send agents which are enabled, in the order in which they are to be
   * executed. You should make sure that the agents exist (and are properly
   * instantiated) in the send agent table.
   @param active a Vector containing String objects: the class names of active
     send agent objects.
   */
  public void setEnabledSendAgentNames(Vector active) {
     m_activeSendAgents = active;
  }

  /**
   * Set the table of all send agents. This table is a Hashtable mapping
   * agent names to objects which implement the ISendAgent interface. You can
   * add agents which don't exist (typically agents the user has added to the
   * preference file rather than through the user interface, since you shouldn't
   * allow the user to specify invalid agent classes from the UI), by including
   * an entry mapping the class name for the invalid agent to an Object object
   * or other non-ISendAgent implementing object.
   @param table a Hashtable as described above.
   */
  public void setSendAgentsTable(Hashtable table) {
     m_allSendAgents = table;
  }

  /**
   * Set the active list agents. This should be a vector containing the names of
   * list agents which are enabled, in the order in which they are to be
   * executed. You should make sure that the agents exist (and are properly
   * instantiated) in the list agent table.
   @param active a Vector containing String objects: the class names of active
     list agent objects.
   */
  public void setEnabledListAgentNames(Vector active) {
     m_activeListAgents = active;
  }

  /**
   * Set the table of all list agents. This table is a Hashtable mapping
   * agent names to objects which implement the IListAgent interface. You can
   * add agents which don't exist (typically agents the user has added to the
   * preference file rather than through the user interface, since you shouldn't
   * allow the user to specify invalid agent classes from the UI), by including
   * an entry mapping the class name for the invalid agent to an Object object
   * or other non-IListAgent implementing object.
   @param table a Hashtable as described above.
   */
  public void setListAgentsTable(Hashtable table) {
     m_allListAgents = table;
  }

  /**
   * Get a specific send agent, given its class name
   @return null if the agent doesn't exist, or couldn't be initialised for
       some reason.
   */
  public ISendAgent getSendAgent(String agentName) {
   Object result =  m_allSendAgents.get(agentName);
   if (result instanceof ISendAgent)
     return (ISendAgent)result;
   else
     return null;
  }

  /**
   * Get a specific list agent, given its class name
   @return null if the agent doesn't exist, or couldn't be initialised for
       some reason.
   */
  public IListAgent getListAgent(String agentName) {
   Object result =  m_allListAgents.get(agentName);
   if (result instanceof IListAgent)
     return (IListAgent)result;
   else
     return null;
  }

  /**
   * Create a new send agent given a class name. The agent isn't added into
   * any lists or preferences, allowing you to save the preferences at a
   * later time.<P>
   @return an ISendAgent or null.
   */
  public ISendAgent createSendAgent(String agentClassName) {
   ISendAgent sendAgent;
   sendAgent = (ISendAgent) createClass(agentClassName);
   return sendAgent;
  }

  /**
   * Create a new list agent given a class name. The agent isn't added into
   * any lists or preferences, allowing you to save the preferences at a
   * later time.<P>
   @return an IListAgent or null.
   */
  public IListAgent createListAgent(String agentClassName) {
   IListAgent listAgent;
   listAgent = (IListAgent) createClass(agentClassName);
   return listAgent;
  }

  /**
   * Store information about send agents. Agents which don't have the property
   * isSystemAgent are stored in the newsagent.agents.send.installed user
   * preference. Agent properties are saved to their respective files,
   * creating directories for them if necessary, under the top level agents
   * directory. This directory will be created if it doesn't exist. The
   * user preference file <b>is not saved</b> by this method. You should be
   * sure to call GlobalState.savePreferences() after calling this method
   * at some point.
   */
  public void saveSendAgents() {
     UserPreferences prefs = GlobalState.getPreferences();
   /* First, store the names of active agents in order in the
    * newsagent.agents.send.active preference.
    */
   prefs.setPreference(PreferenceKeys.AGENTS_SEND_ACTIVE,
     StringUtils.createSentence(m_activeSendAgents));
   /*
    * Now save preferences for all agents which are really agents.
    */
   Vector installedAgents = new Vector();
   Enumeration allAgentsEnum = m_allSendAgents.keys();
   while (allAgentsEnum.hasMoreElements()) {
     String agentName = (String) allAgentsEnum.nextElement();
     Object agent = m_allSendAgents.get(agentName);
     if (agent instanceof ISendAgent) {
       Properties p = ((ISendAgent)agent).getProperties();
       if (p != null) {
          saveAgentProperties(agentName, p);
          /* If the agent isn't a system agent, store it in the
           * newsagent.agents.send.installed property. */
          if (((String)p.getProperty("isSystemAgent", "No")).equals("No"))
           installedAgents.addElement(agentName);
       }
     }
   }
   /* Store the newsagent.agents.send.installed property. */
   prefs.setPreference(PreferenceKeys.AGENTS_SEND_INSTALLED,
     StringUtils.createSentence(installedAgents));
  }

  /**
   * Store information about list agents. Agents which don't have the property
   * isSystemAgent are stored in the newsagent.agents.list.installed user
   * preference. Agent properties are saved to their respective files,
   * creating directories for them if necessary, under the top level agents
   * directory. This directory will be created if it doesn't exist. The
   * user preference file <b>is not saved</b> by this method. You should be
   * sure to call GlobalState.savePreferences() after calling this method
   * at some point.
   */
  public void saveListAgents() {
     UserPreferences prefs = GlobalState.getPreferences();
   /* First, store the names of active agents in order in the
    * newsagent.agents.list.active preference.
    */
   prefs.setPreference(PreferenceKeys.AGENTS_LIST_ACTIVE,
     StringUtils.createSentence(m_activeListAgents));
   /*
    * Now save preferences for all agents which are really agents.
    */
   Vector installedAgents = new Vector();
   Enumeration allAgentsEnum = m_allListAgents.keys();
   while (allAgentsEnum.hasMoreElements()) {
     String agentName = (String) allAgentsEnum.nextElement();
     Object agent = m_allListAgents.get(agentName);
     if (agent instanceof IListAgent) {
       Properties p = ((IListAgent)agent).getProperties();
       if (p != null) {
          saveAgentProperties(agentName, p);
          /* If the agent isn't a system agent, store it in the
           * newsagent.agents.send.installed property. */
          if (((String)p.getProperty("isSystemAgent", "No")).equals("No"))
           installedAgents.addElement(agentName);
       }
     }
   }
   /* Store the newsagent.agents.send.installed property. */
   prefs.setPreference(PreferenceKeys.AGENTS_LIST_INSTALLED,
     StringUtils.createSentence(installedAgents));
  }

  /**
   * Allow list agents to process a single header in some way.
   @param hd a MessageHeader object to be passed to all the list agents.
   @return true if the message is to be kept, false if it is to be deleted.
   */
  public boolean callListAgents(MessageHeader hd) {
   hd.setFont(new Font("Dialog", Font.PLAIN, 12));
   hd.setForeground(Color.black);
   Enumeration en = m_activeListAgents.elements();
   boolean keepMessage = true;
   while (en.hasMoreElements()) {
     IListAgent myAgent = getListAgent((String)en.nextElement());
     if (myAgent != null) {
       keepMessage = !myAgent.checkMessage(hd).isDelete();
     }
   }
   return keepMessage;
  }

  /**
   * Allow send agents to process a single message in some way. This routine
   * interacts with the user. If any errors or warnings occurred, a dialogue
   * will be displayed containing all the error and / or warning messages from
   * agents. If all the messages are warnings, the user will be asked if they
   * are sure they want to send the message. If any of the messages are errors,
   * the user will not have the option of sending the message. The user can use
   * the following configuration options to change the behaviour of this routine:
   * <UL>
   * <LI><B>newsagent.agents.send.ignorewarnings</B>
   *    -  If true, warning messages will be suppressed and the message will
   *       always be sent, provided there are no errors.
   * </LI>
   * <LI><B>newsagent.agents.send.ignoreerrors</B>
   *    -  If true, error (and warning) messages will be suppressed and the
   *       message will always be sent. It is intended that this option is
   *       not directly available from the preferences user interface, the
   *       user should just disable offending individual agents.
   * </LI>
   * <LI><B>newsagent.agents.send.alwayspreview</B>
   *    -  If true, a preview window including all headers and the full body
   *       text of the message (after it has been processed by all agents)
   *       will be displayed before the message is sent. If there were warnings,
   *       the preview window will only be displayed if the user chose to
   *       send the message anyway.
   * </LI>
   * </UL>
   @param hd a MessageHeader object to be passed to all send agents.
   @param bd a MessageBody object to be passed to all send agents.
   @return true if it is still ok to send the message.
   */
  public boolean callSendAgents(MessageHeader hd, MessageBody bd) {
     Enumeration en = m_activeSendAgents.elements();
     Frame fraTmp = new Frame();
     SendAgentWarningsDialog dlgWarnings = new SendAgentWarningsDialog(fraTmp);

     boolean wasWarning = false;
     boolean wasError   = false;
     boolean wasChanged = false;
     boolean postOk     = true;

     while (en.hasMoreElements()) {
        ISendAgent myAgent = getSendAgent((String)en.nextElement());
        if (myAgent != null) {
           SendAgentMessage msg = myAgent.checkMessage(hd, bd);
           if (msg.isErrorMessage()) {
              wasError = true;
              dlgWarnings.addError(msg.getErrorMessage(), myAgent.getName());
           }
           if (msg.isWarningMessage()) {
              wasWarning = true;
              dlgWarnings.addWarning(msg.getErrorMessage(), myAgent.getName());
           }
           wasChanged = (msg.isBodyChanged() || msg.isHeaderChanged());
        } else {
           Debug.println("AgentManager.callSendAgents: Encountered a null Send Agent.");
        }
     }

     /* display the errors / warnings dialogue iff:
      * !ignorewarnings && wasWarning ||
      * !ignoreerrors && wasError
      * and post the message only if isPostingOk() on the dialogue returns
      * true after displaying it.
      */

     UserPreferences prefs = GlobalState.getPreferences();

     boolean ignorewarnings = prefs.getBoolPreference(
           PreferenceKeys.AGENTS_SEND_IGNOREWARNINGS, false);
     boolean ignoreerrors   = prefs.getBoolPreference(
           PreferenceKeys.AGENTS_SEND_IGNOREERRORS, false);
     if ((!ignorewarnings && wasWarning) || (!ignoreerrors && wasError)) {
        dlgWarnings.setVisible(true);
        postOk = dlgWarnings.isPostingOk();
     }

     // Dispose the warnings dialogue, whether it was used or not.
     dlgWarnings.dispose();

     /*
      * Display the preview window only if the alwayspreview option is set
      * and the message has been changed and posting is still ok.
      */
     boolean alwayspreview = prefs.getBoolPreference(
           PreferenceKeys.AGENTS_SEND_ALWAYSPREVIEW, true);
     if (postOk && alwayspreview && wasChanged) {
        SendAgentPreviewDialog dlgPreview = new SendAgentPreviewDialog(fraTmp, hd, bd);
        dlgPreview.setVisible(true);
        postOk = dlgPreview.isPostingOk();
        dlgPreview.dispose();
     }

     // Dispose of the temporary frame
     fraTmp.dispose();

     return postOk;
  }

  private void initSendAgents() {
   /* Read the default send agents and user installed agents */

     UserPreferences prefs = GlobalState.getPreferences();
     m_activeSendAgents = new Vector();
     m_allSendAgents    = new Hashtable();
     String[] allAgentNames = StringUtils.getWords(
       GlobalState.getRes().getString("newsagent.agents.send.defaultlist")+" "+
       prefs.getPreference(PreferenceKeys.AGENTS_SEND_INSTALLED, ""));
     for (int i=0; i< allAgentNames.length; i++) {
       ISendAgent sendAgent;
       sendAgent = (ISendAgent) createClass(allAgentNames[i]);
       if (sendAgent == null) {
         m_allSendAgents.put(allAgentNames[i], new Object());
       } else  {
         sendAgent.setProperties(loadAgentProperties(allAgentNames[i]));
         m_allSendAgents.put(allAgentNames[i], sendAgent);
       } //if
     } //for

     /* Read the list of active agents */

     String sActive = prefs.getPreference(PreferenceKeys.AGENTS_SEND_ACTIVE, "");
     String[] active = StringUtils.getWords(sActive);
     for (int i=0;i<active.length;i++) {
       m_activeSendAgents.addElement(active[i]);
     }

  }

  private void initListAgents() {
   /* Read the default send agents and user installed agents */

     UserPreferences prefs = GlobalState.getPreferences();
     m_activeListAgents = new Vector();
     m_allListAgents    = new Hashtable();
     String[] allAgentNames = StringUtils.getWords(
       GlobalState.getRes().getString("newsagent.agents.list.defaultlist")+" "+
       prefs.getPreference(PreferenceKeys.AGENTS_LIST_INSTALLED, ""));
     for (int i=0; i< allAgentNames.length; i++) {
       IListAgent listAgent;
       listAgent = (IListAgent) createClass(allAgentNames[i]);
       if (listAgent == null) {
         m_allListAgents.put(allAgentNames[i], new Object());
       } else  {
         listAgent.setProperties(loadAgentProperties(allAgentNames[i]));
         m_allListAgents.put(allAgentNames[i], listAgent);
       } //if
     } //for

     /* Read the list of active agents */

     String sActive = prefs.getPreference(PreferenceKeys.AGENTS_LIST_ACTIVE, "");
     String[] active = StringUtils.getWords(sActive);
     for (int i=0;i<active.length;i++) {
       m_activeListAgents.addElement(active[i]);
     }

  }

  /**
   * Check the agents directory exists. Tries to create it if not.
   @return true if the agents directory exists, or was created, false if there
     was a problem with the directory.
   */
  private boolean checkAgentsDir() {
   /*
   File agentDir = new File(GlobalState.agentDir);
   if (!agentDir.exists()) {
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Agents directory doesn't exist: creating.");
     return agentDir.mkdir();
   }
   if (!agentDir.isDirectory()) {
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Agents directory ("+GlobalState.agentDir+") is a file!!!");
     return false;
   }
   /* Agent dir already exists and is a directory, so everything is OK */
   return true;
  }

  /**
   * Load the properties of an agent if they exist, or return a new Properties
   * object.
   */
  private Properties loadAgentProperties(String agentClassName) {
      return null;

  }

  /**
   * Save the properties of an agent, creating directories if necessary.
   @param agentClassName the name of the class of the agent.
   @param agentProperties the Properties object for the agent.
   @return false if the agent couldn't be serialised for some reason (should
     really throw an exception)
   */
  private boolean saveAgentProperties(String agentClassName,
       Properties agentProperties) {

   return false;

  }

  /**
   * Create an instance of a specified class
   */
  private Object createClass(String classname) {
   try {
     Class objClass = Class.forName(classname);
     Object objObject = objClass.newInstance();
     return objObject;
   } catch (ClassNotFoundException cnf) {
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Agent class "+classname+" not found on CLASSPATH");
   } catch (InstantiationException iexp) {
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Agent class "+classname+" couldn't be instantiated");
   } catch (IllegalAccessException ilex) {
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Agent class "+classname+" couldn't be accessed.");
   }
   return null;
  }


}

//
// Old history
// 0.1 [14/04/98]: Initial Revision
// 0.2 [15/04/98]: Added setEnabledSendAgentNames and setAgentsTable
// 0.3 [28/04/98]: Added support for list agents
// 0.4 [06/06/98]: Added dubh utils import for StringUtils
// 0.5 [07/06/98]: Added support for send agents. Moved into the
//    org.dubh.apps.newsagent.agent package.
//
// New history
// $Log: not supported by cvs2svn $
// Revision 1.6  2001/02/11 02:50:59  briand
// Repackaged from org.javalobby to org.dubh
//
// Revision 1.5  1999/11/09 22:34:40  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.4  1999/06/01 00:25:16  briand
// Change to use DJU ResourceManager, UserPreferences, DubhOkCancelDialog, Debug.
//