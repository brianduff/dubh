// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: DefaultHeaderEditor.java,v 1.1 1999-10-17 17:02:48 briand Exp $
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
package dubh.apps.newsagent.mailviewer;

import java.awt.Component;

import javax.swing.JTextField;

/**
 * The default header editor is used where no other editor
 * is registered.
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: DefaultHeaderEditor.java,v 1.1 1999-10-17 17:02:48 briand Exp $
 */
class DefaultHeaderEditor implements HeaderEditor
{
   protected JTextField m_tfField;
   
   public DefaultHeaderEditor()
   {
      m_tfField = new JTextField();
   }
   
   /**
    * Your header editor will be set to read only if the message
    * viewer is being used to display a message rather than 
    * edit it.
    */
   public void setReadOnly(boolean b)
   {
      m_tfField.setEditable(!b);
   }

   /**
    * The header viewer will tell your editor what the current
    * value of the header is
    */
   public void setHeaderValue(String s)
   {
      m_tfField.setText(s);
   }
   
   /**
    * In composition mode, the header viewer will ask your editor
    * what the current value of the header is.
    */
   public String getHeaderValue()
   {
      return m_tfField.getText();
   }

   /**
    * You should return the component used to display your header
    * from this method. It's layout within the header will be subject
    * to the layout mechanism of the headerviewer.
    */
   public Component getComponent()
   {
      return m_tfField;
   }
}

//
// $Log: not supported by cvs2svn $
//