/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
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

package dubh.utils.test;


import java.io.*;
import java.net.*;

/**
 * Utility class that provides an stdio / stdin interface to a TCP/IP socket
 * connection.<P>
 * Version History: <UL>
 * <LI>0.1 [05/06/98]: Initial Revision
 * <LI>0.2 [26/01/99]: Removed calls to Thread.stop(), because this is
 *      unsafe, and is deprecated in Java2 (this class is still 1.1
 *      compatible...)
 *</UL>
 @author Brian Duff
 @version 0.2 [26/01/99]
 */
public class SocketTalker {
  private RunnableSocketTalker talker;

  public SocketTalker(String host, int port, boolean client) {
     if (client) talker = new ClientSocketTalker(host, port);
     else talker = new ServerSocketTalker(port);
  }

  public void run() {
     talker.openConnection();
     talker.run();
  }

  /**
   * Application's main program. Arguments to the program are the TCP/IP host
   * name and port number to connect to.
   */
  public static void main(String args[]) {
     int portNum=0;
     SocketTalker me;

     if (args.length < 2) {
        System.err.println("Usage: SocketTalker -client <hostname> <port> | -server <port>");
        System.exit(1);
     }



     if (args[0].equals("-client")) {
        if (args.length < 3) {
           System.err.println("Usage: SocketTalker -client <hostname> <port> | -server <port>");
           System.exit(1);
        }
        try {
           portNum = Integer.parseInt(args[2].trim());
        } catch (NumberFormatException e) {
           System.err.println(args[2]+" is not a valid port number.");
           System.exit(1);
        }
        me = new SocketTalker(args[1], portNum, true);
        me.run();
     } else if (args[0].equals("-server")) {
        try {
           portNum = Integer.parseInt(args[1].trim());
        } catch (NumberFormatException e) {
           System.err.println(args[1]+" is not a valid port number.");
           System.exit(1);
        }
        me = new SocketTalker("", portNum, false);
        me.run();
     } else {
        System.err.println("Usage: SocketTalker [-server | -client] <hostname> <port>");
        System.exit(1);
     }


  }

  /**
   * A ClientSocketTalker connects to a client
   */
  class ClientSocketTalker extends RunnableSocketTalker {
     private Socket clientSock;
     protected String m_host;
     protected int    m_port;
     
     public ClientSocketTalker(String host, int port) {
        setHost(host, port);
     }


     /**
      * Open a connection with our client
      */
     public void openConnection() {
        try {
           clientSock = new Socket(m_host, m_port);
           //clientSock.setSoTimeout(100);
           m_in   = clientSock.getInputStream();
           m_out  = clientSock.getOutputStream();
        } catch (UnknownHostException exHost) {
           System.err.println("Unknown Host: "+m_host);
        } catch (IOException exIO) {
           System.err.println("IOException: "+m_host+" "+m_port);
        } // try
     }  // openConnection

     public void closeConnection() {
        try {
           stopReaders();
           m_in.close();
           m_out.close();
           clientSock.close();
        } catch (IOException ex) {
           System.err.println("Can't close socket connection");
        } // try

     }  // closeConnection

     public void setHost(String hostname, int port) {
        m_host = hostname;
        m_port = port;
     }  // setHost


  } // ClientSocketTalker

  /**
   * A ServerSocketTalker waits for a connection from a client and then
   * begins a conversation with the client. The openConnection() method
   * <b>blocks</b> until a client connects.
   */
  class ServerSocketTalker extends RunnableSocketTalker {
     private ServerSocket servSockListener;
     private Socket servSocket;
     private int m_port;

     public ServerSocketTalker(int port) {
        m_port = port;
     } // constructor


     public void openConnection() {
        try {
           servSockListener = new ServerSocket(m_port);
           servSocket = servSockListener.accept();
           if (servSocket != null) {
              m_in = servSocket.getInputStream();
              m_out = servSocket.getOutputStream();
           }
           System.out.println("Connection from "+servSocket.getInetAddress());
        } catch (IOException e) {
           System.err.println("IOException: "+e);
        }
     } // openConnection

     public void closeConnection() {
        try {
           stopReaders();
           m_in.close();
           m_out.close();
           servSocket.close();
        } catch (IOException ex) {
           System.err.println("Can't close socket connection");
        } // try

     }  // closeConnection

  } // ServerSocketTalker


  abstract class RunnableSocketTalker implements Runnable {

     protected InputStream m_in;
     protected OutputStream m_out;
     StreamProxy input;
     StreamProxy output;

     public void run() {
        input = new StreamProxy(m_in, System.out);
        output = new StreamProxy(System.in, new PrintStream(m_out));
        input.start();
        output.start();   
     } // run()


     public void openConnection() {;}
     public void closeConnection() {;}

     protected void stopReaders() {
        input.setRunning(false);
        output.setRunning(false);   
     }  // stopReaders

  }  // RunnableSocketTalker

  /**
   * A thread which continually reads input from an InputStream (a line at a time)
   * and then writes it to an output stream.
   */
  class StreamProxy  extends Thread {
     private LineNumberReader m_in;
     private PrintStream m_out;
     
     private boolean m_isRunning;

     public StreamProxy(InputStream ins,PrintStream  outs) {
        m_in = new LineNumberReader(new InputStreamReader(ins));
        m_out = outs;
     }  // Constructor


     /**
      * Added to prevent use of stop(), which is unsafe and deprecated
      * in JDK 1.2
      */
     public synchronized void setRunning(boolean b)
     {
        m_isRunning = b;  
     }
     
     /**
      * Runs the input reader
      */
     public void run() {
        String inString = "";
       // String outString = "";

        while(inString != null && m_isRunning) {
           try {
              inString = m_in.readLine();
              m_out.println(inString);
           } catch (IOException ex) {
              System.err.println("IO Exception in InputReader:"+ex);
              inString = null;
           }  // try
           if (m_in == null || m_out == null) setRunning(false);
        }  // while 
     }  // run

  }  // InputReader

}  // SocketTalker