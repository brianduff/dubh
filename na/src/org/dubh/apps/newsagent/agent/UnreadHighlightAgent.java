// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: UnreadHighlightAgent.java,v 1.6 2001-02-11 02:50:59 briand Exp $
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

package org.dubh.apps.newsagent.agent;

import java.awt.*;
import java.awt.event.*;
import org.dubh.dju.ui.GridBagConstraints2;
import javax.swing.*;
import org.dubh.apps.newsagent.nntp.MessageHeader;
import org.dubh.apps.newsagent.nntp.MessageBody;
import org.dubh.dju.misc.StringUtils;
import org.dubh.apps.newsagent.dialog.ErrorReporter;
import org.dubh.apps.newsagent.agent.ISendAgent;
import java.util.Properties;
import org.dubh.dju.misc.Debug;

/**
 * An agent that marks unread messages differently from other messages.
 @author Brian Duff
 @version $Id: UnreadHighlightAgent.java,v 1.6 2001-02-11 02:50:59 briand Exp $
 */
public class UnreadHighlightAgent implements IListAgent {

   private static final String m_name = "Unread Message Highlighter";
   private static final String m_desc =
   "This agent highlights messages which you haven't read yet in a customisable way (e.g. different font or colour).";
   private Properties m_properties = new Properties();

// Properties Keys

   private static final String propFontName      = "FontName";
   private static final String defFontName       = "Dialog";
   private static final String propFontSize      = "FontSize";
   private static final String defFontSize       = "12";
   private static final String propFontBold      = "FontBold";
   private static final boolean  defFontBold       = true;
   private static final String propFontItalic    = "FontItalic";
   private static final boolean  defFontItalic     = false;
   private static final String propNewsgroups    = "Newsgroups";
   private static final boolean defNewsgroups    = false;
   private static final String propFilterGroups  = "NewsgroupsFilter";
   private static final String defFilterGroups   = "";
   /** The colour property is stored as "#rbg" where r, b, g are hex 00-FF */
   private static final String propColour        = "Colour";
   private static final String defColour         = "#000000";

   private static final int MIN_FONTSIZE         = 8;


  public UnreadHighlightAgent() {
     m_properties = new Properties();
  }

 /**
  * Get the name of this agent. This should be a short (1-3 word) description
  * of the agent.
  @return a String name for the agent
  */
 public String getName() {
   return m_name;
 }

 /**
  * Get an icon for the agent.
  @return a Swing Icon object, or null if no icon is to be used for this agent.
  */
 public Icon getIcon() {
   return null;
 }

 /**
  * Get a longer description of this agent.
  @return a string describing the agent in a few short sentences.
  */
 public String getDescription() {
   return m_desc;
 }

 /**
  * Return a panel which can be used to configure this agent in some way.
  @return a JPanel which the user will be able to configure your agent with
  */
 public JPanel getConfigurationPanel() {
   UnreadHighlightAgentPanel confpanel = new UnreadHighlightAgentPanel();
   confpanel.setFormatFont(getFont());
   confpanel.setFormatColor(decodeColor(
     m_properties.getProperty(propColour, defColour)));
   confpanel.setSubstringFilter(getBoolProp(propNewsgroups, defNewsgroups));
   confpanel.setNewsgroupText(m_properties.getProperty(propFilterGroups,
     defFilterGroups));
   return confpanel;
 }

 /**
  * Called when the user applies changes to a configuration panel.
  @param panConfig the configuration panel you returned in
     getConfigurationPanel(). You probably want to use a subclass of JPanel
     so you can get and set component values through an interface.
  */
 public void applyConfiguration(JPanel panConfig){
   if (panConfig instanceof UnreadHighlightAgentPanel) {
     UnreadHighlightAgentPanel pan = (UnreadHighlightAgentPanel) panConfig;
     setFont(pan.getFormatFont());
     m_properties.put(propNewsgroups, bToS(pan.isSubstringFilter()));
     m_properties.put(propFilterGroups, pan.getNewsgroupText());
     m_properties.put(propColour, encodeColor(pan.getFormatColor()));
   } else {
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this,"Can't apply UnreadHighlightAgent configuration");
   }
 }

 /**
  * Called by NewsAgent before it lists a message. Your agent should process
  * the message and return a ListAgentMessage object describing what
  * NewsAgent should do next.<P>
  * NewsAgent calls its agents in a <B>user specified</B> order, calling
  * checkMessage(), checking the return code. Your agent may alter the header
  * in any way you like, since the header passed to this method is the actual
  * header object that will be used. You should take care to notify NewsAgent
  * using the ListAgentMessage if you have decided to delete the message.
  @param msgHead the head of the message to be processed
  @return a ListAgentMessage object describing what the agent thought of the
     message it recieved
  */
 public ListAgentMessage checkMessage(MessageHeader msgHead) {
   /*
    * If the message is unread, format it differently.
    */
   String realName = msgHead.getRealName();
   if (!msgHead.isRead()) {
     // Change the appearance of the message
     msgHead.setFont(getFont());
     msgHead.setForeground(decodeColor(
       m_properties.getProperty(propColour, defColour)));
     return new ListAgentMessage(false, false, true, false, "");
   } else {
     // make the header appear as default.
     /* This should ****not****** be hard coded!!!!!!!!!!! */
     msgHead.setFont(new Font("SansSerif", Font.PLAIN, 12));
     msgHead.setForeground(Color.black);
     return new ListAgentMessage(false, false, true, false, "");
   }

 }

 /**
  * Get the properties of this agent, so that they can be serialised. These
  * are usually the configurations you allow the user to change with the
  * config panel.
  */
 public Properties getProperties() {
   return m_properties;

 }

 /**
  * Set the properties
  */
 public void setProperties(Properties p) {
   m_properties = p;

 }




// Private Parts

 private Font getFont() {
   // assemble a font from the properties. The default font for unread messages
   // is a bold 10pt dialogue font.
   if (m_properties == null) {
     m_properties = new Properties();
   }
   String name = m_properties.getProperty(propFontName, defFontName);
   int    size = StringUtils.stringToInt(
     m_properties.getProperty(propFontSize, defFontSize));

   if (size < MIN_FONTSIZE) size = MIN_FONTSIZE;
   int style = Font.PLAIN;
   if (getBoolProp(propFontBold, defFontBold))     style += Font.BOLD;
   if (getBoolProp(propFontItalic, defFontItalic)) style += Font.ITALIC;
   return new Font(name, style, size);

 }

 private void setFont(Font f) {
   // given a font, put it into the properties
   m_properties.put(propFontName, f.getName());
   setBoolProp(propFontBold, f.isBold());
   setBoolProp(propFontItalic, f.isItalic());
   m_properties.put(propFontSize, StringUtils.intToString(f.getSize()));
 }


 private String bToS(boolean b) {
   if (b) return "1"; else return "0";
 }

 private boolean getBoolProp(String propName, boolean def) {
   String defVal;
   defVal = bToS(def);
   if (StringUtils.stringToInt(m_properties.getProperty(propName, defVal))==1)
     return true;
   return false;
 }

 private void setBoolProp(String propName, boolean val) {
   String sVal;
   sVal = bToS(val);
   m_properties.put(propName, sVal);
 }

 private Color decodeColor(String colName) {
   try {
     return Color.decode(colName);
   } catch (NumberFormatException nfe) {
     if (Debug.TRACE_LEVEL_1) Debug.println(1, this,"Invalid colour for UnreadHighlightAgent: "+colName+". Using black.");
     return Color.black;
   }
 }

 private String encodeColor(Color col) {
   String prefix = "#";
   return prefix + StringUtils.makeDoubleHex(col.getRed()) +
                   StringUtils.makeDoubleHex(col.getGreen()) +
                   StringUtils.makeDoubleHex(col.getBlue());
 }

}
//
// Old History:
//
// <LI>0.1 [28/04/98]: Initial Revision
// <LI>0.2 [06/06/98]: Added dubh utils import for StringUtils
//
// New history:
//
// $Log: not supported by cvs2svn $
// Revision 1.5  1999/11/09 22:34:41  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.4  1999/06/01 00:25:16  briand
// Change to use DJU ResourceManager, UserPreferences, DubhOkCancelDialog, Debug.
//