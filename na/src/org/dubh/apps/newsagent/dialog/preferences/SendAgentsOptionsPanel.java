// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: SendAgentsOptionsPanel.java,v 1.4 1999-06-01 00:35:09 briand Exp $
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
package dubh.apps.newsagent.dialog.preferences;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import dubh.apps.newsagent.agent.ISendAgent;
import dubh.apps.newsagent.GlobalState;
import dubh.apps.newsagent.agent.AgentManager;
import dubh.apps.newsagent.dialog.ErrorReporter;
import dubh.utils.ui.preferences.PreferencePage;
import dubh.utils.misc.UserPreferences;

/**
 * Send agents options panel for the send agents tab in the options dialog:
 @author Brian Duff
 @version $Id: SendAgentsOptionsPanel.java,v 1.4 1999-06-01 00:35:09 briand Exp $
 */
public class SendAgentsOptionsPanel extends PreferencePage {
  public TitledBorder borderAvailable = new TitledBorder(new EtchedBorder(),
   GlobalState.getRes().getString("AgentsOptionsPanel.AvailableAgents"));
  public TitledBorder borderAgent= new TitledBorder(new EtchedBorder(),
   GlobalState.getRes().getString("AgentsOptionsPanel.AgentName"));
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel panAvailableAgents = new JPanel();
  JPanel panAgent = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JList listAgents = new JList();
  JTextPane taDescription = new JTextPane();
  JScrollPane scrollAgents = new JScrollPane(listAgents);
  JButton cmdMoveUp = new JButton();
  JButton cmdMoveDown = new JButton();
  JButton cmdAdd = new JButton();
  JLabel labDescription = new JLabel();
  JScrollPane scrollDesc = new JScrollPane(taDescription);
  JButton cmdConfigure = new JButton();
  DefaultListModel lmAgents = new DefaultListModel();
   JPanel panMain;


  public SendAgentsOptionsPanel() {
    super("Send Agents", "Configure agents that affect outgoing messages",
       null);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    setContent(panMain);
  }

  public void jbInit() throws Exception{
     dubh.utils.misc.ResourceManager r = GlobalState.getRes();
 //NLS   r.initButton(cmdMoveUp, "AgentsOptionsPanel.MoveUp");
    cmdMoveUp.addActionListener(new SendAgentsOptionsPanel_cmdMoveUp_actionAdapter(this));
 //NLS   r.initButton(cmdMoveDown, "AgentsOptionsPanel.MoveDown");
    cmdMoveDown.addActionListener(new SendAgentsOptionsPanel_cmdMoveDown_actionAdapter(this));
 //NLS   r.initButton(cmdAdd, "AgentsOptionsPanel.Add");
    labDescription.setText(GlobalState.getRes().getString("AgentsOptionsPanel.Description"));
 //NLS   r.initButton(cmdConfigure, "AgentsOptionsPanel.Configure");
    cmdConfigure.addActionListener(new SendAgentsOptionsPanel_cmdConfigure_actionAdapter(this));
    cmdAdd.addActionListener(new SendAgentsOptionsPanel_cmdAdd_actionAdapter(this));
    panAgent.setLayout(gridBagLayout3);
    panAvailableAgents.setLayout(gridBagLayout2);
  //  this.setSize(new Dimension(350, 330));
    panAvailableAgents.setBorder(borderAvailable);
    panAgent.setBorder(borderAgent);
    listAgents.addMouseListener(new SendAgentsOptionsPanel_listAgents_mouseAdapter(this));
    listAgents.addListSelectionListener(new SendAgentsOptionsPanel_listAgents_listSelectionAdapter(this));
    listAgents.setModel(lmAgents);
    listAgents.setCellRenderer(new SendAgentsListRenderer());
    taDescription.setEditable(false);
    panMain = new JPanel();
    panMain.setLayout(gridBagLayout1);
    panMain.add(panAvailableAgents, new GridBagConstraints2(0, 0, 1, 1, 1.0, 0.35
            ,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    panAvailableAgents.add(scrollAgents, new GridBagConstraints2(0, 0, 1, 3, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    panAvailableAgents.add(cmdMoveUp, new GridBagConstraints2(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 1, 1, 5), 0, 0));
    panAvailableAgents.add(cmdMoveDown, new GridBagConstraints2(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 5, 5), 0, 0));
    panAvailableAgents.add(cmdAdd, new GridBagConstraints2(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 0, 5), 0, 0));
    panMain.add(panAgent, new GridBagConstraints2(0, 1, 1, 1, 1.0, 0.65
            ,GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    panAgent.add(labDescription, new GridBagConstraints2(0, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 1, 5), 0, 0));
    panAgent.add(scrollDesc, new GridBagConstraints2(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1, 5, 5, 5), 0, 0));
    panAgent.add(cmdConfigure, new GridBagConstraints2(1, 1, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(1, 1, 5, 5), 0, 0));
  //   revertPreferences();
     /*
      * No initial selection
      */
       borderAgent.setTitle(GlobalState.getRes().getString("SendAgentsOptionsPanel.NoSelection"));
       cmdConfigure.setEnabled(false);
       cmdMoveUp.setEnabled(false);
       cmdMoveDown.setEnabled(false);
       taDescription.setText("");
  }

  public void save(UserPreferences s) {
   Vector installedNames = new Vector();
   Hashtable allAgents   = new Hashtable();

   /*
    * Go through the list in order. Add all agents to the hashtable unless
    * they are null agents, in which case add an empty Object. Add the name
    * of the item if it is active.
    */
   Enumeration listItems = lmAgents.elements();
   while (listItems.hasMoreElements()) {
     AgentListEntry entry = (AgentListEntry) listItems.nextElement();
     if (entry.agent == null) {
       allAgents.put(entry.agentClassName, new Object());
     } else {
       allAgents.put(entry.agentClassName, entry.agent);
     }
     if (entry.isEnabled) {
       installedNames.addElement(entry.agentClassName);
     }
   }
   /*
    * Now save the agents
    */
    GlobalState.getAgentManager().setEnabledSendAgentNames(installedNames);
    GlobalState.getAgentManager().setSendAgentsTable(allAgents);
    GlobalState.getAgentManager().saveSendAgents();
  }

  public void revert(UserPreferences s) {
   populateAgentsList();
  }

  void cmdAdd_actionPerformed(ActionEvent e) {
   boolean ok = true;
   String className=null;
   while(ok) {
     className = ErrorReporter.getInput("SendAgentsOptionsPanel.GetAgentClass");
     if ((ok =  isAgentAlreadyInList(className))) {
       ErrorReporter.error("SendAgentsOptionsPanel.AlreadyInList",
         new String[] {className});
     }
   }
   if (className != null) {
     ISendAgent agent = GlobalState.getAgentManager().createSendAgent(className);
     if (agent == null) {
       ErrorReporter.error("SendAgentsOptionsPanel.CantEnable",
         new String[] {className});
     } else {
       /*
        * Insert the new agent and select it.
        */
        AgentListEntry newEntry = new AgentListEntry(agent, false, className);
       /*
        * Hmm. Swing seems to have a synchronisation bug of some sort here.
        * I think it probably has something to do with the modal input dialog
        * but I'm not sure. Forcing mutual exclusion seems to help.
        */
       synchronized(lmAgents) {
         lmAgents.addElement(newEntry);
       }
       listAgents.setSelectedIndex(lmAgents.getSize()-1);
     }

   }
  }

  void cmdMoveUp_actionPerformed(ActionEvent e) {
   int curIndex = listAgents.getSelectedIndex();
   if (curIndex > 0) {
     AgentListEntry sel = (AgentListEntry)lmAgents.elementAt(curIndex);
     lmAgents.removeElementAt(curIndex);
     lmAgents.insertElementAt(sel, curIndex-1);
     listAgents.setSelectedIndex(curIndex-1);
     listAgents.repaint();
     if (curIndex == 1) // if we've moved to the top
       cmdMoveUp.setEnabled(false);
     if (curIndex == lmAgents.getSize()-1) // if we've moved from the bottom
       cmdMoveDown.setEnabled(true);
   }
  }

  void cmdMoveDown_actionPerformed(ActionEvent e) {
   int curIndex = listAgents.getSelectedIndex();
   if (curIndex < lmAgents.getSize()-1) {
     AgentListEntry sel = (AgentListEntry)lmAgents.elementAt(curIndex);
     lmAgents.removeElementAt(curIndex);
     lmAgents.insertElementAt(sel,curIndex+1);
     listAgents.setSelectedIndex(curIndex+1);
     listAgents.repaint();
     if (curIndex == 0) // if we've moved from the top
       cmdMoveUp.setEnabled(true);
     if (curIndex == lmAgents.getSize()-2) // if we've moved to the bottom
       cmdMoveDown.setEnabled(false);
   }
  }

  void cmdConfigure_actionPerformed(ActionEvent e) {

  }

  void listAgents_valueChanged(ListSelectionEvent e) {
   if (!e.getValueIsAdjusting()) {
     if (e.getFirstIndex() >= 0) {
       // dodoebuyg
       // An agent is selected. Display its details.
       if (listAgents.getSelectedIndex()==0) 
         cmdMoveUp.setEnabled(false);
       else
         cmdMoveUp.setEnabled(true);
       if (listAgents.getSelectedIndex()==lmAgents.getSize()-1)
         cmdMoveDown.setEnabled(false);
       else
         cmdMoveDown.setEnabled(true);
       cmdMoveUp.repaint();
       cmdMoveDown.repaint();
       int selection = listAgents.getSelectedIndex();
       if (selection >= 0) selectAgent((AgentListEntry)lmAgents.elementAt(selection));
     } else {
       // nowt selected, clear panels, disable buttons etc.
       borderAgent.setTitle(GlobalState.getRes().getString("SendAgentsOptionsPanel.NoSelection"));
       cmdConfigure.setEnabled(false);
       cmdMoveUp.setEnabled(false);
       cmdMoveDown.setEnabled(false);
       cmdMoveUp.repaint();
       cmdMoveDown.repaint();
       taDescription.setText("");
       cmdConfigure.repaint();
       taDescription.repaint();
       panAgent.repaint();

     }
   }
  }

  void listAgents_mouseClicked(MouseEvent e) {
   if (e.getClickCount() == 2) {    // Double click events
     /* Toggle whether the agent is enabled, unless it is a "not found"
      * agent, in which case, it should always be disabled.
      */
     AgentListEntry currentEntry = (AgentListEntry)lmAgents.elementAt(
       listAgents.getSelectedIndex());
     if (currentEntry.agent == null && !currentEntry.isEnabled) {
       ErrorReporter.error("SendAgentsOptionsPanel.CantEnable",
           new String[] {currentEntry.agentClassName});
       return;
     }
     /* OK, Agent really exists, so we toggle whether it is enabled. */
     currentEntry.isEnabled = !currentEntry.isEnabled;
     listAgents.repaint();

   }
  }

  private boolean isAgentAlreadyInList(String agentClassName) {
   if (agentClassName == null) return false;
   Enumeration enum = lmAgents.elements();
   while (enum.hasMoreElements()) {
       AgentListEntry thisEntry = (AgentListEntry) enum.nextElement();
       if (thisEntry.agentClassName.equals(agentClassName))
         return true;
   }
   return false;

  }

  /**
   * Displays the information on an agent in the panel to the bottom.
   */
  private void selectAgent(AgentListEntry alEntry) {
   if (alEntry.agent == null) {
     borderAgent.setTitle(alEntry.agentClassName);
     taDescription.setText(GlobalState.getRes().getString("SendAgentsOptionsPanel.BadAgent"));
     cmdConfigure.setEnabled(false);
   } else {
     borderAgent.setTitle(alEntry.agent.getName());
     taDescription.setText(alEntry.agent.getDescription());
     cmdConfigure.setEnabled(true);
   }
   taDescription.repaint();
   cmdConfigure.repaint();
   panAgent.repaint();
  }

  /**
   * Populate the available agents list.
   */
  private void populateAgentsList() {
   AgentManager am = GlobalState.getAgentManager();
   Vector activeAgents = am.getEnabledSendAgentNames();
   Enumeration enum = activeAgents.elements();
   Enumeration allAgentNames = am.getAllSendAgentNames();

   /*
    * Add enabled agents in the correct order
    */
   while (enum.hasMoreElements()) {
     String thisAgentName =  (String)enum.nextElement();
     ISendAgent agent = am.getSendAgent(thisAgentName);
     if (agent == null)
       lmAgents.addElement(new AgentListEntry(agent, false, thisAgentName));
     else
       lmAgents.addElement(new AgentListEntry(agent, true, thisAgentName));
   }

   /*
    * Add disabled agents
    */
   while (allAgentNames.hasMoreElements()) {
     String key = (String) allAgentNames.nextElement();
     if (!activeAgents.contains(key))
       lmAgents.addElement(new AgentListEntry(am.getSendAgent(key),
           false,key));
   }
  }

}

class AgentListEntry {

 public ISendAgent agent;
 public boolean    isEnabled;
 public String     agentClassName;

 public AgentListEntry(ISendAgent agent, boolean isEnabled) {
   this.agent = agent;
   this.isEnabled = isEnabled;
 }

 public AgentListEntry(ISendAgent agent, boolean isEnabled, String className) {
   this(agent, isEnabled);
   this.agentClassName = className;
 }

}

class SendAgentsOptionsPanel_cmdAdd_actionAdapter implements java.awt.event.ActionListener {
  SendAgentsOptionsPanel adaptee;

  SendAgentsOptionsPanel_cmdAdd_actionAdapter(SendAgentsOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdAdd_actionPerformed(e);
  }
}

class SendAgentsOptionsPanel_cmdMoveUp_actionAdapter implements java.awt.event.ActionListener {
  SendAgentsOptionsPanel adaptee;

  SendAgentsOptionsPanel_cmdMoveUp_actionAdapter(SendAgentsOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdMoveUp_actionPerformed(e);
  }
}

class SendAgentsOptionsPanel_cmdMoveDown_actionAdapter implements java.awt.event.ActionListener {
  SendAgentsOptionsPanel adaptee;

  SendAgentsOptionsPanel_cmdMoveDown_actionAdapter(SendAgentsOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdMoveDown_actionPerformed(e);
  }
}

class SendAgentsOptionsPanel_cmdConfigure_actionAdapter implements java.awt.event.ActionListener {
  SendAgentsOptionsPanel adaptee;

  SendAgentsOptionsPanel_cmdConfigure_actionAdapter(SendAgentsOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdConfigure_actionPerformed(e);
  }
}

class SendAgentsOptionsPanel_listAgents_listSelectionAdapter implements javax.swing.event.ListSelectionListener {
  SendAgentsOptionsPanel adaptee;

  SendAgentsOptionsPanel_listAgents_listSelectionAdapter(SendAgentsOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void valueChanged(ListSelectionEvent e) {
    adaptee.listAgents_valueChanged(e);
  }
}

class SendAgentsOptionsPanel_listAgents_mouseAdapter extends java.awt.event.MouseAdapter {
  SendAgentsOptionsPanel adaptee;

  SendAgentsOptionsPanel_listAgents_mouseAdapter(SendAgentsOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e) {
    adaptee.listAgents_mouseClicked(e);
  }
}


class SendAgentsListRenderer extends JCheckBox implements ListCellRenderer {

   public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus)
   {
     AgentListEntry entry = (AgentListEntry)value;
     if (entry.agent == null) {
       setText(entry.agentClassName+" - "+
         GlobalState.getRes().getString("SendAgentsOptionsPanel.NotFound"));
       setSelected(false);
     } else {
       setText(entry.agent.getName());
       setSelected(entry.isEnabled);
     }
    setOpaque(true);
    if (isSelected) {
       this.setBackground(UIManager.getColor("textHighlight"));
       this.setForeground(UIManager.getColor("textHighlightText"));
    } else {
       this.setBackground(UIManager.getColor("white"));
       this.setForeground(UIManager.getColor("textText"));
    }
      return this;
   }
}

//
// Old History:
// <LI>0.1 [14/04/98]: Initial Revision
// <LI>0.2 [08/05/98]: Now extends JPanel, internationalised
//
// New History:
//
// $Log: not supported by cvs2svn $