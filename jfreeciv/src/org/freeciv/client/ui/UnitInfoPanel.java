package org.freeciv.client.ui;

import org.freeciv.client.*;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import javax.swing.*;
import java.awt.*;


public class UnitInfoPanel extends VerticalFlowPanel
{
   private Client m_client;

   private JLabel m_labMoves, m_labTerrain, m_labHomeCity;

   public UnitInfoPanel(Client c)
   {
      m_client = c;
      m_labMoves = new JLabel("Moves");
      m_labTerrain = new JLabel("Terrain");
      m_labHomeCity = new JLabel("HomeCity");

      addRow(m_labMoves);
      addRow(m_labTerrain);
      addRow(m_labHomeCity);

      setBorder(BorderFactory.createTitledBorder(_("Unit Info")));
   }

   // TODO: Add listeners to client for changes in pop, year, gold and tax. (and
   // game state)



   private static String _(String txt)
   {
      return Localize.translation.translate(txt);
   }
}