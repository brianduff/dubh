// ---------------------------------------------------------------------------
//   Dubh Java Utilities
//   $Id: FormattingChooser.java,v 1.7 2001-02-11 02:52:11 briand Exp $
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.dubh.dju.misc.StringUtils;
import org.dubh.dju.misc.Debug;
import org.dubh.dju.misc.ResourceManager;

/**
 * A FormattingChooser is a panel that allows the user to select a font and
 * optionally a foreground colour.<P>
 *
 * @author Brian Duff
 * @version $Id: FormattingChooser.java,v 1.7 2001-02-11 02:52:11 briand Exp $
 */
public class FormattingChooser extends ValidatorPanel implements ValidatorPanel.Validator {

   protected static final int MIN_FONTSIZE = 8;
   private static final Font s_DEFAULT_FONT =
      new Font("SansSerif", Font.PLAIN, 12);
   private static final Color s_DEFAULT_COLOR =
      Color.black;

   private JPanel m_panFont;
   private GridBagLayout m_layoutPanFont;

   private JLabel m_lblFont;
   private JTextField m_tfFont;
   private JScrollPane m_scrollFont;
   private TextFieldActivatedList  m_lstFont;

   private JLabel m_lblStyle;
   private JTextField m_tfStyle;
   private JScrollPane m_scrollStyle;
   private TextFieldActivatedList m_lstStyle;

   private JLabel m_lblSize;
   private JTextField m_tfSize;
   private JScrollPane m_scrollSize;
   private TextFieldActivatedList m_lstSize;

   private JLabel m_lblColor;
   private JComboBox m_cmbColor;

   private JPanel m_panSample;

   private JLabel m_labSample;


   public FormattingChooser(Font font, Color color) {
      createComponents();

      layoutComponents();
      ResourceManager.getManagerFor(
         "org.dubh.dju.ui.res.FormattingChooser"
      ).initComponents(this);
      //   initialiseLists();
      setFormatFont(font);
      setFormatColor(color);
   }

   public FormattingChooser() {
      this(s_DEFAULT_FONT, s_DEFAULT_COLOR);
   }


   private void createComponents()
   {
      this.setName("FormattingChooser");

      m_panFont = new JPanel();
      m_panFont.setName("panFont");
      m_layoutPanFont = new GridBagLayout();

      m_lblFont = new JLabel();
      m_lblFont.setName("lblFont");
      m_tfFont = new JTextField();
      m_tfFont.setName("tfFont");
      m_lstFont = new TextFieldActivatedList(new FontListModel(), true);
      m_lstFont.setName("lstFont");
      m_scrollFont = new JScrollPane(m_lstFont);
      TextFieldListBinder fontBinder =
            new TextFieldListBinder(m_tfFont, m_lstFont);
      m_tfFont.getDocument().addDocumentListener(fontBinder);
      m_lstFont.addListSelectionListener(fontBinder);
      registerValidator(fontBinder);

      m_lblStyle = new JLabel();
      m_lblStyle.setName("lblStyle");
      m_tfStyle = new JTextField();
      m_tfStyle.setName("tfStyle");
      m_lstStyle = new TextFieldActivatedList(new StyleListModel());
      m_lstStyle.setName("lstStyle");
      m_scrollStyle = new JScrollPane(m_lstStyle);
      TextFieldListBinder styleBinder =
            new TextFieldListBinder(m_tfStyle, m_lstStyle);
      m_tfStyle.getDocument().addDocumentListener(styleBinder);
      m_lstStyle.addListSelectionListener(styleBinder);
      registerValidator(styleBinder);

      m_lblSize = new JLabel();
      m_lblSize.setName("lblSize");
      m_tfSize = new JTextField();
      m_tfSize.setName("tfSize");
      m_lstSize = new TextFieldActivatedList(new SizeListModel());
      m_lstSize.setName("lstSize");
      m_scrollSize = new JScrollPane(m_lstSize);
      TextFieldListBinder sizeBinder =
            new TextFieldListBinder(m_tfSize, m_lstSize);
      m_tfSize.getDocument().addDocumentListener(sizeBinder);
      m_lstSize.addListSelectionListener(sizeBinder);
      registerValidator(sizeBinder);

      m_lblColor = new JLabel();
      m_lblColor.setName("lblColor");
      m_cmbColor = new JComboBox();
      m_cmbColor.setName("cmbColor");
      m_cmbColor.setRenderer(new ColorListCellRenderer());
      m_cmbColor.setEditable(false);
      m_cmbColor.setModel(new ColorComboBoxModel());

      m_panSample = new JPanel();
      m_panSample.setName("panSample");

      m_labSample = new JLabel();
      m_labSample.setName("labSample");
   }

   private void layoutComponents()
   {
      m_panFont.setLayout(m_layoutPanFont);
      m_panFont.add(m_lblFont, new GridBagConstraints2(
         0, 0, 1, 1, 0.3, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
         new Insets(5, 5, 0, 2), 0, 0
      ));
      m_panFont.add(m_tfFont, new GridBagConstraints2(
         0, 1, 1, 1, 0.3, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
         new Insets(0, 5, 0, 2), 0, 0
      ));
      m_panFont.add(m_scrollFont, new GridBagConstraints2(
         0, 2, 1, 1, 0.3, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
         new Insets(0, 5, 5, 2), 0, 0
      ));
      m_panFont.add(m_lblStyle, new GridBagConstraints2(
         1, 0, 1, 1, 0.3, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
         new Insets(5, 2, 0, 2), 0, 0
      ));
      m_panFont.add(m_tfStyle, new GridBagConstraints2(
         1, 1, 1, 1, 0.3, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
         new Insets(0, 2, 0, 2), 0, 0
      ));
      m_panFont.add(m_scrollStyle, new GridBagConstraints2(
         1, 2, 1, 1, 0.3, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
         new Insets(0, 2, 5, 2), 0, 0
      ));
      m_panFont.add(m_lblSize, new GridBagConstraints2(
         2, 0, 1, 1, 0.3, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
         new Insets(5, 2, 0, 5), 0, 0
      ));
      m_panFont.add(m_tfSize, new GridBagConstraints2(
         2, 1, 1, 1, 0.3, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
         new Insets(0, 2, 0, 5), 0, 0
      ));
      m_panFont.add(m_scrollSize, new GridBagConstraints2(
         2, 2, 1, 1, 0.3, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
         new Insets(0, 2, 5, 5), 0, 0
      ));
      m_panFont.add(m_lblColor, new GridBagConstraints2(
         0, 3, 1, 1, 0.3, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
         new Insets(5, 5, 0, 2), 0, 0
      ));
      m_panFont.add(m_cmbColor, new GridBagConstraints2(
         0, 4, 1, 1, 0.3, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
         new Insets(0, 5, 5, 2), 0, 0
      ));

      m_panSample.add(m_labSample, BorderLayout.CENTER);
      this.setLayout(new GridBagLayout());
      this.add(m_panFont, new GridBagConstraints2(
         0,0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(0,0,0,0), 0, 0
      ));
      this.add(m_panSample, new GridBagConstraints2(
         0,1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
         new Insets(0,0,0,0), 0, 0
      ));
   }


   /**
   * Sets the Font that this panel is currently displaying properties of.
   @param f a Font object. The attributes of this font will be displayed in
   the various widgets.
   */
   public void setFormatFont(Font f) {
      if (f==null) {
         Debug.println("Dubh Utils: null font in FormattingChooser.setFormatFont!");
         return;
      }
      setFontName(f.getName());
      setFontStyle(f.getStyle());
      setFontSize(f.getSize());
   }

   /**
   * Retrieves the Font that is configured in this
   * panel. If the size field is blank or less than a minimum size, it is
   * automagically set to the minimum font size.
   @return a Font object.
   */
   public Font getFormatFont() {
      return new Font(getFontName(), getFontStyle(), getFontSize());
   }

   /**
   * Retrieves the Color that is configured in this panel
   @return a Color object
   */
   public Color getFormatColor() {
      return (Color)m_cmbColor.getSelectedItem();
   }

   /**
   * Set the Color that is displayed in this panel.
   @param c a Color object
   */
   public void setFormatColor(Color c) {
      m_cmbColor.setSelectedItem(c);
   }


   private void setFontName(String name)
   {
      m_tfFont.setText(name);
      m_lstFont.setSelectedValue(name, true);
   }

   private void setFontStyle(int style)
   {
      StyleListModel slm = (StyleListModel) m_lstStyle.getModel();

      for (int i=0; i < slm.getSize(); i++)
      {
         if (((Style)slm.getElementAt(i)).getStyle() == style)
         {
            m_lstStyle.setSelectedValue(slm.getElementAt(i), true);
            m_tfStyle.setText(slm.getElementAt(i).toString());
         }
      }
   }

   private void setFontSize(int size)
   {
      m_tfSize.setText(String.valueOf(size));
      m_lstSize.setSelectedValue(new Integer(size), true);
   }


   private String getFontName()
   {
      return m_tfFont.getText();
   }

   private int getFontStyle()
   {
      StyleListModel slm = (StyleListModel) m_lstStyle.getModel();

      for (int i=0; i < slm.getSize(); i++)
      {
         if (((Style)slm.getElementAt(i)).getName().equals(m_tfStyle.getText()))
         {
            return ((Style)slm.getElementAt(i)).getStyle();
         }
      }
      return Font.PLAIN;
   }

   private int getFontSize()
   {
      return Integer.parseInt(m_tfSize.getText());
   }

   public static FormattingChooser doDialog(JFrame parent)
   {
      FormattingChooser fc = new FormattingChooser();
      FormattingChooserDialog dlg = fc.new FormattingChooserDialog(parent);
      dlg.setChooser(fc);
      dlg.pack();
      dlg.setVisible(true);
      dlg.dispose();
      if (dlg.isCancelled())
         return null;
      else
         return dlg.getChooser();
   }

   public static FormattingChooser doDialog(JFrame parent, Font f, Color c)
   {
      FormattingChooser fc = new FormattingChooser(f, c);
      FormattingChooserDialog dlg = fc.new FormattingChooserDialog(parent);
      dlg.setChooser(fc);
      dlg.pack();
      dlg.setVisible(true);
      dlg.dispose();
      if (dlg.isCancelled())
         return null;
      else
         return dlg.getChooser();
   }

   class TextFieldActivatedList extends JList
   {
      private boolean m_sensitive;

      public TextFieldActivatedList(ListModel m)
      {
         this(m, false);
      }

      public TextFieldActivatedList(ListModel m, boolean isCaseSensitive)
      {
         super(m);
         m_sensitive = isCaseSensitive;
      }

      public boolean selectItemStartingWith(String s)
      {
         for (int i=0; i < getModel().getSize(); i++)
         {
            String thisItem = getModel().getElementAt(i).toString();
            if (match(thisItem, s))
            {
               setSelectedValue(getModel().getElementAt(i), true);
               return true;
            }
            else
            {
               clearSelection();
            }
         }
         return false;
      }

      private boolean match(String thisItem, String searchItem)
      {
         if (m_sensitive)
            return thisItem.equals(searchItem);
         else
            return thisItem.equalsIgnoreCase(searchItem);
      }

   }

   class FormattingChooserDialog extends DubhOkCancelDialog
   {
      FormattingChooser m_chooser;
      public FormattingChooserDialog(JFrame parent)
      {
         super(parent, "Formatting Chooser", true);
      }

      public FormattingChooser getChooser()
      {
         return m_chooser;
      }

      public void setChooser(FormattingChooser f)
      {
         m_chooser = f;
         setPanel(f);
      }
   }

   class FontListModel extends AbstractListModel
   {
      private String[] m_fonts;

      public FontListModel()
      {
         m_fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().
            getAvailableFontFamilyNames();
      }

      public int getSize()
      {
         return m_fonts.length;
      }

      public Object getElementAt(int i)
      {
         return m_fonts[i];
      }

   }

   class Style
   {
      private String m_name;
      private int    m_style;

      public Style(String name, int fontStyle)
      {
         setName(name);
         setStyle(fontStyle);
      }

      public String getName() { return m_name; }
      public void setName(String n) { m_name = n; }

      public int getStyle() { return m_style; }
      public void setStyle(int s) { m_style = s; }

      public String toString() { return getName(); }

   }

   class StyleListModel extends AbstractListModel
   {

      private Style[] m_style =
         new Style[] {
            new Style("Plain",  Font.PLAIN),
            new Style("Bold",   Font.BOLD),
            new Style("Italic", Font.ITALIC),
            new Style("Bold Italic", Font.BOLD + Font.ITALIC)
         };

      public int getSize()
      {
         return m_style.length;
      }

      public Object getElementAt(int i)
      {
         return m_style[i];
      }

   }

   class SizeListModel extends AbstractListModel
   {
      private Integer[] m_sizes = new Integer[] {
         new Integer(8), new Integer(9), new Integer(10),
         new Integer(12), new Integer(14), new Integer(16),
         new Integer(18), new Integer(20), new Integer(22),
         new Integer(24), new Integer(26), new Integer(28),
         new Integer(36), new Integer(46), new Integer(72)
      };

      public int getSize()
      {
         return m_sizes.length;
      }

      public Object getElementAt(int i)
      {
         return m_sizes[i];
      }


   }

   class ColorComboBoxModel extends AbstractListModel implements ComboBoxModel
   {
      private Object m_selection;

      private Color[] m_colors = new Color[] {
         Color.black, Color.blue, Color.cyan, Color.green, Color.pink,
         Color.red, Color.yellow, Color.orange, Color.white,
         Color.darkGray, Color.gray, Color.lightGray
      };

      public int getSize()
      {
         return m_colors.length;
      }

      public Object getElementAt(int i)
      {
         return m_colors[i];
      }

      public void setSelectedItem(Object o)
      {
         m_selection = o;
      }

      public Object getSelectedItem()
      {
         return m_selection;
      }
   }

   class TextFieldListBinder implements ListSelectionListener,
                                        DocumentListener,
                                        ValidatorPanel.Validator

   {
      private JTextField m_textField;
      private TextFieldActivatedList m_list;
      private boolean m_valid = false;


      public TextFieldListBinder(JTextField tf, TextFieldActivatedList list)
      {
         m_textField = tf;
         m_list = list;
      }

      // ListSelectionListener interface

      public void valueChanged(ListSelectionEvent e)
      {
         //
         // Mustn't change text if selection was changed as a result of
         // text the user entered.
         //
         try
         {
            if (!m_textField.getText().equals(m_list.getSelectedValue().toString()))
            {
               m_textField.setText(m_list.getSelectedValue().toString());
               m_valid = true;
               checkValid();
            }
         }
         catch (Throwable t)
         {
         }
      }

      private void docChange()
      {
         boolean ok = m_list.selectItemStartingWith(m_textField.getText());
         if (m_list != m_lstSize)
         {
            m_valid = ok;
            checkValid();
         }
         else
         {
         // hacky case for the size list...
            try
            {
               m_valid = (Integer.parseInt(m_textField.getText()) >= MIN_FONTSIZE);
            }
            catch (NumberFormatException nfe)
            {
               m_valid = false;
            }
            checkValid();
         }
      }

      // DocumentListener interface

      public void changedUpdate(DocumentEvent e)
      {
         docChange();
      }

      public void insertUpdate(DocumentEvent e)
      {
         docChange();
      }

      public void removeUpdate(DocumentEvent e)
      {
         docChange();
      }

      // ValidatorPanel.Validator interface

      public boolean isValid()
      {
         return m_valid;
      }

   }

}



//
// Old Revision History:
//
// * <LI>0.1 [14/05/98]: Initial Revision
// * <LI>0.2 [07/06/98]: Removed all references to classes in NewsAgent.
// * <LI>0.3 [17/06/98]: Added isEnabled().
// * <LI>0.4 [03/12/98]: Updated to Swing 1.1
// * <LI>0.5 [08/12/98]: Added dialog version
// * <LI>1.0 [12/12/98]: Added NLS support, changed dialog appearance to match
// *   word 97's font dialog (massive internal change; interface should be the
// *   same)


// New Revision History:
//
// $Log: not supported by cvs2svn $
// Revision 1.6  2000/08/19 21:16:58  briand
// Now uses Java 2 GraphicsEnvironment to get font list.
//
//




