// ---------------------------------------------------------------------------
//   Dubh Servlets and Tools
//   $Id: HTMLFileRecognizer.java,v 1.1.1.1 2001-06-03 05:07:03 briand Exp $
//   Copyright (C) 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 2001 Brian Duff
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

package org.dubh.tool.findnew;

import java.io.File;
import java.net.URL;

import oracle.xml.parser.v2.*;
import org.w3c.dom.*;

import org.dubh.tool.JTidyConverter;

/**
 * A FileRecognizer that recognizes HTML documents. HTML documents
 * are recognizable by the .html or .htm extension.
 *
 * @author Brian.Duff@oracle.com
 */
public class HTMLFileRecognizer implements FileRecognizer
{
   /**
    * If this recognizer recognizes the specified file, it should return
    * true. The HTML recognizer recognizes files with .html or .htm
    * extensions.
    *
    * @param f a file
    * @return true if this recognizer recognizes f
    */
   public boolean isRecognizedFile(File f)
   {
      String filePath = f.getPath();
      int lastDot = filePath.lastIndexOf('.');
      if (lastDot != -1)
      {
         String ext = filePath.substring(lastDot+1);
         return ("html".equalsIgnoreCase(ext) || "htm".equalsIgnoreCase(ext));
      }
      return false;
   }

   /**
    * Get an HTML meta value
    *
    * @param htmlDoc a parsed HTML document
    * @param metaName the name of the meta value
    * @return the meta content or null.
    */
   private String getMetaValue(XMLDocument htmlDoc, String metaName)
      throws XSLException
   {
      XMLNode theNode = (XMLNode) htmlDoc.selectSingleNode(
         "/html/head/meta[@name='"+metaName+"']"
      );

      if (theNode != null)
      {
         return theNode.valueOf("@content");
      }

      return null;
   }

   /**
    * The recognizer should return information on the specified file.
    *
    * @param f a file
    * @return information on the file
    */
   public FileInformation getInformation(File f)
   {
      String tTitle = null,
         tDescription = null, tAuthor = null, tRevision = null;
      try
      {
         JTidyConverter j = new JTidyConverter();
         URL u = f.toURL();
         XMLDocument doc = j.XMLifyHTMLFrom(u);

         XMLNode headNode = (XMLNode) doc.selectSingleNode( "/html/head" );
         final String title =
            (headNode == null ? null : headNode.valueOf("title"));

         final String description = getMetaValue(doc, "description");
         final String author = getMetaValue(doc, "author");
         final String revision = getMetaValue(doc, "revision");

         return new FileInformation() {
            public String getTitle() { return title; }
            public String getDescription() { return description; }
            public String getAuthor() { return author; }
            public String getRevision() { return revision; }
         };

      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      return null;
   }

   public static void main(String[] args)
   {
      HTMLFileRecognizer rec = new HTMLFileRecognizer();
      File f = new File(args[0]);
      if (rec.isRecognizedFile(f))
      {
         FileInformation finfo = rec.getInformation(f);
         System.out.println("Title: "+finfo.getTitle());
         System.out.println("Description: "+finfo.getDescription());
         System.out.println("Author: "+finfo.getAuthor());
         System.out.println("Revision: "+finfo.getRevision());
      }
   }


}