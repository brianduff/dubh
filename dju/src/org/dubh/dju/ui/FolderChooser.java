// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FolderChooser.java,v 1.1 2000-06-14 21:26:53 briand Exp $
//   Copyright (C) 1997-2000  Brian Duff
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
package org.javalobby.dju.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;

import java.text.Collator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * This UI component displays a tree of all directories in the system. One
 * can be selected at any time.
 * <P>
 * @author Brian Duff
 */
public class FolderChooser extends JPanel
{
   private JTree m_tree;
   private FolderChooserRoot m_root;
   private JScrollPane m_scroll;

   private JLabel m_label;

   private FileSystemView m_fileSystem;
   private FileView m_fileView;
   private FileFilter m_filter;

   private Comparator m_sortComparator;

   private boolean m_isSorted;

   private BusyTargetImpl m_busy = new BusyTargetImpl();

   private boolean m_dialogOK = false;
   private JDialog m_dialog = null;

   /**
    * Create a folder chooser
    */
   public FolderChooser()
   {
      setFileSystemView(FileSystemView.getFileSystemView());
      JFileChooser c = new JFileChooser();
      setFileView(c.getUI().getFileView(c));
      setFileFilter(new DefaultFileFilter());
      setBranchComparator(new FileNodeComparator());

      setSorted(false);

      m_tree = new JTree();
      m_scroll = new JScrollPane(m_tree);
      m_root = new FolderChooserRoot();
      m_tree.setModel(new DefaultTreeModel(m_root));
      m_tree.setCellRenderer(new FolderChooserTreeCellRenderer());
      m_tree.setRootVisible(false);
      m_tree.setShowsRootHandles(true);

      m_label = new JLabel();

      setLayout(new BorderLayout());
      add(m_label, BorderLayout.NORTH);
      add(m_scroll, BorderLayout.CENTER);
   }



   /**
    * Accept decides whether a file will be displayed in the chooser. By
    * default, only folders are accepted, but might add filtering to this
    * class.
    */
   private boolean accept(File f)
   {
      return getFileFilter().accept(f);
   }

   /**
    * You can use this to customize the way in which branches are sorted.
    * A default comparator is used, which sorts alphabetically by folder name.
    * If you override the file filter (for example to show files), you might
    * also want to set your own comparator.
    *
    * @param c A Comparator which will be used to sort the objects. The object
    *   parameters in the Comparator methods will always be passed objects
    *   that implement the FolderChooser.FileNode interface.
    */
   public void setBranchComparator(Comparator c)
   {
      m_sortComparator = c;
   }

   private Comparator getBranchComparator()
   {
      return m_sortComparator;
   }

   /**
    * Sets whether the folders are sorted in their branch. By default, there
    * is no sorting.
    * If sorting is used, the branch comparator is used to make the sort.
    */
   public void setSorted(boolean b)
   {
      m_isSorted = b;
   }

   /**
    * Returns whether sorting is switched on.
    *
    * @see #setSorted(boolean)
    */
   public boolean isSorted()
   {
      return m_isSorted;
   }

   /**
    * You can set the filter on the dialog. The default filter only allows
    * folders to be displayed, but this can be replaced by anything.
    */
   public void setFileFilter(FileFilter f)
   {
      m_filter = f;
   }

   /**
    * Get the file filter.
    */
   public FileFilter getFileFilter()
   {
      return m_filter;
   }

   /**
    * A label is displayed above the tree. You can use this method to set
    * the text for the label.
    *
    * @param labelText The text to display in the label.
    */
   public void setLabelText(String labelText)
   {
      m_label.setText(labelText);
   }

   /**
    * Get the text that is currently being displayed in the label.
    *
    * @return a String containing the text of the label
    */
   public String getLabelText()
   {
      return m_label.getText();
   }

   /**
    * Set the object that is used to provide a view on to the file system.
    * If you don't call this, the default view for your OS will be used.
    *
    * @param fsv a file system view
    */
   public void setFileSystemView(FileSystemView fsv)
   {
      m_fileSystem = fsv;
   }

   /**
    * Get the object that is used to provide a view on to the file system.
    *
    * @return a file system view
    */
   public FileSystemView getFileSystemView()
   {
      return m_fileSystem;
   }

   /**
    * Set the object that is used to provide information about the way in
    * which files are displayed. If you don't call this, the default view is
    * used, which is the same as the one used in JFileChooser.
    *
    * @param fv a file view.
    */
   public void setFileView(FileView fv)
   {
      m_fileView = fv;
   }

   /**
    * Get the file view that is being used to display files.
    */
   public FileView getFileView()
   {
      return m_fileView;
   }

   /**
    * Get the selected file object.
    *
    * @return the selected file, or null if no file is selected.
    */
   public File getFile()
   {
      TreePath p = m_tree.getSelectionPath();
      if (p == null)
      {
         return null;
      }
      
      FileNode f = (FileNode) p.getLastPathComponent();
      if (f == null)
      {
         return null;
      }
      return f.getFile();
   }

   /**
    * Set the selected file object. This will automatically expand the
    * tree to make the specified file visible. This method will be ignored
    * if the specified file does not exist, or if the filter does not accept it.
    */
   public void setFile(File file)
   {
      if (!file.exists() || !getFileFilter().accept(file))
      {
         return;
      }

      List pathComponents;

      try
      {
         pathComponents = getPathComponents(file.getCanonicalPath());
      }
      catch (IOException ioe)
      {
         return;
      }

      int componentCount = pathComponents.size() + 1;

      LazyTreeNode path[] = new LazyTreeNode[componentCount];
      path[0] = m_root;

      for (int i=1; i < componentCount; i++)
      {
         // Get all the children of path[i-1] and find the one that matches
         // this path component.
         String thisComponent = (String)pathComponents.get(i-1);
         Enumeration enumKids = path[i-1].children();
         while (enumKids.hasMoreElements())
         {
            FolderChooserNode fcn = (FolderChooserNode)enumKids.nextElement();
            String nodeName = fcn.toString();
            // Workaround for / at the end of drive letters under windows
            if (nodeName.charAt(nodeName.length()-1) == File.separatorChar)
            {
               nodeName = nodeName.substring(0, nodeName.length()-1);
            }
            if (thisComponent.equals(nodeName))
            {
               path[i] = fcn;
               break;
            }
         }
         if (path[i] == null)
         {
            // The path was not found.
            return;
         }
      }

      TreePath fullPath = new TreePath(path);

      m_tree.setSelectionPath(fullPath);
   }

   /**
    * Given a filename, return a list of the path components.
    */
   private List getPathComponents(String fileName)
   {
      ArrayList l = new ArrayList();
      StringTokenizer st = new StringTokenizer(fileName, File.separator);

      while (st.hasMoreTokens())
      {
         l.add(st.nextToken());
      }

      return l;
   }


   /**
    * Shows the component in a modal dialog. This method will block until the
    * user chooses OK, Cancel or otherwise closes the dialog.
    *
    * @param parent the parent component. This is used for modality.
    * @param title The title to dispay in the dialog
    *
    * @return null if the user cancelled or closed the dialog. A File object
    *   corresponding to the selected directory if the user clicked OK.
    */
   public File showDialog(Component parent, String title)
   {
      Frame parentFrame = JOptionPane.getFrameForComponent(parent);
      m_dialog = new JDialog(parentFrame, title, true);

      JButton okButton = new JButton("OK");
      final JButton cancelButton = new JButton("Cancel");
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

      buttonPanel.add(cancelButton);
      buttonPanel.add(okButton);

      okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e)
         {
            m_dialogOK = true;
            m_dialog.setVisible(false);
         }
      });

      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e)
         {
            m_dialogOK = false;
            m_dialog.setVisible(false);
         }
      });

      OKEnabler oke = new OKEnabler(okButton);
      m_tree.addTreeSelectionListener(oke);
      okButton.setEnabled((m_tree.getSelectionPath() != null));


      m_dialog.getContentPane().setLayout(new BorderLayout());
      m_dialog.getContentPane().add(this, BorderLayout.CENTER);
      m_dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

      m_dialog.pack();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension dialogSize = m_dialog.getSize();

      m_dialog.setLocation((screenSize.width/2 - dialogSize.width/2),
         (screenSize.height/2 - dialogSize.height/2)
      );

      m_dialog.setVisible(true);

      m_dialog.dispose();
      m_dialog = null;

      m_tree.removeTreeSelectionListener(oke);

      if (m_dialogOK)
      {
         return getFile();
      }
      else
      {
         return null;
      }
   }


   /**
    * This listens to the tree and enables/disables a specific button depending
    * on whether there is a selection. Used by showDialog().
    */
   class OKEnabler implements TreeSelectionListener
   {
      private JButton m_okButton;

      public OKEnabler(JButton okButton)
      {
         m_okButton = okButton;
      }
      public void valueChanged(TreeSelectionEvent e)
      {
         m_okButton.setEnabled((getFile() != null));
      }
   }


   /**
    * This is the root node of the folder chooser. The root is not usually
    * displayed.
    */
   class FolderChooserRoot extends LazyTreeNode
   {
      public FolderChooserRoot()
      {
         super();
         setBusyTarget(m_busy);
      }

      /**
       * The root node is populated.
       */
      protected void populate()
      {
         File[] roots = getFileSystemView().getRoots();
         
         for (int i=0; i < roots.length; i++)
         {
            File f = roots[i];
            if (accept(f))
            {
               addChild(new FolderChooserNode(this, f, true));
            }
         }
         if (isSorted())
         {
            sortChildren(getBranchComparator());
         }
      }
   }

   /**
    * This interface is implemented by all nodes which hold on to a file.
    */
   public interface FileNode
   {
      File getFile();
   }


   /**
    * This is the root node of the folder chooser. The root is not usually
    * displayed.
    */
   class FolderChooserNode extends LazyTreeNode implements FileNode
   {
      private File m_file;
      private boolean m_isLazy;
      
      public FolderChooserNode(LazyTreeNode parent, File file, boolean isLazy)
      {
         super(parent);
         m_file = file;
         m_isLazy = isLazy;
         setBusyTarget(m_busy);
      }

      /**
       * The root node is populated.
       */
      protected void populate()
      {
         File[] kids = getFileSystemView().getFiles(m_file, false);

         for (int i=0; i < kids.length; i++)
         {
            File f = kids[i];
            if (accept(f))
            {
               addChild(new FolderChooserNode(this, f, false));
            }
         }
         if (isSorted())
         {
            sortChildren(getBranchComparator());
         }
      }

      /**
       * Get the name of the node
       */
      public String toString()
      {
         return getFileView().getName(m_file);
      }

      /**
       * Get the file for this node
       */
      public File getFile()
      {
         return m_file;
      }

      protected boolean isLazy()
      {
         return m_isLazy;
      }
   }

   /**
    * Responsible for rendering the tree items. Delegates to the
    * FileView to get the icon and name for the file corresponding to each
    * node in the tree.
    */
   class FolderChooserTreeCellRenderer extends DefaultTreeCellRenderer
   {
      public Component getTreeCellRendererComponent(JTree tree, Object value,
         boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
      {
         JLabel l = (JLabel)super.getTreeCellRendererComponent(tree, value,
            sel, expanded, leaf, row, hasFocus);

         if (value instanceof FolderChooserNode)
         {
            FolderChooserNode fcn = (FolderChooserNode)value;
            l.setText(getFileView().getName(fcn.getFile()));
            l.setIcon(getFileView().getIcon(fcn.getFile()));
         }
         return l;
      }
   }

   /**
    * The default filter, which only allows files which are directories.
    */
   private class DefaultFileFilter extends FileFilter
   {
      public boolean accept(File f)
      {
         return f.isDirectory();
      }

      public String getDescription()
      {
         return "";
      }
   }

   /**
    * The default sorting comparator uses a locale specific collator to
    * alphabetically arrange files. Directories are always displayed before
    * files.
    */
   class FileNodeComparator implements Comparator
   {
      Collator m_collator = Collator.getInstance();

      public int compare(Object x1, Object x2)
      {
         // We sort files as well as directories, in case the user sets a
         // filter on the chooser to display files.
         File f1 = ((FileNode)x1).getFile();
         File f2 = ((FileNode)x2).getFile();

         if ((f1.isDirectory() && f2.isDirectory()) ||
             (f1.isFile() && f2.isFile()))
         {
            return m_collator.compare(getFileView().getName(f1),
               getFileView().getName(f2));
         }

         if (f1.isDirectory() && f2.isFile())
         {
            return -1;
         }
         else
         {
            return 1;
         }
      }

      public boolean equals(Object o)
      {
         return (o instanceof FileNodeComparator);
      }
   }

   /**
    * This just allows us to hide the fact that we're implementing BusyTarget
    * Otherwise, we'd have to make setBusy() public, which would be an
    * unnecessary exposure.
    */
   class BusyTargetImpl implements LazyTreeNode.BusyTarget
   {
      public void setBusy(boolean b)
      {
         if (b)
         {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         }
         else
         {
            setCursor(Cursor.getDefaultCursor());
         }
      }
   }

   /**
    * Test harness code
    */
   public static void main(String[] args)
   {
      /*try
      {
      javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e) {}
      */


      FolderChooser chooser = new FolderChooser();
      chooser.setLabelText("Testing");
      chooser.setSorted(true);
      //chooser.setFile(new File("C:\\wInNt\\jAVA\\trustlib\\com\\ms\\mtx"));


      File f = chooser.showDialog(new javax.swing.JFrame(), "Funky");

      System.out.println("Selected file was "+f);

      System.exit(0);
   }
}

//
// $Log: not supported by cvs2svn $
//

 