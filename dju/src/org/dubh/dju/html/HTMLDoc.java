// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: HTMLDoc.java,v 1.3 1999-03-22 23:37:17 briand Exp $
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
package dubh.utils.html;

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