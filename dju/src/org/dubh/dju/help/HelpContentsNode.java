// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: HelpContentsNode.java,v 1.5 2001-02-11 02:52:11 briand Exp $
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


package org.dubh.dju.help;


/**
 * A node in the help contents tree.<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [03/07/98]: Initial Revision
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.1 [03/07/98]
 */
class HelpContentsNode {
  protected String m_title;
  protected String m_class;
  protected String m_url;

  /**
   * Construct a Help Contents node.
   @param title the title of the node
   @param cls the class of node (see ContentsParser)
   @param url the url that the node points to (relative to the classpath)
   */
  public HelpContentsNode(String title, String cls, String url) {
     setTitle(title);
     setCls(cls);
     setURL(url);
  }

  public String getTitle() { return m_title; }
  public void setTitle(String t) { m_title = t; }

  public String getCls() { return m_class; }
  public void setCls(String c) { m_class = c; }

  public String getURL() { return m_url; }
  public void setURL(String u) { m_url = u; }

  public String toString() { return getTitle(); }
}