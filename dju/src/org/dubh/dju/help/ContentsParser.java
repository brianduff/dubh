// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: ContentsParser.java,v 1.3 1999-03-22 23:37:17 briand Exp $
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
package dubh.utils.help;

import java.net.URL;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;
import dubh.utils.html.*;
import dubh.utils.misc.*;

/**
 * Parses a help contents file for use with the Dubh Help system. The contents
 * file is an HTML file. Only &lt;ul&gt; and &lt;lu&gt; tags are important. LI
 * tags can have a class parameter that specifies the kind of node this is.
 * Valid classes are: dhFolder (for folder items), dhTopic (for topic items),
 * dhNewFolder (for new folder items), dhNewTopic (for new topic items).
 * You just use hyperlinks for nodes that go somewhere. <P>
 * Example: <PRE>
 *   <html>
 *   <body>
 *   <ul>
 *      <li class="dhFolder"><a href="about/index.html">About The Program</a></li>
 *      <ul>
 *         <li class="dhNewTopic"><a href="about/whatsnew.html">What's New?</a></li>
 *         ...
 *      </ul>
 *      ...
 *   </ul>
 * </PRE><P>
 * Version History: <UL>
 * <LI>0.1 [02/07/98]: Initial Revision
 *</UL>
 @author Brian Duff
 @version 0.1 [02/07/98]
 */
class ContentsParser {
  private String m_contents="";

  public ContentsParser(String text) {
     setText(text);
  }

  public ContentsParser(URL url) throws IOException {
     setURL(url);
  }

  public void setURL(URL url) throws IOException {
     LineNumberReader lnr;
     StringWriter     sw = new StringWriter();
     PrintWriter      pw = new PrintWriter(sw);

     lnr = new LineNumberReader(new InputStreamReader(url.openStream()));
     String input = "";
     while (input != null) {
        input = lnr.readLine();
        if (input != null) pw.println(input);
     }
     lnr.close();
     pw.close();
     m_contents = sw.toString();

  }

  public void setText(String text) {
     m_contents = text;
  }

  /**
   * Retrieves the tree model for this HTML Contents file.
   @throws dubh.utils.html.HTMLParseException if the HTML in the contents file is dodgy.
   */
  public TreeModel getTree() throws HTMLParseException {
     HTMLDoc doc = HTMLDoc.parse(m_contents);
     DefaultMutableTreeNode root = new DefaultMutableTreeNode("Contents");
     Vector docels = doc.getParts();
     int el = 0;
     addNodesToParent(root, docels, 0);
     TreeModel m = new DefaultTreeModel(root);
     return m;
  }

  /***************************************************************************
   * IMPLEMENTATION                                                          *
   ***************************************************************************/

  private int addNodesToParent(DefaultMutableTreeNode parent, Vector elements, int start)
     throws HTMLParseException {
     if (start >= elements.size()) return elements.size();
     int curel = start;
     DefaultMutableTreeNode lastAdded=parent;

     while (curel < elements.size()) {
        if (elements.elementAt(curel) instanceof HTMLTag) {
           HTMLTag theTag = (HTMLTag)elements.elementAt(curel);

           Object  objTag;
           if (theTag.getCommand().equalsIgnoreCase("li") && !theTag.isClosingTag()) {

              try {
                 String cls = theTag.getAttribute("class");

                 String href=null;
                 String title="";
                 if (cls == null) cls = "";
                 // next element should be a hyperlink tag.
                 objTag = "";
                 while (!(objTag instanceof HTMLTag)) {
                    objTag = elements.elementAt(++curel);
                 }

                 if (objTag instanceof HTMLTag) {

                    if (((HTMLTag)objTag).getCommand().equalsIgnoreCase("a") && !((HTMLTag)objTag).isClosingTag()) {
                       href = ((HTMLTag)objTag).getAttribute("href");

                    }
                    objTag = elements.elementAt(++curel);
                 }
                 // Should be a string title.
                 if (objTag instanceof String) {
                    title = (String)objTag;

                 }
                 HelpContentsNode node = new HelpContentsNode(title, cls, href);
                 lastAdded = new DefaultMutableTreeNode(node);
                 parent.add(lastAdded);

              } catch (ArrayIndexOutOfBoundsException e) {

                 return curel;
              }
           } else if (theTag.getCommand().equalsIgnoreCase("ul")) {

              if (theTag.isClosingTag()) {

                 return curel;        // tail recursion
              } else {
                 // recursion

                 curel = addNodesToParent(lastAdded, elements, curel+1);
              }
           }
        }
        curel++;
     }
     return elements.size();
  }



  public void printTree(TreeModel m) {
     printNode((DefaultMutableTreeNode)m.getRoot(), 0);
  }

  public void printNode(DefaultMutableTreeNode n, int indent) {
     for (int i=0; i<indent; i++) Debug.print("-");
     Debug.println(n.toString());
     if (n.getChildCount() > 0) {
        Enumeration kids = n.children();
        while(kids.hasMoreElements()) {
           printNode((DefaultMutableTreeNode)kids.nextElement(), indent+1);
        }
     }
  }

  public static void main(String[] args) {
     try {
        ContentsParser test = new ContentsParser(ClassLoader.getSystemResource("index.html"));
        test.printTree(test.getTree());
     } catch (HTMLParseException e) {
        Debug.println("Parse Exception");
     } catch (IOException ex) {
        Debug.println("IO Exception");
     }
  }

}