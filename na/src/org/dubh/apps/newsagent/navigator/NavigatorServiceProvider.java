// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorServiceProvider.java,v 1.3 1999-11-09 22:34:42 briand Exp $
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
import javax.mail.Store;

import javax.swing.Icon;

import org.javalobby.dju.misc.UserPreferences;

/**
 * A navigator service provider contains a (possibly nested)
 * set of folders which in turn contain objects of some kind.
 * The service provider provides information about which 
 * folders to display. 
 * 
 * Folders are JavaMail Folder objects.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorServiceProvider.java,v 1.3 1999-11-09 22:34:42 briand Exp $
 */
public interface NavigatorServiceProvider extends NavigatorNode
{ 
   public UserPreferences getPreferences();
  
   /**
    * Get the root folder, which is assumed to contain all other
    * folders on the service. The root folder is usually not displayed.
    */
   public Folder getRootFolder() throws MessagingException; 

   /**
    * Set the internal name of the service. This will be used by
    * the NavigatorService to set your service instance's name
    * when the service provider is instantiated.
    */
   public void setName(String name);

   /**
    * The NavigatorService will register itself with all it's child
    * providers after instantiation.
    */
   public void setService(NavigatorService s);

   public NavigatorService getService();
   
   /**
    * Get the (internal) name of the service.
    */
   public String getName();

   /**
    * Get the nice name of the service as it will be displayed to the user.
    * This can be the same as the internal name if you like.
    */
   public String getNiceName();
   
}

//
// $Log: not supported by cvs2svn $
// Revision 1.2  1999/10/24 00:44:38  briand
// Interface changes.
//
// Revision 1.1  1999/10/17 17:03:58  briand
// Initial revision.
//
//