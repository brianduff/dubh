// $Id: TestHarness.java,v 1.1.1.1 2000-09-17 17:38:15 briand Exp $
// Copyright (c) 2000 Brian Duff
package org.dubh.jdav.test;

import java.net.URL;
import java.io.*;

import org.dubh.jdav.WebDAVURLConnection;

import org.dubh.jdav.DAVResourceOutputStream;
import org.dubh.jdav.DAVResourceInputStream;
import org.dubh.jdav.DAVUtils;
import org.dubh.jdav.DAVLockToken;
import org.dubh.jdav.DAVHref;

/**
 * A test harness for JDav.
 *
 * @author Brian Duff (<a href="mailto:brian@dubh.org">brian@dubh.org</a>)
 */
public class TestHarness
{
   private static void doGETTest() throws Exception
   {
      System.out.println("GET http://www.dubh.org/dav ---------------------");
      WebDAVURLConnection conn = new WebDAVURLConnection(
         new URL("http://www.dubh.org/dav"), null, -1
      );

      conn.connect();

      dumpStream(conn.getInputStream());

      conn.disconnect();
   }

   private static void doCOPYTest() throws Exception
   {
      System.out.println("COPY http://www.dubh.org/dav/here.txt -> copied_here.txt ---------------------");


      WebDAVURLConnection conn = new WebDAVURLConnection(
         new URL("http://www.dubh.org/dav/here.txt"), null, -1
      );

      conn.setRequestProperty("Destination", "http://www.dubh.org/dav/copied_here.txt");
      conn.setRequestMethod("COPY");
      conn.connect();

      dumpStream(conn.getInputStream());

      conn.disconnect();
   }

   private static void doPROPFIND() throws Exception
   {
      System.out.println("PROPFIND http://www.dubh.org/dav ---------------------");


      WebDAVURLConnection conn = new WebDAVURLConnection(
         new URL("http://www.dubh.org/dav/"), null, -1
      );
      conn.setDoOutput(true);
      conn.setRequestMethod("PROPFIND");
      conn.setRequestProperty("Depth", "1");

      PrintWriter p = new PrintWriter(conn.getOutputStream());

      p.println("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
      p.println("<D:propfind xmlns:D=\"DAV:\">");
      p.println("<D:allprop />");
      p.println("</D:propfind>");
      p.close();

      conn.connect();

      dumpStream(conn.getInputStream());

      conn.disconnect();
   }

   private static void doLOCKTest() throws Exception
   {
      System.out.println("LOCK http://www.dubh.org/dav/locktest.txt ---------------------");

      DAVLockToken tok = DAVUtils.lockResource(new URL("http://www.dubh.org/dav/locktest.txt"), false,
         "Brian Duff"
      );

      System.out.println("Lock token(s):");
      DAVHref[] tokenURIS = tok.getTokenURIs();
      for (int i=0; i < tokenURIS.length; i++)
      {
         System.out.println(tokenURIS[i].getURI());
      }
   }

   private static void doLOCKUNLOCKTest() throws Exception
   {
      System.out.println("LOCK http://www.dubh.org/dav/locktest.txt ---------------------");

      DAVLockToken tok = DAVUtils.lockResource(new URL("http://www.dubh.org/dav/locktest.txt"), true,
         "Brian Duff"
      );

      System.out.println("Lock token(s):");
      DAVHref[] tokenURIS = tok.getTokenURIs();
      for (int i=0; i < tokenURIS.length; i++)
      {
         System.out.println(tokenURIS[i].getURI());
      }
      System.out.println("UNLOCK http://www.dubh.org/dav/locktest.txt ---------------------");
      DAVUtils.unlockResource(new URL("http://www.dubh.org/dav/locktest.txt"),
         tok
      );
   }


   private static void doMKCOLTest() throws Exception
   {
      System.out.println("MKCOL http://www.dubh.org/dav/subcollection ---------------------");


      WebDAVURLConnection conn = new WebDAVURLConnection(
         new URL("http://www.dubh.org/dav/subcollection"), null, -1
      );

      conn.setRequestMethod("MKCOL");
      conn.connect();

      dumpStream(conn.getInputStream());

      conn.disconnect();
   }

   private static void doDELETETest() throws Exception
   {
      System.out.println("DELETE http://www.dubh.org/dav/subcollection ---------------------");


      WebDAVURLConnection conn = new WebDAVURLConnection(
         new URL("http://www.dubh.org/dav/subcollection"), null, -1
      );

      conn.setRequestMethod("DELETE");
      conn.connect();

      System.out.println(conn.getResponseCode()+" "+conn.getResponseMessage());
      dumpStream(conn.getInputStream());

      conn.disconnect();
   }

   private static void doDFISTest() throws Exception
   {
      DAVResourceInputStream dfis = new DAVResourceInputStream(
         "http://www.dubh.org/dav"
      );
      dumpStream(dfis);
      dfis.close();
   }

   private static void doFOSTest() throws Exception
   {
      DAVResourceOutputStream dfis = new DAVResourceOutputStream(
         "http://www.dubh.org/dav/test.txt", true, "Brian Duff"
      );

      PrintWriter pw = new PrintWriter(dfis);

      pw.println("Hello World");
      pw.println("This is a funky little test of some fantastic bollocks");
      pw.println("Java & DAV: a cool and funky combination. Some new stuff");
      dfis.close();
   }

   private static void doFISTest() throws Exception
   {
      DAVResourceInputStream dfis = new DAVResourceInputStream(
         "http://www.dubh.org/dav/somefilethatdoesntexist.txt"
      );

      dumpStream(dfis);
      dfis.close();
   }

   public static void main(String[] args)
   {
      try
      {
         //doLOCKUNLOCKTest();
         //doDFISTest();
         //doPROPFIND();
         //doFOSTest();
         //doFISTest();
         //doLOCKTest();
         doGETTest();
         //doCOPYTest();
         //doPROPPATCHTest();
         //doMKCOLTest();
         //doDELETETest();
         //doLOCKTest();

      }
      catch (Throwable t)
      {
         System.err.println("Error");
         t.printStackTrace();
      }
   }

   private static void dumpStream(InputStream is)
      throws IOException
   {
      BufferedReader br = new BufferedReader(new InputStreamReader(
         is
      ));

      String line = br.readLine();
      while (line != null)
      {
         System.out.println(line);
         line = br.readLine();
      }
      br.close();

   }
}

 