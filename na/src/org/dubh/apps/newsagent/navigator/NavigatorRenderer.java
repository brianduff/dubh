// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorRenderer.java,v 1.1 1999-10-24 00:46:45 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
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
package dubh.apps.newsagent.navigator;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.JTree;
import javax.swing.Icon;

import java.awt.Component;
import java.awt.Font;

import dubh.utils.misc.Debug;
/**
 * Tree renderer for the navigator.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorRenderer.java,v 1.1 1999-10-24 00:46:45 briand Exp $
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
//