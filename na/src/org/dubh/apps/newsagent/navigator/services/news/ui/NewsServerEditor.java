// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NewsServerEditor.java,v 1.1 2000-06-14 21:40:08 briand Exp $
//   Copyright (C) 1997-2000  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://wired.st-and.ac.uk/~briand/newsagent/
// ---------------------------------------------------------------------------
// Copyright (c) 2000 by the Java Lobby
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
package org.javalobby.apps.newsagent.navigator.services.news.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.javalobby.dju.misc.ResourceManager;
import org.javalobby.dju.ui.DubhOkCancelDialog;
import org.javalobby.dju.ui.IconicPreferencePanel;
import org.javalobby.dju.ui.VerticalFlowPanel;

import org.javalobby.apps.newsagent.navigator.services.news.NewsServerServiceProvider;

/**
 * This is a UI control that can be used to change the properties of a
 * NewsServerServiceProvider object. From a user's point of view, it is the
 * UI for creating or modifying properties of a news server.
 *
 * @author Brian Duff
 * @version $Id: NewsServerEditor.java,v 1.1 2000-06-14 21:40:08 briand Exp $
 */
public class NewsServerEditor
{
   /**
    * The location of internationalized resources for this UI
    */
   private final static String RES =
      "org.javalobby.apps.newsagent.navigator.services.news.ui.res.NewsServerEditor";


   private JPanel m_mainPanel;
   private IconicPreferencePanel m_server;
   private IconicPreferencePanel m_authentication;

   private JLabel m_labDisplayedName;
   private JTextField m_tfDisplayedName;

   private JLabel m_labHost;
   private JTextField m_tfHost;

   private JLabel m_labPort;
   private JTextField m_tfPort;
   private JButton m_butDefaultPort;

   private JCheckBox m_cbRequiresLogin;

   private JLabel m_labUsername;
   private JTextField m_tfUsername;

   private JLabel m_labPassword;
   private JPasswordField m_tfPassword;

   public NewsServerEditor()
   {
      createComponents();
      layoutComponents();
      addListeners();
      ResourceManager.getManagerFor(RES).initComponents(m_mainPanel);

      // Bit of dodginess to make the port button the right size
      m_butDefaultPort.setPreferredSize(new Dimension(
            m_butDefaultPort.getPreferredSize().width,
            m_tfPort.getPreferredSize().height
      ));
   }

   /**
    * Get the component for this editor. This can be used to embed the
    * editor in some other UI.
    */
   public Component getComponent()
   {
      return m_mainPanel;
   }

   /**
    * Instantiate and set the names of all the components in the UI
    */
   private void createComponents()
   {
      m_mainPanel = new JPanel();
      m_mainPanel.setName("MainPanel");

      m_server = new IconicPreferencePanel();
      m_server.setName("Server");
      m_authentication = new IconicPreferencePanel();
      m_authentication.setName("Auth");

      m_labDisplayedName = new JLabel();
      m_labDisplayedName.setName("DisplayedName");
      m_tfDisplayedName = new JTextField();
      m_labDisplayedName.setLabelFor(m_tfDisplayedName);

      m_labHost = new JLabel();
      m_labHost.setName("Host");
      m_tfHost = new JTextField();
      m_labHost.setLabelFor(m_tfHost);

      m_labPort = new JLabel();
      m_labPort.setName("Port");
      m_tfPort = new JTextField();
      m_tfPort.setText("119");
      m_labPort.setLabelFor(m_tfPort);
      m_butDefaultPort = new JButton();
      m_butDefaultPort.setName("DefaultPort");

      m_cbRequiresLogin = new JCheckBox();
      m_cbRequiresLogin.setName("RequiresLogin");

      m_labUsername = new JLabel();
      m_labUsername.setName("Username");
      m_tfUsername = new JTextField();
      m_labUsername.setLabelFor(m_tfUsername);

      m_labPassword = new JLabel();
      m_labPassword.setName("Password");
      m_tfPassword = new JPasswordField();
      m_labPassword.setLabelFor(m_tfPassword);

      setAuthEnabled(false);
   }

   /**
    * Layout all the components in the UI
    */
   private void layoutComponents()
   {
      VerticalFlowPanel serverPanel = m_server.getContainer();
      serverPanel.addRow(m_labDisplayedName);
      serverPanel.addRow(m_tfDisplayedName);
      serverPanel.addRow(m_labHost);
      serverPanel.addRow(m_tfHost);
      serverPanel.addRow(m_labPort);
      JPanel portPanel = new JPanel();
      portPanel.setName("PortPanel");
      portPanel.setLayout(new BorderLayout());
      portPanel.add(m_tfPort, BorderLayout.CENTER);
      portPanel.add(m_butDefaultPort, BorderLayout.EAST);

      serverPanel.addRow(portPanel);

      VerticalFlowPanel authPanel = m_authentication.getContainer();
      authPanel.addRow(m_cbRequiresLogin);
      authPanel.addRow(m_labUsername);
      authPanel.addRow(m_tfUsername);
      authPanel.addRow(m_labPassword);
      authPanel.addRow(m_tfPassword);

      m_mainPanel.setLayout(new BoxLayout(m_mainPanel, BoxLayout.Y_AXIS));
      m_mainPanel.add(m_server);
      m_mainPanel.add(m_authentication);
      m_mainPanel.add(Box.createGlue());

   }

   /**
    * Enable or disable the authenticate controls
    */
   private void setAuthEnabled(boolean b)
   {
      m_labUsername.setEnabled(b);
      m_tfUsername.setEnabled(b);
      m_labPassword.setEnabled(b);
      m_tfPassword.setEnabled(b);
   }

   /**
    * Add listeners to the components.
    */
   private void addListeners()
   {
      m_butDefaultPort.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e)
         {
            m_tfPort.setText("119");
         }
      });

      m_cbRequiresLogin.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e)
         {
            setAuthEnabled(m_cbRequiresLogin.isSelected());
         }
      });
   }



   /**
    * Validate the contents. If validation fails, throws an exception. The
    * message of the exception describes what needs to be changed.
    */
   public void validateContents() throws Exception
   {
      // TODO: NLS these errors

      // Check the nice name
      if (isEmptyString(m_tfDisplayedName.getText()))
      {
         throw new Exception(
            "Please specify a display name for this server."
         );
      }

      // Check the host name
      if (isEmptyString(m_tfHost.getText()))
      {
         throw new Exception(
            "Please specify a host name or IP address to connect to."
         );
      }

      // Check the port number
      try
      {
         int port = Integer.parseInt(m_tfPort.getText());

         // BD: Check that 9999 is actually a real upper limit
         if (port < 0 || port > 9999)
         {
            throw new Exception(
               "Please enter a valid port number. The port number must be between 0 and 9999"
            );
         }
      }
      catch (NumberFormatException nfe)
      {
         throw new Exception(
            "The port number must be numerical."
         );
      }

      // If the authenticate checkbox is checked,
      if (m_cbRequiresLogin.isSelected())
      {
         // The user name must not be empty
         if (isEmptyString(m_tfUsername.getText()))
         {
            throw new Exception(
               "Please specify a username to connect with"
            );
         }

         if (isEmptyString(m_tfPassword.getText()))
         {
            throw new Exception(
               "Please specify a password to connect with"
            );
         }
      }
   }

   private boolean isEmptyString(String s)
   {
      return (s == null || s.trim().length() == 0);
   }

   public void setDisplayedName(String s)
   {
      m_tfDisplayedName.setText(s);
   }

   public String getDisplayedName()
   {
      return m_tfDisplayedName.getText();
   }

   public void setHost(String s)
   {
      m_tfHost.setText(s);
   }

   public String getHost()
   {
      return m_tfHost.getText();
   }

   public void setPort(int port)
   {
      m_tfPort.setText(""+port);
   }

   public int getPort()
   {
      return Integer.parseInt(m_tfPort.getText());
   }

   public void setUsername(String username)
   {
      m_tfUsername.setText(username);
      boolean isAuthServer =
         (username != null && username.trim().length() != 0);
      setAuthEnabled(isAuthServer);
      m_cbRequiresLogin.setSelected(isAuthServer);
   }

   public String getUsername()
   {
      return m_tfUsername.getText();
   }

   public void setPassword(String pw)
   {
      m_tfPassword.setText(pw);
   }

   public String getPassword()
   {
      return m_tfPassword.getText();
   }


   /**
    * You can use this to display the news server editor in a modal dialog.
    */
   public boolean showDialog(Component parent, String title)
   {

      NewsServerEditorDialog d = new NewsServerEditorDialog(parent, title);

      d.setPanel(m_mainPanel);
      d.pack();
      d.setVisible(true);

      boolean isCancelled = d.isCancelled();
      d.dispose();

      d = null;

      return (!isCancelled);
   }


   class NewsServerEditorDialog extends DubhOkCancelDialog
   {

      public NewsServerEditorDialog(Component parent, String title)
      {
         super(parent, title, true);
      }

      public boolean okClicked()
      {
         try
         {
            validateContents();

            return true;
         }
         catch (Exception e)
         {
            // TODO: Use error UI
            System.err.println(e.getMessage());
            return false;
         }
      }
   }
}