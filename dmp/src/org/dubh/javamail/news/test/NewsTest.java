package org.javalobby.javamail.news.test;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

import javax.activation.*;

import org.javalobby.javamail.news.*;
import org.javalobby.javamail.client.ClientRegistry;

import org.javalobby.apps.newsagent.mailviewer.*;

public class NewsTest
{

   public static final void doTests() throws Exception
   {
      initCap();
      org.javalobby.javamail.client.news.NetworkNewsClient.attachStream(
         System.out
      );
      // Register the test news client
      ClientRegistry.setStoreClient("news",
           "org.javalobby.javamail.client.news.NetworkNewsClient");
      //   "org.javalobby.javamail.client.news.TestNewsClient");

      // Connect to a store
      Properties props = System.getProperties();
      Session session = Session.getDefaultInstance(props, null);
      String root = null;
      
      session.setDebug(true);
      
      Store store = session.getStore("news");
      
//      ((NewStore)store).attachDebugStream(new PrintWriter(new OutputStreamWriter(System.out)));
      
      store.connect("127.0.0.1", null, null);
      
      // List namespace
      Folder rf;

      rf = store.getDefaultFolder();
      
    //  System.out.println("Got default folder. Getting children.");
      Folder[] f = rf.list();
    //  System.out.println("Got children");
    //  System.out.flush();
    //  for (int i = 0; i < f.length; i++)
    //      dumpFolder(f[i], "");
      
      
      System.out.println("Enter the name of a newsgroup to list messages in:");
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      
      String ng = br.readLine();
      
      Folder fol = store.getFolder(ng);
      
      fol.open(0);
      
      Message[] m = fol.getMessages();
      
      System.out.println("There are "+m.length+" messages in "+ng);
      
      for (int i=0; i < m.length; i++)
      {
          //Display all the headers
         Enumeration hd = ((MimeMessage)m[i]).getAllHeaderLines();
         
         while (hd.hasMoreElements())
         {
            String line = (String)hd.nextElement();
            
            
            System.out.println(line);
            
         }
         
         System.out.println("-----");
         
         Object content = ((MimeMessage)m[i]).getContent();
         if (!(content instanceof String))
         {
            System.out.println("content is of type"+content.getClass().toString());
         }
         else
         {
            System.out.println(content.toString()); 
         }
         // Display UI
         MessageViewer mv = new MessageViewer(m[i]);
         JFrame fr = new JFrame();
         fr.getContentPane().add(mv, BorderLayout.CENTER);
         fr.pack();
         fr.setVisible(true);
      }
      
      
      store.close();
          
   }
   
   
   public static void initCap()
   {
         MailcapCommandMap mc = new MailcapCommandMap();
         mc.addMailcap("message/*; ;             x-java-view=org.javalobby.apps.newsagent.mailviewer.MessageViewer");
         mc.addMailcap("text/plain; ;            x-java-view=org.javalobby.apps.newsagent.mailviewer.content.TextPlainViewer");
         mc.addMailcap("text/html; ;             x-java-view=org.javalobby.apps.newsagent.mailviewer.content.TextHtmlViewer");
         mc.addMailcap("multipart/*; ;           x-java-view=org.javalobby.apps.newsagent.mailviewer.content.MultipartViewer");
         
         
         CommandInfo[] ci = mc.getPreferredCommands("text/plain");
         
         for (int i=0; i < ci.length; i++)
         {
            System.out.println(ci[i].getCommandName());
            String className = ci[i].getCommandClass();
            System.out.println(ci[i].getCommandClass());
            
            try
            {
               Class c = Class.forName(className);
               Object o = c.newInstance();
            }
            catch (Exception e)
            {
               System.err.println("Failed to instantiate viewer: "+e);
               e.printStackTrace();
            }
         }
         
       
         CommandMap.setDefaultCommandMap( mc );   
   }
   

   public static void main(String[] args)
      throws Exception
   {
      doTests();
   }



    static void dumpFolder(Folder folder, String tab) throws Exception {
       System.out.println(tab + "Name:      " + folder.getName());
   System.out.println(tab + "Full Name: " + folder.getFullName());
   System.out.println(tab + "URL:       " + folder.getURLName());

   if (!folder.isSubscribed())
       System.out.println(tab + "Not Subscribed");

   if (((folder.getType() & Folder.HOLDS_MESSAGES) != 0) && 
       folder.hasNewMessages())
       System.out.println(tab + "Has New Messages");

   if ((folder.getType() & Folder.HOLDS_FOLDERS) != 0) {
       System.out.println(tab + "Is Directory");


   }
    }

}