// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: HTMLDoc.java,v 1.4 1999-11-11 21:24:34 briand Exp $
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
package org.javalobby.dju.html;

import java.util.*;
/**
 * Represents an HTML document, modeled as a collection of HTMLTag objects
 * and Strings.
 *<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [03/07/98]: Initial Revision
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.1 [03/07/98]
 */
public class HTMLDoc {
  protected Vector m_parts;

  public HTMLDoc() {
     m_parts = new Vector();
  }

  public void append(HTMLTag tag) {
     m_parts.addElement(tag);
  }

  public void append(String text) {
     m_parts.addElement(text);
  }

  /**
   * Return the contents of the document as a vector. The vector contains
   * tags and text entries.     (HTMLTag and String objects)
   */
  public Vector getParts() {
     return m_parts;
  }

  public void insertAt(HTMLTag tag, int location) {
     m_parts.insertElementAt(tag, location);
  }

  public void insertAt(String text, int location) {
     m_parts.insertElementAt(text, location);
  }

  public Object getPartAt(int location) {
     return m_parts.elementAt(location);
  }

  /**
   * Parse a text string and turn it into an HTMLDoc object.
   */
  public static HTMLDoc parse(String text) throws HTMLParseException {
     /* Split string up into chunks separated by "<>". Return the tokens so that
      * we know which are which.
      */
     HTMLDoc doc = new HTMLDoc();
     StringTokenizer tok = new StringTokenizer(text, "<>", true);
     while (tok.hasMoreTokens()) {
        String cur = (String) tok.nextToken();
        if (cur.charAt(0) == '<') {
           if (tok.hasMoreTokens()) {
              String tag = "<" + (String) tok.nextToken() + ">";
              doc.append(HTMLTag.parse(tag));
              if (tok.hasMoreTokens()) {
                 tag = tok.nextToken();
              }
           }
        }
        else doc.append(cur);
     }
     return doc;
  }

  public String toString() {
     StringBuffer result = new StringBuffer();
     Enumeration enum = m_parts.elements();
     while (enum.hasMoreElements()) {
        result.append(enum.nextElement().toString());   
     }
     return result.toString();
  }



  public static void main(String[] args) {
     try {
        HTMLDoc test  = HTMLDoc.parse("Welcome to my <a href=http://st-and.ac.uK>home</a>. \n<i>bye fore now!!!</i>");
        System.out.println(" "+test);
     } catch (HTMLParseException p) {
        System.out.println("Parse exception "+p);
     }
  }

}