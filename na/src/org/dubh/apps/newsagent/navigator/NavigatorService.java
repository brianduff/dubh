// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorService.java,v 1.2 1999-10-24 00:43:55 briand Exp $
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

import java.util.ArrayList;

import javax.swing.Icon;

import javax.mail.Store;

import dubh.utils.misc.UserPreferences;

/**
 * A NavigatorService is a top level branch in the navigator.
 * A service consists of one or more ServiceProviders which in turn 
 * contain folders.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorService.java,v 1.2 1999-10-24 00:43:55 briand Exp $
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
// Revision 1.1  1999/10/17 17:03:58  briand
// Initial revision.
//
//