/*   NewsAgent: A Java USENET Newsreader
 *   Copyright (C) 1997-8  Brian Duff
 *   Email: bd@dcs.st-and.ac.uk
 *   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package dubh.apps.newsagent.nntp;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.zip.*;
import javax.swing.ImageIcon;
import dubh.utils.ui.StatusBar;

import dubh.apps.newsagent.dialog.ErrorReporter;
import dubh.apps.newsagent.dialog.LongOperationDialog;
import dubh.apps.newsagent.Folder;
import dubh.apps.newsagent.GlobalState;
/**
 * Deals with all hard disk storage of messages etc. There is normally only one
 * storage manager, it is initialised at startup, and can be obtained using
 * GlobalState.getStorageManager().<P>
 * Version History: <UL>
 * <LI>0.1 [22/03/98]: Initial Revision
 * <LI>0.2 [23/03/98]: Checked for duplicate servers
 * <LI>0.3 [25/03/98]: Fixed create folder to block null or zero length folders.
 *   caught an unhandled exception when deserializing servers.dat
 *   added connectIfNeeded method.
 * <LI>0.4 [31/03/98]: Added creation / deletion of server directory when adding
 *   or removing newsgroups.
 * <LI>0.5 [01/04/98]: Something got a bit confused in the addServer() folder
 *   creation code. I've taken out the code which checks to see if a folder
 *   exists before creating it, because the whole point was that the folder
 *   shouldn't exist prior to creating it. Oddly, this problem only showed up
 *   under UNIX!
 * <LI>0.6 [02/04/98]: Added enabling of the GoOffline Action from
 *   connectIfNeeded.
 * <LI>0.7 [04/04/98]: Added disconnectFromAllServers. Added nntpException().
 *   Added long operation dialog to connectIfNeeded, and multithreaded it so
 *   the UI updates properly while connecting.
 * <LI>0.8 [05/04/98]: Added getServerCache(), saveCaches(), loadCaches()
 * <LI>0.9 [06/04/98]: Multithreaded the servers serialisation routine.
 * <LI>0.10 [07/04/98]: Added progress dialog to cache serialisation routine.
 * <LI>0.11 [08/04/98]: Added exception handling for BadArticleException.
 * <LI>0.12 [16/01/99]: Quick fix for null pointer exception bug
 *</UL>
 @author Brian Duff
 @version 0.12 [16/01/99]
 */
public class StorageManager {

  /** Hashtable of nntpServers, indexed by hostname. */
  private Hashtable nntpServers;
  /** Hashtable of ServerCache objects, indexed by hostname-newsgroupname */
  private Hashtable nntpCaches;

  public StorageManager() {
     nntpServers = new Hashtable();
     nntpCaches = new Hashtable();
     // Restore nntpServers from serialised file.
     deserializeServers();
     // Restore caches from serialised file.
  //   deserializeCaches();
  }

  /**
   * Determines all the folders available for storing messages in.
   @return an Enumeration consisting entirely of Folder objects.
   */
  public Enumeration getFolders() {
     // all folders are subdirectories of GlobalState.foldersDir
     Vector folders = new Vector();
     File foldersDir = new File(GlobalState.foldersDir);
     String foldernames[] = foldersDir.list();
     // In case the user creates files or whatnot in this directory, only include
     // items which are really folders.
     for (int i=0;i<foldernames.length;i++) {
        File currentFile = new File(GlobalState.foldersDir+foldernames[i]);
        if (currentFile.isDirectory())
           folders.addElement(new Folder(foldernames[i]));
        else
           ErrorReporter.debug("Non-directory "+foldernames[i]+" found in "+GlobalState.foldersDir);
     }
     return folders.elements();
  }

  /**
   * Determines all the NNTPServers available to this application.
   @return an Enumeration consisting entirely of NNTPServer objects.
   */
  public Enumeration getServers() {
     return nntpServers.elements();
  }

  /**
   * Retrieves the active NNTPServer object corresponding to a given server
   * hostname.
   @param hostname The hostname of the server to retrieve.
   */
  public NNTPServer getServer(String hostname) {
     return (NNTPServer) nntpServers.get(hostname);
  }

  /**
   * Returns a File object corresponding to the directory in which the specified
   * NNTP Server object stores its cached header information.
   @param server The NNTPServer object to look up
   @return a File object which will be a directory.
   */
  public File getServerDirectory(NNTPServer server) {
     // the directory is GlobalState.serversDir+server.getHostName()+File.sep
     return new File(GlobalState.serversDir+File.separator+server.getHostName());
  }

  /**
   * Adds a server to the list of NNTPServers available to the application.
   * Normally called in the ServerOptionsPane when the user adds a new NNTP
   * Host. Has <b>no</b> effect on the properties file whatsover. Just makes
   * sure that the server is available to the rest of the application and
   * opens a socket connection to it. Creates a directory for caching message
   * headers for this server, based on its hostname.
   @param servername The hostname of the server. You should have already created
     entries in the user preference file containing information on this server
   */
  public void addServer(String servername, int port) throws  NNTPServerException,IOException {
     if (!doesServerExist(servername)) {
        // Create a new NNTPServer connection and try to connect to it.
        NNTPServer newServer = new NNTPServer(servername, port);
        nntpServers.put(servername, newServer);
        // Create a directory for storing headers for the server.
        File serverDir = getServerDirectory(newServer);
       // if (!serverDir.exists()) {
       //    ErrorReporter.debug("servers folder doesn't exist.");
       //    ErrorReporter.fatality("CantCreateDir", new String[] {serverDir.getAbsolutePath(), GlobalState.appName});
       // }
        ErrorReporter.debug("Creating directory for "+servername);
        serverDir.mkdir();
        /*if (!serverDir.mkdir()) {
           ErrorReporter.debug("Mkdirs failed.");
           ErrorReporter.fatality("CantCreateDir", new String[] {serverDir.getAbsolutePath(), GlobalState.appName});
        } */
     } else {
        ErrorReporter.debug("Server "+servername+" already exists. Not added.");
     }
  }

  public void addServer(String serverName) throws NNTPServerException , IOException {
     addServer(serverName, 119);
  }

  /**
   * Check if we are currently connected to a server. If not, prompt the user
   * and ask if they want to connect.
   @param server the NNTPServer to connect to
   @return true if the user decided to connect, and the connection was
     established.
   */
  public boolean connectIfNeeded(NNTPServer server) {
     if (!server.isConnected()) {
        if (ErrorReporter.yesNo("ServerConnect", new String[] {server.toString()})) {
           // connect to server. We do this in a separate thread so the UI
           // updates properly.
           final LongOperationDialog longop = new LongOperationDialog(GlobalState.getMainFrame(), true);
           final NNTPServer innerServer = server;

           longop.setLeftIcon(new ImageIcon(GlobalState.getImage("busyimage.gif")));
           longop.setText("Connecting to "+server+"...");
           Thread connectThread = new Thread() {
             public void run() {
               try {
                 innerServer.openConnection();
                 // redraw the folder tree panel.
                 GlobalState.getMainFrame().getFolderTreePanel().repaint();
                 // Enable the GoOffline Action.
                 // Disabled line below 19990116: Breaking stuff.
                 //GlobalState.getMainFrame().getAction("gooffline").setEnabled(true);
                // return true;
               } catch (IOException ie) {
                 ErrorReporter.error("IOExceptionConnect", new String[] {innerServer.toString()});
                 //return false;
                 // ErrorReporter.error("UnknownHostException", new String[] {server.getHostName()});
               } catch (NNTPServerException ne) {
                 nntpException(ne,
                   GlobalState.getResString("Action.Connecting"), innerServer.toString());
               } // try
               // hide the opdlg
               longop.setVisible(false);
               longop.dispose();
             } // run
           }; // Thread
           connectThread.start();
           /* Display the long operation dialog. Since its modal, this will
            * block until the connectThread makes it disappear.
            */
           longop.showAtCentreOfParent();
           longop.dispose();
           return true;   // OK, I'm lying, but since it's threaded, no choice.

        } else {
           // User doesnt want to connect.
           return false;
        }
     } else {
        // Already connected
        return true;
     }
  }

  /**
   * Removes a server from the list of NNTPServers available to the application.
   * Normally called in the ServerOptionsPane when the user removes an NNTP
   * host. Note: this method <b>doesn't</b> alter the properties file in any
   * way, just disconnects from the specified server and ensures it is no
   * longer available to the rest of the application.
   @param servername The hostname of the server.
   */
  public void removeServer(String servername) throws IOException {

     // Disconnect from the server
     NNTPServer theServer = (NNTPServer) nntpServers.get(servername);
     File serverdir = getServerDirectory(theServer);
     if (serverdir.exists() && serverdir.isDirectory()) {
        // Delete all files in the folder
        String files[] = serverdir.list();
        for (int i=0;i<files.length;i++) {
           File thisfile = new File(files[i]);
           thisfile.delete();
        }
        // Delete the folder. This will catch errors in the above loop, because
        // a folder can't be deleted if it contains any files.
        if (!serverdir.delete()) {
           ErrorReporter.debug("Unable to delete server header cache directory.");
        }
     } else {
        ErrorReporter.debug("Server dir doesn't exist, or isn't a directory: can't be deleted.");
     }
     theServer.closeConnection();
     // Remove the server from the hashtable
     nntpServers.remove(servername);
  }


  /**
   * Creates a folder which the user can store messages in. Displays an error
   * dialog if the folder couldn't be created.
   @param name the name of the folder
   */
  public void createFolder(String name) {
     // Check that the folder doesn't already exist
     if (name != null) {
        File newFolder = new File(GlobalState.foldersDir+name);
        if (newFolder.exists()) {
           ErrorReporter.error("CantCreateFolder", new String[] {name});
        } else {
           if (!newFolder.mkdir()) {
              ErrorReporter.error("CantCreateFolderUnknown", new String[] {name});
           }
        }
     }
  }

  /**
   * Deletes a folder and all the messages contained in it.
   @param folder The Folder object to delete.
   */
  public void deleteFolder(Folder folder) {
     File theFolder = folder.getFile();
     if (theFolder.exists()) {
        // First delete all the files in the folder.
        String[] files = theFolder.list();
        File temp;
        for (int i=0;i<files.length;i++) {
           temp = new File(theFolder, files[i]);
           if (!temp.delete()) {
              ErrorReporter.error("CantDeleteFolder", new String[] {folder.getName()});
              return;
           }
        }

        if (!theFolder.delete()) {
           ErrorReporter.error("CantDeleteFolder", new String[] {folder.getName()});
        }
     }
  }

  /**
   * Disconnect from all active server connections. Doesn't attempt to update
   * the UI or anything.
   */
  public void disconnectFromAllServers() {
       Enumeration enum = getServers();
       while (enum.hasMoreElements()) {
         try {
           ((NNTPServer)enum.nextElement()).closeConnection();
         } catch (IOException ioe) {
           ErrorReporter.debug("IO Exception disconnecting from a server in GoOffline");
         }
       }
  }

  /**
   * Tell the user an NNTP Exception occurred. Displays an error message of the
   * form: <BR>
   * Error while <action> to <server>\n <exception description>
   @param e The NNTPException which occurred
   @param action The action (-ing) which was happening.
   @param server The NNTPServer.
   */
  public void nntpException(NNTPServerException e, String action, NNTPServer server) {
   nntpException(e, action, server.toString());
  }

  /**
   * Tell the user an NNTPException occurred. Uses a string instead of an
   * NNTPServer, in case you happen to be using a provider. Argh.
   */
  public static void nntpException(NNTPServerException e, String action, String name) {
   String errText;
   if (e instanceof NNTPServerPermissionException)
     errText = GlobalState.getResString("StorageManager.NNTPPermissionException");
   else if (e instanceof NNTPServerArticleRefusedException)
     errText = GlobalState.getResString("StorageManager.NNTPArticleRefusedException");
   else if (e instanceof NNTPServerBadArticleException)
     errText = GlobalState.getResString("StorageManager.NNTPBadArticleException");
   else {
     ErrorReporter.debug("Unhandled NNTPException in StorageManager.nntpException: "+e);
     errText = GlobalState.getResString("StorageManager.UnknownError");
   }
   ErrorReporter.error("StorageManager.NNTPException", new String[] {
     action, name, errText});

  }

// Private methods

  /**
   * Deserialize (read in) the nntpServers.
   */
  private void deserializeServers() {
     File f = new File(GlobalState.serversFile);
     if (!f.exists())  {
        serializeServers();   // If file doesn't exist, create it.
     } else {
        try {
           FileInputStream fis = new FileInputStream(GlobalState.serversFile);
           GZIPInputStream gis = new GZIPInputStream(fis);
           ObjectInputStream in = new ObjectInputStream(gis);
           nntpServers = (Hashtable) in.readObject();
           in.close();
        } catch (IOException e) {
           ErrorReporter.debug("Unable to deserialize "+GlobalState.serversFile+" ("+e+")");
           ErrorReporter.fatality("CantDeserializeServers", new String[] {GlobalState.serversFile});
        } catch (ClassNotFoundException cnf) {
           ErrorReporter.debug("Unable to deserialize "+GlobalState.serversFile+" ("+cnf+")");
           ErrorReporter.fatality("CantDeserializeServers", new String[] {GlobalState.serversFile});
        } catch (Exception other) {
           ErrorReporter.debug("Unable to deserialize "+GlobalState.serversFile+" ("+other+")");
           ErrorReporter.fatality("CantDeserializeServers", new String[] {GlobalState.serversFile});

        }
     }
  }

  /**
   * Serialize (save) the nntpServers. Multithreaded; this routine returns
   * immediately, while the servers are serialized in a separate thread. This
   * method is synchronized (atomic)
   */
  public synchronized void serializeServers() {
    final Hashtable finalServers = nntpServers;
    Thread serialiserThread = new Thread() {
     public void run() {
       try {
        FileOutputStream fos = new FileOutputStream(GlobalState.serversFile);
        GZIPOutputStream gzos = new GZIPOutputStream(fos);
        ObjectOutputStream out = new ObjectOutputStream(gzos);
        out.writeObject(finalServers);
        out.flush();
        out.close();
       } catch (IOException e) {
        ErrorReporter.debug("Unable to serialize "+GlobalState.serversFile+" ("+e+")");
        ErrorReporter.error("CantSerializeServers", new String[] {GlobalState.serversFile});
       } // try
      } // run
     }; // thread object
     serialiserThread.start(); // fork the serialiser
  }

  /**
   * Determine whether a server already exists.
   */
  private boolean doesServerExist(String serverName) {
     Enumeration enum = nntpServers.keys();
     while (enum.hasMoreElements()) {
        if (((String)enum.nextElement()).equals(serverName))
           return true;
     }
     return false;
  }

  /**
   * Retrieve the cache associated with a given server and newsgroup.
   @param server an NNTPServer object
   @param newsgroup a Newsgroup object
   */
  public ServerCache getCache(NNTPServer server, Newsgroup newsgroup) {
   ServerCache luCache =
     (ServerCache)nntpCaches.get(server.getHostName()+"-"+newsgroup.getName());
   if (luCache == null) {
     // a newsgroup has been added that we don't know about.
     luCache = new ServerCache();
     nntpCaches.put(server.getHostName()+"-"+newsgroup.getName(),
       luCache);
   }
   return luCache;
  }

  /**
   * Serialise all cache objects. This will probably take some time for big
   * caches, so should only be called at the end of the program
   */
  public void serializeCaches() {
    // needsSaved
  /*  final ProgressDialog csProgress = new ProgressDialog(GlobalState.getMainFrame(), true);
    csProgress.setMinimum(0);
    csProgress.setMaximum(nntpCaches.size());
    csProgress.setProgress(0);
    csProgress.setMessage("Saving message caches...");
    csProgress.setNote("");  */
    Thread cacheSerThread = new Thread() {
     public void run() {
       Enumeration servers = getServers();
       StatusBar status = GlobalState.getMainFrame().getStatusBar();
       status.setText("Reading Cache...");
       status.setMinimumProgress(0);
       status.setProgress(0);
       status.setProgressVisible(true);
       GlobalState.getMainFrame().setNetworkActionsEnabled(false);
       int count = 0;
       while (servers.hasMoreElements()) {
         NNTPServer thisServer = (NNTPServer)servers.nextElement();
         Enumeration newsgroups = thisServer.getSubscriptions().elements();
         while (newsgroups.hasMoreElements()) {
           Newsgroup thisGroup = (Newsgroup)newsgroups.nextElement();
           ServerCache thisCache = getCache(thisServer, thisGroup);
           File cacheFile =
             new File(getServerDirectory(thisServer), thisGroup.getName());
           status.setProgress(count);
           status.setText("Saving cache for "+thisGroup.getName()+" on "+thisServer.getNiceName());
           serializeCache(cacheFile, thisCache);
           count++;
         }
       }
       status.setProgressVisible(false);
       GlobalState.getMainFrame().setNetworkActionsEnabled(true);
     }
    }; // thread
    cacheSerThread.start();

  }

  /**
   * Deserialise all cache objects. This will take a while, and is best done
   * at the start of the program. If no cache file exists for a server /
   * newsgroup, an empty cache is created.
   */
  public void deserializeCaches() {
    /*
     * Hack to get the number of servers*newsgroups we will have to go thru
     */
    Enumeration cservers = getServers();
    int count = 0;
    while (cservers.hasMoreElements()) {
     count = count + ((NNTPServer)cservers.nextElement()).getSubscriptions().size();
    }
   /* final ProgressDialog desProg = new ProgressDialog(GlobalState.getMainFrame(), true);
    desProg.setMessage("Reading cached messages...");
    desProg.setMaximum(count);
    */

    Thread desThread = new Thread() {
     public void run() {
       ErrorReporter.debug("Please wait while NewsAgent loads its cache...");
       int actualcount = 0;
       StatusBar status = GlobalState.getMainFrame().getStatusBar();
       status.setText("Reading Cache...");
       status.setMinimumProgress(0);
       status.setProgress(0);
       status.setProgressVisible(true);
       GlobalState.getMainFrame().setNetworkActionsEnabled(false);
       Enumeration servers = getServers();
       while (servers.hasMoreElements()) {
         NNTPServer thisServer = (NNTPServer)servers.nextElement();
         Enumeration newsgroups = thisServer.getSubscriptions().elements();
         while (newsgroups.hasMoreElements()) {
           Newsgroup thisGroup = (Newsgroup)newsgroups.nextElement();
           status.setText("Reading cache for "+thisGroup.getName()+" on "+thisServer.getNiceName());
           status.setProgress(actualcount);
           File cacheFile =
             new File(getServerDirectory(thisServer), thisGroup.getName());
           nntpCaches.put(thisServer.getHostName()+"-"+thisGroup.getName(),
             deserializeCache(cacheFile));
           actualcount++;
         }
       }
       status.setProgressVisible(false);
       status.setText("");
       GlobalState.getMainFrame().setNetworkActionsEnabled(true);
     } // run
    }; // thread
    desThread.start();
  //  desProg.setVisible(true);
  }

  /**
   * Serialise one cache object
   */
  private void serializeCache(File cacheFile, ServerCache cache) {
   if (cache.needsSaved()) {
     try {
       FileOutputStream fos = new FileOutputStream(cacheFile);
       //GZIPOutputStream gzos = new GZIPOutputStream(fos);
       ObjectOutputStream out = new ObjectOutputStream(fos);
       out.writeObject(cache);
       out.flush();
       out.close();
     } catch (IOException e) {
       ErrorReporter.debug("IO Error while serialising cache: "+e);
     }
   }
  }

  /**
   * Deserialise one cache object
   @return a deserialized ServerCache object, or a new ServerCache object if
     the cache couldn't be deserialized.
   */
  private ServerCache deserializeCache(File cacheFile) {
     if (!cacheFile.exists()) {

       return new ServerCache();
     }
     try {
       FileInputStream fis = new FileInputStream(cacheFile);
     //  GZIPInputStream gis = new GZIPInputStream(fis);
       ObjectInputStream in = new ObjectInputStream(fis);
       ServerCache cache = (ServerCache) in.readObject();
       in.close();
       return cache;
     } catch (IOException e) {
       ErrorReporter.debug("IO Error while deserialising cache: "+e);
     } catch (ClassNotFoundException cnf) {
       ErrorReporter.debug("Class Error while deserialising cache: "+cnf);
     } catch (Exception other) {
       ErrorReporter.debug("Unknown Error while deserialising cache: "+other);
     }
     return new ServerCache();
  }

  /**
   * Test harness method. Should create three folders, complain that it can't
   * create a folder with a silly name, complain that it can't
   * create one of them because it already exists, list all folders (with the
   * exception of a file in that directory, which it will print a debug message
   * about), then delete all the folders.
   */
  public void doTest() {
       ErrorReporter.debug("***StorageManager.doTest");
     // Create a few folders, list them and then delete them.
     createFolder("TestHarnessOne");
     createFolder("Another Folder");
     createFolder("Third Test Folder");
     // Try creating a folder with a silly name
     createFolder("(£*RH(//\\\\/*HVD::");
     // Try creating a duplicate folder
     createFolder("TestHarnessOne");
     // Create a file, to check that files are not included in the list of folders
     try {
        PrintWriter test = new PrintWriter(new FileOutputStream(GlobalState.foldersDir+"testFile.tst"));
        test.println("I am a file.");
        test.flush();
        test.close();
     } catch (IOException e) {
        ErrorReporter.debug("IO Error in StorageManager.doTest");
     }

     // List all the folders, and delete them

     Enumeration e = getFolders();
     while (e.hasMoreElements()) {
        Folder f = (Folder) e.nextElement();
        ErrorReporter.debug(f.getName());
        deleteFolder(f);
     }
     // Delete the file we created.
     File temp = new File(GlobalState.foldersDir+"testFile.tst");
     if (!temp.delete())
        ErrorReporter.debug("Can't delete test file");

     // Create some NNTPServers, and add them, then serialise.
     try {
        addServer("calvin.st-and.ac.uk");
        addServer("wiredsoc.ml.org");
        serializeServers();
     } catch (Exception er) {
        ErrorReporter.debug("An error occurred in StorageManager.doTest: "+er);

     }
     }

}