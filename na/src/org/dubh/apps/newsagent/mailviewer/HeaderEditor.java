// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: HeaderEditor.java,v 1.2 1999-11-09 22:34:42 briand Exp $
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

/**
 * You can implement this interface to provide a custom editor for 
 * a particular header. To use your custom editor, call
 * HeaderViewer.registerCustomHeaderEditor(String, HeaderEditor)
 */
public interface HeaderEditor
{
   /**
    * Your header editor will be set to read only if the message
    * viewer is being used to display a message rather than 
    * edit it.
    */
   public void setReadOnly(boolean b);

   /**
    * The header viewer will tell your editor what the current
    * value of the header is
    */
   public void setHeaderValue(String s);
   
   /**
    * In composition mode, the header viewer will ask your editor
    * what the current value of the header is.
    */
   public String getHeaderValue();

   /**
    * You should return the component used to display your header
    * from this method. It's layout within the header will be subject
    * to the layout mechanism of the headerviewer.
    */
   public Component getComponent();
}

//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/10/17 17:02:59  briand
// Initial revision.
//
//