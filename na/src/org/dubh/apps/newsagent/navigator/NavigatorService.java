// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorService.java,v 1.3 1999-11-09 22:34:42 briand Exp $
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

import java.util.ArrayList;

import javax.swing.Icon;

import javax.mail.Store;

import org.javalobby.dju.misc.UserPreferences;

/**
 * A NavigatorService is a top level branch in the navigator.
 * A service consists of one or more ServiceProviders which in turn 
 * contain folders.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorService.java,v 1.3 1999-11-09 22:34:42 briand Exp $
 */
public interface NavigatorService extends NavigatorNode
{  

   /**
    * Any NavigatorService can have a set of UserPreferences 
    * associated with it.
    */
   public UserPreferences getPreferences();
 
   /**
    * Return the protocol name of your service from this method. 
    * For instance, a service providing news would have the service
    * name "news" or "nntp".
    */
   public String getServiceProtocolName(); 

   /**
    * Return a nice (preferrably internationalized) name for your
    * service. This will be the name displayed in the navigator.
    */
   public String getServiceNiceName();
   
   /**
    * Return a list of objects which implement the ServiceProvider
    * interface. For the news example, these would be nntp servers.
    */
   public ArrayList getServiceProviders();
   
   /**
    * Return a list of command objects that are invokable on your
    * service. These will be used to populate the drop down list for
    * right clicks over the service in the navigator.
    */
   public Class[] getCommandList();
   
   /**
    * Get a list of commands that are invokable on service providers
    * contained in this service. It is assumed that the list of commands
    * is the same for all providers, but you can always disable commands
    * depending on the selected service.
    */
   public Class[] getProviderCommandList();
   
   /**
    * Get a list of commands that are invokable on folders contained
    * in service providers on this service.
    */
   public Class[] getFolderCommandList();
   
   /**
    * Get the icon to display for this service.
    */
   public Icon getIcon();
   
   /**
    * Get the icon to display for providers in this service.
    */
   public Icon getProviderIcon();
   
   /**
    * Get the icon to display for folders in this service.
    */
   public Icon getFolderIcon();
}

//
// $Log: not supported by cvs2svn $
// Revision 1.2  1999/10/24 00:43:55  briand
// Change to use new DJU command mechanism
//
// Revision 1.1  1999/10/17 17:03:58  briand
// Initial revision.
//
//