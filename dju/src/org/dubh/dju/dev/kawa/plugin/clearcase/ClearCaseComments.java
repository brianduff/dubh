// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ClearCaseComments.java,v 1.1 1999-04-10 20:38:53 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
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
package dubh.utils.dev.kawa.plugin.clearcase;

import com.tektools.kawa.plugin.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Dialog for entering comments for checkin / checkout to Kawa.
 * @author Brian Duff, bduff@uk.oracle.com
 * @version $Id: ClearCaseComments.java,v 1.1 1999-04-10 20:38:53 briand Exp $
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
//