// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: TestHarness.java,v 1.2 1999-11-09 22:34:42 briand Exp $
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
package org.javalobby.apps.newsagent.navigator.test;

import org.javalobby.apps.newsagent.navigator.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.mail.*;
import java.io.*;
/**
 * Test Harness for the navigator. 
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: TestHarness.java,v 1.2 1999-11-09 22:34:42 briand Exp $
 */
public class TestHarness 
{   
   public static void main(String[] args)
   {
      testNav();
   }    


   public static void testNav()
   {
      Navigator n = new Navigator(new TestList());
      
      JFrame test = new JFrame();
      
      test.getContentPane().add(n.getComponent(), BorderLayout.CENTER);
      test.pack();
      test.setVisible(true);  
   }
   
   static class TestList implements NavigatorServiceList
   {
      public ArrayList getServices()
      {
         ArrayList l = new ArrayList();
         l.add(new PropertiesService("news",       
            PropertyFileResolver.getDefaultedProperties(
               "navigator"+File.separator+"services"+File.separator+"news", 
               "news.properties", 
               "dubh/apps/newsagent/navigator/services/news", 
               "news.properties"
             )
         ));
         
         return l;
      }   
   }
}


//
// $Log: not supported by cvs2svn $
// Revision 1.1  1999/10/24 00:47:11  briand
// Initial revision.
//
//