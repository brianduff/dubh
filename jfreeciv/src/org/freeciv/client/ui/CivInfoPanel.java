package org.freeciv.client.ui;

import org.freeciv.client.*;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import javax.swing.*;
import java.awt.*;


public class CivInfoPanel extends VerticalFlowPanel
{
   private Client m_client;

   private JLabel m_labPopulation, m_labYear, m_labGold, m_labTax;

   public CivInfoPanel(Client c)
   {
      m_client = c;
      m_labPopulation = new JLabel("Pop");
      m_labYear = new JLabel("Year");
      m_labGold = new JLabel("Gold");
      m_labTax = new JLabel("Tax");

      addRow(m_labPopulation);
      addRow(m_labYear);
      addRow(m_labGold);
      addRow(m_labTax);

      setBorder(BorderFactory.createTitledBorder(_("Civ Info")));
   }

   public void setGold(int gold)
   {
      setAndValidate(m_labGold, "Gold: "+gold);
   }

   public void setYear(int year)
   {
      // TODO: AD & BC
      setAndValidate(m_labYear, "Year: "+year);
   }

   public void setTax(int tax, int science, int luxury)
   {
      setAndValidate(m_labTax, "Tax: "+tax+" Lux: "+luxury+" Sci: "+science);
   }

   public void setPop(int pop)
   {
      setAndValidate(m_labPopulation, "Population: "+formatPopulation(pop));
   }

   public void setNationName(String nationName)
   {
      setBorder(BorderFactory.createTitledBorder(_(nationName)));
      this.validate();
   }

   private String formatPopulation(int pop)
   {
      // TODO: format as 1,000,000 etc.
      return ""+pop;
   }

   private void setAndValidate(JLabel lab, String value)
   {
      lab.setText(value);
      this.validate();
   }

   private static String _(String txt)
   {
      return Localize.translation.translate(txt);
   }
}