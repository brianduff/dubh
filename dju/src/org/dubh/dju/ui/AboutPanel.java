// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: AboutPanel.java,v 1.8 2001-02-11 15:36:39 briand Exp $
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

package org.dubh.dju.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.AbstractListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.dubh.dju.version.ComponentInfo;
import org.dubh.dju.version.ComponentInfoFactory;



/**
 * A generic about dialog that can display information about the versions
 * of this product and any dependent products that use the versioning support
 * in Java 2.
 *
 * @author Brian Duff
 * @version $Id: AboutPanel.java,v 1.8 2001-02-11 15:36:39 briand Exp $
 */
public class AboutPanel extends JPanel
{
   private JButton     m_cmdOK;
   private JLabel      m_labIcon;
   private JLabel      m_labAppName;
   private JLabel      m_labVersion;
   private JLabel      m_labVendor;
   private JList       m_lstDepVersions;
   private JScrollPane m_scrDepVersions;

   public AboutPanel()
   {
      init();
      initLayout();
   }

   private void init()
   {
      m_labIcon        = new JLabel();
      m_labAppName     = new JLabel();
      m_labVersion     = new JLabel();
      m_labVendor   = new JLabel();
      m_lstDepVersions = new JList();
      m_scrDepVersions = new JScrollPane(m_lstDepVersions);

   }

   private void initLayout()
   {

      setLayout(new GridBagLayout());
      add(m_labIcon, new GridBagConstraints(
         0, 0, 1, 3, 0.0, 0.0, GridBagConstraints.EAST,
         GridBagConstraints.BOTH, new Insets(5, 5, 5, 5),
         0, 0
      ));

      add(m_labVendor, new GridBagConstraints(
         1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
         GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5),
         0, 0
      ));

      add(m_labAppName, new GridBagConstraints(
         1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
         GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5),
         0, 0
      ));

      add(m_labVersion, new GridBagConstraints(
         1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
         GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5),
         0, 0
      ));

      add(m_scrDepVersions, new GridBagConstraints(
         0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
         GridBagConstraints.BOTH, new Insets(0, 5, 5, 5),
         0, 75
      ));
   }

   /**
    * Set the icon to display at the left of the dialogue.
    * @param icon any Icon object.
    */
   public void setIcon(Icon icon)
   {
      m_labIcon.setIcon(icon);
   }

   /**
   * Set the version information for the main product.
   *
   * @param component a DJU ComponentInfo object that contains information
   *     about the main component in the application (i.e. the application
   *     itself)
   */
   public void setProduct(ComponentInfo component)
   {
      m_labVendor.setText(component.getVendor());
      m_labAppName.setText(component.getName());
      m_labVersion.setText(component.getShortVersion());
   }

   /**
   * Add version information for a components on which this
   * application is dependent.
   *
   * @param components an array of components on which the application depends
   */
   public void setDependencies(ComponentInfo[] components)
   {
      m_lstDepVersions.setModel(new DependenciesListModel(components));
   }


   /**
    * Simple list model
    */
   class DependenciesListModel extends AbstractListModel
   {
      private ComponentInfo[] m_versions;

      public DependenciesListModel(ComponentInfo[] pkglist)
      {
         m_versions = pkglist;
      }

      public Object getElementAt(int index)
      {
         ComponentInfo c = m_versions[index];

         return c.getVendor() + " " + c.getName() + " " + c.getShortVersion();
      }

      public int getSize()
      {
         return m_versions.length;
      }
   }

   public static AboutPanel doDialog(JFrame parent, ComponentInfo product,
      ComponentInfo[] dependencies)
   {
      return doDialog(parent, product, dependencies, null);
   }

   public static AboutPanel doDialog(JFrame parent, ComponentInfo product)
   {
      return doDialog(parent, product, null);
   }

   public static AboutPanel doDialog(JFrame parent, ComponentInfo product,
      ComponentInfo[] dependencies, Icon icon)
   {
      AboutPanel ap = new AboutPanel();
      if (icon != null) ap.setIcon(icon);
      ap.setProduct(product);
      if (dependencies != null) ap.setDependencies(dependencies);
      AboutPanelDialog apd = ap.new AboutPanelDialog(parent);
      apd.setPanel(ap);
      apd.pack();
      apd.setVisible(true);
      if (apd.isCancelled())
         return null;
      else
         return ap;
   }


   /**
    * Dummy placeholder class so that dialog position gets
    * stored uniquely.
    */
   class AboutPanelDialog extends DubhOkCancelDialog
   {

      public AboutPanelDialog(JFrame parent)
      {
         super(parent, "About", true);
         setButtonVisible(DubhOkCancelDialog.s_CANCEL_BUTTON, false);
      }
   }

}