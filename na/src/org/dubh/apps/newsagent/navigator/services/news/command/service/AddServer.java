// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: AddServer.java,v 1.1 1999-10-24 00:48:19 briand Exp $
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
package dubh.apps.newsagent.navigator.services.news.command.service;

import dubh.apps.newsagent.navigator.services.news.NewsServerServiceProvider;

import dubh.utils.command.AbstractCommand;
import dubh.utils.misc.ResourceManager;

/**
 *
 * This command adds a server to the news provider.
 * 
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: AddServer.java,v 1.1 1999-10-24 00:48:19 briand Exp $
 */
public class AddServer extends AbstractCommand 
{   
   private static final String NAME = "news.service.addServer";

   public String getName()
   {
      return NAME;
   }
   
   protected ResourceManager getResourceManager()
   {
      return NewsServerServiceProvider.getResourceManager();
   }

   public boolean canUndo()
   {
      return false;
   }
   
   public boolean canRedo()
   {
      return false;   
   }
   
   public void doCommand(Object target)
   {
      System.out.println("Do command "+getNiceName()+" on "+target);
   }
   
   public void undoCommand()
   {
   
   }
   
   public void redoCommand()
   {
   
   }
}

//
// $Log: not supported by cvs2svn $
//