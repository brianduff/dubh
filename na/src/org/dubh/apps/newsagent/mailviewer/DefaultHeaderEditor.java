// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: DefaultHeaderEditor.java,v 1.2 1999-11-09 22:34:42 briand Exp $
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
package org.javalobby.apps.newsagent.mailviewer;

import java.awt.Component;

import javax.swing.JTextField;

/**
 * The default header editor is used where no other editor
 * is registered.
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: DefaultHeaderEditor.java,v 1.2 1999-11-09 22:34:42 briand Exp $
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
// Revision 1.1  1999/10/17 17:02:48  briand
// Initial revision.
//
//