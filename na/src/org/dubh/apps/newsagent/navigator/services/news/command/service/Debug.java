// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: Debug.java,v 1.2 2001-02-11 02:51:01 briand Exp $
//   Copyright (C) 1997 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1997 - 2001 Brian Duff
//
// This program is free software.
//
// You may redistribute it and/or modify it under the terms of the
// license as described in the LICENSE file included with this
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://www.dubh.org/license'
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

package org.dubh.apps.newsagent.navigator.services.news.command.service;

import java.io.IOException;

import org.dubh.apps.newsagent.GlobalState;

import org.dubh.apps.newsagent.navigator.services.news.NewsServerServiceProvider;
import org.dubh.apps.newsagent.navigator.NavigatorService;
import org.dubh.apps.newsagent.navigator.PropertiesService;
import org.dubh.apps.newsagent.navigator.services.news.ui.NewsServerEditor;

import org.dubh.dju.command.AbstractCommand;
import org.dubh.dju.diagnostic.Assert;
import org.dubh.dju.misc.ResourceManager;

/**
 *
 * This command dumps out some debug properties for a news service.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: Debug.java,v 1.2 2001-02-11 02:51:01 briand Exp $
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
// Revision 1.1  2000/06/14 21:37:59  briand
// nitial rev.
//
//