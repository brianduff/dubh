// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: Folder.java,v 1.4 1999-06-01 00:30:52 briand Exp $
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

package dubh.apps.newsagent;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.swing.tree.*;
import javax.swing.JProgressBar;

import dubh.apps.newsagent.dialog.ErrorReporter;
import dubh.apps.newsagent.nntp.MessageProvider;
import dubh.apps.newsagent.nntp.MessageHeader;
import dubh.apps.newsagent.nntp.MessageBody;


import dubh.utils.misc.Debug;

/**
 * Represents a folder containing messages in permanent storage.
 * @author Brian Duff
 * @version $Id: Folder.java,v 1.4 1999-06-01 00:30:52 briand Exp $
 */
public class Folder implements MessageProvider {
// Private instance variables
  private String m_folderName;
  /** Contains all the Nodes, indexed by header, for quick lookup */
  private Hashtable m_messages;
  /** Filename for serialised messages */
  private static final String serFile = "messages.dat";

  /**
   * Creates a folder with the specified name. This constructor <b>never</b>
   * physically creates folders, so you should be sure that the folder actually
   * already exists before constructing a folder object with a certain name. The
   * getFolders() method in StorageManager will return an enumeration of all
   * available folders. If you try to create a folder object with an unknown
   * folder name, an error will occur.
   */
  public Folder(String name) {
     m_folderName = name;
     m_messages = new Hashtable();
     restoreMessages();   // create serialised file.
  }

  /**
   * Retrieves the name of this folder object.
   */
  public String getName() {
     return m_folderName;
  }

  public String toString() {
   return getName();
  }

  /**
   * For the MessageProvider interface. Returns the name of this folder.
   @see dubh.apps.newsagent.Folder.getName()
   */
  public String getProviderName() {
   return getName();
  }

  /**
   * Retrieves a File object corresponding to this folder.
   */
  public File getFile() {
     return new File(GlobalState.foldersDir+getName());
  }

  /**
   * Saves a message in this folder.
   @param head the header of the message
   @param body the body of the message
   */
  public void saveMessage(MessageHeader head, MessageBody body) {
     // Check if the message header already exists in the hashtable.
     if (!alreadyExists(head)) {
        m_messages.put(head, body);
        storeMessages();
     } else {
        if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Message not saved: already exists in folder");
     }
  }

  /**
   * Implements the MessageProvider.ensureConnected() method. Pointless for
   * folders.
   @returns true, always.
   */
  public boolean ensureConnected() {
   // Maybe this method should check if the folder exists.
   return true;
  }

  /**
   * Determines whether a message already exists in this folder.
   */
  private boolean alreadyExists(MessageHeader head) {
     // Hashtable.containsKey() doesn't seem to work properly. Ho hum.
     Enumeration e = m_messages.keys();
     while (e.hasMoreElements()) {
        if (((MessageHeader)e.nextElement()).equals(head))
           return true;
     }
     return false;
  }

  /**
   * Retrieves a Tree of all the messages in this folder. Implements
   * MessageProvider. Messages in a folder aren't threaded at the moment.
   @return a Vector consisting entirely of MessageHeader objects.
   */
  public TreeModel getHeaders() {
    return getTreeModel(null);
  }

  /**
   * Retrieves a Tree of all the messages in this folder. Implements
   * MessageProvider. Messages in a folder aren't threaded at the moment.
   @param max the maximum number of headers to download. Ignored for a folder.
   @return a Vector consisting entirely of MessageHeader objects.
   */
  public TreeModel getHeaders(int max) {
    return getHeaders();
  }

  /**
   * Retrieves a Tree of all the messages in this folder. Implements
   * MessageProvider. Messages in a folder aren't threaded at the moment.
   @param progress a Swing Progress monitor to use.
   @return a Vector consisting entirely of MessageHeader objects.
   */
  public TreeModel getHeaders(JProgressBar progress) {
    return getTreeModel(progress);
  }

  /**
   * Retrieves a Tree of all the messages in this folder. Implements
   * MessageProvider. Messages in a folder aren't threaded at the moment.
   @param progress a Swing Progress monitor to use.
   @param max the maximum number of headers to download. Ignored for a folder.
   @return a Vector consisting entirely of MessageHeader objects.
   */
  public TreeModel getHeaders(JProgressBar progress, int max) {
    return getHeaders(progress);
  }

  /**
   * Returns the number of messages in this folder.
   */
  public int getHeaderCount() {
   return m_messages.size();
  }


  /**
   * Given a message header, returns the body of that message, provided the
   * message exists in this folder.
   @param mhead the header of a message which must exist in this folder.
   @return the body of the requested message.
   */
  public MessageBody getBody(MessageHeader mhead) {
     return (MessageBody) m_messages.get(mhead);
  }

  /**
   * Stores the messages into the folder.
   */
  private void storeMessages() {
     /* Implemented using serialization at the moment, which will be horribly
        inefficient for folders with lots of messages. Something to think about
        in future versions, maybe.
     */
     String fileName = GlobalState.foldersDir + getName() + File.separator + serFile;
     try {
        FileOutputStream fos = new FileOutputStream(fileName);
        GZIPOutputStream gzos = new GZIPOutputStream(fos);
        ObjectOutputStream out = new ObjectOutputStream(gzos);
        out.writeObject(m_messages);
        out.flush();
        out.close();
     } catch (IOException e) {
        if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "IOException writing folder: "+e);
        ErrorReporter.error("CantSaveToFolder", new String[] {getName()});
     }
  }

  /**
   * Retrieves the messages into memory.
   */
  private void restoreMessages() {
     // See implementation comment for storeMessages().
     // Check to see if the serialised file exists. If not, call storeMessages
     // which will create an empty serialised file.
     String fileName = GlobalState.foldersDir + getName() + File.separator + serFile;
     File test = new File(fileName);
     if (test.exists()) {
        if (test.isDirectory()) {
           if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "User has replaced serialised object with a folder: "+getName());
        } else {
           try {
              FileInputStream fis = new FileInputStream(fileName);
              GZIPInputStream gis = new GZIPInputStream(fis);
              ObjectInputStream in = new ObjectInputStream(gis);
              m_messages = (Hashtable) in.readObject();
              in.close();
           } catch (IOException e) {
              if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "IOException reading from folder: "+e);
              ErrorReporter.error("CantReadFromFolder", new String[] {getName()});
           } catch (ClassNotFoundException ce) {
              if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "IOException reading from folder: "+ce);
              ErrorReporter.error("CantReadFromFolder", new String[] {getName()});
           }
        }
     } else {
        // File doesn't exist. Serialise just to create the file, folder is empty.
        storeMessages();
     }
  }

  /**
   * Deletes a message from the folder.
   */
  public void deleteMessage(MessageHeader mhead) {

  }

  /**
   * Marks a message as read
   */
  public void setMessageRead(MessageHeader mhead, boolean read) {


  }

// Private utils

  /**
   * Returns the list of message headers as a DefaultTreeModel.
   @param progress a Swing ProgressMonitor or null.
   */
  private DefaultTreeModel getTreeModel(JProgressBar monitor) {
     if (monitor != null) {
      // monitor.setMessage("Loading from "+getName());
     //  monitor.setNote("Retrieving headers...");
       monitor.setMinimum(0);
       monitor.setMaximum(getHeaderCount());
       monitor.setValue(0);
     }
     int count = 0;
     DefaultMutableTreeNode root = new DefaultMutableTreeNode(null);
     Enumeration enum = m_messages.keys();
     while (enum.hasMoreElements()) {
        root.add(new DefaultMutableTreeNode(enum.nextElement()));
        if (monitor != null) {
         count++;
         monitor.setValue(count);
        }
     }
     return new DefaultTreeModel(root);
  }

  /**
   * Test harness method.
   */
  public void doTest() {
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "***Folder.doTest");
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Folder name is "+getName());
     // Create two simple messages.
     MessageHeader headone = new MessageHeader();
     headone.setField("Subject", "Test Message");
     headone.setField("Message-Id", "1234");
     MessageBody bodyone = new MessageBody("This is a test message.");

     MessageHeader headtwo = new MessageHeader();
     headtwo.setField("Subject", "Re: Test Message");
     headtwo.setField("Message-Id", "4321");
     MessageBody bodytwo = new MessageBody("This is another test message.");

     // Store the two messages in this folder.
     saveMessage(headone, bodyone);
     saveMessage(headtwo, bodytwo);

     // Check duplicate messages
     saveMessage(headone, bodyone);

     // Display the subjects of all messages in this folder.
     try {
       // Vector v = getHeaders();
       Vector v = new Vector();
        Enumeration enum = v.elements();
        while (enum.hasMoreElements()) {
           MessageHeader h = (MessageHeader) enum.nextElement();
           if (Debug.TRACE_LEVEL_1) Debug.println(1, this, h.getFieldValue("Subject"));
        }
     } catch (Exception e) {
        if (Debug.TRACE_LEVEL_1) Debug.println(1, this, "Error getting headers.");
     }


  }

}

// Old Log:
// 0.1 [22/03/98]: Initial Revision
// 0.2 [31/03/98]: Changed to new MessageProvider interface. Added tree
//   model.
// 0.3 [01/04/98]: Implemented ensureConnected method.
// 0.4 [02/04/98]: Added toString(). Fixed minor bug with getTreeModel().
// 0.5 [04/04/98]: Implemented new MessageProvider methods (progress, max
//  etc.
// 0.6 [05/04/98]: Changed to use ProgressDialog
// 0.7 [08/04/98]: Changed to JProgressBar
// New Log:
// $Log: not supported by cvs2svn $
// Revision 1.3  1999/03/22 23:48:28  briand
// New headers
//