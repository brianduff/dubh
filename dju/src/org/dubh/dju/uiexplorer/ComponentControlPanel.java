
// Copyright (c) 2000 Dubh 
package org.javalobby.dju.uiexplorer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.lang.reflect.Method;

/**
 * Contains controls that can be used to do funky things to components.
 * <P>
 * @author Brian Duff
 */
public class ComponentControlPanel
{
   private JPanel m_panel;
   private Component m_component;

   private JPanel m_getPropPanel;
   private JLabel m_getPropLabel;
   private JTextField m_getPropDisplay;
   private JButton m_getPropButton;

   /**
    * Constructor
    */
   public ComponentControlPanel()
   {
      m_panel = new JPanel();

      m_panel.setLayout(new BorderLayout());

      m_getPropPanel = new JPanel();
      m_getPropLabel = new JLabel("Invoke Method: ");
      m_getPropDisplay = new JTextField();
      m_getPropButton = new JButton("Go");
      m_getPropPanel.setLayout(new BorderLayout());
      m_getPropPanel.add(m_getPropLabel, BorderLayout.WEST);
      m_getPropPanel.add(m_getPropDisplay, BorderLayout.CENTER);
      m_getPropPanel.add(m_getPropButton, BorderLayout.EAST);

      m_getPropButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae)
         {
            invokeGetProperty();
         }
      });

      m_panel.add(m_getPropPanel, BorderLayout.NORTH);
   }

   /**
    * Set the target component
    */
   public void setTargetComponent(Component c)
   {
      m_component = c;
   }

   /**
    * Get a panel containing all controls
    */
   public Component getPanel()
   {
      return m_panel;
   }

   private void invokeGetProperty()
   {

      if (m_component != null)
      {
         try
         {
            String propName = m_getPropDisplay.getText();
            Class c = m_component.getClass();
            Method m = c.getDeclaredMethod(propName, null);
            Object o = m.invoke(m_component, null);
            if (o == null)
            {
               m_getPropDisplay.setText("Called OK (Returned void)");
            }
            else
            {
               m_getPropDisplay.setText("Result: "+o.toString());
            }
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }
}

 