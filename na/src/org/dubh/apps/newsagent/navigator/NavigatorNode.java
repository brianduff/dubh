// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: NavigatorNode.java,v 1.4 2001-02-11 02:51:00 briand Exp $
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

import javax.swing.Icon;

/**
 * All nodes in the navigator implement this interface.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorNode.java,v 1.4 2001-02-11 02:51:00 briand Exp $
 */
public interface NavigatorNode
{
   /**
    * Get a list of classes implementing the Command
    * interface that can be invoked on this object.
    */
   Class[] getCommandList();

   /**
    * Get the displayed string of this node
    */
   String getDisplayedNodeName();

   /**
    * Get the displayed icon of this node
    */
   Icon getDisplayedNodeIcon();
}

//
// $Log: not supported by cvs2svn $
// Revision 1.3  2000/06/14 21:36:45  briand
// OK, a bit suspicious; cvs diff is finding files that I don't think I've
// modified. But I'm gonna checkin anyway, and keep a backup.
//
// Revision 1.2  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.1  1999/10/24 00:46:45  briand
// Initial revision.
//
//