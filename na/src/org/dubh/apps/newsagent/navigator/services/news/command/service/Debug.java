// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: Debug.java,v 1.1 2000-06-14 21:37:59 briand Exp $
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
import org.javalobby.apps.newsagent.navigator.PropertiesService;
import org.javalobby.apps.newsagent.navigator.services.news.ui.NewsServerEditor;

import org.javalobby.dju.command.AbstractCommand;
import org.javalobby.dju.diagnostic.Assert;
import org.javalobby.dju.misc.ResourceManager;

/**
 *
 * This command dumps out some debug properties for a news service.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: Debug.java,v 1.1 2000-06-14 21:37:59 briand Exp $
 */
public class Debug extends AbstractCommand
{
   private static final String NAME = "news.service.debug";

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
         Assert.that((target instanceof PropertiesService),
            "Target for Properties is not a PropertiesService: "+
               target
         );
      }

      PropertiesService server = (PropertiesService)target;

      System.out.println("Node is "+server);
      System.out.println("Node has "+server.getChildCount()+" children.");


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