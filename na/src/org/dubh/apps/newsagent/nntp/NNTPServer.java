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

import dubh.apps.newsagent.GlobalState;
import dubh.apps.newsagent.dialog.ErrorReporter;
import javax.swing.tree.*;
import javax.swing.JProgressBar;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.text.*;
import dubh.utils.misc.StringUtils;
import dubh.utils.misc.Debug;

/**
 * Represents a News Server.<P>
 * Version History: <UL>
 * <LI>0.1 [21/11/97]: Basic Implementation Working. Commands not yet impl.
 * <LI>0.2 [03/02/98]: Added exception structure. Removed error messages. Seems
 *   to be a problem with the input stream....
 * <LI>0.3 [04/02/98]: Fixed input stream. Needs tidying up. There's an implicit
 *   assumption that the server will only return one line of status info after
 *   each command. This isn't the case with some servers, however (e.g. calvin).
 *   should keep eating until we are sure the message isn't a status message.
 * <LI>0.4 [05/02/98]: Fixed above bug. Added date conversion.
 * <LI>0.5 [18/02/98]: Added support for XOVER command, added getHeaders method.
 *	 added several (internal) string handling utilities, should probably move
 *	 these elsewhere...
 * <LI>0.6 [03/03/98]: Added support for message body retrieval. Might be a
 *	 performance problem, since we have to convert a vector to a String.
 * <LI>0.7 [23/03/98]: Made NNTPServer a MessageProvider. Effectively, I've
 *    removed the NNTPServerProvider class and just rolled it all into this class.
 *    eventually, I'll phase out some of the public interface of the NNTPServer
 *    class. Added getSubscriptions method.
 * <LI>0.8 [25/03/98]: Added subscription addition and removal and newsgroup
 *    updating. Added server date checking (rather than using the local machine's
 *    date).
 * <LI>0.9 [29/03/98]: Bug fix for NEWGROUPS implementation (sending bad date
 *   format).
 * <LI>0.10 [31/03/98]: Added header storage support.
 * <LI>0.11 [01/04/98]: Added ensureConnected implementation. Added a fix for
 *   zero-length line bug in getMessage(). Implemented getProviderName().
 * <LI>0.12 [02/04/98]: Fixed bug introduced in last version (dot appearing
 *   at the end of message text.
 * <LI>0.13 [03/04/98]: Implemented new MessageProvider methods getHeaders
 *   with a maximum number of items / progress monitor checking and
 *   getHeaderCount.
 * <LI>0.14 [04/04/98]: Added POSTing support. About time :) . Fixed
 *   getMessage() to make sure it stops reading if and only if there is one
 *   single dot on a line (before, it was just checking that the first char
 *   was a dot, which broke the connection if lines of text started with
 *   a dot --- urgh!)
 * <LI>0.15 [05/04/98]: UNIX has problems with the built in ProgressMonitor.
 *   now we're using my own, handrolled ProgressDialog.
 * <LI>0.16 [06/04/98]: Major change to header downloading: added caching
 *   support and therefore message threading too (at last). Added support
 *   for authorised (password protected) servers. Fixed getyymmdd to return
 *   GMT date format. Caught ArrayIndexOutOfBoundsException in constructXover-
 *   header, for badly formed Xover headers. Changed getBody so that it
 *   uses (long) Message-Id instead of numeric id for retrieval.
 * <LI>0.17 [30/04/98]: Removed assumption that DATE command is present on the
 *   server (the command is not specified in the RFC, but does seem to be
 *   implemented on some servers).
 * <LI>0.18 [06/06/98]: Added dubh utils import for StringUtils
 * <LI>0.19 [09/06/98]: Changed the output format for debugging (echo())
 *   slightly. Now attached streams recieve debugging of the form: <PRE>
 *      server.dns << LIST
 *      server.dns >> 215 List of newsgroups follows
 *   </PRE>
 * <LI>0.20 [10/06/98]: Added a serialVersionUID constant
 * <LI>0.21 [05/02/99]: Fix for bug reported by James Cooper (pixel@bitmechanic.com)
 *    using correct newlines should fix "hanging" problems with some UNIX
 *    servers. (this is an interim fix that should really be backported to
 *    1.0.3, since NNTPServer is soon to be retired.)
 *</UL>
 @author Brian Duff
 @version 0.21 [05/02/99]
 */
public class NNTPServer implements MessageProvider, Serializable {
  /*
   * The serial version of this (serializable) class. Update this when major
   * changes are made to the class.
   */
  static final long serialVersionUID = -3969201570343249343L;

// Static Variables. These are the NNTP Commands defined in the RFC.

   private static final String cmdQuit = "QUIT";
   private static final String cmdGroup = "GROUP";
   private static final String cmdList = "LIST";
   private static final String cmdNext = "NEXT";
   private static final String cmdNewgroups = "NEWGROUPS";
   private static final String cmdHead = "HEAD";
   private static final String cmdBody = "BODY";
   private static final String cmdXover = "XOVER";
   private static final String cmdDate  = "DATE";
   private static final String cmdPost  = "POST";
   private static final String cmdNewNews = "NEWNEWS";
   private static final String cmdAuthUser = "AUTHINFO USER";
   private static final String cmdAuthPass = "AUTHINFO PASS";


   public static transient PrintWriter debugStream=null;

// Protected Variables.

       private transient Socket nntpConnection; // TCP/IP Socket to NNTP Host
       private transient LineNumberReader is;   // Input stream
       private transient PrintWriter os;        // Output stream
       private String serverName;     // DNS of the News server
       private int portNumber;        // The port
       private transient String lastReply;      // The last status line the server sent us
       private transient PrintWriter m_out;     // Debug output stream
       protected Newsgroup currentGroup;	// The current newsgroup
       protected Vector subscriptions = new Vector(); // all subscribed newsgroup
       protected Vector allNewsgroups = new Vector(); // all newsgroups
       protected String lastNewsgroupCheck = null; // date newsgroups were last checked.
       private String m_niceName = "";
       private String m_userName = "";
       private String m_password = "";
       private boolean m_authenticate = false;
       transient private TreeModel headers=null; // Newsgroup -> TreeModel mapping
  /**
   * Creates a new NNTPServer and opens a socket connection to the specified
   * NNTP Host, using the default port (119) as specified in RFC977.
   @param serverName The DNS name of the server to connect to
   @throws java.net.UnknownHostException The DNS Name is unknown
   @throws java.io.IOException Could not connect to the server (network problem)
   */
  public NNTPServer(String serverName)
    throws IOException, UnknownHostException, NNTPServerException {
         this(serverName, 119);
  }

  /**
   * Creates a new NNTPServer, opening a socket connection to the specified
   * NNTP Host, using the specified port number
   @param serverName The DNS name of the server to connect to
   @param port The TCP/IP port to connect to (normally 119)
   @throws java.net.UnknownHostException The DNS Name is unknown
   @throws java.io.IOException Could not connect to the server (network problem)
   */
  public NNTPServer(String serverName, int port)
    throws IOException, UnknownHostException, NNTPServerException {
         this(serverName, port, true);
  }

  /**
   * Creates a new NNTPServer to the specified port, opening a socket
   * connection if required, or delaying connection until openConnection() is
   * called.
   @param connect set to true if you wish to connect to the nntp host now
   @throws java.net.UnknownHostException The DNS Name is unknown
   @throws java.io.IOException Could not connect to the server (network problem)
   @throws dubh.apps.newsagent.NNTPServerException an NNTP Protocol error
   */
   public NNTPServer(String serverName, int port, boolean connect)
     throws UnknownHostException, IOException, NNTPServerException {
          this.serverName = serverName;
          this.portNumber = port;
          this.m_out = null;
          currentGroup = null;
          if (connect) {
             openConnection();
          }
   }

   /**
    * Opens a connection to the NNTPServer.
    @throws java.net.UnknownHostException The DNS name is unknown.
    @throws java.io.IOException Could not connect to the server (network prob?)
    @throws dubh.apps.newsagent.NNTPServerException NNTP Error connecting
    */
   public void openConnection() throws UnknownHostException, IOException,
     NNTPServerException {
          nntpConnection = new Socket(serverName, portNumber);
          os = new PrintWriter(new OutputStreamWriter(nntpConnection.getOutputStream()));
          is = new LineNumberReader(new InputStreamReader(nntpConnection.getInputStream()));

          if (isConnected()) {
             getReply();   // Get the NNTP Server Version information
             authorise();  // Authorise if necessary
          }
   }

   public static void setDebugStream(PrintWriter p) {
     debugStream = p;
   }

   /**
    * Establishes authorisation with the server, if this is a secure NNTP
    * server.
    */
   private void authorise() throws NNTPServerException, IOException {
     if (isSecureServer()) {
       sendMessage(cmdAuthUser+" "+getLogin());
       getReply();
       sendMessage(cmdAuthPass+" "+getPassword());
       getReply();
     }
   }

   /**
    * Closes the current socket connection.
    @throws java.io.IOException Network or I/O Error
    */
   public void closeConnection() throws IOException {
          sendMessage(cmdQuit);
          if (isConnected()) {
             is.close();
             os.close();
             nntpConnection.close();
             is = null;
             os = null;
             nntpConnection = null;
          }
   }

   /**
    * Determines whether the socket connection is currently open
    @returns a boolean value indicating whether the connection is open
    */
   public boolean isConnected() {
          return (nntpConnection != null && is != null && os != null);
   }

   /**
    * Returns the last error that occurred.
    */
   public String getLastError() {
          return lastReply;
   }

   /* The following methods correspond to the NNTP Commands defined in
      RFC 977. They all may raise a (non fatal) NNTPServerException, which
      will specify the reason why the command could not be completed.*/

   /**
    * Sets the current article to the article with the specified ID.
    @id The message id (numeric or long) of the article to select
    @throws IOException an I/O error occurred
    @throws NNTPServerBadArticleException the article doesn't exist
    @throws NNTPServerBadNewsgroupException no current newsgroup
    @throws NNTPServerException other NNTP Protocol error
    */
   public void selectArticle(String id)
     throws NNTPServerException, IOException {
            sendMessage(cmdHead+" "+id);     // throws IOException
            getReply();                      // throws NNTPServerException
   }

   /**
    * Deletes a message from the headers of the current group. (Implements
    * MessageProvider).
    */
   public void deleteMessage(MessageHeader mhead) {

   }

   /**
    * Marks a message as read or unread. (IMplements MessageProvider).
    */
   public void setMessageRead(MessageHeader mhead, boolean read) {
     // not yet implemented!
   }

   /**
    * Sets the current article and retreives its header.
    @param The text or numeric message identifier for the required message
    */
   public MessageHeader getHeader(String msg_id) {
          return new MessageHeader();
   }

   /**
    * Retreives the header of the current message.
    */
   public MessageHeader getHeader() {
          return new MessageHeader();
   }

   /**
    * Retrieves the body of an NNTP Message. You should select a group first.
    * The header <b>must</b> contain a numerical id, or the article won't be
    * retrieved.
    @param head The header of the message you want to retrieve
    @returns a new MessageBody containing the body of the message
    @throws dubh.apps.newsagent.NNTPServerException  NNTP error
    @throws java.io.IOException a network or IO Error occurred.
    */
   public MessageBody getBody(MessageHeader head)
   	throws IOException, NNTPServerException {
      Vector v;
      String s = new String();
   		sendMessage(cmdBody+" "+head.getFieldValue("Message-Id"));
      getReply();	// Should be 222 Body
      v = getMessage();
      Enumeration enum = v.elements();
      while (enum.hasMoreElements()) {
      	s = s + (String) enum.nextElement() + "\n";
      }
      return new MessageBody(s);
   }

   /**
    * Gets the provider name for this server. For an NNTP Server, this is the
    * name of the current newsgroup, rather than the name of the server itself.
    @return The name of the current newsgroup, or the name of the server if
     no newsgroup is currently selected.
    */
   public String getProviderName() {
     if (currentGroup == null)
       return getNiceName();
     else
       return currentGroup.getName();
   }

   /**
    * Selects the next logical article in the current newsgroup.
    @throws java.io.IOException a network or IO Error occurred.
    @throws dubh.apps.newsagent.NNTPBadArticleException no next article
    @throws dubh.apps.newsagent.NNTPBadNewsgroupException no current group
    @throws dubh.apps.newsagent.NNTPServerException other NNTP error
    @deprecated
    */
   public void nextArticle() throws IOException, NNTPServerException {
          sendMessage(cmdNext);     // Throws IOException
          getReply();               // Throws NNTPServerException
   }

   /**
    * Changes the currently selected newsgroup.
    @param ng The newsgroup to select
    @throws java.io.IOException a network or IO Error occurred.
    @throws dubh.apps.newsagent.NNTPServerException Protocol error.
    */
   public void selectGroup(Newsgroup ng)
    throws IOException, NNTPServerException {
           // Serialize currently selected group's headers.
       //   serializeHeaders();
          sendMessage(cmdGroup+" "+ng.getName());
          getReply();           // Should just be a status message.
          currentGroup = ng;
          // The group reply message contains a string of the form:
          // 211 <count> <first> <last> <newsgroup-name>
          currentGroup.setArticleCount(StringUtils.stringToInt(getWord(lastReply, 2)));
          currentGroup.setFirstArticle(StringUtils.stringToInt(getWord(lastReply, 3)));
          currentGroup.setLastArticle(StringUtils.stringToInt(getWord(lastReply, 4)));
          // Deserialize new group's headers.
        //  headers = deserializeHeaders();

   }

   /**
    * Makes sure this NNTPServer is connected if necessary.
    */
   public boolean ensureConnected() {
     return GlobalState.getStorageManager().connectIfNeeded(this);
   }

   /**
    * Serialises the currently selected Newsgroup's header structure.
    */
   private void serializeHeaders() {
     try {
        FileOutputStream fos = new FileOutputStream(
           new File(GlobalState.getStorageManager().getServerDirectory(this),
           currentGroup.getName()));
        GZIPOutputStream gzos = new GZIPOutputStream(fos);
        ObjectOutputStream out = new ObjectOutputStream(gzos);
        out.writeObject(headers);
        out.flush();
        out.close();
     } catch (IOException e) {
        ErrorReporter.debug("NNTPServer.serializeHeaders(): Unable to serialize "+currentGroup.getName()+" ("+e+")");
        ErrorReporter.error("CantSerializeHeaders", new String[] {currentGroup.getName(), getHostName()});
     }
   }

   /**
    * Deserialise the currently selected Newsgroup's header structure.
    */
   private TreeModel deserializeHeaders() {
     File f = new File(GlobalState.getStorageManager().getServerDirectory(this),
           currentGroup.getName());
     if (!f.exists())  {
        serializeHeaders();   // If file doesn't exist, create it.
     } else {
        try {
           FileInputStream fis = new FileInputStream(f);
           GZIPInputStream gis = new GZIPInputStream(fis);
           ObjectInputStream in = new ObjectInputStream(gis);
           TreeModel theHeaders = (TreeModel) in.readObject();
           in.close();
           return theHeaders;
        } catch (IOException e) {
           ErrorReporter.debug("Unable to deserialize "+currentGroup.getName()+" ("+e+")");
           ErrorReporter.fatality("CantDeserializeHeaders", new String[] {currentGroup.getName()});
        } catch (ClassNotFoundException cnf) {
           ErrorReporter.debug("Unable to deserialize "+currentGroup.getName()+" ("+cnf+")");
           ErrorReporter.fatality("CantDeserializeHeaders", new String[] {currentGroup.getName()});
        } catch (Exception other) {
           ErrorReporter.debug("Unable to deserialize "+currentGroup.getName()+" ("+other+")");
           ErrorReporter.fatality("CantDeserializeHeaders", new String[] {currentGroup.getName()});

        }
     }
     return null;
   }

   /**
    * Retreives the list of newsgroups on the server.
    @returns a vector containing Newsgroup items, one for each newsgroup.
    @throws java.io.IOException a network or IO Error occurred.
    @throws dubh.apps.newsagent.NNTPServerException Protocol error.
    */
   private Vector listNewsgroups()
    throws IOException, NNTPServerException {
          sendMessage(cmdList);
          return getNewsgroupList();
   }

   /**
    * Asks the server what date and time it thinks it is.
    @returns a string in YYYYMMDDhhmmss format.
    */
   private String serverDate() throws IOException, NNTPServerException {
   /*
    * (0.17 BD): Changed this so that it catches NNTPServerCommandException
    * if the DATE command isn't implemented, and uses the clients system
    * date instead. This might be problematic for servers located in different
    * timezones from the client, but there's nothing I can do about that.
    */
    try {
     sendMessage(cmdDate);
     getReply();
     // Extract the 2nd part of the server's reply to us and return it.
     return (StringUtils.getWord(lastReply, 1));
    } catch (NNTPServerCommandException cmdex) {
     Calendar today = Calendar.getInstance();
     today.setTime(new Date());
     // construct a string of the format YYYYMMDDhhmmss
     return (constructDateString(today));
    }
   }

   /**
    * Constructs a YYYYMMDDhhmmss date string, given a calendar
    @param date a Calendar object
    */
   private String constructDateString(Calendar date) {
     String year  = StringUtils.intToString(date.get(Calendar.YEAR));
     String month = makeLengthTwo(StringUtils.intToString(date.get(Calendar.MONTH)+1));
     String day   = makeLengthTwo(StringUtils.intToString(date.get(Calendar.DATE)));
     String hour  = makeLengthTwo(StringUtils.intToString(date.get(Calendar.HOUR_OF_DAY)));
     String minute= makeLengthTwo(StringUtils.intToString(date.get(Calendar.MINUTE)));
     String second= makeLengthTwo(StringUtils.intToString(date.get(Calendar.SECOND)));
     return year+month+day+hour+minute+second;
   }

   /**
    * Packs the string with an extra 0 if its length < 2
    */
   private String makeLengthTwo(String str) {
     if (str.length() == 1) {
        return "0"+str;
     }
     return str;
   }

   /**
    * The DATE NNTP command returns a date of the form "yyyymmddhhmmss" (in GMT)
    * all other NNTP commands expect "yymmdd hhmmss" (in local time), or
    * "yymmdd hhmmss GMT" for GMT. This string converts the former to the
    * latter.
    */
   private String getyymmdd(String nntp) {
     return nntp.substring(2,8)+" "+nntp.substring(8)+" GMT";
   }

   /**
    * Retrieves a list of newsgroups added to the server since a specified
    * date.
    @param when a date in NNTP format. All newsgroups added on or after this date
    are returned.
    @returns a vector containing Newsgroup objects
    @throws java.io.IOException a network or IO Error occurred.
    @throws dubh.apps.newsagent.NNTPServerException Protocol error.
   */
   private Vector listNewGroups(String when)
    throws IOException, NNTPServerException {
           sendMessage(cmdNewgroups+" "+getyymmdd(when));
           return getNewsgroupList();
   }

   /**
    * Retrieves a tree of all MessageHeaders in the current newsgroup.
    */
   public TreeModel getHeaders()
   		throws IOException, NNTPServerException {

      return getHeaders(null);
   }

   /**
    * Retrieves a tree of the headers in the current newsgroup
    @param max The maximum number of headers to retrieve. The retrieved headers
    are not guaranteed to be the most recent.
    */
   public TreeModel getHeaders(int max)
     throws IOException, NNTPServerException {
      return getHeaders(null, max);

   }

   /**
    * Retrieves a tree of the headers in the current newsgroup.
    @param monitor A Swing ProgressMonitor dialog which will be used to show
    how far along the operation is.
    @return a tree of headers
    */
   public TreeModel getHeaders(JProgressBar monitor)
     throws IOException, NNTPServerException {
      ServerCache myCache = GlobalState.getStorageManager().getCache(this,
             currentGroup);
      if (!myCache.hasEverDownloaded()) {
         sendMessage(cmdXover+" "+currentGroup.getFirstArticle()+
                            "-"+currentGroup.getLastArticle());
         getReply();	// Should be 211 Article List Follows

         myCache.addHeaders(getHeaderList(monitor), serverDate());
         return myCache.getHeaders();
      } else {
         return downloadNewNews(myCache);
      }

   }

   /**
    * Retrieves a tree of the headers in the current newsgroup.
    @param monitor A ProgressDialog  which will be used to show
    how far along the operation is.
    @param max The maximum number of headers to retrieve. The retrieved headers
    are not guaranteed to be the most recent.
    */
   public TreeModel getHeaders(JProgressBar monitor, int max)
     throws IOException, NNTPServerException {
       return getHeaders(monitor);
   }

   /**
    * Retrieves only new articles from the server, adds them to the cache,
    * and returns the header tree.
    */
   private TreeModel downloadNewNews(ServerCache cache)
     throws NNTPServerException, IOException {
     /*
      * Can't use Xover, because there is no way of just asking for new articles
      * with xover, so we use newnews with the last cache update timestamp.
      * newnews returns a simple list of Msg-Ids: have to then get the head
      * of each msg-id in turn for its other fields. newnews seems to be
      * quite slow on some nntp servers.
      */
     // Ask the server for new news
     sendMessage(cmdNewNews+" "+currentGroup.getName()+" "+
                getyymmdd(cache.getLastUpdate()));
     getReply();
     Vector ids = getMessage(); // dot terminated list of message-ids
     // for each id, get the header, and construct a MessageHeader out of it
     Vector headers = new Vector();
     Enumeration emid = ids.elements();
     while (emid.hasMoreElements()) {
       sendMessage(cmdHead+" "+(String)emid.nextElement());
       getReply();     // mustn't forget this ;)
       headers.addElement(new MessageHeader(getMessage()));
     }
     cache.addHeaders(headers, serverDate());
     return cache.getHeaders();
   }


   /**
    * Returns a count of the number of messages in the current newsgroup. This
    * returns the actual number of messages on the 
    @return an integer count of the number of messages.
    */
   public int getHeaderCount() {
     if (currentGroup != null)
       return currentGroup.getArticleCount();
     else
       return 0;
   }

   // POST
   public void postArticle(MessageHeader head, MessageBody body)
     throws IOException, NNTPServerException {
     // finally got round to implementing this :)
     // need to dump the headers first, then a blank line then the body. I think.
     // First, send a POST command.
     sendMessage(cmdPost);
     getReply();
     os.print(head.toString());
     os.println("");
     os.println(body.getText());
     os.println(".");
     os.flush();
     getReply();

   }


   /**
    * Attatches an output stream to the server connection. All commands sent
    * to the server and all _status_ responses (i.e. not message text or lists)
    * is echoed to this stream if it exists.<P>
    * From NewsAgent 1.02, this method is no longer used to attach a debugging
    * stream to a news server: the NewsAgent public static pwDebugNet field
    * is used directly (if the NewsAgent.flagDebugNet p.s.  field is true).
    * Streams can still be attached to NNTPServer objects, though.
    @param p A PrintWriter stream to attatch to the server connection. Use
    <code>new PrintWriter(System.out)</code> to specify stdout for example.
    */
    public void attachStream(PrintWriter p) {
           m_out = p;
    }

    /**
     * Disconnects an output stream previously attatched by attachStream.
     @see dubh.apps.newsagent.NNTPServer#attachStream
     */
    public void detachStream() {
           m_out = null;
    }

    /**
     * Returns an enumeration of Newsgroups the user is
     * subscribed to on this server.
     */
    public Vector getSubscriptions() {
     return subscriptions;

    }

    /**
     * Subscribes to a Newsgroup.
     @param n The newsgroup to subscribe to. This must exist on the server.
     */
    public void addSubscription(Newsgroup n) {
     if (allNewsgroups.contains(n) && !subscriptions.contains(n)) {
        subscriptions.addElement(n);       
     }
    }

    /**
     * Unsubscribes from a Newsgroup.
     @param n The newsgroup to unsubscribe from. This must exist in the list
     of subscribed newsgroups.
     */
    public void removeSubscription(Newsgroup n) {
     if (subscriptions.contains(n)) {
        subscriptions.removeElement(n);
     }
    }

    /**
     * Sets the "nice" name for this server.
     */
    public void setNiceName(String name) {
     m_niceName = name;
    }

    /**
     * Gets the "nice" name for this server.
     */
    public String getNiceName() {
     return m_niceName;
    }

    /**
     * Sets whether the user must authenticate (login) to this server.
     */
    public void setSecureServer(boolean b) {
     m_authenticate = b;
    }

    /**
     * Determines whether the user must authenticate (login) to this server.
     */
    public boolean isSecureServer() {
     return m_authenticate;
    }

    /**
     * Sets the login name and password for this server. This only makes      
     * if the server is a secure server.
     @see dubh.apps.newsagent.NNTPServer.isSecureServer()
     @param login The login name to use
     @param passwd The (unencrypted) password to use
     */
    public void setLoginInfo(String login, String passwd) {
     m_userName = login;
     m_password = passwd;
    }

    /**
     * Gets the login name used for this server. Only makes sense if the server
     * is secure.
     @see dubh.apps.newsagent.NNTPServer.isSecureServer()
     */
    public String getLogin() {
     return m_userName;
    }

    /**
     * Gets the <b>unencrypted</b> password for this server. Only makes sense if
     * the server is secure.
     @see dubh.apps.newsagent.NNTPServer.isSecureServer()
     */
    public String getPassword() {
     return m_password;
    }

    /**
     * Retrieves the hostname of this server.
     */
    public String getHostName() {
     return serverName;
    }

    /**
     * Gets the port of this server.
     */
    public int getPort() {
     return portNumber;
    }

    /**
     * Sets the port to use for this server. This <b>doesn't</b> close any
     * existing socket connection.
     @param port The integer port number to use.
     */
    public void setPort(int port) {
     portNumber = port;
    }

    /**
     * Returns the last Date on which the newsgroup list was updated, or null
     * if the Newsgroup list has never been downloaded from the server.
     */
    public String getLastNewsgroupUpdate() {
     return lastNewsgroupCheck;
    }

    /**
     * Forces the object to connect to the server and retrieve the name of
     * newsgroups added since the last update. If the newsgroup list has
     * never been generated, this method will download all newsgroups and
     * return them, otherwise, the new newsgroups are added to the list
     * of new groups, and the new groups are returned.
     @return a Vector of Newsgroup objects.
     */
    public Vector getNewNewsgroups() throws IOException, NNTPServerException {
       if (getLastNewsgroupUpdate() == null) {
           // Newsgroup list has never been downloaded.
           allNewsgroups = listNewsgroups();   // Might be a lengthy operation.
           lastNewsgroupCheck = serverDate();
           return allNewsgroups;
       } else {
           // Download new newsgroups since last update, append them to list
           Vector newGroups = listNewGroups(getLastNewsgroupUpdate());
           appendNewsgroups(newGroups);
           lastNewsgroupCheck = serverDate();
           return newGroups;
       }

    }

    /**
     * Returns all the existing newsgroups available from this server. Doesn't
     * connect to the server, so should be quite fast. You should call
     * getNewNewsgroups first to make sure all available newsgroups are
     * included. Returns an empty vector if the newsgroup list has never been
     * downloaded.
     @return a Vector of Newsgroup objects
     */
    public Vector getAllNewsgroups() {
        if (getLastNewsgroupUpdate() == null) {
           return new Vector();
        } else {
           return allNewsgroups;
        }
    }

    /**
     * Returns the "nice" name of this server.
     */
    public String toString() {
     return getNiceName();
    }

// Private Methods

  /**
   * Appends a vector of Newsgroup objects to the server's list of newsgroups.
   */
  private void appendNewsgroups(Vector newGroups) {
     Enumeration enum = newGroups.elements();
     while (enum.hasMoreElements()) {
        allNewsgroups.addElement(enum.nextElement());
     }
  }

   /**
    * Retreives a list of message headers from the server. Only to be used for
    * actually downloading all headers from a newsgroup the first time.
    @param monitor A progress monitor to use, or null if none specified.
    @returns a vector of Headers.
    */
   private synchronized Vector getHeaderList(JProgressBar monitor) throws
   	IOException, NNTPServerException {
     // Will eventually change this to use our own tree and thread as it goes.
			boolean inList= false;
    	String temp = " ";
     int count = 0;
     Vector collection = new Vector(currentGroup.getArticleCount());
     if (monitor != null) {
     //  monitor.setMessage("Connected to "+getNiceName());
      // monitor.setNote("Downloading headers...");
       monitor.setMinimum(0);
       monitor.setMaximum(currentGroup.getArticleCount());
       monitor.setValue(0);
     }

      while(temp.charAt(0) != '.') {
      	temp = is.readLine();             // throws IOException
        if (inList) {
        	if (temp.charAt(0) != '.') {
           MessageHeader hd = constructXoverHeader(temp);
           if (hd != null) collection.addElement(hd);
         }
        } else {
        	char firstchar = temp.charAt(0);
          inList = true;
          if (temp.charAt(0) != '.') {
           MessageHeader hd = constructXoverHeader(temp);
           if (hd != null) collection.addElement(hd);
          }
        }
        if (monitor != null) {
         count++;
         monitor.setValue(count);
        }
      }
      return collection;
   }

   /**
    * Constructs a threaded tree of message headers.
    @param collection a Vector of message headers.
    @return a tree of message headers.
    @deprecated
    */
   private TreeModel makeThreadTree(Vector collection) {
     // not yet implemented, just return all the messages in the root.
     DefaultMutableTreeNode root = new DefaultMutableTreeNode(null);
     DefaultTreeModel model = new DefaultTreeModel(root);
     Enumeration enum = collection.elements();
     while (enum.hasMoreElements())
        root.add(new DefaultMutableTreeNode(enum.nextElement()));
     return model;
   }

   /**
    * Converts a date from a java Date object to an NNTP date specifier of
    * the form yyMMdd hhmmss
    @param d The Date object to be converted
    @returns a string containing a date of the form yyMMdd HHmmss
    */

   private String convertDate(Date d) {
           SimpleDateFormat df = new SimpleDateFormat("yyMMdd HHmmss");
           return df.format(d);
   }

   /**
    * Sends a message through the socket.
    @param mesg The text of the message to send
    @throws java.io.IOException An I/O or network error occurred
    */
   private synchronized void sendMessage(String mesg) throws IOException {
           if (isConnected()) {
              echo(getNiceName()+" << "+mesg);
              os.println(mesg);
              os.flush();
           }
   }



   /**
    * makes a header based on an xover line
    */
   private MessageHeader constructXoverHeader(String xoverline) {
    /* the xover header format is:
    artid|subject|From|Date|Message-Id|References|bytecount|linecount|[opt]*
     where | is a tab and opt represents optional headers.
    */
   	MessageHeader h = new MessageHeader();
    Vector v = getTabSubstrings(xoverline);
    // should check length of v here.
    try {
    // h.setID(Integer.parseInt((String)v.elementAt(0)));  don't use the ID anymore
   	h.setField("Subject", (String)v.elementAt(1));
     h.setField("From", (String)v.elementAt(2));
     h.setField("Date", (String)v.elementAt(3));
     h.setField("Message-Id", (String)v.elementAt(4));
     h.setField("References", (String)v.elementAt(5));
    } catch (ArrayIndexOutOfBoundsException e) {
     ErrorReporter.debug("constructXoverHeader: Malformed xover header. Message ignored. Header was\n++"+xoverline);
     return null;
    } catch (NumberFormatException nformat) {
     ErrorReporter.debug("constructXoverHeader: Malformed xover header (number format). Message ignored. Header was\n++"+xoverline);
     return null;
    }
    //h.setField("Lines", (String)v.elementAt(7)); Not implemented sometimes.
    // ignore optional headers for now
    return h;
   }

   /**
    * Retreives a list of newsgroups from the server. Similar to getMessage().
    @returns A vector of Newsgroups.
    @throws java.io.IOException An I/O or network error occurred
    @throws dubh.apps.newsagent.NNTPServerException an NNTP Error
    */
   private synchronized Vector getNewsgroupList() throws
     IOException, NNTPServerException {
     /* We also need to check for status messages. We can safely assume these
       are going to occur at the start of the list, and we can exploit this
       fact to make the code more efficient.
     */
           boolean inList= false;
           String temp = " ";
           Vector collection = new Vector(100);

           while(temp.charAt(0) != '.') {
             temp = is.readLine();             // throws IOException
             if (inList) {
                collection.addElement(new Newsgroup(temp));
             } else {
                char firstchar = temp.charAt(0);
                if (firstchar < '1' || firstchar > '4') {
                   inList = true;
                   collection.addElement(new Newsgroup(temp));
                } else {
                   /* Newsgroups can't start with numbers, so this must be
                      a status message. We should throw an exception if its
                      an error. */
                   throwExceptions(temp);
                }
             }
           }
           // We must remove the last element (the dot). This is more efficient
           // Than testing inside the loop.
           collection.removeElementAt(collection.size()-1);
           return collection;
   }

   /**
    * Retreives a list of data from the server. This should be called in
    * response to status codes which say "data to follow". The data is always
    * period terminated. We assign the data into a Vector and return it.
    @returns A vector of Strings. Each is a line of text. The period is not included.
    @throws java.io.IOException An I/O or network error occurred
    */
    private synchronized Vector getMessage() throws IOException {

            String temp = " ";
            Vector collection = new Vector(100);
            boolean finished = false;
             while(!finished) {
               temp = is.readLine();             // throws IOException
               //System.out.println("+"+temp);
               collection.addElement(temp);
               if (temp.length() < 1) temp = " "; // So we dont get an error
               if (temp.length() == 1 && temp.charAt(0) == '.')
                 finished = true;

             }
             // We must remove the last element (the dot). This is more efficient
             // Than testing inside the loop.
             collection.removeElementAt(collection.size()-1);

            return collection;
    }

   /**
    * Recieves a reply from the socket.
    @returns A vector of lines from the reply. May be null.
    @throws java.io.IOException An I/O or network error occurred
    @throws dubh.apps.newsagent.NNTPServerException an NNTP Protocol error
    */
   private synchronized Vector getReply() throws IOException,
     NNTPServerException {
        String message="";
        String temp = " ";
        Newsgroup blah;
        Vector collection = new Vector(100);
           if (isConnected()) {
             lastReply = is.readLine();        // Read the status line
             echo(getNiceName()+" >> "+lastReply);
             throwExceptions(lastReply);       // Throw an error if we have to
			        message = new String();
             if (getStatusNumber(lastReply) == 215) { // Here come the groups
                collection = getMessage();
                return collection;
             }

           }
       return null;
   }


   /**
    * getStatusNumber returns the status identifier for the status message
    * sent by the server.
    @param status The status line
    @returns an integer value corresponding to the 3 digit status number
    @throws dubh.apps.newsagent.NNTPServerException an NNTP Protocol error
    */
    private int getStatusNumber(String status) throws NNTPServerException {
            int statcode;
            if (status == null)
               throw new NNTPServerInternalException("Null status string");
            if (status.length() < 3) // Status must have a 3 digit code...
               throw new NNTPServerInternalException("Status String is too short");

            // Convert the status code to an int so we can use a switch
            try {
                statcode = Integer.parseInt(status.substring(0,3));
            } catch (NumberFormatException e) {
                throw new NNTPServerInternalException("Status line corrupt");
            }
            return statcode;
    }

   /**
    * throwExceptions throws any necessary exceptions resulting from the
    * reply status code from the NNTP Server.
    @param status The status line to check for exceptions
    @throws dubh.apps.newsagent.NNTPServerException an NNTP Protocol error
    */
    private void throwExceptions(String status) throws NNTPServerException {

            int statcode = getStatusNumber(status);
            switch(statcode) {
            case 400:      // NNTP Server has discontinued service
                 throw new NNTPServerDiscontinuedException(status);
            case 500:
			       case 501:      // NNTP command syntax error
                 throw new NNTPServerCommandException(status);
            case 502:      // Permission Denied
                 throw new NNTPServerPermissionException(status);
            case 503:      // Server fault
                 throw new NNTPServerFaultException(status);
            case 420:
            case 421:
			       case 422:
			       case 423:
			       case 430:      // Bad article
                 throw new NNTPServerBadArticleException(status);
            case 411:
			       case 412:      // Bad newsgroup
                 throw new NNTPServerBadNewsgroupException(status);
            case 437:
			       case 440:
			       case 441:      // POST rejected
                 throw new NNTPServerArticleRefusedException(status);
            default:
                 if (statcode >= 400)  // codes > 400 are errors
					          throw new NNTPServerInternalException(status);
            }

    }

    /**
     * Sends a message to any
    * attached stream, as well as the network debug stream configured in the
    * NewsAgent class.
     @param s A string to send to the output stream if it is attached.
     */
    private void echo(String s) {
            if (m_out != null) {
               m_out.println(s);
            }
            if (debugStream != null) {
              debugStream.println(s);
            } 

    }

    /**
     * Given a string with several elements separated by spaces, retrieves
     * a specified element.
     @param s A string containing several substrings s separated by spaces.
     @param n The index of the substring to retrieve.
     @returns the nth word in s
     */
    private String getWord(String s, int n) {
      int curpos;
      int endpos;
      int wordnumber=1;
			BreakIterator bi = BreakIterator.getWordInstance();
      // in breakiterator, spaces count as words...
      bi.setText(s);
      curpos = bi.first();
      while (curpos != BreakIterator.DONE && wordnumber < n) {
      	curpos = bi.next();
        if (curpos > s.length()-1) return "";
        if (s.charAt(curpos) != ' ') wordnumber++;
      }
      // curpos is the start location of the word, now determine the end.
      endpos = bi.next();
      if (endpos == BreakIterator.DONE) endpos = s.length();
      return s.substring(curpos, endpos);
    }

    /**
     * returns a Vector of Strings culled from a string containing several
     * substrings separated by tab characters.
     */
    public Vector getTabSubstrings(String s) {
      Vector v = new Vector(10);
    	int curpos = 0;
      int nexttab = s.indexOf('\t');
      if (nexttab == 0) {		// s has only one element.
      	v.addElement(s);
        return v;
      }
      while (nexttab > 0) {
      	v.addElement(s.substring(curpos, nexttab));
        curpos = nexttab+1;
        nexttab = s.indexOf('\t', curpos);
    	}
      v.addElement(s.substring(curpos, s.length()));
      return v;
    }

   /**
    * Test Method.
    * Remove this in the final version.
    */
   public void doTest() throws IOException, NNTPServerException {
     Calendar test = Calendar.getInstance();
     test.setTime(new Date());
     System.out.println(constructDateString(test));
     
   }

}