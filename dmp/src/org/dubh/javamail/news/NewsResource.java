// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: NewsResource.java,v 1.1 2000-06-14 21:33:57 briand Exp $
//   Copyright (C) 1997-2000  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://wired.st-and.ac.uk/~briand/newsagent/
// ---------------------------------------------------------------------------
// Copyright (C) 1997-2000  Brian Duff
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

package org.javalobby.javamail.news;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.text.DateFormat;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class represents a local store which contains information about
 * newsgroups the user has subscribed to and the messages they have read
 * for each newsgroup. There is a separate news resource for each server.
 *
 * @author Brian Duff
 * @version $Id: NewsResource.java,v 1.1 2000-06-14 21:33:57 briand Exp $
 */
public class NewsResource
{
   private HashMap m_groups;
   private List m_subscribedGroups;
   private HashMap m_extendedProperties;

   private static final char
      COMMENT_CHAR = '#',
      SUBSCRIBE_CHAR = ':',
      COMMA_CHAR = ',',
      DASH_CHAR = '-';

   public static final String PROP_LAST_UPDATE = "lastUpdate";



   /**
    * Construct a new, empty news resource.
    */
   public NewsResource()
   {
      init();
   }

   private void init()
   {
      m_groups = new HashMap();
      m_subscribedGroups = null;
      m_extendedProperties = new HashMap();
   }

   /**
    * Ask the news resource if the user has subscribed to the specified
    * newsgroup.
    *
    * @param groupName The name of a group to get the subscription of
    *   Must not be null.
    * @return true if the user has subscribed to the specified group.
    *   false if not.
    * @throws java.lang.IllegalArgumentException if the group doesn't
    *   exist.
    */
   public boolean isSubscribed(String groupName)
   {
      GroupInfo g = getGroupInfo(groupName);

      return g.isSubscribed();
   }

   /**
    * Change the subscription of a newsgroup.
    *
    * @param groupName The name of a group to change the subscription of
    *  Must not be null.
    * @param s Whether the group is subscribed.
    * @throws java.lang.IllegalArgumentException if the group doesn't
    *   exist.
    */
   public void setSubscribed(String groupName, boolean s)
   {
      GroupInfo g = getGroupInfo(groupName);

      g.setSubscribed(s);

      // Need to add/remove the group to the subscribed groups list
      if (m_subscribedGroups != null)
      {
         if (s)
         {
            m_subscribedGroups.add(groupName);
         }
         else
         {
            m_subscribedGroups.remove(groupName);
         }
      }
   }

   /**
    * Get the group info for a group.
    *
    * @param groupName the name of the group
    * @returns a GroupInfo object.
    * @throws java.lang.IllegalArgumentException if the group doesn't
    *   exist.
    */
   private GroupInfo getGroupInfo(String groupName)
   {
      return (GroupInfo)m_groups.get(groupName);
   }

   /**
    * Get all groups in this news resource.
    *
    * @return an iterator over all groups. Each iteration will be a String
    *   object corresponding to the group name.
    */
   public Iterator getAllGroups()
   {
      return m_groups.keySet().iterator();
   }

   /**
    * Get the number of groups in this resource.
    */
   public int getGroupCount()
   {
      return m_groups.size();
   }

   /**
    * This is normally called when a new NewsResource is created and a fresh
    * list of groups has been read in from the server.
    *
    * @param groups an iterator on your groups. Each iteration should provide
    *    a String corresponding to the name of the group.
    */
   public void setAllGroups(Iterator groups)
   {
      // If we already have entries, throw them away.
      if (m_groups.size() > 0)
      {
         m_groups = new HashMap();
      }

      while (groups.hasNext())
      {
         String groupName = (String)groups.next();
         addNewGroup(groupName);
      }
   }

   /**
    * Add a new group to the news resource. It will initially be unsubscribed.
    *
    * @param groupName the name of the group to add. Must not be null
    */
   public void addNewGroup(String groupName)
   {
      m_groups.put(groupName, new GroupInfo());
   }

   /**
    * Internal convenience method for creating a group. We use this
    * when parsing the .newsrc to stop us from having to look up the
    * group info after addNewGroup(). Proabably negligable performance
    * advantage.....
    */
   private void putGroup(String groupName, GroupInfo info)
   {
      m_groups.put(groupName, info);
   }

   /**
    * Remove a group from the news resource.
    */
   public void removeGroup(String groupName)
   {
      m_groups.remove(groupName);
      if (m_subscribedGroups != null)
      {
         m_subscribedGroups.remove(groupName);
      }
   }

   /**
    * Get a count of the subscribed groups
    */
   public int getSubscribedGroupCount()
   {
      if (m_subscribedGroups == null)
      {
         return 0;
      }
      return m_subscribedGroups.size();
   }

   /**
    * Get a list of just the subscribed groups in this news resource.
    *
    * @return an iterator over subscribed groups. Each iteration will be
    *   the String name of a group.
    */
   public Iterator getSubscribedGroups()
   {
      // We populate this once, and forever after maintain the list.
      // It might have already been populated, e.g. on load or a
      // previous call to this method
      if (m_subscribedGroups == null)
      {
         m_subscribedGroups = new ArrayList();
         Iterator allGroups = getAllGroups();
         while (allGroups.hasNext())
         {
            String groupName = (String)allGroups.next();
            if (isSubscribed(groupName))
            {
               m_subscribedGroups.add(groupName);
            }
         }
      }
      return m_subscribedGroups.iterator();
   }

   /**
    * Ask the news resource if the specified message in the specified group
    * has been read.
    *
    * @param groupName the name of a news group. Must not be null
    * @param msgno the message number to check
    * @throws IllegalArgumentException if the specified group doesn't exist
    */
   public boolean isMessageMarkedRead(String groupName, int msgno)
   {
      // This is the easy one

      GroupInfo groupInfo = getGroupInfo(groupName);

      MessageEntry entry = groupInfo.getEntryForMessage(msgno);

      return (entry != null);
   }

   /**
    * Mark or Unmark a message in a group as read.
    *
    * @param groupName the name of a news group. Must not be null.
    * @param msgno the message number to check
    * @param isRead true to mark a message read, false to mark a message unread.
    * @throws IllegalArgumentException if the specified group doesn't exist
    */
   public void setMessageMarkedRead(String groupName, int msgno, boolean isRead)
   {
      GroupInfo groupInfo = getGroupInfo(groupName);

      if (isRead)
      {
         groupInfo.addEntry(new SingleMessageEntry(msgno));
      }
      else
      {
         groupInfo.removeMessage(msgno);
      }
   }

   /**
    * Mark or Unmark a message in a group as read.
    *
    * @param groupName the name of a news group. Must not be null.
    * @param msgno the message number to check
    * @param isRead true to mark a message read, false to mark a message unread.
    * @throws IllegalArgumentException if the specified group doesn't exist
    */
   public void setMessageMarkedRead(String groupName, int lowMess, int highMess, boolean isRead)
   {
      GroupInfo groupInfo = getGroupInfo(groupName);

      if (isRead)
      {
         groupInfo.addEntry(new RangeMessageEntry(lowMess, highMess));
      }
      else
      {
         // Should implement this differently, probably.
         for (int i = lowMess; i <= highMess; i++)
         {
            groupInfo.removeMessage(i);
         }
      }
   }

   /**
    * Consolidate ranges; should be run every so often to reduce the amount of space
    * used.
    */
   protected void consolidateRanges()
   {
      // TODO
   }

   /**
    * Load and parse the specified file into the NewsResource.
    *
    * @param fname A filename to load into the NewsResource. Must not be null
    * @throws java.io.IOException if an error occurred while reading the file
    */
   public void loadFile(String fname)
      throws IOException
   {
      File f = new File(fname);

      if (f.exists() && f.isFile() && f.canRead())
      {
         BufferedReader br = new BufferedReader(new FileReader(f));

         String curLine = br.readLine();

         while (curLine != null)
         {
            if (curLine.trim().charAt(0) != COMMENT_CHAR)
            {
               StringTokenizer st = new StringTokenizer(curLine,
                  ":!,", true
               );

               // Check for a malformed line; should have at least a
               // newsgroup name and a : or !
               if (st.countTokens() > 2)
               {
                  GroupInfo gi = new GroupInfo();
                  String groupName = st.nextToken();
                  String subscribeFlag = st.nextToken();
                  gi.setSubscribed(subscribeFlag.charAt(0) == SUBSCRIBE_CHAR);
                  // Will set unsubscribed for any other char (ought to be !)

                  // Get the message counts
                  while(st.hasMoreTokens())
                  {
                     String theToken = st.nextToken();
                     if (theToken.charAt(0) != COMMA_CHAR)
                     {
                        //System.out.println(theToken);
                        // The token is a message count. It's a range if it
                        // contains a dash.
                        int dashIndex = theToken.indexOf(DASH_CHAR);

                        try
                        {
                           if (dashIndex > -1)
                           {
                              //System.out.println(theToken.substring(0, dashIndex));
                              //System.out.println(theToken.substring(dashIndex+1));
                              gi.addEntry(new RangeMessageEntry(
                                 Integer.parseInt(theToken.substring(0, dashIndex).trim()),
                                 Integer.parseInt(theToken.substring(dashIndex+1).trim())
                              ));
                           }
                           else
                           {
                              gi.addEntry(new SingleMessageEntry(
                                 Integer.parseInt(theToken.trim())
                              ));
                           }
                        }
                        catch (NumberFormatException nfe)
                        {
                        }

                     }
                  }
                  // Got all the message counts. Time to add the group to the
                  // res.
                  putGroup(groupName, gi);
               }
            }
            else
            {
               // The line is a comment. But it may contain an "Extended
               // Property". If there is an equals sign, we store the
               // property.
               String trimLine = curLine.trim();
               int equalPos = trimLine.indexOf("=");
               if (equalPos > 0 && trimLine.length() > equalPos)
               {
                  setExtendedProperty(
                     trimLine.substring(1, equalPos).trim(),
                     trimLine.substring(equalPos+1).trim()
                  );
               }
            }
            curLine = br.readLine();
         }
         br.close();
      }
   }

   /**
    * Set an extended property
    */
   protected void setExtendedProperty(String key, String value)
   {
      m_extendedProperties.put(key, value);
   }

   protected String getExtendedProperty(String key)
   {
      return (String)m_extendedProperties.get(key);
   }

   /**
    * Get the date the list of newsgroups was last updated. The newsresource
    * just stores this date.
    */
   public Date getLastGroupUpdate()
   {
      // Never == the start of the epoch 1/1/70 0:0:0
      Date never = new Date(0);
      String dateString = getExtendedProperty(PROP_LAST_UPDATE);
      if (dateString == null)
      {
         return never;
      }

      DateFormat df = DateFormat.getDateTimeInstance();

      try
      {
         return df.parse(dateString);
      }
      catch (ParseException pe)
      {
         return never;
      }
   }

   /**
    * Call this to change the date the list of newsgroups was last updated to
    * the current date & time.
    */
   public void setLastGroupUpdateNow()
   {
      Date now = new Date();
      DateFormat df = DateFormat.getDateTimeInstance();
      setExtendedProperty(PROP_LAST_UPDATE, df.format(now));
   }

   /**
    * Dump the resource to the specified printwriter
    */
   protected void dump(PrintWriter pw)
   {
      // First, dump any extended properties.
      Iterator keyIter = m_extendedProperties.keySet().iterator();
      boolean isHeaderDone = false;
      boolean needToDoFooter = false;
      while (keyIter.hasNext())
      {
         if (!isHeaderDone)
         {
            pw.println("# Extended properties start here. DO NOT REMOVE!");
            isHeaderDone = true;
            needToDoFooter = true;
         }
         pw.print("# ");
         String key = (String)keyIter.next();
         String val = getExtendedProperty(key);
         pw.print(key);
         pw.print("=");
         pw.print(val);
         pw.println("");
      }
      if (needToDoFooter)
      {
         pw.println("# Extended properties end.");
      }

      Iterator allGroups = getAllGroups();
      while (allGroups.hasNext())
      {
         String groupName = (String)allGroups.next();
         pw.print(groupName);
         GroupInfo info = getGroupInfo(groupName);
         if (info.isSubscribed())
         {
            pw.print(": ");
         }
         else
         {
            pw.print("! ");
         }

         int entries = info.getEntryCount();
         for (int i=0; i < entries; i++)
         {
            MessageEntry me = info.getEntry(i);
            me.write(pw);
            if (i < entries -1)
            {
               pw.print(",");
            }
         }
         pw.println("");
      }
      pw.flush();
   }

   /**
    * Save the news resource into a file.
    *
    * @param f A filename to save the NewsResource to. Must not be null.
    * @throws java.io.IOException if an error occurred while writing the file
    * @throws java.lang.IllegalArgumentException if the file was not writable.
    */
   public void saveFile(String f)
      throws IOException
   {
      File file = new File(f);


      PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

      dump(pw);

      pw.close();
   }


   /**
    * A group info is the value in the groups hashmap. Each group info
    * contains a list of MessageEntry objects and a boolean value indicating
    * whether the group is subscribed.
    */
   private class GroupInfo
   {
      private boolean m_isSubscribed;

      /**
       * We don't provide direct access to this list so we
       * don't have to instantiate it if there is no information for
       * this group. This cuts down our memory overhead.
       */
      private List m_messageEntries;

      public GroupInfo()
      {
         m_isSubscribed = false;
      }

      public boolean isSubscribed()
      {
         return m_isSubscribed;
      }

      public void setSubscribed(boolean isSubscribed)
      {
         m_isSubscribed = isSubscribed;
      }

      public int getEntryCount()
      {
         if (m_messageEntries == null)
         {
            return 0;
         }
         return m_messageEntries.size();
      }

      public MessageEntry getEntryForMessage(int msgno)
      {
         if (m_messageEntries == null)
         {
            return null;
         }
         for (int i=0; i < m_messageEntries.size(); i++)
         {
            MessageEntry entry = (MessageEntry)m_messageEntries.get(i);
            if (entry.matches(msgno))
            {
               return entry;
            }
         }
         return null;
      }

      /**
       * Add a message entry.
       */
      public void addEntry(MessageEntry me)
      {
         if (m_messageEntries == null)
         {
            m_messageEntries = new ArrayList();
         }
         m_messageEntries.add(me);
      }

      public void removeMessage(int msgno)
      {
         if (m_messageEntries != null)
         {
            MessageEntry m = getEntryForMessage(msgno);
            if (m != null)
            {
               m.remove(m_messageEntries, msgno);
            }
         }
      }

      public MessageEntry getEntry(int index)
      {
         if (m_messageEntries == null)
         {
            throw new ArrayIndexOutOfBoundsException(
               index+" is greater than the list size (0)"
            );
         }
         return (MessageEntry)m_messageEntries.get(index);
      }

   }


   /**
    * Each group is hashed with a list of message entries. These can either be
    * ranges or single values. We abstract over this by providing an interface
    * that doesn't care whether a range or single value is involved. This just
    * helps make things neater.
    */
   private interface MessageEntry
   {
      /**
       * The entry returns true if the specified message number is part of
       * the entry.
       */
      public boolean matches(int msgno);

      /**
       * The entry writes itself to the specified writer
       */
      public void write(PrintWriter pw);

      /**
       * Remove this entry from the list. This may also cause additional
       * entries to be added if the original entry was a range that had
       * to be split up.
       */
      public void remove(List l, int msgno);
   }

   /**
    * An implementation of MessageEntry that corresponds to a single marked message
    */
   private class SingleMessageEntry implements MessageEntry
   {
      private int m_message;

      public SingleMessageEntry(int messageNo)
      {
         m_message = messageNo;
      }

      public boolean matches(int msgno)
      {
         return (msgno == m_message);
      }

      public void write(PrintWriter pw)
      {
         pw.print(m_message);
      }

      public void remove(List l, int msgno)
      {
         l.remove(this);
      }
   }


   private class RangeMessageEntry implements MessageEntry
   {
      private int m_lowMessage;
      private int m_highMessage;

      public RangeMessageEntry(int lowMessage, int highMessage)
      {
         m_lowMessage = lowMessage;
         m_highMessage = highMessage;
      }

      public boolean matches(int msgno)
      {
         return (m_lowMessage <= msgno && msgno <= m_highMessage);
      }

      public void write(PrintWriter pw)
      {
         pw.print(m_lowMessage);
         pw.print("-");
         pw.print(m_highMessage);
      }

      public void remove(List l, int msgno)
      {

         // The (most common) case is that we have to remove this
         // entry and replace it with two new ranges. This happens
         // if the msgno is > m_lowMessage and < m_highMessage.
         if (msgno > m_lowMessage && msgno < m_highMessage)
         {
            l.remove(this);

            MessageEntry low, high;
            // If the deleted msgno is adjacent to low or high,
            // we have to create a single entry rather than a range.
            if (msgno-m_lowMessage == 1)
            {
               low = new SingleMessageEntry(m_lowMessage);
            }
            else
            {
               low = new RangeMessageEntry(m_lowMessage, msgno-1);
            }

            if (m_highMessage-msgno == 1)
            {
               high = new SingleMessageEntry(m_highMessage);
            }
            else
            {
               high = new RangeMessageEntry(msgno+1, m_highMessage);
            }

            l.add(low);
            l.add(high);

            return;

         }

         // We can keep this entry if the msgno == m_lowMessage or
         // m_highMessage by just shifting the indices.
         if (msgno == m_lowMessage)
         {
            m_lowMessage++;
         }

         if (msgno == m_highMessage)
         {
            m_highMessage--;
         }

         // Not sure if code below is needed

         // But now, we might have gotten into a situation where
         // both indices are the same; we should remove this
         // range and insert a single value.
         if (m_lowMessage == m_highMessage)
         {
            l.remove(this);
            l.add(new SingleMessageEntry(m_lowMessage));
         }

      }
   }




   public static void main(String[] args)
   {
      NewsResource res = new NewsResource();

      ArrayList initGroups = new ArrayList();
      initGroups.add("comp.lang.java.programmer");
      initGroups.add("comp.lang.java.advocacy");
      initGroups.add("borland.public.jbuilder.swing");
      initGroups.add("some.random.group");

      res.setAllGroups(initGroups.iterator());

      // Subscribe to some groups

      res.setSubscribed("comp.lang.java.programmer", true);
      res.setSubscribed("comp.lang.java.advocacy", true);

      // Print out subscribed groups
      System.out.println("Subscribed groups:");
      Iterator sGroups = res.getSubscribedGroups();
      while (sGroups.hasNext())
      {
         System.out.println((String)sGroups.next());
      }

      // Mark some read messages
      res.setMessageMarkedRead("comp.lang.java.programmer", 1, 2, true);
      res.setMessageMarkedRead("comp.lang.java.programmer", 7, 9, true);
      res.setMessageMarkedRead("comp.lang.java.programmer", 10, true);
      res.setMessageMarkedRead("comp.lang.java.programmer", 12, 50, true);
      res.setMessageMarkedRead("comp.lang.java.programmer", 75, true);
      res.setMessageMarkedRead("comp.lang.java.programmer", 100, 200, true);

      // Verify some msgnos.
      System.out.println("Is msg 5 read? (should be false)");
      System.out.println(res.isMessageMarkedRead("comp.lang.java.programmer", 5));
      System.out.println("Is msg 15 read? (should be true)");
      System.out.println(res.isMessageMarkedRead("comp.lang.java.programmer", 15));
      System.out.println("Is msg 75 read? (should be true)");
      System.out.println(res.isMessageMarkedRead("comp.lang.java.programmer", 75));

      // Now mark msg 75 unread.
      res.setMessageMarkedRead("comp.lang.java.programmer", 75, false);

      // And message 13 unread (breaks up a range at the low end)
      res.setMessageMarkedRead("comp.lang.java.programmer", 13, false);

      // And message 49 (breaks up a range at the high end)
      res.setMessageMarkedRead("comp.lang.java.programmer", 49, false);

      // And 2 (breaks up a small range)
      res.setMessageMarkedRead("comp.lang.java.programmer", 2, false);

      // And 8 (breaks up a threesome)
      res.setMessageMarkedRead("comp.lang.java.programmer", 8, false);

      // Now verify these are all false:
      System.out.println("Should see 5 false's");
      System.out.println(res.isMessageMarkedRead("comp.lang.java.programmer", 75));
      System.out.println(res.isMessageMarkedRead("comp.lang.java.programmer", 13));
      System.out.println(res.isMessageMarkedRead("comp.lang.java.programmer", 49));
      System.out.println(res.isMessageMarkedRead("comp.lang.java.programmer", 2));
      System.out.println(res.isMessageMarkedRead("comp.lang.java.programmer", 8));

      // Finally, dump out the file in its current state to system.out

      PrintWriter outWriter = new PrintWriter(System.out);

      res.dump(outWriter);

      // Remove most messages and dump again.
      res.setMessageMarkedRead("comp.lang.java.programmer", 0, 199, false);

      outWriter = new PrintWriter(System.out);
      res.setExtendedProperty("TestProperty", "Funky Biscuits On Fire");

      res.dump(outWriter);

      // OK, just for a laugh, let's put a huge number of groups in the resource


      int NUMGROUPS = 80000;
      System.out.println("Performance tests with "+NUMGROUPS+" groups");
      res = new NewsResource();
      long startTime = System.currentTimeMillis();
      ArrayList lGrps = new ArrayList();
      for (int i=0; i < NUMGROUPS; i++)
      {
         lGrps.add("group."+i);
      }
      long createStart = System.currentTimeMillis();
      res.setAllGroups(lGrps.iterator());
      long subscribeStart = System.currentTimeMillis();
      for (int i=0; i < NUMGROUPS; i+=2)
      {
         res.setSubscribed((String)lGrps.get(i), true);
      }
      long markReadStart = System.currentTimeMillis();
      for (int i=0; i < NUMGROUPS; i+=4)
      {
         res.setMessageMarkedRead((String)lGrps.get(i), 22, true);
         res.setMessageMarkedRead((String)lGrps.get(i+1), 0, 100, true);
         res.setMessageMarkedRead((String)lGrps.get(i+1), 50, false);
      }
      long endTime = System.currentTimeMillis();

      System.out.println("Timings:");
      System.out.println((createStart-startTime)+"ms to create newsgroups");
      System.out.println((subscribeStart-createStart)+"ms to put groups in the newsrc");
      System.out.println((markReadStart-subscribeStart)+"ms to subscribe to  groups");
      System.out.println((endTime-markReadStart)+"ms to mark some messages read/unread in groups");


      long startSave=0, stopSave=0;
      try
      {
         startSave = System.currentTimeMillis();
         res.setExtendedProperty("SaveTest", "This is a test property");
         res.saveFile("c:\\.newsrc.test");
         stopSave = System.currentTimeMillis();
      }
      catch (IOException ioe)
      {
         stopSave = System.currentTimeMillis();
         System.out.println("IO Exception saving file: "+ioe);
         ioe.printStackTrace();
      }
      System.out.println((stopSave-startSave)+"ms to save the .newsrc");
      //res.dump(new PrintWriter(System.out));

      res = new NewsResource();

      long startLoad = System.currentTimeMillis();
      long stopLoad = 0;
      try
      {
         res.loadFile("c:\\.newsrc.test");
         stopLoad = System.currentTimeMillis();
         res.saveFile("c:\\.newsrc.out");
      }
      catch (IOException ioe)
      {
         stopLoad = System.currentTimeMillis();
         System.out.println("IOException loading file "+ioe);
         ioe.printStackTrace();
      }

      System.out.println((stopLoad-startLoad)+"ms to load the .newsrc");





   }

}


//
// $Log: not supported by cvs2svn $
//