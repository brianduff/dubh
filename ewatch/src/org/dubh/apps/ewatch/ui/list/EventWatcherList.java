package org.dubh.apps.ewatch.ui.list;

import java.awt.BorderLayout;
import java.awt.Component;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;

import org.dubh.apps.ewatch.*;

import org.dubh.apps.ewatch.type.*;
import org.dubh.apps.ewatch.type.file.*;
import org.dubh.apps.ewatch.type.url.*;
/**
 * This class is a test of the event watcher stuff that just displays events
 * in a JList control. This is mostly for testing.
 */
public class EventWatcherList extends JPanel
{
   private JList m_list;
   private EventTypeRegistrar m_registrar;

   public EventWatcherList()
   {
      m_registrar = new EventTypeRegistrar();
      m_list = new JList();
      m_list.setModel(new ListModel());
      m_list.setCellRenderer(new ListRenderer());

      setLayout(new BorderLayout());
      add(new JScrollPane(m_list), BorderLayout.CENTER);


      FileCreatedEventType fcet = new FileCreatedEventType("test");

      List l = new ArrayList();
      l.add("C:\\testfile.txt");
      l.add("C:\\anotherfile.txt");
      l.add("C:\\jdevout.txt");
      fcet.setListProperty(FileCreatedEventType.FILENAME_LIST_PROPERTY,
         l
      );
      fcet.setFontProperty(FileCreatedEventType.EVENT_FONT_PROPERTY,
         new Font("dialog", Font.PLAIN, 12)
      );
      fcet.setColorProperty(FileCreatedEventType.EVENT_COLOR_PROPERTY,
         Color.blue
      );

      FileDeletedEventType fdet = new FileDeletedEventType("test");

      fdet.setListProperty(FileDeletedEventType.FILENAME_LIST_PROPERTY,
         l
      );
      fdet.setFontProperty(FileDeletedEventType.EVENT_FONT_PROPERTY,
         new Font("dialog", Font.PLAIN, 12)
      );
      fdet.setColorProperty(FileDeletedEventType.EVENT_COLOR_PROPERTY,
         Color.red
      );

      FileChangeEventType fuet = new FileChangeEventType("test");
      fuet.setFontProperty(FileChangeEventType.EVENT_FONT_PROPERTY,
         new Font("dialog", Font.PLAIN, 12)
      );
      fuet.setColorProperty(FileChangeEventType.EVENT_COLOR_PROPERTY,
         Color.green
      );

      fuet.setListProperty(FileChangeEventType.FILENAME_LIST_PROPERTY,
         l
      );

      URLChangeEventType ucet = new URLChangeEventType("testurl");
      ucet.setFontProperty(URLChangeEventType.EVENT_FONT_PROPERTY,
         new Font("dialog", Font.BOLD, 12)
      );
      ucet.setColorProperty(URLChangeEventType.EVENT_COLOR_PROPERTY,
         Color.black
      );

      l = new ArrayList();
      l.add("http://localhost:8080/cv/somefaf.txt");
      ucet.setListProperty(URLChangeEventType.URL_LIST_PROPERTY, l);

      m_registrar.registerEventType(fcet);
      m_registrar.registerEventType(fdet);
      m_registrar.registerEventType(fuet);
      m_registrar.registerEventType(ucet);

      m_registrar.setWatching(true);

      
   }

   /**
    * main
    * @param args
    */
   public static void main(String[] args) {
      EventWatcherList eventWatcherList = new EventWatcherList();

      JFrame f = new JFrame();
      f.getContentPane().setLayout(new BorderLayout());
      f.getContentPane().add(eventWatcherList, BorderLayout.CENTER);
      f.pack();
      f.setVisible(true);
   }


   class ListModel extends AbstractListModel
   {
      public ListModel()
      {
         m_registrar.getEventQueue().addEventQueueUpdateListener(
            new EventQueue.EventQueueUpdateListener() {
               public void eventAdded(Event e)
               {
                  ListModel.this.notifyChange(e);
               }

               public void eventRemoved(Event e)
               {
                  ListModel.this.notifyChange(e);
               }
            }
         );
      }

      public void notifyChange(final Event e)
      {
         // We receive these events in a different thread, make sure we
         // update the UI in the correct thread.
         SwingUtilities.invokeLater(new Runnable() {
            public void run()
            {
               fireAllContentsChanged(e);
            }
         });
      }

      public void fireAllContentsChanged(Event e)
      {
         fireContentsChanged(e, 0, getSize()-1);
      }

      public int getSize()
      {
         return m_registrar.getEventQueue().getSize();
      }

      public Object getElementAt(int i)
      {
         return m_registrar.getEventQueue().getEventAt(i);
      }
   }

   class ListRenderer implements ListCellRenderer
   {
      public Component getListCellRendererComponent(
         JList list,
         Object value,
         int index,
         boolean isSelected,
         boolean cellHasFocus)
      {
         Event e = (Event)value;

         return
            e.getEventType().getEventRenderer().getEventRendererComponent(e);
      }
   }
}

