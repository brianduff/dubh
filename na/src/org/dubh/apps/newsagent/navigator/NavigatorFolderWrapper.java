// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorFolderWrapper.java,v 1.1 1999-10-24 00:46:45 briand Exp $
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
import javax.mail.Folder;

/**
 * This class wraps up a JavaMail folder and implements
 * the NavigatorNode interface
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorFolderWrapper.java,v 1.1 1999-10-24 00:46:45 briand Exp $
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
//