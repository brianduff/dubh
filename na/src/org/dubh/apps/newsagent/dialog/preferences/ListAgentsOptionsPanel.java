// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: ListAgentsOptionsPanel.java,v 1.8 2001-02-11 15:42:42 briand Exp $
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

package org.dubh.apps.newsagent.dialog.preferences;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.dubh.dju.ui.GridBagConstraints2;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.dubh.apps.newsagent.agent.IListAgent;
import org.dubh.apps.newsagent.agent.AgentConfigDialogue;
import org.dubh.apps.newsagent.GlobalState;
import org.dubh.apps.newsagent.agent.AgentManager;
import org.dubh.apps.newsagent.dialog.ErrorReporter;
import org.dubh.dju.ui.preferences.PreferencePage;
import org.dubh.dju.misc.UserPreferences;
import org.dubh.dju.misc.Debug;
import org.dubh.apps.newsagent.PreferenceKeys;
/**
 * List agents options panel for the list agents tab in the options dialog:<P>
 @author Brian Duff
 @version $Id: ListAgentsOptionsPanel.java,v 1.8 2001-02-11 15:42:42 briand Exp $
 */
public class ListAgentsOptionsPanel extends PreferencePage {
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

   JPanel panTop = new JPanel();

  public ListAgentsOptionsPanel() {
    super("List Agents", "Configure agents that affect the display of messages",
          null);
    try {
      jbInit();
      setContent(panTop);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void jbInit() throws Exception{
    org.dubh.dju.misc.ResourceManager r = GlobalState.getRes();
//NLS    r.initButton(cmdMoveUp, "AgentsOptionsPanel.MoveUp");
    cmdMoveUp.addActionListener(new ListAgentsOptionsPanel_cmdMoveUp_actionAdapter(this));
//NLS    r.initButton(cmdMoveDown, "AgentsOptionsPanel.MoveDown");
    cmdMoveDown.addActionListener(new ListAgentsOptionsPanel_cmdMoveDown_actionAdapter(this));
//NLS    r.initButton(cmdAdd, "AgentsOptionsPanel.Add");
    labDescription.setText(GlobalState.getRes().getString("AgentsOptionsPanel.Description"));
//NLS    r.initButton(cmdConfigure, "AgentsOptionsPanel.Configure");
    cmdConfigure.addActionListener(new ListAgentsOptionsPanel_cmdConfigure_actionAdapter(this));
    cmdAdd.addActionListener(new ListAgentsOptionsPanel_cmdAdd_actionAdapter(this));
    panAgent.setLayout(gridBagLayout3);
    panAvailableAgents.setLayout(gridBagLayout2);
    panTop = new JPanel();
   // this.setSize(new Dimension(350, 330));
    panAvailableAgents.setBorder(borderAvailable);
    panAgent.setBorder(borderAgent);
    listAgents.addMouseListener(new ListAgentsOptionsPanel_listAgents_mouseAdapter(this));
    listAgents.addListSelectionListener(new ListAgentsOptionsPanel_listAgents_listSelectionAdapter(this));
    listAgents.setModel(lmAgents);
    listAgents.setCellRenderer(new ListAgentsListRenderer());
    taDescription.setEditable(false);
    panTop.setLayout(gridBagLayout1);
    panTop.add(panAvailableAgents, new GridBagConstraints2(0, 0, 1, 1, 1.0, 0.35
            ,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    panAvailableAgents.add(scrollAgents, new GridBagConstraints2(0, 0, 1, 3, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    panAvailableAgents.add(cmdMoveUp, new GridBagConstraints2(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 1, 1, 5), 0, 0));
    panAvailableAgents.add(cmdMoveDown, new GridBagConstraints2(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 5, 5), 0, 0));
    panAvailableAgents.add(cmdAdd, new GridBagConstraints2(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 0, 5), 0, 0));
    panTop.add(panAgent, new GridBagConstraints2(0, 1, 1, 1, 1.0, 0.65
            ,GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    panAgent.add(labDescription, new GridBagConstraints2(0, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 1, 5), 0, 0));
    panAgent.add(scrollDesc, new GridBagConstraints2(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1, 5, 5, 5), 0, 0));
    panAgent.add(cmdConfigure, new GridBagConstraints2(1, 1, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(1, 1, 5, 5), 0, 0));
     /*
      * No initial selection
      */
       borderAgent.setTitle(GlobalState.getRes().getString("ListAgentsOptionsPanel.NoSelection"));
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
     AgentListEntryL entry = (AgentListEntryL) listItems.nextElement();
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
    //GlobalState.getAgentManager().setEnabledListAgentNames(installedNames);
    //GlobalState.getAgentManager().setListAgentsTable(allAgents);
    //GlobalState.getAgentManager().saveListAgents();
  }

  public void revert(UserPreferences s) {
   populateAgentsList();
  }

  void cmdAdd_actionPerformed(ActionEvent e) {
   /*
   boolean ok = true;
   String className=null;
   while(ok) {
     className = ErrorReporter.getInput("ListAgentsOptionsPanel.GetAgentClass");
     if ((ok =  isAgentAlreadyInList(className))) {
       ErrorReporter.error("ListAgentsOptionsPanel.AlreadyInList",
         new String[] {className});
     }
   }
   if (className != null) {
     IListAgent agent = GlobalState.getAgentManager().createListAgent(className);
     if (agent == null) {
       ErrorReporter.error("ListAgentsOptionsPanel.CantEnable",
         new String[] {className});
     } else {
        AgentListEntryL newEntry = new AgentListEntryL(agent, false, className);

       synchronized(lmAgents) {
         lmAgents.addElement(newEntry);
       }
       listAgents.setSelectedIndex(lmAgents.getSize()-1);
     }

   }
   */
  }

  void cmdMoveUp_actionPerformed(ActionEvent e) {
   int curIndex = listAgents.getSelectedIndex();
   if (curIndex > 0) {
     AgentListEntryL sel = (AgentListEntryL)lmAgents.elementAt(curIndex);
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
     AgentListEntryL sel = (AgentListEntryL)lmAgents.elementAt(curIndex);
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
     // display the configuration dialogue for the currently selected agent.
   int curIndex = listAgents.getSelectedIndex();
  // if (curIndex < lmAgents.getSize()-1) {
     AgentListEntryL sel = (AgentListEntryL)lmAgents.elementAt(curIndex);
     JPanel optPan = sel.agent.getConfigurationPanel();
     if (optPan == null) {
       // agent doesn't have an options dialogue. Disable the button.
       if (Debug.TRACE_LEVEL_1)
          Debug.println(1, this, "This agent has no configuration dialogue!");
       cmdConfigure.setEnabled(false);
     } else {
       // construct a config dialogue and display it.
       AgentConfigDialogue config = new AgentConfigDialogue(new Frame(),
         optPan, sel.agent.getName());
       config.showAtCentre();
       if (!config.isCancelled())
         sel.agent.applyConfiguration(optPan);
       config.dispose();
     }
  // }
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
       if (selection >= 0) selectAgent((AgentListEntryL)lmAgents.elementAt(selection));
     } else {
       // nowt selected, clear panels, disable buttons etc.
       borderAgent.setTitle(GlobalState.getRes().getString("ListAgentsOptionsPanel.NoSelection"));
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
     AgentListEntryL currentEntry = (AgentListEntryL)lmAgents.elementAt(
       listAgents.getSelectedIndex());
     if (currentEntry.agent == null && !currentEntry.isEnabled) {
       ErrorReporter.error("ListAgentsOptionsPanel.CantEnable",
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
   Enumeration en = lmAgents.elements();
   while (en.hasMoreElements()) {
       AgentListEntryL thisEntry = (AgentListEntryL) en.nextElement();
       if (thisEntry.agentClassName.equals(agentClassName))
         return true;
   }
   return false;

  }

  /**
   * Displays the information on an agent in the panel to the bottom.
   */
  private void selectAgent(AgentListEntryL alEntry) {
   if (alEntry.agent == null) {
     borderAgent.setTitle(alEntry.agentClassName);
     taDescription.setText(GlobalState.getRes().getString("ListAgentsOptionsPanel.BadAgent"));
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
   /*AgentManager am = GlobalState.getAgentManager();
   Vector activeAgents = am.getEnabledListAgentNames();
   Enumeration en = activeAgents.elements();
   Enumeration allAgentNames = am.getAllListAgentNames();

   while (en.hasMoreElements()) {
     String thisAgentName =  (String)en.nextElement();
     IListAgent agent = am.getListAgent(thisAgentName);
     if (agent == null)
       lmAgents.addElement(new AgentListEntryL(agent, false, thisAgentName));
     else
       lmAgents.addElement(new AgentListEntryL(agent, true, thisAgentName));
   }

   while (allAgentNames.hasMoreElements()) {
     String key = (String) allAgentNames.nextElement();
     if (!activeAgents.contains(key))
       lmAgents.addElement(new AgentListEntryL(am.getListAgent(key),
           false,key));
   }
   */
  }

}

class AgentListEntryL {

 public IListAgent agent;
 public boolean    isEnabled;
 public String     agentClassName;

 public AgentListEntryL(IListAgent agent, boolean isEnabled) {
   this.agent = agent;
   this.isEnabled = isEnabled;
 }

 public AgentListEntryL(IListAgent agent, boolean isEnabled, String className) {
   this(agent, isEnabled);
   this.agentClassName = className;
 }

}

class ListAgentsOptionsPanel_cmdAdd_actionAdapter implements java.awt.event.ActionListener {
  ListAgentsOptionsPanel adaptee;

  ListAgentsOptionsPanel_cmdAdd_actionAdapter(ListAgentsOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdAdd_actionPerformed(e);
  }
}

class ListAgentsOptionsPanel_cmdMoveUp_actionAdapter implements java.awt.event.ActionListener {
  ListAgentsOptionsPanel adaptee;

  ListAgentsOptionsPanel_cmdMoveUp_actionAdapter(ListAgentsOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdMoveUp_actionPerformed(e);
  }
}

class ListAgentsOptionsPanel_cmdMoveDown_actionAdapter implements java.awt.event.ActionListener {
  ListAgentsOptionsPanel adaptee;

  ListAgentsOptionsPanel_cmdMoveDown_actionAdapter(ListAgentsOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdMoveDown_actionPerformed(e);
  }
}

class ListAgentsOptionsPanel_cmdConfigure_actionAdapter implements java.awt.event.ActionListener {
  ListAgentsOptionsPanel adaptee;

  ListAgentsOptionsPanel_cmdConfigure_actionAdapter(ListAgentsOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.cmdConfigure_actionPerformed(e);
  }
}

class ListAgentsOptionsPanel_listAgents_listSelectionAdapter implements javax.swing.event.ListSelectionListener {
  ListAgentsOptionsPanel adaptee;

  ListAgentsOptionsPanel_listAgents_listSelectionAdapter(ListAgentsOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void valueChanged(ListSelectionEvent e) {
    adaptee.listAgents_valueChanged(e);
  }
}

class ListAgentsOptionsPanel_listAgents_mouseAdapter extends java.awt.event.MouseAdapter {
  ListAgentsOptionsPanel adaptee;

  ListAgentsOptionsPanel_listAgents_mouseAdapter(ListAgentsOptionsPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void mouseClicked(MouseEvent e) {
    adaptee.listAgents_mouseClicked(e);
  }
}


class ListAgentsListRenderer extends JCheckBox implements ListCellRenderer {

   public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus)
   {
     AgentListEntryL entry = (AgentListEntryL)value;
     if (entry.agent == null) {
       setText(entry.agentClassName+" - "+
         GlobalState.getRes().getString("ListAgentsOptionsPanel.NotFound"));
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
//
// <LI>0.1 [28/04/98]: Initial Revision
// <LI>0.2 [08/05/98]: Now extends JPanel, internationalised
// New History:
//
// $Log: not supported by cvs2svn $
// Revision 1.7  2001/02/11 02:51:00  briand
// Repackaged from org.javalobby to org.dubh
//
// Revision 1.6  1999/11/09 22:34:41  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.5  1999/06/01 00:36:38  briand
// Change to use DJU ResourceManager, UserPreferences, DubhOkCancelDialog, Debug.
//