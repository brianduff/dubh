// From "Building Oracle XML Applications" by Steve Muench

package org.dubh.tool;

import java.io.*;
import java.net.URL;
import oracle.xml.parser.v2.*;
import org.w3c.dom.*;
import org.w3c.tidy.Tidy;

public class JTidyConverter {
   // Parse a URL returning for a possibly ill-formed HTML page and return
   // a "tidied" up XML document for the page using JTidy
   public XMLDocument XMLifyHTMLFrom(URL u) throws IOException {
      // (1) Construct a new Tidy bean to use for converting HTML to XML
      Tidy tidy = new Tidy();
      // (2) Set some Tidy options to get the best results for "data scraping"
      tidy.setMakeClean(true);
      tidy.setBreakBeforeBR(true);
      tidy.setShowWarnings(false);
      tidy.setOnlyErrors(true);
      tidy.setErrout(new PrintWriter(new StringWriter()));
      // (3) Construct an empty target Oracle XML DOM Document
      XMLDocument xmldocToReturn = new XMLDocument();
      // (4) Get an InputStream of HTML from the URL
      InputStream HTMLInput = u.openStream();
      // (5) Ask Tidy to parse the incoming HTML into an in-memory DOM tree
      Document tidiedHTMLDoc = tidy.parseDOM(u.openStream(), null);
      // (6) Clone the JTidy DOM tree by recursively building up an Oracle DOM copy
      cloneXMLFragment(tidiedHTMLDoc,xmldocToReturn);
      return xmldocToReturn;
   }
   // Recursively build an Oracle XML Parser DOM tree based
   // on walking the JTidy DOM tree of the "tidied" page.
   private void cloneXMLFragment(Node node, Node curTarget) {
      if ( node == null ) return;
      Document d = curTarget instanceof Document ? (Document)curTarget :
                                        curTarget.getOwnerDocument();
      int type = node.getNodeType();
      switch ( type ) {
        // If we get the root node of the document, start the recursion
        // by calling build the Doc Element
        case Node.DOCUMENT_NODE:
           cloneXMLFragment(((Document)node).getDocumentElement(),d);
           break;

        // If we get an Element in the JTidy DOM, create Element in Oracle DOM
        // and append it to the current target node as a child. Also build
        // Oracle DOM attribute nodes for each attrib of the JTidy DOM Element
        case Node.ELEMENT_NODE:
           Element e = d.createElement(node.getNodeName());
           NamedNodeMap attrs = node.getAttributes();
           for ( int i = 0; i < attrs.getLength(); i++ ) {
              e.setAttribute(attrs.item(i).getNodeName(),
                             attrs.item(i).getNodeValue());
           }
           curTarget.appendChild(e);
           NodeList children = node.getChildNodes();

           // Recurse to build any children
           if ( children != null ) {
              int len = children.getLength();
              for ( int i = 0; i < len; i++ ) {
                 cloneXMLFragment(children.item(i),e);
              }
           }
           break;
        // If we get a Text Node in the JTidy DOM, create Text Node in Oracle
        // DOM and append it to the current target node as a child
        case Node.TEXT_NODE:
           curTarget.appendChild(d.createTextNode(node.getNodeValue()));
           break;
      }
   }
}

