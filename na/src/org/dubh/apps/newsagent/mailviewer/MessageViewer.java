// ---------------------------------------------------------------------------
//   NewsAgent: A Java USENET Newsreader
//   $Id: MessageViewer.java,v 1.1 1999-10-17 17:02:59 briand Exp $
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
package dubh.apps.newsagent.mailviewer;

import javax.swing.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


import java.awt.*;

import java.io.*;

/**
 * The message viewer is used to view the contents of a JavaMail message.
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: MessageViewer.java,v 1.1 1999-10-17 17:02:59 briand Exp $
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