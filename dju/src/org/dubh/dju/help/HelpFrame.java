// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: HelpFrame.java,v 1.5 2001-02-11 02:52:11 briand Exp $
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

package org.dubh.dju.help;

import org.dubh.dju.ui.*;
import javax.swing.*;
import java.net.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import org.dubh.dju.html.HTMLParseException;
import org.dubh.dju.misc.Debug;
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