/*   Dubh Java Utilities Library: Useful Java Utils
 *
 *   Copyright (C) 1997-9  Brian Duff
 *   Email: dubh@btinternet.com
 *   URL:   http://www.btinternet.com/~dubh
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package dubh.utils.ui;

import java.awt.*;
import java.awt.event.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;

import dubh.utils.misc.ReadOnlyVersion;
import dubh.utils.misc.VersionManager;

/**
 * A generic about dialog that can display information about the versions
 * of this product and any dependent products that follow the dubh versioning
 * system. In future, support will be added for Java 2 style versioning.
 * Version History: <UL>
 * <LI>0.01 [21/04/98]: Initial Revision
 * <LI>0.02 [23/11/98]: And a mere 7 months later, I actually get round to
 *    finishing it... :) 
 * <LI>0.03 [12/12/98]: Ported over from NewsAgent. Changed to a panel and
 *    added static dialog invoker.
 *</UL>
 @author Brian Duff
 @version 0.03 [12/12/98]
 */
public class AboutPanel extends JPanel {
   private JButton     m_cmdOK;
   private JLabel      m_labIcon;
   private JLabel      m_labAppName;
   private JLabel      m_labVersion;
   private JLabel      m_labCopyright;
   private JList       m_lstDepVersions;
   private JScrollPane m_scrDepVersions;

   public AboutPanel() {
      init();
      initLayout();
   }

   private void init() {
      m_labIcon        = new JLabel();
      m_labAppName     = new JLabel();
      m_labVersion     = new JLabel();
      m_labCopyright   = new JLabel();
      m_lstDepVersions = new JList();
      m_scrDepVersions = new JScrollPane(m_lstDepVersions);     
      
   }
   
   private void initLayout() {
   
      setLayout(new GridBagLayout());
      add(m_labIcon, new GridBagConstraints2(
         0, 0, 1, 3, 0.0, 0.0, GridBagConstraints.EAST, 
         GridBagConstraints.BOTH, new Insets(5, 5, 5, 5),
         0, 0
      ));
      
      add(m_labAppName, new GridBagConstraints2(
         1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
         GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5),
         0, 0
      ));
      
      add(m_labVersion, new GridBagConstraints2(
         1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
         GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5),
         0, 0
      ));

      add(m_labCopyright, new GridBagConstraints2(
         1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
         GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5),
         0, 0
      ));
      
      add(m_scrDepVersions, new GridBagConstraints2(
         0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
         GridBagConstraints.BOTH, new Insets(0, 5, 5, 5),
         0, 75
      ));
   }

   /**
    * Set the icon to display at the left of the dialogue.
    * @param icon any Icon object.
    */
   public void setIcon(Icon icon) {
      m_labIcon.setIcon(icon);
   }

   /**
   * Set the version information for the main product.
   */
   public void setProductVersion(ReadOnlyVersion rov)
   {
      m_labAppName.setText(rov.getProductName());
      m_labVersion.setText(rov.getVersionDescription(
         "Version {3}.{4}.{5} build {6} [{2}] - {7}"
      ));
      m_labCopyright.setText(rov.getProductCopyright());
   }
  
   /**
   * Add a version information for a product on which this
   * product is dependent.
   */
   public void setDependencies(ReadOnlyVersion[] rov)
   {
      m_lstDepVersions.setModel(new DependenciesListModel(rov));
   }
  
  
   class DependenciesListModel extends AbstractListModel
   {
      private ReadOnlyVersion[] m_versions;
   
      public DependenciesListModel(ReadOnlyVersion[] rov)
      {
         m_versions = rov;
      }
      
      public Object getElementAt(int index)
      {
         return m_versions[index].getVersionDescription(
            "{0} {3}.{4}.{5} build {6} [{2}]"
         );
      }

      public int getSize()
      {
         return m_versions.length;
      }
   }
   
   public static AboutPanel doDialog(JFrame parent, ReadOnlyVersion product, 
      ReadOnlyVersion[] dependencies)
   {
      return doDialog(parent, product, dependencies, null);
   }
   
   public static AboutPanel doDialog(JFrame parent, ReadOnlyVersion product)
   {
      return doDialog(parent, product, null);
   }            
   
   public static AboutPanel doDialog(JFrame parent, ReadOnlyVersion product,
      ReadOnlyVersion[] dependencies, Icon icon)
   {
      AboutPanel ap = new AboutPanel();
      if (icon != null) ap.setIcon(icon);
      ap.setProductVersion(product);
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
   

   public static void main(String[] args)
   {
       AboutPanel.doDialog(new JFrame(), VersionManager.getInstance().getVersion("dubh.utils"));
   }
   
}