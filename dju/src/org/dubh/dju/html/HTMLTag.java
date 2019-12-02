// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: HTMLTag.java,v 1.5 2001-02-11 02:52:11 briand Exp $
//   Copyright (C) 1997 - 2001  Brian Duff
//   Email: Brian.Duff@oracle.com
//   URL:   http://www.dubh.org
// ---------------------------------------------------------------------------
// Copyright (c) 1997 - 2001 Brian Duff
//
// This program is free software.
//
// You may redistribute it and/or modify it under the terms of the
// license as described in the LICENSE file included with this
// distribution.  If the license is not included with this distribution,
// you may find a copy on the web at 'http://www.dubh.org/license'
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


package org.dubh.dju.html;
import java.util.*;
import org.dubh.dju.misc.*;

/**
 * Generic representation of an HTML Tag. Rather simplistic at the moment, and
 * slighly unpolished (parameters with different cases are regarded as
 * different parameters).
 *<P>
 * <B>Revision History:</B><UL>
 * <LI>0.1 [02/07/98]: Initial Revision
 * </UL>
 @author <A HREF="http://wiredsoc.ml.org/~briand/">Brian Duff</A>
 @version 0.1 [02/07/98]
 */
public class HTMLTag {

  private static final Hashtable kludge = new Hashtable();

  private static final char TAG_OPEN = '<';
  private static final char TAG_CLOSE = '>';
  private static final char CLOSE = '/';

  protected String m_command;
  protected Hashtable m_attributes;
  protected boolean m_closecommand;

  protected HTMLTag() {
     m_command = new String();
     m_attributes = new Hashtable();
  }

  public HTMLTag(String command) {
     m_command = command;
     m_attributes = new Hashtable();
  }

  public String getCommand() { return m_command; }
  public void setCommand(String cmd) { m_command = cmd; }

  /**
   * Set the given attribute to the given value. If the attribute doesn't exist,
   * it is added.
   */
  public void setAttribute(String attribute, String value) {
     m_attributes.put(attribute, value);
  }

  /**
   * Retrive the value of an attribute. Returns null if the attribute doesn't
   * exist.
   */
  public String getAttribute(String attribute) {
     if (m_closecommand) return null;
     return (String)m_attributes.get(attribute);
  }

  /**
   * Get an enumeration of all attributes.
   */
  public Enumeration getAttributes() {
     if (m_closecommand) {
        return kludge.keys();
     }
     return m_attributes.keys();
  }

  /**
   * Sets whether this HTML tag is a closing tag. If set to a closing tag,
   * the tag has no attributes.
   */
  public void setClosingTag(boolean b) {
     m_closecommand = b;
  }

  public boolean isClosingTag() { return m_closecommand; }

  /**
   * Parse a piece of HTML code and produce a new HTMLTag from it
   */
  public static HTMLTag parse(String htmlTagSpec) throws HTMLParseException {
     HTMLTag tag = new HTMLTag();
     String[] words = StringUtils.getWords(htmlTagSpec);
     int wordCount = words.length;

     if (wordCount == 0)
        throw new HTMLParseException("HTML tag is empty");

     int curWord = 0;
     /* If word starts with TAG_OPEN, it should contain the command. If not,
      * the next word (if it exists) must be the command. i.e. either
      * <command .... or < command ....
      */
     if (words[curWord].charAt(0) == TAG_OPEN) {
        if (words[curWord].length() > 1) { // no space between < and command
           if (parseCommand(words[curWord].substring(1), tag)) return tag;
        } else {  // space between < and command.
           curWord++;
           if (curWord > wordCount)
              throw new HTMLParseException("HTML tag has no command: "+htmlTagSpec);
           if (parseCommand(words[curWord], tag)) return tag;
        }
     }
     /* If we get here, the tag is still open, and presumably either has
      * attributes or the next word is a TAG_CLOSE.
      */
     curWord++;
     if (curWord > wordCount)
        throw new HTMLParseException("HTML tag is not closed: "+htmlTagSpec);

     parseAttribs(htmlTagSpec, tag);

     return tag;
  }

  /**
   * Extract the attributes from an HTML Tag
   */
  protected static void parseAttribs(String tagSpec, HTMLTag tag)
      throws HTMLParseException {
     /*
      * Ok, bizarre implementation time. We split the tag into chunks separated
      * by '=' and then call makeAttrib on each pair of strings.
      */
     String[] chunks = StringUtils.getTokens(tagSpec, "=");
     if (chunks.length < 2) {
        // No attributes. Should really check that the tag is closed properly
        return;
     }

     for (int i=1; i<chunks.length; i++) {
        parseAttrib(tag, chunks[i-1], chunks[i]);
     }

  }

  protected static void parseAttrib(HTMLTag tag, String chunk1, String chunk2) {
     String[] kwords = StringUtils.getWords(chunk1);
     String key = kwords[kwords.length-1];     // the last word in chunk1.

     String value;
     String[] vwords = StringUtils.getWords(chunk2);
     if (vwords.length == 1) value = chunk2;
     else {
        int r = chunk2.lastIndexOf(' '); // must be the space before the attrib key
        value = chunk2.substring(0, r).trim();
     }
     /*
      * Check for a tag closure
      */
     if (value.charAt(value.length()-1) == TAG_CLOSE) {
        value = value.substring(0, value.length()-1);
     }

     /*
      * Strip quotes
      */
     value = StringUtils.stripQuotes(value);

     tag.setAttribute(key, value);
  }


  /**
   * Parse the command part of an HTML Tag. This can be either:
   * <PRE>
   *    a        (simple command on its own)
   *    /a       (simple closing command on its own)
   *    b>       (simple command that is closed)
   *    /b>      (simple closing command that is closed)
   * </PRE><P>
   * In any case, sets the command of tag accordingly.
   @returns true if the tag was terminated (contained >), or was a closing
   * command (in which case, there is no need to read the attributes)
   */
  protected static boolean parseCommand(String cmd, HTMLTag tag) {
     if (cmd.charAt(0) == CLOSE) {
        if (cmd.charAt(cmd.length()-1) == TAG_CLOSE) { // "/b>"
           tag.setCommand(cmd.substring(1, cmd.length()-1));
           tag.setClosingTag(true);
           return true;
        } else { // "/a"
           tag.setCommand(cmd.substring(1));
           tag.setClosingTag(true);
           return true;
        }
     } else {
        if (cmd.charAt(cmd.length()-1) == TAG_CLOSE) { // "b>"
           tag.setCommand(cmd.substring(0, cmd.length()-1));
           return true;
        } else { // "a"
           tag.setCommand(cmd);
           return false;
        }
     }
  }

  /**
   * Display the HTML tag in HTML format.
   */
  public String toString() {
     StringBuffer result = new StringBuffer();
     result.append(TAG_OPEN);
     if (isClosingTag()) result.append(CLOSE);
     result.append(m_command);
     if (!isClosingTag()) {
        Enumeration e = getAttributes();
        while (e.hasMoreElements()) {
           result.append(" ");
           String attrib = (String) e.nextElement();
           String value = getAttribute(attrib);
           result.append(attrib);
           result.append("=\"");
           result.append(value);
           result.append("\"");
          // if (enum.hasMoreElements()) result.append(" ");
        }
     }
     result.append(TAG_CLOSE);
     return result.toString();
  }



  public static void main(String[] args) {
     HTMLTag test = new HTMLTag("a");
     test.setAttribute("href", "http://st-and.compsoc.org.uk/~briand");
     test.setAttribute("target", "_top");
     System.out.println("Link is "+test);

     try {
        HTMLTag test2 = HTMLTag.parse(
           "<a>"
        );
        System.out.println("Parsed to: "+test2);
     } catch (HTMLParseException pe) {
        System.out.println("Parse exception: "+pe);
     }

  }

}