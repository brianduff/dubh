package dubh.mail.nntp.test;

import javax.mail.*;
import java.util.*;

public class NNTPTest
{

   public static final void doTests() throws Exception
   {
      // Connect to a store
      Properties props = System.getProperties();
      Session session = Session.getDefaultInstance(props, null);
      String root = null;
      
      session.setDebug(true);
      
      Store store = session.getStore("news");
      
      store.connect("news.btinternet.com", null, null);
      
      // List namespace
      Folder rf;

      rf = store.getDefaultFolder();
      
      System.out.println("Got default folder. Getting children.");
      Folder[] f = rf.list();
      System.out.println("Got children");
      System.out.flush();
      for (int i = 0; i < f.length; i++)
          dumpFolder(f[i], "");
      
      
      store.close();
          
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