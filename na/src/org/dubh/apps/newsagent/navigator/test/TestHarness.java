// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: TestHarness.java,v 1.1 1999-10-24 00:47:11 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: bduff@uk.oracle.com
//   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
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
package dubh.apps.newsagent.navigator.test;

import dubh.apps.newsagent.navigator.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.mail.*;
import java.io.*;
/**
 * Test Harness for the navigator. 
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: TestHarness.java,v 1.1 1999-10-24 00:47:11 briand Exp $
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
//