// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: TreeTableExample0.java,v 1.4 2001-02-11 02:52:12 briand Exp $
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

package org.dubh.dju.ui.treetable;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A TreeTable example, showing a JTreeTable, operating on the local file
 * system.
 *
 * @version %I% %G%
 *
 * @author Philip Milne
 */

public class TreeTableExample0
{
    public static void main(String[] args) {
   new TreeTableExample0();
    }

    public TreeTableExample0() {
   JFrame frame = new JFrame("TreeTable");
   JTreeTable treeTable = new JTreeTable(new FileSystemModel());

   frame.addWindowListener(new WindowAdapter() {
       public void windowClosing(WindowEvent we) {
      System.exit(0);
       }
   });

   frame.getContentPane().add(new JScrollPane(treeTable));
   frame.pack();
   frame.show();
    }
}