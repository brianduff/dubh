// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorRenderer.java,v 1.2 1999-11-09 22:34:42 briand Exp $
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
package org.javalobby.apps.newsagent.navigator;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.JTree;
import javax.swing.Icon;

import java.awt.Component;
import java.awt.Font;

import org.javalobby.dju.misc.Debug;
/**
 * Tree renderer for the navigator.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorRenderer.java,v 1.2 1999-11-09 22:34:42 briand Exp $
 */
class NavigatorRenderer extends DefaultTreeCellRenderer
{   
   public Component getTreeCellRendererComponent(
      JTree tree, Object value, boolean sel, boolean expanded, 
      boolean leaf, int row, boolean hasFocus)
   {
      Component c = super.getTreeCellRendererComponent(
         tree, value, sel, expanded, leaf, row, hasFocus
      );
      
      if (!(value instanceof NavigatorNode))
      {
         throw new IllegalArgumentException(
            "Node at row "+row+" is not a navigator node: "+value
         );
      }

      NavigatorNode n = (NavigatorNode)value;
      
      setText(n.getDisplayedNodeName());
      Icon i = n.getDisplayedNodeIcon();
      if (i != null)
         setIcon(i);

      return c;
   }  
   
}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/10/24 00:46:45  briand
// Initial revision.
//
//