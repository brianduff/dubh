// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorFolderWrapper.java,v 1.2 1999-11-09 22:34:42 briand Exp $
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
import javax.mail.Folder;

/**
 * This class wraps up a JavaMail folder and implements
 * the NavigatorNode interface
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorFolderWrapper.java,v 1.2 1999-11-09 22:34:42 briand Exp $
 */
public class NavigatorFolderWrapper implements NavigatorNode
{   
   protected Folder m_folder;
   protected NavigatorServiceProvider m_provider;
   
   /**
    * Construct a folder wrapper
    * @param np The parent provider
    * @param f The folder to wrap
    */
   public NavigatorFolderWrapper(NavigatorServiceProvider np, Folder f)
   {
      m_folder = f;
      m_provider = np;
   }

   /**
    * Get the folder wrapped up in this node
    */
   public Folder getFolder()
   {
      return m_folder;
   }
   
   /**
    * Get the provider for this folder
    */
   public NavigatorServiceProvider getProvider()
   {
      return m_provider;
   }
   
   /**
    * Get the list of valid commands for folders
    */
   public Class[] getCommandList()
   {
      return getProvider().getService().getFolderCommandList();
   }
   
   /**
    * Get the name of the node as displayed in the navigator
    */
   public String getDisplayedNodeName()
   {
      return getFolder().getName();
   }
   
   /**
    * get the icon to display in the navigator
    */
   public Icon getDisplayedNodeIcon()
   {
      return getProvider().getService().getFolderIcon();
   }

}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/10/24 00:46:45  briand
// Initial revision.
//
//