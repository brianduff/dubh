// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: AddServer.java,v 1.3 2000-06-14 21:36:46 briand Exp $
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
package org.javalobby.apps.newsagent.navigator.services.news.command.service;

import java.io.IOException;

import org.javalobby.apps.newsagent.GlobalState;

import org.javalobby.apps.newsagent.navigator.services.news.NewsServerServiceProvider;
import org.javalobby.apps.newsagent.navigator.NavigatorService;
import org.javalobby.apps.newsagent.navigator.services.news.ui.NewsServerEditor;

import org.javalobby.dju.command.AbstractCommand;
import org.javalobby.dju.diagnostic.Assert;
import org.javalobby.dju.misc.ResourceManager;

/**
 *
 * This command adds a server to the news provider.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: AddServer.java,v 1.3 2000-06-14 21:36:46 briand Exp $
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
      // Object must be a navigator service.
      if (Assert.ENABLED)
      {
         Assert.that((target instanceof NavigatorService),
            "Target for AddServer is not a navigator service!!"
         );
      }

      NavigatorService parent = (NavigatorService)target;

      NewsServerEditor ed = new NewsServerEditor();

      if (ed.showDialog(GlobalState.getMainFrame(), "Create News Server"))
      {
         NewsServerServiceProvider nsp =
            new NewsServerServiceProvider();
         nsp.setParent(parent);
         nsp.setName(ed.getHost());

         nsp.setHost(ed.getHost());
         nsp.setNiceName(ed.getDisplayedName());
         nsp.setPort(ed.getPort());
         if (ed.getUsername() != null)
         {
            nsp.setUserName(ed.getUsername());
            nsp.setPassword(ed.getPassword());
         }

         try
         {
            nsp.save();
            // Add the child to the navigator
            parent.addProvider(nsp.getName(), nsp);
         }
         catch (IOException ioe)
         {
            System.err.println("TODO: GUI report IO exception");
            ioe.printStackTrace();
         }


      }
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
// Revision 1.2  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.1  1999/10/24 00:48:19  briand
// Initial Revision
//
//