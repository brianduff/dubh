// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ClearCaseComments.java,v 1.2 1999-11-11 21:24:34 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
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
package org.javalobby.dju.dev.kawa.plugin.clearcase;

import com.tektools.kawa.plugin.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Dialog for entering comments for checkin / checkout to Kawa.
 * @author Brian Duff, dubh@btinternet.com
 * @version $Id: ClearCaseComments.java,v 1.2 1999-11-11 21:24:34 briand Exp $
 */
class ClearCaseComments extends Dialog
{
   private Label m_label;
   private TextArea m_text;
   private Button m_button;
   
   private static ClearCaseComments m_singleton = null;
   
   /**
    * Construct a dialog. Don't to this directly; use the
    * {@link #doDialog(String)} static method to use a shared
    * instance of the dialog.
    */
   private ClearCaseComments()
   {
      super(new Frame(), "Dubh ClearCase Kawa Plugin", true);
      m_label = new Label("Comments");
      m_text = new TextArea();
      m_button = new Button("OK");
      
      add(m_label, BorderLayout.NORTH);
      add(m_text, BorderLayout.CENTER);
      add(m_button, BorderLayout.SOUTH);
      
      m_button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e)
         {
            setVisible(false);
         }
      });
      
      
   }
   
   /**
    * Override set visible to make the dialog center itself when
    * shown.
    */
   public void setVisible(boolean b)
   {
      if (b)
      {
         Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
         Dimension me     = getSize();
         
         setLocation(screen.width / 2 - me.width /2, screen.height / 2 - me.height /2);
      }
      super.setVisible(b);
   }
   
   /**
    * Use this method to display the comments dialog. 
    * @param comments initial comments to display
    * @return comments entered by user.
    */
   public static String doDialog(String comments)
   {
      if (m_singleton == null)
      {
         m_singleton = new ClearCaseComments();
      }
      
      m_singleton.m_text.setText(comments);
      m_singleton.pack();
      m_singleton.setVisible(true);
      
      return m_singleton.m_text.getText();
   }


}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/04/10 20:38:53  briand
// Plugin for Kawa for ClearCase revision control system.
//
//