// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorNode.java,v 1.1 1999-10-24 00:46:45 briand Exp $
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

import javax.swing.Icon;

/**
 * All nodes in the navigator implement this interface.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorNode.java,v 1.1 1999-10-24 00:46:45 briand Exp $
 */
public interface NavigatorNode  
{   
   /**
    * Get a list of classes implementing the Command
    * interface that can be invoked on this object.
    */
   public Class[] getCommandList();
   
   /**
    * Get the displayed string of this node
    */
   public String getDisplayedNodeName();
   
   /**
    * Get the displayed icon of this node
    */
   public Icon getDisplayedNodeIcon();
}

//
// $Log: not supported by cvs2svn $
//