// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: Newsgroup.java,v 1.4 1999-11-09 22:34:42 briand Exp $
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
import java.io.*;

/**
 * Newsgroup represents a collection of articles.
 * Version History: <UL>
 * <LI>0.1 [03/02/98]: Initial Revision
 * <LI>0.2 [04/02/98]: Added constructor that takes a long newsgroup specifier
 *   and determines whether posting is allowed to this newsgroup.
 * <LI>0.3 [18/02/98]: Added article IDs
 * <LI>0.4 [25/03/98]: Added serialization support, added toString().
 * <LI>0.5 [04/04/98]: Added set/getArticleCount.
 * </UL>
 @author Brian Duff
 @version 0.5 [04/04/98]
 */
public class Newsgroup implements Serializable {

// Private attributes
   private String m_name;
   private int m_posting;
   private int firstNumericID;         // The first numerical article ID
   private int lastNumericID;          // The last numerical article ID
   private int articleCount;       // The number of articles in this newsgroup

// Public constants
   public static final int NOPOSTING = 0;
   public static final int POSTINGOK = 1;
   public static final int MODERATED = 2;

// Constructors
   /**
    * Construct a new, blank Newsgroup.
    */
   public Newsgroup() {
         m_name = "";
         m_posting = NOPOSTING;
         firstNumericID = lastNumericID = articleCount = 0;
   }

   /**
    * Construct a new newsgroup with the specified name.
    @param s Full newsgroup name (e.g. comp.lang.java.programmer). If the string
    contains any other information (e.g. is a response from a newsgroup list
    command to the server), that information is used to initialise the Newsgroup.
    @throws org.javalobby.apps.newsagent.NNTPServerException can't determine type.
    This is subject to change (shouldn't really use an NNTPServer exception,
    since Newsgroup has nothing to do with the server conceptually).
    */
   public Newsgroup(String s) throws NNTPServerInternalException {
         int firstart, lastart, type;
         char specifier;
         firstart = s.indexOf(' ');
         if (firstart > 0) {
            lastart = s.indexOf(' ', firstart+1);
            type = s.indexOf(' ', lastart+1);
            specifier = s.substring(type+1).charAt(0);
            m_name = s.substring(0,firstart);
            switch(specifier) {
            case 'y':
                 m_posting = POSTINGOK;
                 break;
            case 'n':
                 m_posting = NOPOSTING;
                 break;
            case 'm':
                 m_posting = MODERATED;
                 break;
            default:
                 throw new NNTPServerInternalException("newsgroup specifier garbled: "+s);
            }
         } else {
            m_name = s;
         }
         firstNumericID = lastNumericID = 0;
   }

// Public Methods

   /**
    * Retreive the fully qualified newsgroup name of this
    * Newsgroup object.
    @returns A full newsgroup name (e.g. comp.lang.java.programmer)
    */
    public String getName() {
           return m_name;
    }

    public String toString() {
        return getName();
    }

    /**
     * Determine whether posting is OK to this newsgroup.
     @returns a value corresponding to one of the constants NOPOSTING, POSTINGOK
     or MODERATED.
     */
    public int getPostingType() {
           return m_posting;
    }

    /**
     * Determine the first article available in this group. The article might
     * not actually exist, but gives a lower bound for determining which
     * articles the server does have.
     @returns a numerical article ID
     */
    public int getFirstArticle() {
      return firstNumericID;
    }

    /**
     * Set the first article available in this group.
     @param f The numerical Id of the first available article in this group.
     */
    public void setFirstArticle(int f) {
      firstNumericID = f;
    }

    /**
     * Determine the last article available in this group. The article might
     * not actually exist, but gives an upper bound for determining which
     * articles the server does have.
     @returns a numerical article ID
     */
    public int getLastArticle() {
      return lastNumericID;
    }

    /**
     * Set the last article available in this group.
     @param l The numerical ID of the last available article in this group.
     */
    public void setLastArticle(int l) {
      lastNumericID = l;
    }

    /**
     * Determine the number of articles available in this newsgroup.
     @return a count of the articles
     */
    public int getArticleCount() {
     return articleCount;

    }

    /**
     * Set the number of articles in this newsgroup.
     @param count a count of articles
     */
    public void setArticleCount(int count) {
     articleCount = count;

    }

}