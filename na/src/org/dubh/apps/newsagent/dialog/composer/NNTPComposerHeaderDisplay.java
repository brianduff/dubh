// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NNTPComposerHeaderDisplay.java,v 1.6 1999-11-09 22:34:41 briand Exp $
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
package org.javalobby.apps.newsagent.dialog.composer;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import org.javalobby.apps.newsagent.PreferenceKeys;
import org.javalobby.apps.newsagent.GlobalState;
import org.javalobby.apps.newsagent.dialog.ErrorReporter;
import org.javalobby.dju.misc.*;
import java.beans.*;
/**
 * A subclass of ComposerDisplay that has a popup menu containing optional
 * headers that can be added or removed from the header display.<BR>
 *
 * @author Brian Duff
 * @version $Id: NNTPComposerHeaderDisplay.java,v 1.6 1999-11-09 22:34:41 briand Exp $
 */
class NNTPComposerHeaderDisplay extends ComposerHeaderDisplay {


  private String[] optHeaders = {
     "From",
     "Reply-To",
     "Followup-To",
     "Expires",
     "Distribution",
     "Organization",
     "Cc",
     "Bcc",
     "Fcc"
  };
  private static final int
     HD_FROM           = 0,
     HD_REPLYTO        = 1,
     HD_FOLLOWUPTO     = 2,
     HD_EXPIRES        = 3,
     HD_DISTRIBUTION   = 4,
     HD_ORGANIZATION   = 5,
     HD_CC             = 6,
     HD_BCC            = 7,
     HD_FCC            = 8,
     HD_COUNT          = 9;

  private Vector    otherHeaders;
  private Hashtable headerMenuItems;

  private JPopupMenu popupHeaders;

  private Frame parentFrame;

  private PropertyChangeListener m_proplistener;

  public NNTPComposerHeaderDisplay(Frame parent) {
     try {
        otherHeaders = new Vector();
        popupHeaders = createPopup();
        initHeaders();
        addMouseListener(new OptPopupListener());
        Debug.println("Adding property listener for NNTPComposerHeaderDisplay");
        m_proplistener = new PropListener();
        GlobalState.getPreferences().addPropertyChangeListener(m_proplistener);
     } catch (ExceptionInInitializerError e) {
        Debug.println("Exception while initialising NNTPComposerHeaderDisplay");
        e.getException().printStackTrace();
     }
     parentFrame = parent;
  }



  /**
   * Set up the NNTP Headers that are installed by default, and add additional
   * headers as specified in the user preferences.
   */
  private void initHeaders() {
     String[] words;

     addButtonHeader(
        "Newsgroups", "",
        GlobalState.getRes().getImage("Composer.newsgroup"),
        new NewsgroupsListener()
     );
     addHeader("Subject", "");

     /*
      * Add all predefined optional headers
      */
     for (int i=0; i<optHeaders.length; i++) {
        addOptionalHeader(i);
        setHeaderVisible(optHeaders[i], false);
        if (i == HD_FROM)
           setHeaderVisible("Sender", false);
     }


     /*
      * Check preferences to see which optional headers to include
      */
     words = StringUtils.getWords(GlobalState.getPreferences().getPreference(PreferenceKeys.COMPOSER_ADDITIONALHEADERS, ""));
     //words = StringUtils.getWords("Distribution additional1 additional2");
     for (int i=0; i<words.length; i++) {
        JCheckBoxMenuItem mi;
        mi =  (JCheckBoxMenuItem) headerMenuItems.get(words[i]);
        if (mi != null) {
           mi.doClick();
        } else {
           /* Unknown headers are "additional" optional headers that the user
            * has installed manually. Add these to the popup menu before the
            * separator, set their state to checked, and add headers for them
            */
            if (!words[i].equals("Subject") && !words[i].equals("Newsgroups"))
              addAdditionalHeader(words[i]);
        }
     }
  }

  private void addAdditionalHeader(String headername) {
     JCheckBoxMenuItem mi;
     otherHeaders.addElement(headername);
     addHeader(headername, "");
     setHeaderVisible(headername, false);
     // add after the separator
     mi = createPopupItem(otherHeaders.size()+HD_COUNT);
     popupHeaders.insert(mi, otherHeaders.size()+HD_COUNT);
     mi.doClick();
  }

  /**
   * Get the name of the popup menu item at a given position. Returns the
   * correct name regardless of whether the item is a default additional
   * header or has been installed by the user
   @param num Menu item number
   @return the name of the item
   */
  private String getHeaderAt(int num) {
     if (num < HD_COUNT)
        return optHeaders[num];
     else
        return (String) otherHeaders.elementAt(num - HD_COUNT - 1);
  }

  /**
   * Create a checkbox popup menu item for the popup menu. Adds an entry in the
   * hashtable for the menu item and adds event handling for the item.
   */
  private JCheckBoxMenuItem createPopupItem(int num) {
     String menuName = getHeaderAt(num);
     JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(menuName);
     menuItem.addActionListener(new PopupActionListener(num));
     headerMenuItems.put(menuName, menuItem);
     return menuItem;
  }

  /**
   * Create the popup menu that is used to add and remove headers from the
   * headers list.
   */
  private JPopupMenu createPopup() {
     headerMenuItems = new Hashtable();
     JMenu menu = new JMenu();
     for (int i=0; i<optHeaders.length; i++) {
        menu.add(createPopupItem(i));
     }

     menu.add(new JSeparator());

     JMenuItem menuItem = new JMenuItem("Other...");
     menuItem.addActionListener(new OtherHeaderActionListener());
     menu.add(menuItem);

     return menu.getPopupMenu();
  }

  /**
   * Removes one of the optional headers in the optional headers popup menu
   */
  private void removeOptionalHeader(int number) {
     if (number == HD_FROM)
        removeHeaderFrom();
     else
        removeHeader(getHeaderAt(number));
  }

  /**
   * Adds one of the optional headers specified in the optional headers popup
   * menu
   */
  private void addOptionalHeader(int number) {
     switch (number) {
        case HD_EXPIRES:
           addHeaderExpires();
           break;
        case HD_FCC:
           addHeaderFcc();
           break;
        case HD_FOLLOWUPTO:
           addHeaderFollowupTo();
           break;
        case HD_FROM:
           addHeaderFrom();
           break;
        case HD_ORGANIZATION:
           addHeaderOrganization();
           break;
        default:
           addHeader(getHeaderAt(number), "");
           break;
     }
     // Repack the dialogue to update changes.
     //validate();
  }


  /**
   * Popups the headers popup menu
   */
  private void showOptionalHeaderPopup(MouseEvent e) {
     popupHeaders.show(this, e.getX(), e.getY());
  }

  /**
   * Add the Followup-To header
   */
  private void addHeaderFollowupTo() {
     // Followup-To has a button that allows the user to select newsgroups
     // button nyi
     addButtonHeader(
        "Followup-To", "",
        GlobalState.getRes().getImage("Composer.newsgroup"),
        new FollowupToListener()
     );
  }

  /**
   * Add the From header
   */
  private void addHeaderFrom() {
     // Adding the From field means also adding the (immutable) Sender field.
     addHeader("Sender", "");
     setFieldEnabled("Sender", false);
     setHeaderValue(
        "Sender",
        GlobalState.getPreferences().getPreference(PreferenceKeys.IDENTITY_EMAIL) +
           " (" + GlobalState.getPreferences().getPreference(PreferenceKeys.IDENTITY_REALNAME) + ")"
     );
   //  setHeaderValue("Sender", "Fix this!!");
     addHeader("From", "");
     setHeaderValue("From", getHeaderValue("Sender"));
     /*
      * NOTE: Will need to add some kind of event mechanism to preferences
      * (been meaning to get round to this anyway), so that the Sender:
      * address updates if the user changes their preferences while the
      * message composer is on screen. Could use a
      *    PreferenceChangeListener
      *    PreferenceEvent
      * should be part of Dubh Utils...
      */
  }

  /**
   * Remove the From header
   */
  private void removeHeaderFrom() {
     removeHeader("From");
     removeHeader("Sender");
  }

  /**
   * Add the Expires header
   */
  private void addHeaderExpires() {
     // Expires has a button that lets the user choose a date
     // nyi
     addHeader("Expires", "");
  }

  /**
   * Add the Organization header
   */
  private void addHeaderOrganization() {
     // Organization should be initialised to the user property
     addHeader(
        "Organization",
        GlobalState.getPreferences().getPreference(PreferenceKeys.IDENTITY_ORGANISATION)
     );
  }

  /**
   * Add the Fcc header
   */
  private void addHeaderFcc() {
     // Fcc has a button letting the user choose a filename to Fcc to.
     // nyi
     addButtonHeader(
        "Fcc","",
        GlobalState.getRes().getImage("Composer.folder"),
        new FccListener()
     );
  }

  /**
   * Determine if a menu item in the optional headers popup menu is checked
   */
  private boolean isPopupItemChecked(String popupName) {
     JCheckBoxMenuItem cb = (JCheckBoxMenuItem) headerMenuItems.get(popupName);
     if (cb == null)
        return false;
     return cb.getState();
  }

  public static final void main(String[] args) {
   //  GlobalState.appInit();
     JFrame wibbleFrame = new JFrame();
     NNTPComposerHeaderDisplay hd = new NNTPComposerHeaderDisplay(wibbleFrame);



     wibbleFrame.getContentPane().setLayout(new BorderLayout());
     wibbleFrame.getContentPane().add(hd, BorderLayout.CENTER);

     wibbleFrame.pack();
     wibbleFrame.setVisible(true);
  }

  /**
   * Unregisters this header display by removing its property listener. You
   * should always do this if you are disposing of a dialogue that contains
   * this display. This method also stores user preferences indicating which
   * header fields are visible in the dialogue.
   */
  public void unregister() {
     UserPreferences prf = GlobalState.getPreferences();
     prf.removePropertyChangeListener(m_proplistener);
     prf.setPreference(PreferenceKeys.COMPOSER_ADDITIONALHEADERS, StringUtils.createSentence(getHeaderList()));
     try {
        prf.save();
     } catch (java.io.IOException e) {
        Debug.println("IO Exception trying to save NNTP Headers preferences.");
     }
  }

  /**
   * Finalizer for garbage collection. Removes the property change listener
   * from user preferences.
   */
  protected void finalize() {
     unregister();
  }

  /**
   * Listens out for changes to the organisation, from and real name user
   * preferences and changes the corresponding header fields.
   */
  class PropListener implements PropertyChangeListener {
     public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop.equals("") || prop.equals(PreferenceKeys.IDENTITY_ORGANISATION)) {
           setHeaderValue("Organization", (String) e.getNewValue());
           repaint();
        }
        if (prop.equals("") || prop.equals(PreferenceKeys.IDENTITY_EMAIL) || prop.equals(PreferenceKeys.IDENTITY_REALNAME)) {
           String rn = GlobalState.getPreferences().getPreference(PreferenceKeys.IDENTITY_REALNAME);
           String em = GlobalState.getPreferences().getPreference(PreferenceKeys.IDENTITY_EMAIL);
           setHeaderValue("Sender", em + "(" + rn + ")");
           setHeaderValue("From", em + "(" + rn + ")");
           repaint();
        }
     }
  }


  /**
   * Event class for the other... item on the optional headers popup menu
   */
  class OtherHeaderActionListener implements ActionListener {

     private boolean isHeaderOk(String headerName) {
        if (StringUtils.getWordCount(headerName) > 1)
           return false;
        for (int i=0; i<headerName.length();i++) {
           if (headerName.charAt(i) == ':')
              return false;
        }
        return true;
     }

     public void actionPerformed(ActionEvent e) {
        String newHeader = "";
        boolean ok = false;
        while (!ok) {
           newHeader = ErrorReporter.getInput("MessageComposer.OptionalHeader");
           if (newHeader == null) return; // user cancelled
           ok = isHeaderOk(newHeader);
           if (!ok) ErrorReporter.error("MessageComposer.BadHeader");
        }
        addAdditionalHeader(newHeader);
        
     }
  }

  /**
   * Event class for each individual menu item on the optional headers popup
   * menu
   */
  class PopupActionListener implements ActionListener {
     int itemNumber;

     PopupActionListener(int item) {
        itemNumber = item;
     }

     public void actionPerformed(ActionEvent e) {
        if (isPopupItemChecked(getHeaderAt(itemNumber)))
           if (itemNumber == HD_FROM) {
              setHeaderVisible("From", true);
              setHeaderVisible("Sender", true);
           } else
              setHeaderVisible(getHeaderAt(itemNumber), true);

        else {
           if (itemNumber == HD_FROM) {
              setHeaderVisible("From", false);
              setHeaderVisible("Sender", false);
           } else
              setHeaderVisible(getHeaderAt(itemNumber), false);
        }
        if (parentFrame != null) parentFrame.pack();
        else Debug.println("NNTPComposerHeaderDisplay: parent frame is null: can't pack");
        //else System.err.println("Can't layout parent frame: it is null");
     }
  }

  /**
   * Event class that actually brings up the insert header popup menu. Should
   * be attached to the headers panel.
   */
  class OptPopupListener implements java.awt.event.MouseListener {

     public void mouseClicked(MouseEvent e) {
        if (e.isPopupTrigger()) showOptionalHeaderPopup(e);
     }

     public void mouseEntered(MouseEvent e) {}

     public void mouseExited(MouseEvent e) {}

     public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) showOptionalHeaderPopup(e);
     }

     public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) showOptionalHeaderPopup(e);
     }
 }

  class NewsgroupsListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        Debug.println("Not yet implemented");
     }
  }

  class FccListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        Debug.println("Not yet implemented");
     }
  }

  class FollowupToListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        Debug.println("Not yet implemented");
     }
  }

  
}

//
// Old History:
//
// <LI>0.1 [12/06/98]: Initial Revision
// <LI>0.2 [13/06/98]: Added property event checking for identity preferences
//
// New History:
//
// $Log: not supported by cvs2svn $
// Revision 1.5  1999/06/01 00:31:37  briand
// Change to use DJU ResourceManager, UserPreferences, DubhOkCancelDialog, Debug.
//