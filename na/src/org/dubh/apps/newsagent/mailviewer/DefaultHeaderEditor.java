// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: DefaultHeaderEditor.java,v 1.3 2001-02-11 02:51:00 briand Exp $
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

package org.dubh.apps.newsagent.mailviewer;

import java.awt.Component;

import javax.swing.JTextField;

/**
 * The default header editor is used where no other editor
 * is registered.
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: DefaultHeaderEditor.java,v 1.3 2001-02-11 02:51:00 briand Exp $
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
// Revision 1.2  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.1  1999/10/17 17:02:48  briand
// Initial revision.
//
//