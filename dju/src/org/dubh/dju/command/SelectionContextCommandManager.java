// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: SelectionContextCommandManager.java,v 1.4 2001-02-11 02:52:10 briand Exp $
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

package org.dubh.dju.command;

import java.util.ArrayList;

import javax.swing.event.TreeSelectionListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.ListSelectionEvent;

import javax.swing.tree.TreePath;

import javax.swing.JList;
import javax.swing.JTree;

/**
 * This command manager listens for selection changes and
 * changes its context based on the current selection.
 * The context is then passed into the command invokation.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: SelectionContextCommandManager.java,v 1.4 2001-02-11 02:52:10 briand Exp $
 */
public class SelectionContextCommandManager extends CommandManager
   implements TreeSelectionListener, ListSelectionListener
{
   protected Object m_context;

   protected void setContext(Object o)
   {
      m_context = o;
   }

   protected void actuallyDoCommand(Command c)
   {
      c.doCommand(m_context);
   }

   public void valueChanged(ListSelectionEvent lse)
   {
      if (!(lse.getSource() instanceof JList))
      {
         throw new IllegalStateException("Recieved ListSelectionEvent, but source wasn't JList.");
      }
      JList lst = (JList)lse.getSource();
      int fi = lse.getFirstIndex();
      int li = lse.getLastIndex();
      if (fi == li && lst.isSelectedIndex(fi))
      {
         setContext(lst.getModel().getElementAt(fi));
      }
      else
      {
         ArrayList alst = new ArrayList();
         // Set the context to an ArrayList of selected items
         for (int i = fi; i <= li; i++)
         {
            if (lst.isSelectedIndex(i))
            {
               alst.add(lst.getModel().getElementAt(i));
            }
         }
         setContext(alst);
      }
   }

   public void valueChanged(TreeSelectionEvent tse)
   {
      JTree tree = (JTree)tse.getSource();

      TreePath[] paths = tree.getSelectionPaths();

      if (paths == null)
      {
         return;
      }
      if (paths.length == 1)
      {
         setContext(paths[0].getLastPathComponent());
      }
      else if (paths.length > 1)
      {
         ArrayList selectionlist = new ArrayList(paths.length);
         for (int i=0; i < paths.length; i++)
         {
            selectionlist.add(paths[i].getLastPathComponent());
         }
         setContext(selectionlist);
      }
   }
}

//
// $Log: not supported by cvs2svn $
// Revision 1.3  2000/06/14 21:25:21  briand
// Mega checkin of stuff I've been working on (too many things to detail)
//
// Revision 1.2  1999/11/11 21:24:34  briand
// Change package and import to Javalobby JFA.
//
// Revision 1.1  1999/10/24 00:38:17  briand
// New Command mechanism.
//
//