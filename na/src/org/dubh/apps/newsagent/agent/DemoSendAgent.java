// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: DemoSendAgent.java,v 1.3 1999-03-22 23:46:42 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
// ---------------------------------------------------------------------------
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 2 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software
//   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
// ---------------------------------------------------------------------------
//   Original Author: Brian Duff
//   Contributors:
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package dubh.apps.newsagent.agent;

import java.awt.*;
import java.awt.event.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;
import dubh.apps.newsagent.nntp.MessageHeader;
import dubh.apps.newsagent.nntp.MessageBody;
import dubh.apps.newsagent.agent.ISendAgent;
import java.util.Properties;

/**
 * A very simple SendAgent, meant to serve as an example to users of how to
 * write their own agents.
 * Version History: <UL>
 * <LI>0.1 [14/04/98]: Initial Revision
 * <LI>0.2 [09/06/98]: Changed implementation so that it actually does something
 *</UL>
 @author Brian Duff
 @version 0.2 [09/06/98]  
 */
public class DemoSendAgent implements ISendAgent {

 private static final String m_name = "Demo Agent";
 private static final String m_desc =
   "This agent stops a message from being sent, with an error message";
 private Properties m_properties;

 public DemoSendAgent() {
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
   return new DemoSendAgentPanel();
 }

 /**
  * Called when the user applies changes to a configuration panel.
  @param panConfig the configuration panel you returned in
     getConfigurationPanel(). You probably want to use a subclass of JPanel
     so you can get and set component values through an interface.
  */
 public void applyConfiguration(JPanel panConfig) {
   /*
    * Here, we would typecast panConfig to a DemoSendAgentPanel and use
    * its get methods to find out what the configuration changes were.
    */
 }

 /**
  * Called by NewsAgent before it sends a message. Your agent should process
  * the message and return a SendAgentMessage object describing what
  * NewsAgent should do next.<P>
  * NewsAgent calls its agents in a <B>user specified</B> order, calling
  * checkMessage(), checking the return code, then calling getMessageBody()
  * and getMessageHeader(), before passing the result of these calls into
  * the next agent in the list.
  @param msgBody the body of the message to be processed
  @param msgHead the head of the message to be processed
  @return a SendAgentMessage object describing what the agent thought of the
     message it recieved
  */
 public SendAgentMessage checkMessage(MessageHeader msgHead,
                                      MessageBody msgBody) {
   /*
    * Here we would do any processing with the head and body required.
    */
   return new SendAgentMessage(false, false, true, "I don't like your message.");
 }

 /**
  * Called by NewsAgent after it has called checkMessage, if your
  * SendAgent indicated that the message header has changed.
  */
 public MessageHeader getMessageHeader() {
   return null;
 }

 /**
  * Called by NewsAgent after it has called checkMessage, if your
  * SendAgent indicated that the message body has changed.
  */
 public MessageBody getMessageBody() {
   return null;
 }

 public Properties getProperties() {
   return m_properties;

 }

 public void setProperties(Properties p) {
   m_properties = p;

 }

}

/**
 * UI Panel for the DemoSendAgent. Meant to demonstrate how to do this to the
 * user.
 * Version History: <UL>
 * <LI>0.1 [14/04/98]: Initial Revision
 *</UL>
 @author Brian Duff
 @version 0.1 [14/04/98]
 */
class DemoSendAgentPanel extends JPanel {
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel labParamOne = new JLabel();
  JTextField tfParamOne = new JTextField();
  JCheckBox checkOne = new JCheckBox();
  JCheckBox checkTwo = new JCheckBox();

  public DemoSendAgentPanel() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void jbInit() throws Exception{
    labParamOne.setText("First Configuration Parameter:");
    checkOne.setText("First Checkbox");
    checkTwo.setText("Second Checkbox");
    this.setLayout(gridBagLayout1);
    this.add(labParamOne, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 1, 1), 0, 0));
    this.add(tfParamOne, new GridBagConstraints2(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 1, 0, 5), 0, 0));
    this.add(checkOne, new GridBagConstraints2(0, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(1, 5, 1, 5), 0, 0));
    this.add(checkTwo, new GridBagConstraints2(0, 2, 2, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(1, 5, 5, 5), 0, 0));
  }

  public boolean getCheckOne() {
   return checkOne.isSelected();
  }

  public boolean getCheckTwo() {
   return checkTwo.isSelected();
  }

  public String getFirstParam() {
   return tfParamOne.getText();
  }
}