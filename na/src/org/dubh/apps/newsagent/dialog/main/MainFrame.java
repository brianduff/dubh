// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: MainFrame.java,v 1.9 1999-12-12 03:31:51 briand Exp $
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
package org.javalobby.apps.newsagent.dialog.main;

import org.javalobby.dju.ui.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.text.MessageFormat;
import org.javalobby.apps.newsagent.PreferenceKeys;
import org.javalobby.apps.newsagent.NewsAgent;
import org.javalobby.apps.newsagent.GlobalState;
import org.javalobby.apps.newsagent.HelpSystem;
import org.javalobby.apps.newsagent.dialog.ErrorReporter;
import org.javalobby.apps.newsagent.dialog.FolderSelectorDialog;
import org.javalobby.apps.newsagent.dialog.NewsServerPropsDlg;
import org.javalobby.apps.newsagent.dialog.ServerSubscriptions;
import org.javalobby.apps.newsagent.dialog.composer.MessageComposer;
import org.javalobby.apps.newsagent.dialog.preferences.NewsAgentPreferences;
import org.javalobby.apps.newsagent.nntp.NNTPServer;
import org.javalobby.apps.newsagent.nntp.MessageBody;
import org.javalobby.apps.newsagent.nntp.MessageHeader;
import org.javalobby.apps.newsagent.nntp.Newsgroup;
import org.javalobby.apps.newsagent.Folder;
import org.javalobby.apps.newsagent.IUpdateableClass;

import org.javalobby.apps.newsagent.navigator.Navigator;
import org.javalobby.apps.newsagent.navigator.NavigatorServiceList;
import org.javalobby.apps.newsagent.navigator.PropertiesService;
import org.javalobby.apps.newsagent.navigator.PropertyFileResolver;

import org.javalobby.dju.misc.Debug;
import org.javalobby.dju.misc.ReadOnlyVersion;
import org.javalobby.dju.misc.VersionManager;
import org.javalobby.dju.misc.ResourceManager;

/**
 * The main application window <P>
 * @author Brian Duff
 * @version $Id: MainFrame.java,v 1.9 1999-12-12 03:31:51 briand Exp $
 */
public class MainFrame extends DubhFrame implements IUpdateableClass {

  public int getMajorClassVersion() { return 1; }
  public int getMinorClassVersion() { return 4; }

  /** The resource key for the main menu bar */
  private static final String MENU_BAR = "mainMenu";
  /** The bundle name for menu resources */
  private static final String MENUS_BUNDLE="org/javalobby/apps/newsagent/res/Menus";

  private static final String
        POPUP_FOLDER      =  "popupFolder",
        POPUP_FOLDERS     =  "popupFolders",
        POPUP_MESSAGE     =  "popupMessage",
        POPUP_NEWSGROUP   =  "popupNewsgroup",
        POPUP_SERVER      =  "popupServer",
        POPUP_SERVERS     =  "popupServers";
  private static final String TOOLBAR = "mainToolBar";

   class TestList implements NavigatorServiceList
   {
      public ArrayList getServices()
      {
         ArrayList l = new ArrayList();
         l.add(new PropertiesService("news",       
            PropertyFileResolver.getDefaultedProperties(
               "navigator"+File.separator+"services"+File.separator+"news", 
               "news.properties", 
               "org/javalobby/apps/newsagent/navigator/services/news",
               "news.properties"
             )
         ));
         
         return l;
      }   
   }


  private BorderLayout m_layoutMain = new BorderLayout();
  private ThreadTree   m_thread = new ThreadTree();
  private MsgDisplayPanel m_message = new MsgDisplayPanel();
  private JSplitPane m_horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
  private JSplitPane m_vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
  private Navigator m_folders = new Navigator(new TestList());
  private StatusBar m_status = new StatusBar();
  private JToolBar m_toolBar;
  private JMenuBarResource m_menuBar;

  // Publically accessible variables for popup menus
  /** Popup menu for the Servers tree item */
  public JPopupMenu popupServers;
  /** Popup menu for individual server items */
  public JPopupMenu popupServer;
  /** Popup menu for the folders tree item */
  public JPopupMenu popupFolders;
  /** Popup menu for individual folders */
  public JPopupMenu popupFolder;
  /** Popup menu for newsgroups */
  public JPopupMenu popupNewsgroup;
  /** Popup menu for messages */
  public JPopupMenu popupMessage;

  public MainFrame() {
     // Initialise the window
     super(GlobalState.appName);     
     setName("newsagentMain");

     m_vertical.setContinuousLayout(false);
     m_horizontal.setContinuousLayout(false);
     // Set up the SplitPanes
     m_vertical.setTopComponent(m_thread);
     m_vertical.setBottomComponent(m_message);
     m_horizontal.setLeftComponent(m_folders.getComponent());
     m_horizontal.setRightComponent(m_vertical);

     // Layout the main window's content pane
     getContentPane().setLayout(m_layoutMain);
     getContentPane().add(m_horizontal, BorderLayout.CENTER);
     getContentPane().add(m_status, BorderLayout.SOUTH);

     // Initialise tools
     initMenus();
     initToolBar();
     initPopups();

     // Clear the contents of the thread tree
     m_thread.clearList();

     // Add event bindings
     m_thread.addTreeSelectionListener(new ThreadTreeSelectionListener());
    // m_folders.addTreeSelectionListener(new FolderTreeSelectionListener());

     selInit();

  }

/**************************
 * Public Parts           *
 **************************/

  public void setStatus(String s) {
     m_status.setText(s);
  }

  public void setStatus() {
     m_status.setText("");
  }

  public StatusBar getStatusBar() {
     // TODO: Deprecate and use LongOperationEvents
     return m_status;
  }

  public void setThreadTreeVisible(boolean visible) {
     if (!visible) {
        m_thread.clearList();
     }
  }

/************************************
 * LEGACY CODE / DEPRECATED METHODS *
 ************************************/

  /**
   * Get the message display panel
   @deprecated since NewsAgent 1.02. Classes that use this method are due for
   * a rewrite.
   */
  public MsgDisplayPanel getMsgDisplayPanel() { return m_message; }

  /**
   * Get the thread tree
   @deprecated since NewsAgent 1.02. Classes that use this method are due for
   * a rewrite.
   */
  public ThreadTree getThreadTree() { return m_thread; }

  /**
   * Get the FoldersTree panel.
   @deprecated since NewsAgent 1.02. Classes that use this method are due for
   * a rewrite.
   */
  //public FolderTreePanel getFolderTreePanel() { return m_folders; }

  /**
   * Get an action
   @deprecated since NewsAgent 1.02. Classes that use this method are due for
   * a rewrite.
   */
  public Action getAction(String key) {
     return m_menuBar.getAction(key);
  }

  /**
   * Obsolete menu action setting method.
   @deprecated since NewsAgent 1.02. Classes that use this method are due for
   * a rewrite.
   */
  public void setActions(int actions) {
     Debug.println("MainFrame.setActions is deprecated.");
  }

  /**
   * Obsolete menu action setting method.
   @deprecated since NewsAgent 1.02. Classes that use this method are due for
   * a rewrite.
   */
  public void setNetworkActionsEnabled(boolean b) {
     Debug.println("MainFrame.setNetworkActionsEnabled is deprecated.");
  }




/**************************
 * Menu Items             *
 **************************/

  public void fileNewFolder() {
  /*   String folderName = ErrorReporter.getInput("EnterFolderName");
     if (folderName != null) {
        GlobalState.getStorageManager().createFolder(folderName);
        m_folders.selectItem(0);
        m_folders.treeUpdate();
     }
     */
  }

  public void fileDeleteFolder() {
  /*   Object cursel = m_folders.getSelectedItem();
     if (cursel instanceof Folder) {
        Folder theFolder = (Folder) cursel;
        if (theFolder != null) {
           GlobalState.getStorageManager().deleteFolder(theFolder);
           m_folders.selectItem(0);
           m_folders.treeUpdate();
        }
     } */
  }

  public void fileExportFolder() {
     Debug.println("Not yet implemented.");
  }

  public void fileExit() {
     Debug.println("fileExit: TODO: Tidy up, save cache etc.");
     Debug.closeWindow();
     NewsAgent.closeNetDebug();
     setVisible(false);
     System.exit(0);
  }

  public void editCopy() {
     m_message.copy();   
  }

  public void editSelectAll() {
     Debug.println("Not yet implemented.");   
  }

  public void editPreferences() {
     NewsAgentPreferences.showDialog();
  }

  public void serversAdd() {
     // TODO: Make NewsServerPropsDlg more "bean-like"
     NewsServerPropsDlg newserver = new NewsServerPropsDlg (
        this,
        GlobalState.getRes().getString("MainFrame.AddServer"),
        true
     );
     newserver.pack();
     newserver.showAtCentre();
  //   m_folders.treeUpdate();
     GlobalState.getStorageManager().serializeServers();
  }

  public void serversRemove() {
 /*    Object cursel = m_folders.getSelectedItem();
     if (cursel instanceof NNTPServer) {
        NNTPServer thisserver = (NNTPServer) cursel;
        // Check the user really wants to delete the server.
        if (ErrorReporter.yesNo("ReallyDeleteServer",
                 new String[] {thisserver.getNiceName()})) {
           try {
              GlobalState.getStorageManager().removeServer(thisserver.getHostName());
              GlobalState.getStorageManager().serializeServers();
              m_folders.selectItem(0);
              m_folders.treeUpdate();
           } catch (IOException ex) {
              Debug.println("serversRemove: Unable to disconnect from "+thisserver);
           } // try
        }  // if
     } */
  }

  public void serversProperties() {
 /*    Object selection = m_folders.getSelectedItem();
     if (selection instanceof NNTPServer) {
        NewsServerPropsDlg props = new NewsServerPropsDlg(
           this,
           MessageFormat.format(
              GlobalState.getRes().getString("MainFrame.ServerProperties"),
              new String[] {((NNTPServer)selection).getNiceName()}
           ),
           true
        );
        String oldname = ((NNTPServer)selection).getNiceName();
        props.setServerHost(((NNTPServer)selection).getHostName());
        props.setHostEnabled(false);
        props.revertToProps();
        props.showAtCentre();
        props.dispose();
        // if the "nice" name for the server has changed, update the tree.
        if (!oldname.equals(((NNTPServer)selection).getNiceName()))
           GlobalState.getMainFrame().getFolderTreePanel().treeUpdate();
        // Save the changes
        GlobalState.getStorageManager().serializeServers();
     } */
  }

  public void serversPrefs() {
     NewsAgentPreferences.showDialog();
  }

  public void serversSubscriptions() {
 /*    ServerSubscriptions subs = new ServerSubscriptions(
        GlobalState.getMainFrame(),
        GlobalState.getRes().getString("MainFrame.Subscriptions"),
        true
     );
     subs.showAtCentre();
     */
  }

  public void serversConnected() {
     Debug.println("serversConnected: This menu item is not yet implemented.");
  }

  public void serversGoOffline() {
     GlobalState.getStorageManager().disconnectFromAllServers();
     // Disable the GoOffline action.
     m_menuBar.setActionEnabled("serversGoOffline", false);
     // redraw the folder tree panel.
//     GlobalState.getMainFrame().getFolderTreePanel().repaint();

  }

  public void messagesPost() {
  /*   NNTPServer server = m_folders.getLastSelectedServer();
     Newsgroup  group  = m_folders.getLastSelectedNewsgroup();
     if (server == null || group == null) {
        Debug.println("messagesPost: No current server and newsgroup to post to.");
        return;
     }

     MessageComposer composer = new MessageComposer(server, group);
     composer.pack();
     composer.show();
     
     */
  }

  public void messagesReplyPost() {
 /*    String quotetype;
     NNTPServer server = m_folders.getLastSelectedServer();
     Newsgroup  group  = m_folders.getLastSelectedNewsgroup();
     MessageHeader head = m_thread.getSelectedHeader();
     MessageBody body = m_message.getMessageBody();

     // Check for a server and newsgroup
     if (server == null || group == null) {
        Debug.println("messagesReplyPost: No current server and newsgroup to post to.");
        return;
     }

     // Check for a message to reply to
     if (head == null || body == null) {
        Debug.println("messagesReplyPost: No current message to reply to.");
        return;
     }

     MessageComposer composer;


     // Determine the quoting behaviour
     quotetype = GlobalState.getPreferences().getPreference(PreferenceKeys.SEND_INCLUDEBEHAVIOUR, "all");
     if (quotetype.trim().equalsIgnoreCase("none")) {
        // No quote
        composer = new MessageComposer(server, group, head);

     } else if (quotetype.trim().equalsIgnoreCase("selected")) {
        // Quote current selection
        String selectedText = m_message.getSelectedText();
        if (selectedText == null) {
           Debug.println("messagesReplyPost: No current selection to quote");
           selectedText = "";
        }
        composer = new MessageComposer(server, group, head, selectedText);

     } else {
        // Quote entire message
        composer = new MessageComposer(server, group, head, body);
     }


     composer.pack();
     composer.show(); */

  }

  public void messagesReplyEmail() {
     Debug.println("Not yet implemented.");
  }

  public void messagesCopyToFolder() {
     FolderSelectorDialog sel = new FolderSelectorDialog (
        this,
        GlobalState.getRes().getString("MainFrame.CopyToFolder"),
        true
     );
     sel.setVisible(true);

     Folder theFolder = sel.getChosenFolder();
     if (theFolder != null) {
        theFolder.saveMessage(
           m_thread.getSelectedHeader(),
           m_message.getMessageBody()
        );
     } else {
        Debug.println("messagesCopyToFolder: Can't copy, as destination folder doesn't exist.");
     }
  }

  public void messagesExport() {
     Debug.println("Not yet implemented");
  }

  public void messagesUnsubscribe() {

  }

  public void helpContents() {
     try {
        GlobalState.getHelpSystem().jumpToTopic(HelpSystem.TOPIC_CONTENTS);
     } catch (Exception e) {
        Debug.println("helpContents: The help files couldn't be located.");
     }
  }

  public void helpUsing() {
     GlobalState.getHelpSystem().jumpToTopic(HelpSystem.TOPIC_USINGHELP);
  }

  public void helpNew() {
     GlobalState.getHelpSystem().jumpToTopic(HelpSystem.TOPIC_WHATSNEW);
  }

  public void helpStart() {
     GlobalState.getHelpSystem().jumpToTopic(HelpSystem.TOPIC_GETSTART);
  }

  public void helpDlg() {
     GlobalState.getHelpSystem().jumpToTopic(HelpSystem.TOPIC_DIALOGS);
  }

  public void helpMenus() {
     GlobalState.getHelpSystem().jumpToTopic(HelpSystem.TOPIC_MENUS);
  }

  public void helpAbout() {
     
     ResourceManager rm = ResourceManager.getManagerFor("Menus");
     Icon i = rm.getImage("imgAbout");
     
     ReadOnlyVersion verDubhUtils;
     ReadOnlyVersion verSwing;
     ReadOnlyVersion[] dependencies = new ReadOnlyVersion[2];

     try 
     {
       verDubhUtils = VersionManager.getInstance().getVersion("org.javalobby.dju");
     } 
     catch (IllegalArgumentException e)
     {
        verDubhUtils = new DummyVersion("Dubh Java Utilities");   
     }
     
     try 
     {
       verSwing = VersionManager.getInstance().getVersion("javax.swing");
     } 
     catch (IllegalArgumentException e)
     {
        verSwing = new DummyVersion("Swing");   
     }     
     
     
     dependencies[0] = verDubhUtils;
     dependencies[1] = verSwing;
     
     
     AboutPanel.doDialog(this, GlobalState.getVersion(), dependencies, i);

  }

/**************************
 * Selection Handling     *
 **************************/

  /**
   * Set up menus etc. at initialisation
   */
  protected void selInit() {
     selMenuGroupEnable(
        new String[] {
           "fileDeleteFolder", "fileExportFolder",
           "editCopy", "serversRemove", "serversProperties",
           "serversConnected", "serversGoOffline", "messagesRefresh",
           "messagesPost", "messagesReplyPost", "messagesReplyEmail",
           "messagesCopyToFolder", "messagesExport", "messagesUnsubscribe"
        },
        false
     );
  }

  /**
   * Enable / Disable controls when the Folders item is selected
   */
  protected void selFoldersSelected(boolean b) {

  }

  /**
   * Enable / Disable controls when an individual Folder is selected
   */
  protected void selFolderSelected(Folder f, boolean b) {
     selMenuGroupEnable(
        new String[] {
           "fileDeleteFolder", "fileExportFolder"
        },
        b
     );

  }

  protected void selServersSelected(boolean b) {
  }

  protected void selServerSelected(NNTPServer s, boolean b) {
     selMenuGroupEnable(
        new String[] {
           "serversRemove", "serversProperties", "serversConnected"
        },
        b
     );
  }

  protected void selNewsgroupSelected(Newsgroup g, boolean b) {
     selMenuGroupEnable(
        new String[] {
           "messagesRefresh", "messagesPost", "messagesUnsubscribe"
        },
        b
     );
  }

  protected void selMessageSelected(MessageHeader h, boolean b) {
     selMenuGroupEnable(
        new String[] {
           "messagesReplyPost", "messagesReplyEmail", "messagesCopyFolder",
           "messagesExport"
        },
        b
     );
  }

  protected void selMessageTextSelected(String selText, boolean b) {
     selMenuGroupEnable(
        new String[] {
           "editCopy"
        },
        b
     );
  }

  /**
   * Disable or enable a group of menu commands
   */
  protected void selMenuGroupEnable(String[] ids, boolean enabled) {
     for (int i=0; i<ids.length; i++) {
        m_menuBar.setActionEnabled(ids[i], enabled);
     }
  }


/**************************
 * Private Implementation *
 **************************/

  /**
   * Initialise the window's menu bar.
   */
  private void initMenus() {
     m_menuBar = new JMenuBarResource(MENUS_BUNDLE, MENU_BAR, this);
     setJMenuBar(m_menuBar);
  }

  /**
   * Initialise the window's toolbar
   */
  private void initToolBar() {
     m_toolBar = m_menuBar.getToolBar(TOOLBAR);
     getContentPane().add(m_toolBar, BorderLayout.NORTH);
  }

  /**
   * Initialise the window's popup menus.
   */
  private void initPopups() {
     popupFolder = m_menuBar.getPopupMenu(POPUP_FOLDER);
     popupFolders = m_menuBar.getPopupMenu(POPUP_FOLDERS);
     popupMessage = m_menuBar.getPopupMenu(POPUP_MESSAGE);
     popupNewsgroup = m_menuBar.getPopupMenu(POPUP_NEWSGROUP);
     popupServer = m_menuBar.getPopupMenu(POPUP_SERVER);
     popupServers = m_menuBar.getPopupMenu(POPUP_SERVERS);
  }

  public static void main(String[] args) {
     MainFrame f = new MainFrame();
     f.pack();
     f.setVisible(true);
  }

/**************************
 * Event Classes          *
 **************************/

 /**
  * Listener for selections in the FolderTreePanel
  */
  class FolderTreeSelectionListener implements TreeSelectionListener {
     Object m_previous = null;

     public void valueChanged(TreeSelectionEvent e) {
        Object obj =
           ((DefaultMutableTreeNode)e.getPath().getLastPathComponent()).getUserObject();
        boolean selected = e.isAddedPath();
        // Select the selection
        dispachSelection(obj, selected);
        // Deselect the old selection
        if (m_previous != null) dispachSelection(m_previous, false);
        // store the current selection
        m_previous = obj;
     }

     private void dispachSelection(Object obj, boolean selected) {
        // Determine the type of the selected object
        if (obj instanceof Folder)
           selFolderSelected((Folder)obj, selected);
        else if (obj instanceof NNTPServer)
           selServerSelected((NNTPServer)obj, selected);
        else if (obj instanceof Newsgroup)
           selNewsgroupSelected((Newsgroup)obj, selected);
        else {
           // either "Folders" or "Servers" : do nothing for now.
           Debug.println("Selected object is not a Folder, Newsgroup or NNTPServer");
        }
     }
  }

  /**
   * Listener for selections in the ThreadTreePanel
   */
  class ThreadTreeSelectionListener implements TreeSelectionListener {
     public void valueChanged(TreeSelectionEvent e) {
        Object obj = ((DefaultMutableTreeNode)e.getPath().getLastPathComponent()).getUserObject();
        boolean selected = e.isAddedPath();
        if (obj instanceof MessageHeader) 
        {
          selMessageSelected((MessageHeader)obj, selected);
        }
     }
  }
  
  
    class DummyVersion implements ReadOnlyVersion
    {
        private String m_name;

        public DummyVersion(String name)
        {
           m_name = name;
        }

        public int    getMajorVersion() { return 0; }   
        public int    getMinorVersion() { return 0; }   
        public int    getMicroVersion() { return 0; }   
        public int    getBuildNumber()  { return 0; }
        public String getBuildLabel()   { return ""; }  
        public String getProductName() { return m_name; }   
        public String getProductCopyright() { return ""; }   
        public Date   getReleaseDate() { return new Date(); }
        public String getVersionDescription(String format) { return m_name + " - unknown version"; }
        public String getShortDescription() { return "unknown version"; }
        public String getLongDescription() { return getVersionDescription(""); }    

   }

}

//
// Old Version History: 
 // <LI>0.1 [07/03/98]: Initial Revision
 // <LI>0.2 [23/03/98]: Revised to use FolderTreePanel
 // <LI>0.3 [24/03/98]: Added lots of Actions for PopupMenus on FolderTreePanel.
 // <LI>0.4 [25/03/98]: Added Subscriptions action.
 // <LI>0.5 [29/03/98]: Added ServerProperties action.
 // <LI>0.6 [31/03/98]: Added getThreadTree.
 // <LI>0.7 [01/04/98]: Added getMsgDisplayPanel. Set title to application name
 //   on startup.
 // <LI>0.8 [02/04/98]: Added ServerConnectedAction, GoOfflineAction,
 //   CopyToFolderAction. Added Message popup menu. Added storage of divider
 //   locations. Added status bar.
 // <LI>0.9 [04/04/98]: Added PostNewMessageAction. Changed GoOfflineAction to
 //   use the disconnectFromAllServers() StorageManager method. Updated so that
 //   connections to all servers are closed upon application termination
 // <LI>0.10 [04/04/98]: Added PostMessageAction.
 // <LI>0.11 [07/04/98]: Added ReplyPostAction. Modified terminate() to serialise
 //   the cache (through the StorageManager). Added getStatusBar
 // <LI>0.12 [18/04/98]: Added setActions.
 // <LI>0.13 [20/04/98]: Added support for empty thread tree. Fixed centering of
 //   NewsServerPropsDlg.
 // <LI>0.14 [21/04/98]: Added about dialogue.
 // <LI>0.15 [26/04/98]: Removed emptyPanel, which was causing Initializer
 //  Exceptions for some bizarre reason. (ok, I was wrong here, it was images
 //   in JAR files... The emptyPanel wasn't doing much anyway).
 // <LI>0.16 [08/05/98]: Centred and added title to Subscriptions dialogue.
 //   Added copy action.
 // <LI>0.17 [06/06/98]: Added dubh utils import for StringUtils
 // <LI>0.18 [08/06/98]: Added to the help menu slightly ;)
 // <LI>0.19 [10/06/98]: Changed posting & replying implementation slightly to
 //   use new preferences for quoting replies.
 // <LI>1.00 [30/06/98]: MAJOR REVISION: Rewrote entire class from scratch
 //   to use new Dubh JavaUtils menu constructor and DubhActions.
 // <LI>1.01 [02/07/98]: Added selection event handling. This should now be more
 //   consistent than the last version.
 // <LI>1.02 [04/10/98]: Major package changes; also renamed back to MainFrame.
 //   implemented a temporary file->exit.
 // <LI>1.03 [08/11/98]: Fixed a bug with menu enablement when a message is selected.
 // <LI>1.04 [23/11/98]: Changed about box variable feed to use new version information.
 
//
// New history:
//
// $Log: not supported by cvs2svn $
// Revision 1.8  1999/11/09 22:34:41  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.7  1999/10/24 00:42:45  briand
// Pull out FolderTreePanel and replace with the new Navigator.
//