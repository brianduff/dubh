// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: PreferencePage.java,v 1.2 1999-06-01 00:17:35 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://www.btinternet.com/~dubh/dju
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
package dubh.utils.ui.preferences;
import javax.swing.*;
import java.awt.*;
import dubh.utils.misc.UserPreferences;
import dubh.utils.misc.ResourceManager;
/**
 * A page in a preferences dialog. You should subclass this to 
 * provide your own pages, implementing the save() and revert()
 * methods. 
 * These methods should just write values and read from the 
 * UserPreferences object, not actually save it.
 * @author Brian Duff
 * @version $Id: PreferencePage.java,v 1.2 1999-06-01 00:17:35 briand Exp $
 */
public abstract class PreferencePage extends JPanel 
{
   private JPanel m_titlePanel;
   private JLabel m_titleLabel;
   private JLabel m_hintLabel;
   
   private String m_title;
   private String m_hintText;
   private Container m_content;

   public PreferencePage(String title, String hintText, Container content)
   {
      m_title = title;
      m_hintText = hintText;
      m_content = content;
      BorderLayout b = new BorderLayout();
      b.setHgap(2);
      b.setVgap(2);
      setLayout(b);
      m_titlePanel = new JPanel();
      m_titleLabel = new JLabel(title);
      m_hintLabel = new JLabel(hintText);
      Font f = m_titleLabel.getFont();
      m_hintLabel.setFont(new Font(f.getName(), Font.PLAIN, f.getSize()));
      m_hintLabel.setForeground(Color.white);
      m_titleLabel.setFont(new Font(f.getName(), Font.BOLD, f.getSize()));
      m_titleLabel.setForeground(Color.white);
      m_titlePanel.setBackground(Color.gray);
      m_titlePanel.setLayout(new BorderLayout());
      m_titlePanel.add(m_titleLabel, BorderLayout.WEST);
      m_titlePanel.add(m_hintLabel, BorderLayout.EAST);
      add(m_titlePanel, BorderLayout.NORTH);
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
   
   public String getTitle()
   {
      return m_title;
   }
   
   public String getHintText()
   {
      return m_hintText;
   }
   
   public Container getContent()
   {
      return m_content;
   }
   
   protected void setContent(Container c)
   {
      remove(getContent());
      add(c, BorderLayout.CENTER);
   }
   
   public String toString()
   {
      return getTitle();
   }
   
   abstract public void save(UserPreferences p);
   
   abstract public void revert(UserPreferences p);

}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/03/22 23:32:14  briand
// A page in a dubh utils preferences dialog.
//
//