// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: TreeTableExample0.java,v 1.2 1999-03-22 23:37:19 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://www.btinternet.com/~dubh/dju
// ---------------------------------------------------------------------------
//
//  Copyright 1997, 1998 by Sun Microsystems, Inc.,
//  901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
//  All rights reserved.
//
//  This software is the confidential and proprietary information
//  of Sun Microsystems, Inc. ("Confidential Information").  You
//  shall not disclose such Confidential Information and shall use
//  it only in accordance with the terms of the license agreement
//  you entered into with Sun.
//
// ---------------------------------------------------------------------------
//   Original Author: Philip Milne, Sun Microsystems
//   Contributors: Brian Duff
// ---------------------------------------------------------------------------
//   See bottom of file for revision history
package dubh.utils.ui.treetable;
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