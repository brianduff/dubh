// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: IUpdateableClass.java,v 1.5 1999-11-09 22:34:40 briand Exp $
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

package org.javalobby.apps.newsagent;

/**
 * The interface implemented by classes in NewsAgent that can be updated as
 * part of a patch. Simply allows the class to return its major and minor
 * version numbers.
 * @author Brian Duff
 * @version $Id: IUpdateableClass.java,v 1.5 1999-11-09 22:34:40 briand Exp $
 */
public interface IUpdateableClass {

  public int getMajorClassVersion();
  public int getMinorClassVersion();

}

//
// New (CVS) Log
//
// $Log: not supported by cvs2svn $
// Revision 1.4  1999/06/01 00:27:57  briand
// Comment mangling.
//
//

//
// Old Log:
//
// 0.1 [02/07/98]: Initial Revision