// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: PreferencePage.java,v 1.4 2000-06-14 21:25:22 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
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
package org.javalobby.dju.ui.preferences;
import javax.swing.*;
import java.awt.*;
import org.javalobby.dju.misc.UserPreferences;
import org.javalobby.dju.misc.ResourceManager;

import org.javalobby.dju.diagnostic.Assert;


/**
 * This provides a default implemenation of a preferences page that
 * can be used in concert with the PreferencesDialog class.
 *
 * Normally, you will subclass this object and override the
 * save() and revert() methods. These methods should just write values
 * and read from the UserPreferences object, not actually save it.
 *
 * @author Brian Duff
 * @version $Id: PreferencePage.java,v 1.4 2000-06-14 21:25:22 briand Exp $
 */
public class PreferencePage extends JPanel
{
   private JPanel m_titlePanel;
   private JLabel m_titleLabel;
   private JLabel m_hintLabel;

   private String m_title;
   private String m_hintText;
   private Container m_content;

   /**
    * Construct a preference page.
    *
    * @param title The title of the page. If null, don't display a title.
    * @param hintText The hint text for the page. If null, don't display
    *   hint text.
    * @param content The page contents. If null, display no contents now.
    *   you can use setContent later to change the page content.
    */
   public PreferencePage(String title, String hintText, Container content)
   {
      m_title = title;
      m_hintText = hintText;
      m_content = content;
      BorderLayout b = new BorderLayout();
      b.setHgap(2);
      b.setVgap(2);
      setLayout(b);

      Font f = (new JLabel()).getFont();
      if (title != null)
      {
         m_titleLabel = new JLabel(title);
         m_titleLabel.setFont(new Font(f.getName(), Font.BOLD, f.getSize()));
         m_titleLabel.setForeground(Color.white);
      }
      if (hintText != null)
      {
         m_hintLabel = new JLabel(hintText);
         m_hintLabel.setFont(new Font(f.getName(), Font.PLAIN, f.getSize()));
         m_hintLabel.setForeground(Color.white);
      }

      if (title != null || hintText != null)
      {
         m_titlePanel = new JPanel();
         m_titlePanel.setBackground(Color.gray);
         m_titlePanel.setLayout(new BorderLayout());
         m_titlePanel.add(m_titleLabel, BorderLayout.WEST);
         m_titlePanel.add(m_hintLabel, BorderLayout.EAST);
         add(m_titlePanel, BorderLayout.NORTH);
      }
      if (m_content == null) m_content = new JPanel();
      add(m_content,      BorderLayout.CENTER);
   }

   /**
    * Create a preference page with a title and hint text initialised from
    * a specified resourcemanager. The resource file should have two keys
    * for this page: pageId.title and
    * pageId.hintText. This method sets the content to
    * null. You should call {@link #setContent(Container)} to give this
    * page a content panel.
    */
   public PreferencePage(ResourceManager r, String pageId)
   {
      this(r.getString(pageId+".title"),
           r.getString(pageId+".hintText"),
           null);
   }

   /**
    * Construct a preference page with no initial content, no title and
    * no hint text.
    */
   public PreferencePage()
   {
      this(null, null, null);
   }

   /**
    * Get the title of the page. May be null if no title has been set.
    */
   public String getTitle()
   {
      return m_title;
   }

   /**
    * Get the hint text for the page. May be null if no title has been
    * set.
    */
   public String getHintText()
   {
      return m_hintText;
   }

   /**
    * Get the page content. Will never be null.
    */
   public Container getContent()
   {
      return m_content;
   }

   /**
    * Set the content of the page. This will replace any existing content.
    * Must not be null.
    */
   protected void setContent(Container c)
   {
      if (Assert.ENABLED)
      {
         Assert.that((c != null), "Can't set null content!");
      }
      remove(getContent());
      add(c, BorderLayout.CENTER);
   }

   /**
    * Just returns the page title, or super.toString() if it
    * hasn't been set.
    */
   public String toString()
   {
      if (getTitle() == null)
      {
         return super.toString();
      }
      return getTitle();
   }

   /**
    * You can subclass and implement this method to save your page's properties
    * to the specified UserPreferences object. This is invoked by
    * PreferencesDialog. The default implementation does nothing.
    */
   public void save(UserPreferences p) {}

   /**
    * You can subclass and implement this method to load your page's properties
    * from the specified UserPreferences object. This is invoked by
    * PreferencesDialog. The default implementation does nothing.
    */
   public void revert(UserPreferences p) {}

}

//
// $Log: not supported by cvs2svn $
// Revision 1.3  1999/11/11 21:24:36  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.2  1999/06/01 00:17:35  briand
// Assorted user interface utility code. Mostly for making layout easier.
//
// Revision 1.1  1999/03/22 23:32:14  briand
// A page in a dubh utils preferences dialog.
//
//