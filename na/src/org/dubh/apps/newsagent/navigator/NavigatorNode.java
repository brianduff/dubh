// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorNode.java,v 1.2 1999-11-09 22:34:42 briand Exp $
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

import javax.swing.Icon;

/**
 * All nodes in the navigator implement this interface.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorNode.java,v 1.2 1999-11-09 22:34:42 briand Exp $
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
// Revision 1.1  1999/10/24 00:46:45  briand
// Initial revision.
//
//