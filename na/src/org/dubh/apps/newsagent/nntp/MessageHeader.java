// ---------------------------------------------------------------------------
// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: MessageHeader.java,v 1.4 1999-11-09 22:34:42 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://www.btinternet.com/~dubh/dju
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

package org.javalobby.apps.newsagent.nntp;

import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import org.javalobby.dju.misc.StringUtils;

/**
 * MessageHeader - the header of an NNTP news message
 * Version History: <UL>
 * <LI>0.1 [05/02/98]: Initial Revision: various methods impl. Bug note: Field
 *     names should probably be case *insensitive*, and are case sensitive in
 *     this implementation.
 * <LI>0.2 [06/02/98]: Fixed case sensitivity issue. Added getRealName, toString
 *     Implemented isValid, setField
 * <LI>0.3 [18/02/98]: Added IDs
 * <LI>0.4 [22/03/98]: Added equals() override, so that duplicate messages in
 *     folders are avoided.
 * <LI>0.5 [31/03/98]: Stripped quotes from names in getRealName().
 * <LI>1.1 [28/04/98]: Added Agent support
 * <LI>1.2 [29/04/98]: Added setRead / isRead
 * <LI>1.3 [06/06/98]: Added dubh utils import for StringUtils
 * <LI>1.4 [10/06/98]: Improved normaliseCaps so that it actually makes headers
 *   caseless
 * <LI>1.5 [14/06/98]: Added removeField()
 * </UL>
 @author Brian Duff
 @version 1.5 [14/06/98]
 */
public class MessageHeader implements Serializable {

// Private constants

      // private static final int uplo = 32;   // 'a' - 'A'

// Protected instance variables

       /** The NNTP ID of this message. Now defunct. */
           protected int serverID;

// Private instance variables
       /** The font to display in */
       private Font m_font;
       /** The foreground colour to display in when unselected*/
       private Color m_forecolor;
       /** The background colour to display in when unselected*/
       private Color m_backcolor;
       /** The fields of this header */
       private Hashtable fields;
       /** Whether this message has been read by the user */
       private boolean m_isRead;

       /**
        * Constructs a new, blank message header.
        */
       public MessageHeader() {
              fields = new Hashtable();
              /* Default fonts and colours */
              m_font = UIManager.getFont("Dialog");
              m_forecolor = UIManager.getColor("textText");
              m_backcolor = Color.white;
              m_isRead = false;
       }

       /**
        * Constructs a new message header, given a collection of headers.
        @param v A vector of String objects each corresponding to a header and
                 of the form Header-name: header-value
        @throws java.lang.IllegalArgumentException if any of the Vector items
                are not Strings.
        */
       public MessageHeader(Vector v) {
              this();
              setFields(v);
       }

       /**
        * Constructs a new message header from a line containing the message
        * headers in xover format.
        @param s One line returned by the NNTP xover command.
        */
       public MessageHeader(String s) {
         // parsing goes here
       }

       /**
        * Constructs a new message header as a reply to another message
        @param originalPost The header of the message which this header will
        identify a reply to
        */
       public MessageHeader(MessageHeader originalPost) {
              this();
       }

       /**
        * Checks whether the header conforms to RFC 1036. To do so, it must
        * contain certain required fields:
        *         From, Date, Newsgroups, Subject, MessageId, Path
        @returns a boolean value indicating conformance to RFC 1036
        */
       public boolean isValid() {
              return (hasField("From") && hasField("Date") &&
                      hasField("Newsgroups") && hasField("Subject") &&
                      hasField("Message-Id") && hasField("Path"));
       }

       /**
        * Returns the contents of the specified header field.
        @param fieldName the name of the field (case sensitive)
        @returns the contents of the field
        @throws java.lang.IllegalArgumentException if the field doesn't exist
        */
       public String getFieldValue(String fieldName)
       throws IllegalArgumentException {
                if (hasField(fieldName)) {
                   return (String)fields.get(normaliseCaps(fieldName));
                } else {
                   throw new IllegalArgumentException("Unknown field "+fieldName);
                }
       }

       /**
        * Determines whether this Header contains the specified field.
        @param fieldName the name of the field to check for (case sensitive)
        @returns true if the field exists, false otherwise.
        */
       public boolean hasField(String fieldName) {
              return (fields.containsKey(normaliseCaps(fieldName)));
       }

       /**
        * Sets the value of the specified field, creating it if required.
        @param fieldName the field to be set (case sensitive)
        @param fieldValue the value of fieldName
        */
       public void setField(String fieldName, String fieldValue) {
              fields.put(normaliseCaps(fieldName), fieldValue);
       }

       /**
        * Removes the specified field from the header if it exists.
        @param name the name of the field to remove from the header
        */
       public void removeField(String name) {
           fields.remove(name);
       }

       /**
        * Sets all the fields of the header from a Vector of String objects.
        * Existing headers are wiped out.
        @param v A vector of String objects each corresponding to a header and
                 of the form Header-name: header-value
        @throws java.lang.IllegalArgumentException if any of the Vector items
                are not Strings
        */
       public void setFields(Vector v) {
              Enumeration e;
              Object curelement;
              fields.clear();
              e = v.elements();
              while(e.hasMoreElements()) {
                 curelement = e.nextElement();
                 if (curelement instanceof String) {
                      addHeader((String)curelement);
                 } else {
                     // An element is null, or not a String
                     throw new IllegalArgumentException("non string or null element");
                 }  // if
              }  // while
       }

       /**
        * Determines whether this header indicates a reply message.
        @returns true if the message is a reply, false if it is an original post
        */
       public boolean isAReply() {
              /* A message is a reply if it has a References: header. */
              return (hasField("References"));
       }

       /**
        * Returns the "real name" from the From: field in the header, if it
        * exists. The three valid formats of the From field are:
        *         From: Real Name <user@host.name>
        *         From: user@host.name (Real Name)
        *         From: user@host.name
        * in the third case, this function will just return the unaltered e-mail
        * address.
        */
       public String getRealName() {
              /* ( and < are invalid in e-mail addresses, which makes our life
                 a lot easier. */
              String from;
              int symbpos;

              if (!hasField("From")) return "";
              from = getFieldValue("From");
              symbpos = from.indexOf('<');
              if (symbpos > 0) {         // Real Name <user@host.name>
                 return from.substring(0,symbpos-1);
              }
              symbpos = from.indexOf('(');
              if (symbpos > 0) {        // user@host.name (Real Name)
                 return from.substring(symbpos+1, from.length()-1);
              }
              return StringUtils.stripQuotes(from);
       }

       /**
        * Dump out the Header as a string. Overrides Object.
        */
       public String toString() {
              Enumeration e = fields.keys();
              Object cur;
              StringWriter strw = new StringWriter();
              PrintWriter pw = new PrintWriter(strw);
              while (e.hasMoreElements()) {
                    cur = e.nextElement();
                    pw.print((String)cur+": ");
                    pw.println(getFieldValue(normaliseCaps((String)cur)));
              }
              pw.close();
              return strw.toString();

       }

        /**
         * Set the ID of this message header.
         */
        public void setID(int id) {
         serverID = id;
        }

        /**
         * Retreive the ID of this message.
         */
        public int getID() {
         return serverID;
        }

        /**
         * Checks if two headers are equal. Two headers are equal if they both
         * have a Message-Id field, and these are identical.
         */
        public boolean equals(Object obj) {
           if (obj instanceof MessageHeader) {
              if (((MessageHeader)obj).hasField("Message-Id") && this.hasField("Message-Id"))
                 return (((MessageHeader)obj).getFieldValue("Message-Id").equals(this.getFieldValue("Message-Id")));
           }
           return false;
        }

        /**
         * Sets the font that this message header will be displayed with in the
         * message header list.<p>
         * <I>Added since NewsAgent 1.01B</I>
         @param f The Font to use.
         */
        public void setFont(Font f) {
         m_font = f;
        }

        /**
         * Gets the font that this message header will be displyed with in the
         * message header list.<p>
         * <I>Added since NewsAgent 1.01B</I>
         @return a Font object.
         */
        public Font getFont() {
         return m_font;
        }

        /**
         * Gets the foreground colour this message header will be displayed
         * with in the message header list when unselected.<p>
         * <I>Added since NewsAgent 1.01B</I>
         @return a Color object.
         */
        public Color getForeground() {
         return m_forecolor;
        }


        /**
         * Sets the foreground colour this message header will be displyed with
         * in the messsage header list when unselected<P>
         * <I>Added since NewsAgetn 1.01B</I>
         @param c A Color object
         */
        public void setForeground(Color c) {
         m_forecolor = c;
        }


        /**
         * Sets the background colour this message header will be displyed with
         * in the messsage header list when unselected<P>
         * <I>Added since NewsAgetn 1.01B</I>
         @param c A Color object
         */
        public void setBackground(Color c) {
         m_backcolor = c;
        }

        /**
         * Gets the background colour this message header will be displayed
         * with in the message header list when unselected.<p>
         * <I>Added since NewsAgent 1.01B</I>
         @return a Color object.
         */
        public Color getBackground() {
         return m_backcolor;
        }

        /**
         * Sets whether this message has been read.
         */
        public void setRead(boolean read) {
         m_isRead = read;
        }

        /**
         * Get whether this message has been read.
         */
        public boolean isRead() {
         return m_isRead;
        }

// Private methods
      /**
       * Enters a plain text header (e.g. Headername: value) into the Hashtable
       @param s A string header to be entered
       @throws java.lang.IllegalArgumentException if the string isn't a valid
               header.
       */
      private void addHeader(String s) throws IllegalArgumentException {
              if (s.length() < 3) // a header can't be less than x:y
                 throw new IllegalArgumentException(s+" is too short");
              int colonpos = s.indexOf(':');
              if (colonpos <= 0) // No colon or colon at start
                 throw new IllegalArgumentException(s+" has no field name");
              fields.put(normaliseCaps(s.substring(0,colonpos).trim()),
                         s.substring(colonpos+1).trim());
      }

      /**
       * Usenet headers are case insensitive. Internally, we store all header
       * specs as "first letter capitalised". Any letter following a space or
       * a dash (or at the start of the word) is uppercase. All other letters
       * are lower case.
       @param s The original header field spec
       @returns a properly capitalised version
       */
      private String normaliseCaps(String s) {
              StringBuffer sb = new StringBuffer(s);

              // Convert the whole string to lower case except for the
              // first character and any characters preceeded by '-'
              for (int i=0; i<s.length();i++) {
                 char thisChar = s.charAt(i);
                 if (Character.isLowerCase(thisChar)) {
                    if (i==0) {
                       // First character to upper
                       sb.setCharAt(i, Character.toUpperCase(thisChar));
                    } else {
                       // Characters prefixed by '-' to upper
                       if (s.charAt(i-1) == '-')
                          sb.setCharAt(i, Character.toUpperCase(thisChar));
                    }
                 } else {
                    // Convert to lower case if not the first char and not
                    // prefixed by -
                    if (i>0)
                       if (s.charAt(i-1) != '-')
                          sb.setCharAt(i, Character.toLowerCase(thisChar));
                 }

              }

              return sb.toString();
      }

      /**
       * Test harness method. Remove this on completion.
       */
      public void doTest() {
              System.out.println(normaliseCaps("message-id"));
             //System.out.println(this);
      }

      public static void main(String[] args) {
        MessageHeader hd = new MessageHeader();
        hd.doTest();
      }

}