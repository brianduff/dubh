// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: MessageViewer.java,v 1.2 1999-11-09 22:34:42 briand Exp $
//   Copyright (C) 1997-9  Brian Duff
//   Email: dubh@btinternet.com
//   URL:   http://wired.st-and.ac.uk/~briand/newsagent/
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
package org.javalobby.apps.newsagent.mailviewer;

import javax.swing.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


import java.awt.*;

import java.io.*;

/**
 * The message viewer is used to view the contents of a JavaMail message.
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: MessageViewer.java,v 1.2 1999-11-09 22:34:42 briand Exp $
 */
public class MessageViewer extends JPanel implements CommandObject
{   
   protected Message m_msgCurrentMessage = null;
   protected DataHandler m_dhBodyProvider = null;
   protected String m_strVerb = null;
   protected Component m_comBody;
   protected HeaderViewer m_hvHeaders;
   protected JSplitPane m_spSplitter;

   
   /**
    * Construct a viewer with no initial message
    */
   public MessageViewer()
      throws MessagingException 
   {
      this(null);
   }
    
   /**
    * Construct a viewer with an initial message
    */ 
   public MessageViewer(Message msg)
      throws MessagingException 
   {
      super(new BorderLayout());
      
      m_spSplitter = new JSplitPane();
      
      add(m_spSplitter, BorderLayout.CENTER);
      
      
      // Todo: pass in custom headers
      m_hvHeaders = new HeaderViewer();

      m_spSplitter.setOrientation(JSplitPane.VERTICAL_SPLIT);
      m_spSplitter.setTopComponent(m_hvHeaders);

      setMessage(msg);
   }
    
   /**
   * sets the current message to be displayed in the viewer
   */
   public void setMessage(Message msg) 
      throws MessagingException
   {
      m_msgCurrentMessage = msg;
      
      m_hvHeaders.setMessage((MimeMessage)msg);
      
      if (msg != null) 
      {
         m_comBody = getBodyComponent();
         m_spSplitter.setBottomComponent(m_comBody);
      }

      invalidate();
      validate();
   }



   /**
    * Get a content viewer for the message body
    */
   protected Component getBodyComponent() 
   {
      try 
      {
         DataHandler dh = m_msgCurrentMessage.getDataHandler();
         CommandInfo ci = dh.getCommand("view");
         if (ci == null) 
         {
            throw new MessagingException("view command failed on: " +
                       m_msgCurrentMessage.getContentType());
         }
      
         Object bean = dh.getBean(ci);
         
         if (bean == null) throw new MessagingException("Bean is null");
         
         if (bean instanceof Component) {
            return (Component)bean;
         }
         else
         {
            throw new MessagingException("bean is not a component " +
                       bean.getClass().toString());
         }
      } 
      catch (MessagingException me) 
      {
         System.out.println("Whoops: "+me);
          return new Label(me.toString());
      }
   }
    
   /**
    * the CommandObject method to accept our DataHandler
    * @param dh   the datahandler used to get the content
    */
   public void setCommandContext(String verb,
           DataHandler dh) throws IOException 
   {
      m_strVerb = verb;
      m_dhBodyProvider = dh;
   
      Object o = m_dhBodyProvider.getContent();
      if (o instanceof Message) 
      {
         try
         {
            setMessage((Message)o);
         }
         catch (MessagingException mse)
         {
            System.out.println(mse);
         }
      }
      else
      {
         System.out.println( 
         "MessageViewer - content not a Message object, " + o);
         if (o != null)
         {
            System.out.println(o.getClass().toString());
         }
      }
   }

}