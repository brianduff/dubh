package org.freeciv.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.freeciv.net.*;


public class ResearchDialog extends JInternalFrame implements InternalFrameListener
{
	Client client;
	JLabel currentlyResearching = new JLabel();
	JLabel goal = new JLabel();
	JButton changeGoal = new JButton(_("Goal"));
	JList knownAdvancesList = new JList();
	JList advancesChoice = new JList();

	public ResearchDialog(Client c)
	{
		super(_("Research"),true,true,false,false);
		client = c;
		getContentPane().setLayout(new BorderLayout());
		JPanel west = new JPanel();
		west.setLayout(new BoxLayout(west,BoxLayout.Y_AXIS));
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
		JPanel south = new JPanel();
		south.setLayout(new BoxLayout(south,BoxLayout.X_AXIS));

		updateComponents();

		west.add(new JLabel(_("Known advances")));
		west.add(new JScrollPane(knownAdvancesList));
		center.add(new JLabel(_("Research")));
		// scrollpane below is not needed, but is used to get identical
		// look as list above
		center.add(new JScrollPane(advancesChoice));
		south.add(currentlyResearching);
		south.add(changeGoal);
		south.add(goal);
		south.setBorder(new BevelBorder(BevelBorder.RAISED));
		getContentPane().add(BorderLayout.SOUTH,south);
		getContentPane().add(BorderLayout.WEST,west);
		getContentPane().add(BorderLayout.CENTER,center);
		pack();

		advancesChoice.addMouseListener( new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					Object obj = advancesChoice.getSelectedValue();
					if ( obj == null )
						return;
					// add confirm dialog for tech change
					if ( client.getCurrentPlayer().researchpoints > 0 )
					{
						int result = JOptionPane.showInternalConfirmDialog(ResearchDialog.this,
									_("You will be penalized by cutting science in half\nDo you want to change ?"),
									_("Scrap research"),JOptionPane.YES_NO_OPTION);
								if ( result != JOptionPane.YES_OPTION )
									return;
					}
					//client.commandChangeResearch((PktRulesetTech)obj);
				}
			}
		} );

		changeGoal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//PktRulesetTech t = new GoalPanel(client).showDialog();
				//if ( t != null )
				//	client.commandChangeTechGoal(t);
			}
		});

		addInternalFrameListener(this);

	}

	public void updateComponents()
	{
      /*
		PktPlayerInfo player =client.getCurrentPlayer();
		//currentlyResearching.setText(_("Currently researching ") +
		//	client.rulesetTechArr[player.researching].name.toUpperCase());
		//goal.setText(
		//	client.rulesetTechArr[player.tech_goal].name.toUpperCase() );
		Vector v = new Vector();
		// i = 1 to skip NONE
		for ( int i =1; i < Constants.A_LAST; i++ )
		{
			if ( player.inventions[i] )
				v.add(client.rulesetTechArr[i]);
		}
		knownAdvancesList.setListData(v);
		v = new Vector();
		// i=1 to skip NONE
		for ( int i =1; i < Constants.A_LAST; i++ )
		{
			if ( !player.inventions[i] && canDiscover(player,client.rulesetTechArr[i]) )
				v.add(client.rulesetTechArr[i]);
		}
		advancesChoice.setListData(v);
      */
	}

	public boolean canDiscover(PktPlayerInfo player, PktRulesetTech tech)
	{
		for ( int i=0; i < tech.req.length; i++ )
		{
			int req = tech.req[i];
			if ( req == Constants.A_LAST )
				return false;
			if ( req != Constants.A_NONE && !player.inventions[req] )
				return false;
		}
		return true;
	}

	public void internalFrameOpened(InternalFrameEvent e)
	{
	}

	public void internalFrameClosing(InternalFrameEvent e)
	{
	}

	public void internalFrameClosed(InternalFrameEvent e)
	{
	//	client.researchDialog = null;
//		client.desktop.remove(this); // is this needed ?
	//	client.map.requestFocus();
	}

	public void internalFrameIconified(InternalFrameEvent e)
	{
	}

	public void internalFrameDeiconified(InternalFrameEvent e)
	{
	}

	public void internalFrameActivated(InternalFrameEvent e)
	{
	}

	public void internalFrameDeactivated(InternalFrameEvent e)
	{
	}



	class GoalPanel extends JPanel {

      /*
		JDialog dialog;
		Client client;

		JButton ok = new JButton(_("OK"));
		JButton cancel = new JButton(_("Cancel"));

		PktRulesetTech returnValue;
		JList goal = new JList();

		public GoalPanel(Client c) {
			client = c;
			setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			Vector v = new Vector(100);
			PktRulesetTech[] techs = client.rulesetTechArr;

	techloop:
			for ( int i=1; i < techs.length;i++)
			{
				for ( int j =0; j < techs[i].req.length; j++ )
				{
					if ( techs[i].req[j] == Constants.A_LAST )
						continue techloop;
				}
				v.add(techs[i]);
			}
			goal.setPrototypeCellValue("XXXXXXXXXXXXXXXXXXXXX");
			goal.setListData(v);
			add(new JScrollPane(goal));
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
			p.add(ok);
			p.add(cancel);
			add(p);
			cancel.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					returnValue = null;
					dialog.setVisible(false);
					dialog.dispose();
				}
			} );

			ok.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt)
				{
					returnValue = (PktRulesetTech)goal.getSelectedValue();
					dialog.setVisible(false);
					dialog.dispose();
				}

			} );
			goal.addMouseListener( new MouseAdapter() {
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount() == 2)
					{
						ok.doClick();
					}
				}
			} );

		}

		public PktRulesetTech showDialog()
		{
			dialog = new JDialog(client, _("Choose research goal"), true);
			java.awt.Container contentPane = dialog.getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(this, BorderLayout.CENTER);

			dialog.pack();
			dialog.setLocationRelativeTo(client);
			dialog.show();
			return returnValue;
		}
      */
	}



	private static String _(String txt)
	{
		return Localize.translation.translate(txt);
	}	

}