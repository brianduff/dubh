// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: NavigatorRenderer.java,v 1.4 2001-02-11 02:51:00 briand Exp $
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

package org.dubh.apps.newsagent.navigator;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.JTree;
import javax.swing.Icon;

import java.awt.Component;
import java.awt.Font;

import org.dubh.dju.misc.Debug;
/**
 * Tree renderer for the navigator.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorRenderer.java,v 1.4 2001-02-11 02:51:00 briand Exp $
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
         setText(value.toString());
         if (Debug.TRACE_LEVEL_1)
         {
            Debug.println(1, this, "Node at row "+row+" is not a navigator node: "+value);
         }
      }
      else
      {

         NavigatorNode n = (NavigatorNode)value;

         setText(n.getDisplayedNodeName());
         Icon i = n.getDisplayedNodeIcon();
         if (i != null)
            setIcon(i);
      }

      return c;
   }

}

//
// $Log: not supported by cvs2svn $
// Revision 1.3  1999/12/12 03:31:51  briand
// More bugfixes necessary due to move to javalobby. Mostly changing path from
// dubh.apps.newsagent to org.dubh.apps.newsagent etc. and new locations of
// top level properties files.
//
// Revision 1.2  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.1  1999/10/24 00:46:45  briand
// Initial revision.
//
//