// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NavigatorServiceProvider.java,v 1.1 1999-10-17 17:03:58 briand Exp $
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

import javax.swing.Icon;

/**
 * A navigator service provider contains a (possibly nested)
 * set of folders which in turn contain objects of some kind.
 * The service provider provides information about which 
 * folders to display. 
 * 
 * Folders are JavaMail Folder objects.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: NavigatorServiceProvider.java,v 1.1 1999-10-17 17:03:58 briand Exp $
 */
public interface NavigatorServiceProvider  
{   
   /**
    * Get the root folder, which is assumed to contain all other
    * folders on the service. The root folder is usually not displayed.
    */
   public Folder getRootFolder(); 

   /**
    * Get the (internal) name of the service.
    */
   public String getServiceName();

   /**
    * Get the nice name of the service as it will be displayed to the user.
    * This can be the same as the internal name if you like.
    */
   public String getServiceNiceName();
}

//
// $Log: not supported by cvs2svn $
//