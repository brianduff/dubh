/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package dubh.utils.help;

import dubh.utils.ui.*;
import javax.swing.*;
import java.net.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import dubh.utils.html.HTMLParseException;
import dubh.utils.misc.Debug;
/**
 * An HTML-Based help system. To use this class, you need to have a set of
 * html help files in the CLASSPATH somewhere. You can use local or www
 * hyperlinks within these files.<P>
 * You can also specify a contents file. This creates a tree control at the
 * left of the window containing hierarchical topics. To make this as compatible
 * as possible with HTML, the contents file is itself an HTML file. The correct
 * format is as follows:
 * Only &lt;ul&gt;,  &lt;lu&gt; and &lt;a&gt; tags are important. LI
 * tags can have a class parameter that specifies the kind of node this is.
 * Valid classes are: dhFolder (for folder items), dhTopic (for topic items),
 * dhNewFolder (for new folder items), dhNewTopic (for new topic items) and
 * dhWebLink (for www links). The class doesn't affect the functionality of the
 * node, it just specifies the icon to use. 
 * You just use hyperlinks for nodes that go somewhere. <P>
 * Example: <PRE>
 *   <html>
 *   <body>
 *   <ul>
 *      <li class="dhFolder"><a href="about/index.html">About The Program</a></li>
 *      <ul>
 *         <li class="dhNewTopic"><a href="about/whatsnew.html">What's New?</a></li>
 *         ...
 *      </ul>
 *      ...
 *   </ul>
 * </PRE><P>
 * Use the HelpFrame(String, URL) constructor to set the URL where the contents page comes
 * from. To retrieve a file from the CLASSPATH, use <BR><PRE>
 *   ClassLoader.getSystemResource(url);
 * </PRE>
 * Where url is a url relative to the CLASSPATH. This returns a URL that you
 * can pass to setContents(). <P>
 * You can also use the setURL() method from HtmlBrowserFrame to force the
 * Help system to go to a particular file. In the current version, no attempt
 * is made to synchronise the tree control and HTML document when links are
 * followed.
 * <B>Revision History:</B><UL>
 * <LI>0.1 [03/07/98]: Initial Revision
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.1 [03/07/98]
 */
public class HelpFrame extends HtmlBrowserFrame {

  protected JSplitPane split;
  protected HelpNavPanel nav;

  /**
   * The default constructor doesn't have a document or tree control.
   */
  public HelpFrame() {
     super();   
  }

  /**
   * Construct a HelpFrame with a title and no tree control.
   */
  public HelpFrame(String title) {
     super(title);
  }

  /**
   * Construct a HelpFrame with a title and the specified URL as the tree
   * control.
   */
  public HelpFrame(String title, URL contents) {
     super(title);
     /*
      * Now remove the main panel and add a splitpane instead.
      */
     try {
        nav = new HelpNavPanel();
        nav.setContentsURL(contents);
        nav.addActionListener(new NavContentsListener());
        getContentPane().remove(mainPanel);
        split = new JSplitPane();
        split.setLeftComponent(nav);
        split.setRightComponent(mainPanel);
        getContentPane().add(split, BorderLayout.CENTER);
        mainPanel.setMinimumSize(new Dimension(50,50));
        mainPanel.setPreferredSize(new Dimension(50,50));
     } catch (IOException ioe) {
        Debug.println("Pending: IOException in HelpFrame");
     } catch (HTMLParseException pe) {
        Debug.println("Pending: HTML Parse Exception in HelpFrame");
     }
  }

  /**
   * Set the icons to be used for folder items
   */
  public void setFolderIcons(Icon folder, Icon openFolder) {
     nav.m_icoFolder = folder;
     nav.m_icoOpenFolder = openFolder;
  }

  /**
   * Set the icons to be used for new folder items
   */
  public void setNewFolderIcons(Icon newFolder, Icon openNewFolder) {
     nav.m_icoNewFolder = newFolder;
     nav.m_icoOpenNewFolder = openNewFolder;
  }

  /**
   * Set the icons to be used for web items
   */
  public void setWebIcons(Icon web, Icon newWeb) {
     nav.m_icoWeb = web;
     nav.m_icoNewWeb = newWeb;
  }

  /**
   * Set the icons to be used for topic items
   */
  public void setTopicIcons(Icon topic, Icon newTopic) {
     nav.m_icoTopic = topic;
     nav.m_icoNewTopic = newTopic;
  }


  class NavContentsListener implements ActionListener {
     public void actionPerformed(ActionEvent e) {
        // e.getSource() is a HelpContentsNode
        String url = ((HelpContentsNode)e.getSource()).getURL();
        if (url != null) 
           setURL(ClassLoader.getSystemResource(url));
     }
  }


}