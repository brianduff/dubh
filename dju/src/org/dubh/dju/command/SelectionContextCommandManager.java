// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: SelectionContextCommandManager.java,v 1.1 1999-10-24 00:38:17 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
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
package dubh.utils.command;

import java.util.ArrayList;

import javax.swing.event.TreeSelectionListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.ListSelectionEvent;

import javax.swing.tree.TreePath;

import javax.swing.JList;

/**
 * This command manager listens for selection changes and
 * changes its context based on the current selection. 
 * The context is then passed into the command invokation.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: SelectionContextCommandManager.java,v 1.1 1999-10-24 00:38:17 briand Exp $
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
      TreePath[] tp = tse.getPaths();
      if (tp.length == 1)
      {
         if (tse.isAddedPath())
         {
            setContext(tse.getPath().getLastPathComponent());   
         }
      }
      else
      {
         ArrayList lst = new ArrayList();
         for (int i=0; i < tp.length; i++)
         {
            TreePath p = tp[i];
            if (tse.isAddedPath(p))
            {
               lst.add(p.getLastPathComponent());
            }
         }
         setContext(lst);
      }
   }
}

//
// $Log: not supported by cvs2svn $
//