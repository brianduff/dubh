/*   NewsAgent: A Java USENET Newsreader
 *   Copyright (C) 1997-8  Brian Duff
 *   Email: bd@dcs.st-and.ac.uk
 *   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
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
package dubh.apps.newsagent.dialog;

import java.awt.*;
import java.awt.event.*;
import dubh.utils.ui.GridBagConstraints2;
import javax.swing.*;

import dubh.apps.newsagent.dialog.NDialog;

import dubh.utils.misc.ReadOnlyVersion;

/**
 * The about dialogue.
 * Version History: <UL>
 * <LI>0.01 [21/04/98]: Initial Revision
 * <LI>0.02 [23/11/98]: And a mere 7 months later, I actually get round to
 *    finishing it... :) 
 *</UL>
 @author Brian Duff
 @version 0.02 [23/11/98]
 */
public class AboutDialog extends NDialog {
   private JPanel      m_panMain;
   private JPanel      m_panButtons;
   private JButton     m_cmdOK;
   private JPanel      m_panComp;
   private JLabel      m_labIcon;
   private JLabel      m_labAppName;
   private JLabel      m_labVersion;
   private JLabel      m_labCopyright;
   private JList       m_lstDepVersions;
   private JScrollPane m_scrDepVersions;

   public AboutDialog(Frame parent) {
      super(parent, "About", true);
      init();
      initLayout();
      pack();
      setResizable(false);

   }

   private void init() {
      m_panButtons     = new JPanel();
      m_cmdOK          = new JButton("Ok");
      m_panComp        = new JPanel();
      m_labIcon        = new JLabel();
      m_labAppName     = new JLabel();
      m_labVersion     = new JLabel();
      m_labCopyright   = new JLabel();
      m_lstDepVersions = new JList();
      m_scrDepVersions = new JScrollPane(m_lstDepVersions);     
      
      m_cmdOK.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e)
         {
            setVisible(false);
            dispose();
         }
      });
   }
   
   private void initLayout() {
      m_panButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
      m_panButtons.add(m_cmdOK);
   
      m_panComp.setLayout(new GridBagLayout());
      m_panComp.add(m_labIcon, new GridBagConstraints2(
         0, 0, 1, 4, 0.0, 1.0, GridBagConstraints.EAST, 
         GridBagConstraints.BOTH, new Insets(5, 5, 5, 5),
         0, 0
      ));
      
      m_panComp.add(m_labAppName, new GridBagConstraints2(
         1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
         GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
         0, 0
      ));
      
      m_panComp.add(m_labVersion, new GridBagConstraints2(
         1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
         GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
         0, 0
      ));

      m_panComp.add(m_labCopyright, new GridBagConstraints2(
         1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
         GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
         0, 0
      ));
      
      m_panComp.add(m_scrDepVersions, new GridBagConstraints2(
         1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.EAST,
         GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
         0, 75
      ));
   
      getContentPane().add(m_panComp, BorderLayout.CENTER);
      getContentPane().add(m_panButtons, BorderLayout.SOUTH);
      
   
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
}