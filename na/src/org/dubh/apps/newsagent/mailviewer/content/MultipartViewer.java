// ---------------------------------------------------------------------------
//   NewsAgent
//   $Id: MultipartViewer.java,v 1.3 2001-02-11 02:51:00 briand Exp $
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


package org.dubh.apps.newsagent.mailviewer.content;

import javax.activation.*;
import javax.swing.*;
import javax.mail.*;

import java.awt.*;

import java.awt.event.*;

import java.io.*;

/**
 * A bean that can be used to display multipart mime messages.
 * This displays the main part of the message by delegating to the
 * correct viewer for that mime type, and displays all attachments
 * as icons with context menus for performing various actions on them.
 *
 * @author Brian Duff (dubh@btinternet.com)
 * @version $Id: MultipartViewer.java,v 1.3 2001-02-11 02:51:00 briand Exp $
 */
public class MultipartViewer extends JPanel implements CommandObject
{
   protected DataHandler m_dhHandler = null;
   protected String m_strVerb = null;

   public MultipartViewer()
   {
      super(new BorderLayout());
   }


   public void setCommandContext(String verb, DataHandler dh) throws IOException
   {
      m_strVerb = verb;
      m_dhHandler = dh;

      // get the content, and hope it is a Multipart Object
      Object content = dh.getContent();
      if (content instanceof Multipart)
      {
         setupDisplay((Multipart)content);
      }
      else
      {
         setupErrorDisplay(content);
      }
   }

   protected void setupDisplay(Multipart mp)
   {

      // get the first part
      try
      {
         BodyPart bp = mp.getBodyPart(0);
         Component comp = getComponent(bp);
         add(comp, BorderLayout.CENTER);
      }
      catch (MessagingException me)
      {
         add(new Label(me.toString()), BorderLayout.CENTER);
      }

      // see if there are more than one parts
      try
      {
         int count = mp.getCount();

         JPanel panSouth = new JPanel();
         add(panSouth, BorderLayout.SOUTH);

         panSouth.setLayout(new FlowLayout());

         // for each one we create a button with the content type
         for(int i = 1; i < count; i++)
         {
            BodyPart curr = mp.getBodyPart(i);
            String label = null;
            if (label == null) label = curr.getFileName();
            if (label == null) label = curr.getDescription();
            if (label == null) label = curr.getContentType();

            JButton but = new JButton(label);
            but.addActionListener( new AttachmentViewer(curr));
            add(but);
         }
      }
      catch(MessagingException me2)
      {
         me2.printStackTrace();
      }
   }

   /**
    * Get the component used to display a particular bodypart.
    */
   protected Component getComponent(BodyPart bp)
   {

      try
      {
         DataHandler dh = bp.getDataHandler();
         CommandInfo ci = dh.getCommand("view");
         if (ci == null)
         {
            throw new MessagingException(
               "view command failed on: " +
                bp.getContentType()
             );
         }

         Object bean = dh.getBean(ci);

         if (bean instanceof Component)
         {
            return (Component)bean;
         }
         else
         {
            if (bean == null)
               throw new MessagingException(
                  "bean is null, class " + ci.getCommandClass() +
                  " , command " + ci.getCommandName()
               );
            else
               throw new MessagingException(
                  "bean is not a awt.Component" +
                  bean.getClass().toString()
               );
         }
      }
      catch (MessagingException me)
      {
         return new Label(me.toString());
      }
   }



   protected void setupErrorDisplay(Object content)
   {
      String error;

      if (content == null)
         error = "Content is null";
      else
         error = "Object not of type Multipart, content class = " +
            content.getClass().toString();

      System.out.println(error);
      JLabel lab = new JLabel(error);
      add(lab);
   }

   class AttachmentViewer implements ActionListener
   {

      BodyPart bp = null;

      public AttachmentViewer(BodyPart part)
      {
         bp = part;
      }

      public void actionPerformed(ActionEvent e)
      {
         ComponentFrame f = new ComponentFrame(
         getComponent(bp), "Attachment");
         f.pack();
         f.show();
      }
   }

}

//
// $Log: not supported by cvs2svn $
// Revision 1.2  1999/11/09 22:34:42  briand
// Move NewsAgent source to Javalobby.
//
// Revision 1.1  1999/10/17 17:03:27  briand
// Initial revision.
//
//