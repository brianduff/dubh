package org.freeciv.client.dialog;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.freeciv.client.*;
import org.freeciv.net.*;
import org.freeciv.client.dialog.util.*;
import org.freeciv.client.action.*;
import org.freeciv.client.ui.util.*;


//
// TODO:
//
// The text field on the combo needs to be more intelligent about
// whether the user has typed something in or whether to default
// from items in the list. Theres an aborted attempt at getting it
// to work in here, this needs improving.

/**
 * The main interface to the nation dialog
 */
class ImplNation extends VerticalFlowPanel implements DlgNation, Constants
{
   private final static int NATION_COLUMNS = 5;

   private VerticalFlowPanel m_panNation = new VerticalFlowPanel();
   private JPanel m_panNationList = new JPanel();

   private JPanel m_panSex = new JPanel();
   private JPanel m_panCityStyle = new JPanel();

   private JComboBox m_cmbLeaderName = new JComboBox();

   private JPanel m_panButtons = new JPanel();
   private JButton m_butOK = new JButton(_("OK"));
   private ActionButton m_butDisconnect;
   private ActionButton m_butQuit;


   private ArrayList m_alNationButtons = new ArrayList();

   private JRadioButton m_rbMale = new JRadioButton(_("Male")),
       m_rbFemale = new JRadioButton(_("Female"));
   private JRadioButton[] m_arbNations;
   private JRadioButton[] m_arbCityStyles;

   private ButtonGroup m_bgNation = new ButtonGroup();
   private ButtonGroup m_bgSex = new ButtonGroup();
   private ButtonGroup m_bgCityStyle = new ButtonGroup();

   private boolean bFullyInitialized;

   private RulesetManager m_rs;
   private Client m_client;

   JDialog m_dialog;

   private DialogManager m_dlgManager;

   private boolean m_bNameAltered;
   private boolean m_bJustSelectedALeader;

   public ImplNation(DialogManager mgr, Client c)
   {
      m_client = c;
      bFullyInitialized = false;
      setupNationPanel();
      setupSexPanel();
      setupCityStylePanel();
      setupButtonPanel();
      m_dlgManager = mgr;


   }

   private void setupButtonPanel()
   {
      m_butDisconnect = new ActionButton(m_client.getAction("ACTDisconnect"));
      m_butQuit = new ActionButton(m_client.getAction("ACTQuit"));

      m_panButtons.setLayout(new FlowLayout());
      m_panButtons.add(m_butOK);
      m_panButtons.add(m_butDisconnect);
      m_panButtons.add(m_butQuit);

      this.addRow(m_panButtons);
   }


   private void setupNationPanel()
   {
      m_panNation.setBorder(BorderFactory.createTitledBorder(
         _("Select nation and name")
      ));

      m_panNation.addSpacerRow(m_panNationList);
      m_panNation.addRow(m_cmbLeaderName);

      m_cmbLeaderName.setEditable(true);
      m_cmbLeaderName.addActionListener(new LeaderNameComboListener());
      ((JTextField)m_cmbLeaderName.getEditor().getEditorComponent()).
         getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                m_bNameAltered = !m_bJustSelectedALeader && true;
                m_bJustSelectedALeader = false;
            }
            public void insertUpdate(DocumentEvent e) {
                m_bNameAltered = !m_bJustSelectedALeader && true;
                m_bJustSelectedALeader = false;
            }
            public void removeUpdate(DocumentEvent e) {
                m_bNameAltered = !m_bJustSelectedALeader && true;
                m_bJustSelectedALeader = false;
            }
         });

      this.addSpacerRow(m_panNation);
   }

   private void setupSexPanel()
   {
      m_panSex.setBorder(BorderFactory.createTitledBorder(
         _("Select your sex")
      ));

      m_bgSex.add(m_rbMale);
      m_bgSex.add(m_rbFemale);

      m_panSex.setLayout(new BorderLayout());
      m_panSex.add(m_rbMale, BorderLayout.WEST);
      m_panSex.add(m_rbFemale, BorderLayout.CENTER);

      m_rbMale.setSelected(true);

      this.addRow(m_panSex);
   }

   private void setupCityStylePanel()
   {
      m_panCityStyle.setBorder(BorderFactory.createTitledBorder(
         _("Select your city style")
      ));

      this.addRow(m_panCityStyle);
   }

   private void addNations()
   {
      int numNations = m_rs.getRulesetControl().playable_nation_count;
      m_arbNations = new JRadioButton[numNations];

      int nationsPerColumn = numNations / NATION_COLUMNS;


      m_panNationList.setLayout(new GridLayout(nationsPerColumn,
         NATION_COLUMNS));

      for (int i=0; i < numNations; i++)
      {
         m_arbNations[i] = new JRadioButton(m_rs.getRulesetNation(i).name);
         m_arbNations[i].addActionListener(m_nlp);
         m_panNationList.add(m_arbNations[i]);
         m_bgNation.add(m_arbNations[i]);
      }

      selectInitialNation();
   }

   private void addCityStyles()
   {
      m_panCityStyle.setLayout(new GridLayout());
      int b_s_num;
      ArrayList alCityStyles = new ArrayList();
      for (int i=0; i < m_rs.getRulesetControl().style_count; i++)
      {
         //System.out.println("Tech req of city style "+i+" is "+
         //   m_rs.getRulesetCity(i).techreq+" require "+A_NONE
         //);
         if (m_rs.getRulesetCity(i).techreq == A_NONE)
         {
            alCityStyles.add(m_rs.getRulesetCity(i).name);
         }
      }

      m_arbCityStyles = new JRadioButton[alCityStyles.size()];
      for (int i=0; i < alCityStyles.size(); i++)
      {
         JRadioButton rb = new JRadioButton((String)alCityStyles.get(i));
         m_arbCityStyles[i] = rb;
         if (i==0) rb.setSelected(true);
         m_bgCityStyle.add(rb);
         m_panCityStyle.add(rb);
      }
   }

   private void init()
   {
      if (!bFullyInitialized)
      {
         addNations();
         addCityStyles();
         bFullyInitialized = true;
      }

   }

   /**
    * Selects the specified nation, provided it is enabled.
    */
   private void selectNation(int num)
   {
      JRadioButton b = m_arbNations[num];
      b.setSelected(b.isEnabled());
   }

   private void selectInitialNation()
   {
      // Some suggestion on freeciv-dev that this should be random.
      // We just select the first enabled one for now.
      for (int i=0; i< m_arbNations.length; i++)
      {
         JRadioButton b = m_arbNations[i];
         if (b.isEnabled())
         {
            b.setSelected(true);
            break;
         }
      }
   }

   private int getSelectedNation()
   {
      for (int i=0; i < m_arbNations.length; i++)
      {
         if (m_arbNations[i].isSelected())
         {
            return i;
         }
      }
      return -1;
   }

   private int getSelectedCityStyle()
   {
      for (int i=0; i < m_arbCityStyles.length; i++)
      {
         if (m_arbCityStyles[i].isSelected())
         {
            return i;
         }
      }
      return -1;
   }

   private NationLeaderPopulator m_nlp = new NationLeaderPopulator();



   class NationLeaderPopulator implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         // If the user typed something into the text field, then
         // don't change it.
         if (m_bNameAltered)
            return;

         // TODO: hash the leadername items so that we don't have to
         // instantiate them over and over again.
         JRadioButton btn = (JRadioButton)e.getSource();
         int i;
         for (i=0; i < m_arbNations.length; i++)
         {
            if (m_arbNations[i] == btn)
               break;
         }

         if (i == m_arbNations.length)
         {
            throw new RuntimeException("Click on an unknown radio button.");
         }

         // Now get the leader names for the selected nation.
         PktRulesetNation nation = m_rs.getRulesetNation(i);
         int leaderCount = nation.leader_count;

         m_cmbLeaderName.removeAllItems();
         for (i=0; i < leaderCount; i++)
         {
            LeaderItem li = new LeaderItem();
            li.name = nation.leader_name[i];
            li.is_male = nation.leader_sex[i];
            m_cmbLeaderName.addItem(li);
         }
         m_bNameAltered = false;
      }
   }

   class LeaderItem
   {
      public String name;
      public boolean is_male;
      public String toString()
      {
         return name;
      }
   }

   public class LeaderNameComboListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         Object sel = m_cmbLeaderName.getSelectedItem();
         if (sel instanceof LeaderItem)
         {
            boolean male =  ((LeaderItem)sel).is_male;
            m_rbMale.setSelected(male);
            m_rbFemale.setSelected(!male);

            // Forget about whether the user typed in a name, they've
            // selected one from the list now and anything they typed
            // is forgotten forever :)
            m_bNameAltered = false;
            m_bJustSelectedALeader = true;
         }
      }
   }

   // DlgNation interface

   class TogglerRunnable implements Runnable
   {
      private int bits1, bits2;

      public TogglerRunnable(int bits1, int bits2)
      {
         this.bits1 = bits1; this.bits2 = bits2;
      }

      public void run()
      {
         int i, selected, mybits;

         mybits = bits1;

         System.out.println("Bits for nation toggler:");
         org.freeciv.net.AbstractPacket.printBits(bits1);
         org.freeciv.net.AbstractPacket.printBits(bits2);


         for (i=0; i<m_rs.getRulesetControl().playable_nation_count &&
            i < 32; i++)
         {
            m_arbNations[i].setEnabled(((mybits&1)==0));
            mybits>>=1;
         }

         mybits = bits2;
         for (i=32; i<m_rs.getRulesetControl().playable_nation_count; i++)
         {
            m_arbNations[i].setEnabled(((mybits&1)==0));
            mybits>>=1;
         }

         if ((selected = getSelectedNation())==-1)
            return;

         if (( bits1 & (1<<selected)) != 0 ||
             ( selected > 32 && (bits2 & (1<<(selected-32))) != 0))
         {
            selectInitialNation();
         }


      }

   }

   public void toggleAvailableRaces(int bits1, int bits2)
   {
      SwingUtilities.invokeLater(new TogglerRunnable(bits1, bits2));
   }



   public void display()
   {
      m_rs = m_client.getRulesetManager();

      init();

      JDialog dlg = new JDialog(m_client, _("Choose Race"), true);
      dlg.getContentPane().setLayout(new BorderLayout());
      dlg.getContentPane().add(ImplNation.this, BorderLayout.CENTER);
      m_dialog = dlg;
      m_butOK.addActionListener(ACTSendAllocNation.getInstance(m_client));
      m_dlgManager.showDialog(m_dialog);

   }

   public void undisplay()
   {
      m_dlgManager.hideDialog(m_dialog);
   }

   public int getNation()
   {
      return getSelectedNation();
   }

   public String getLeaderName()
   {
      return m_cmbLeaderName.getSelectedItem().toString();

   }

   public boolean isLeaderMale()
   {
      return m_rbMale.isSelected();
   }

   public int getCityStyle()
   {
      return getSelectedCityStyle();
   }


	// localization
	private static String _(String txt)
	{
		return Localize.translation.translate(txt);
	}
}