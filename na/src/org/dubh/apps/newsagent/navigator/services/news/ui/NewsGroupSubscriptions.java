// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: NewsGroupSubscriptions.java,v 1.2 2001-02-11 02:51:01 briand Exp $
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


package org.dubh.apps.newsagent.navigator.services.news.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import java.text.Collator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.dubh.dju.ui.DubhOkCancelDialog;
import org.dubh.dju.ui.VerticalFlowPanel;

import org.dubh.apps.newsagent.navigator.services.news.NewsServerServiceProvider;

/**
 * This is the UI for news group subscriptions, freshly rewritten for
 * NewsAgent 2. I'm really hoping Java is a lot faster.
 */
public class NewsGroupSubscriptions
{
   private JPanel m_mainPanel;

   private JTable m_groups;
   private JScrollPane m_groupScroll;
   private TableModel m_allModel;
   private JPanel m_allPanel;
   private TableModel m_subscribedModel;
   private JPanel m_subscribePanel;
   private TableModel m_newModel;
   private JPanel m_newPanel;

   private JTabbedPane m_tabPane;

   private JLabel m_searchLabel;
   private JTextField m_searchField;
   private JButton m_searchButton;

   private JButton m_subscribeButton;
   private JButton m_unsubscribeButton;
   private JButton m_resetListButton;

   private List m_sortedGroupsAll;
   private List m_sortedGroupsSubscribed;

   private NewsServerServiceProvider m_server;

   public NewsGroupSubscriptions()
   {
      createComponents();
      layoutComponents();

   }

   private void createComponents()
   {
      m_mainPanel = new JPanel();

      m_allModel = new AllGroupsTableModel();
      m_groups = new JTable(m_allModel);
      m_groupScroll = new JScrollPane(m_groups);

      m_allPanel = new JPanel();
      m_subscribePanel = new JPanel();
      m_newPanel = new JPanel();

      m_tabPane = new JTabbedPane(JTabbedPane.BOTTOM);
      m_tabPane.addTab("All", m_allPanel);
      m_tabPane.addTab("Subscribed", m_subscribePanel);
      m_tabPane.addTab("New", m_newPanel);

      m_searchLabel = new JLabel();
      m_searchField = new JTextField();
      m_searchButton = new JButton();

      m_subscribeButton = new JButton();
      m_unsubscribeButton = new JButton();
      m_resetListButton = new JButton();
   }

   private void layoutComponents()
   {
      JPanel searchPanel = new JPanel();

      searchPanel.setLayout(new BorderLayout());
      searchPanel.add(m_searchLabel, BorderLayout.NORTH);
      searchPanel.add(m_searchField, BorderLayout.SOUTH);

      VerticalFlowPanel listButtonPanel = new VerticalFlowPanel();
      listButtonPanel.addRow(m_subscribeButton);
      listButtonPanel.addRow(m_unsubscribeButton);
      listButtonPanel.addRow(m_resetListButton);
      listButtonPanel.addSpacerPadding();

      m_allPanel.setLayout(new BorderLayout());
      m_allPanel.add(m_groupScroll, BorderLayout.CENTER);

      JPanel centralPanel = new JPanel();
      centralPanel.setLayout(new BorderLayout());
      centralPanel.add(m_tabPane, BorderLayout.CENTER);
      centralPanel.add(listButtonPanel, BorderLayout.EAST);

      m_mainPanel.setLayout(new BorderLayout());
      m_mainPanel.add(searchPanel, BorderLayout.NORTH);
      m_mainPanel.add(centralPanel, BorderLayout.CENTER);

   }

   /**
    * Set the server that the subscriptions panel is displaying groups for.
    */
   public void setNewsServer(NewsServerServiceProvider nssp)
      throws MessagingException
   {
      m_server = nssp;
      // Need to display long operation UI probably.

      Folder[] allFolders = nssp.getAllFolders();
      Arrays.sort(allFolders, new NewsgroupAlphabeticalComparator());

      m_sortedGroupsAll = Arrays.asList(allFolders);

      m_sortedGroupsSubscribed = new ArrayList();

      for (int i=0; i < m_sortedGroupsAll.size(); i++)
      {
         Folder f = (Folder)m_sortedGroupsAll.get(i);
         if (f.isSubscribed())
         {
            m_sortedGroupsSubscribed.add(f);
         }
      }
   }

   /**
    * Get the server that the subscriptions panel is for
    */
   public NewsServerServiceProvider getNewsServer()
   {
      return m_server;
   }

   /**
    * Get the UI component for the subscriptions control.
    */
   public Component getComponent()
   {
      return m_mainPanel;
   }

   /**
    * Show the dialog for the specified news server.
    *
    * @param parent The parent component for modality.
    * @param nssp The news server
    */
   public boolean showDialog(Component parent, NewsServerServiceProvider nssp)
      throws MessagingException
   {
      setNewsServer(nssp);

      DubhOkCancelDialog d = new DubhOkCancelDialog(parent, true);
      d.setPanel(m_mainPanel);
      d.pack();
      d.setVisible(true);


      return true;
   }



   /**
    * Used to sort newsgroup names alphabetically.
    */
   class NewsgroupAlphabeticalComparator implements Comparator
   {
      Collator m_collator = Collator.getInstance();

      public int compare(Object o1, Object o2)
      {
         return m_collator.compare(
            ((Folder)o1).getName(),
            ((Folder)o2).getName()
         );
      }

      public boolean equals(Object o)
      {
         return (o instanceof NewsgroupAlphabeticalComparator);
      }

   }



   class AllGroupsTableModel extends AbstractTableModel
   {
      public int getRowCount()
      {
         if (m_sortedGroupsAll == null)
         {
            return 0;
         }
         return m_sortedGroupsAll.size();
      }

      public int getColumnCount()
      {
         return 3;
      }

      public Object getValueAt(int row, int column)
      {
         Folder group = (Folder)m_sortedGroupsAll.get(row);

         switch (column)
         {
            case 0:
               return group.isSubscribed() ? Boolean.TRUE : Boolean.FALSE;
            case 1:
               return group.getName();
            case 2:
               return "";  // Description, change later.
         }
         return null;
      }
   }

}



// $Log: not supported by cvs2svn $
// Revision 1.1  2000/06/14 21:40:08  briand
// Initial Revision.
//