// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: StringUtils.java,v 1.5 1999-11-11 21:24:35 briand Exp $
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
package org.javalobby.dju.misc;

import java.util.*;
/**
 * Utilities for handling Strings.<P>
 * Version History: <UL>
 * <LI>0.1 [07/03/98]: Initial Revision
 * <LI>0.2 [08/03/98]: Added string / int routines
 * <LI>0.3 [31/03/98]: Added stripQuotes
 * <LI>0.4 [07/04/98]: Added prependChar
 * <LI>0.5 [14/04/98]: Added replaceChar
 * <LI>0.6 [15/04/98]: Added createSentence
 * <LI>0.7 [28/04/98]: Added fix for bug #10: Error catching in stringToInt
 *   and intToString.
 * <LI>0.8 [29/04/98]: Added makeDoubleHex.
 * <LI>0.9 [01/05/98]: Added getTokens(). Changed getWords() to use it. Added
 *   countTokens() and modified getWordCount() to use it. Added wildMatch().
 * <LI>1.0 [05/06/98]: Moved package to org.javalobby.dju.misc (was in NewsAgent).
 *   Added double argument version of stringToInt() and threw exception in
 *   single argument version [THIS WILL CAUSE INTERFACE PROBLEMS WITH EXISTING
 *   CODE THAT DOES NOT CATCH THE EXCEPTION]
 * <LI>1.1 [10/06/98]: Added prefixString. Noticed that prependChar should
 *   really be called prefixChar. I can write Java, but I can't speak
 *   English, it seems ;)
 * <LI>1.2 [13/06/98]: Added createSentence(String[])
 *</UL>
 @author Brian Duff
 @version 1.2 [13/06/98]
 */
public class StringUtils {

// Public Methods

  /**
   * Obtains a word from a string.<P>
   * Example:<P><PRE>
   *  StringUtils.getWord("This is a test", 3);
   * </PRE><P>
   * Returns "test".
   @param s the string
   @param number the word to fetch
   @returns a string corresponding to the number-th word in s (zero based)
   */
   public static String getWord(String s, int number) {
   return getWords(s)[number];
  }

  /**
   * Obtains an array of the words in a String.
   @param s the string
   @returns an array of Strings corresponding to the words in s
   */
  public static String[] getWords(String s) {
     return getTokens(s, " ");
  }

  /**
   * Obtains an array of tokens in a String, separated by any number of chars
   @param s the string
   @returns an array of Strings corresponding to the tokens in s
   */
  public static String[] getTokens(String s, String c) {
   Vector v = new Vector();
      StringTokenizer t = new StringTokenizer(s, c);
      String cmd[];

      while (t.hasMoreTokens())
        v.addElement(t.nextToken());
     cmd = new String[v.size()];
     
     for (int i = 0; i < cmd.length; i++)
        cmd[i] = (String) v.elementAt(i);
      return cmd;
  }

  /**
   * Counts the number of occurrences of tokens separated by a substring
   * E.g. countSeparators("hello world", " ") returns 2.
   */
  public static int countTokens(String s, String substr) {
     StringTokenizer t = new StringTokenizer(s, substr);
     return t.countTokens();
  }

  /**
   * Determines the number of words in a String. 
   @param s the string to count
   @returns the number of words in the string.
  */
  public static int getWordCount(String s) {
    //   return getWords(s).length;
     return countTokens(s, " ");
  }

  /**
   * Convenience method which converts an integer into a String.
   @param i The integer
   @return a string
   */
  public static String intToString(int i) {
   Integer temp = new Integer(i);
    return temp.toString();
  }

  /**
   * Convenience method which converts a String into an integer.
   @param s the string
   @param def the default value if the number format is invalid
   @return an integer, def if the string is non numeric.
   */
  public static int stringToInt(String s, int def) {
     // FIX For bug #10: Now checking for a NumberFormatException.
   try {
     return (Integer.parseInt(s.trim()));
   } catch (NumberFormatException e) {
     return def;
   }
  }

  /**
   * Convenience method to convert a String into an integer. Unlike the
   * double parameter version, this version throws a NumberFormatException
   * if the number format is invalid.
   @param s the string
   @return an integer value
   @throws NumberFormatException the number format is invalid
   */
  public static int stringToInt(String s) throws NumberFormatException {
     if (s == null) throw new NumberFormatException("Number is null");
     return (Integer.parseInt(s.trim()));
  }

  /**
   * Strip one (and only one) set of surrounding single or double quotes from
   * a string.
   */
  public static String stripQuotes(String s) {
     if (s.charAt(0) == '\"' || s.charAt(0) == '\'') {
        return s.substring(1,s.length()-1);
     }
     return s;
  }

  /**
   * Prepend a given character to all lines in the given text
   @param prependChar The character to put at the front of each line
   @param original The original string
   @return a string matching the original, except that each line starts with
     the prependChar.
   */
  public static String prependChar(String original, char prependChar) {
     return prefixString(original, ""+prependChar);
  }

  /**
   * Prefix a given string to all strings in the given text.
   @param prefixString a string to put to the front of each line
   @param original the original string
   @return the prefixed string.
   */
  public static String prefixString(String original, String prefixString) {
     StringBuffer buf = new StringBuffer(original);
     
     int len   = prefixString.length();
     int added = len;

     buf.insert(0, prefixString);

     /**
      * Using exception catching to prevent bounds checking in the loop
      * improves performance under many systems.
      */
     try {
        for (int i=0;;i++) {
           if (original.charAt(i) == '\n') {
              buf.insert(i+1+added, prefixString);
              added += len;
           }
        }
     } catch (StringIndexOutOfBoundsException e) {}


     return buf.toString();
  }

  /**
   * Replace all instances of a specified character by another character
   * in a given string.
   */
  public static String replaceChar(String original, char search, char replace) {
   StringBuffer buf = new StringBuffer(original);
   for (int i=0;i<buf.length();i++) {
     if (buf.charAt(i) == search)
       buf.setCharAt(i, replace);
   }
   return buf.toString();
  }

  /**
   * Creates a sentence of words, given a vector
   @param words a Vector containing Strings
     method defined.
   @return a String with all the words in 'words', separated by spaces.
   */
  public static String createSentence(Vector words) {
   /* String instantiation is slow in Java. we use a StringBuffer of a
    * predefined length to get round this, so must calculate the total
    * length of all the strings in words + the number of words - 1 and
    * allocate a buffer of this size.
    */
    if (words.size() == 0) return "";
   String[] wordsStore = new String[words.size()];
   int i=0, count=0;
   Enumeration wordEnum = words.elements();
   /*
    * The only copying in this loop is reference copying, so it has a low
    * o/head.
    */
   while (wordEnum.hasMoreElements()) {
     wordsStore[i] = (String)wordEnum.nextElement();
     count += wordsStore[i].length();
     i++;
   }
   StringBuffer wordsBuffer = new StringBuffer(count+wordsStore.length-1);
   /*
    * This loop copies the strings into the StringBuffer, so has a higher
    * overhead than the last loop (but as large as direct instantiation)
    */
   for (i=0;i<wordsStore.length-1;i++) {
     wordsBuffer.append(wordsStore[i]);
     wordsBuffer.append(' ');
   }
   /* Add the last one */
   wordsBuffer.append(wordsStore[wordsStore.length-1]);
   return wordsBuffer.toString();
  }

  /**
   * Creates a sentence given an array of words
   */
  public static String createSentence(Object[] words) {
     StringBuffer wordsBuffer = new StringBuffer();
   /*
    * This loop copies the strings into the StringBuffer, so has a higher
    * overhead than the last loop (but as large as direct instantiation)
    */
     for (int i=0;i<words.length-1;i++) {
        wordsBuffer.append(words[i]);
        wordsBuffer.append(' ');
     }
     /* Add the last one */
     wordsBuffer.append(words[words.length-1]);
     return wordsBuffer.toString();
  }

 /**
  * Convert an integer into a double digit hexadecimal number.
  */
 public static String makeDoubleHex(int i) {
   String result = Integer.toHexString(i);
   if (result.length() == 1) {
     return "0"+result;
   }
   return result;
 }

  /**
   * Do a wildcard search on a string.
   * e.g. wildMatch("comp.*.*.programmer", "comp.lang.java.programmer", '*', '.');
   * returns true.
   @param wildcard the string containing wildcards
   @param value an absolute string to check against the wildcard string
   @param wChar the character that stands for "any" (usually *)
   @param sepChar the character to separate items (e.g. . or /)
   */
  public static boolean wildMatch(String wildcard, String value, char wChar, char sepChar) {
     if (wildcard.equals(""+wChar)) return true;  // * matches anything
     int wTokens = countTokens(wildcard, ""+sepChar);
     int vTokens = countTokens(value, ""+sepChar);
     int stopAt = Math.max(wTokens, vTokens);
     if ( wTokens != vTokens ) {
        if (wildcard.endsWith(""+sepChar+wChar) && vTokens > wTokens) {// ends with .*
           // can just do a compare with no tokens in the wildcard.
           stopAt = wTokens;  
        } else
           return false;
     }
     // OK, tokenize the two strings.
     String[] wTokenized = getTokens(wildcard, ""+sepChar);
     String[] vTokenized = getTokens(value,    ""+sepChar);

     for (int i=0; i< stopAt; i++) {
        if (!wTokenized[i].equals(""+wChar)) {
           if (!wTokenized[i].equals(vTokenized[i])) return false;
        }
     }
     return true;
  }

  /**
   * Word wraps an original string to the specified number of columns.
   @param original the original string
   @param columns the number of columns to wrap to.
   @return a new, wrapped string.
   */
  public static String wordWrapString(String original, int columns) {
     /*
      * Probably not the most efficient implementation, but I'm tired.
      */
      StringBuffer buf = new StringBuffer(original);
      int added = 0;
      int lastspacepos = -1;
      int colpos = 0;
      int curpos;
      
      /*
       * Go through the original, a character at a time. If current character
       * is a space, set lastspacepos = curpos. If we hit columns part way
       * through a word, insert a newline after lastspacepos. Add 1 to added
       * whenever a newline is inserted into the buffer, and set lastspacepos
       * to -1. If we hit columns in the middle of a word and lastspacepos == -1
       * then just insert a line break at the column break position.
       */
      char curChar;
      for (curpos=0; curpos < original.length(); curpos ++) {
        colpos++;
        curChar = original.charAt(curpos);
        if (curChar == ' ' || curChar == '\t') {
           lastspacepos = curpos;
        }
        if (curChar == '\n') colpos = 0;
           /* if we hit the column inside a word */
           if (colpos == columns+1) {
              if (lastspacepos == -1) {
                 /* Have no choice but to break the word  */
                 buf.insert(curpos+added+1, '\n');
                 added++;
                 colpos = 0;
              } else {
                 buf.setCharAt(lastspacepos+added, '\n');
                 lastspacepos = -1;
                 colpos = 0;
              }
           }
      
      }
      return buf.toString();

  }

  public static void main(String[] args) {
     System.out.println("0123456789012345678901234567890");
     System.out.println(wordWrapString("now is the time for\n\nall good men\n\nto come to the aid of the party", 30));
     java.io.LineNumberReader blah = new java.io.LineNumberReader(new java.io.InputStreamReader(System.in));
     try {
        blah.readLine();
     } catch (java.io.IOException e) {


     }
  }

}