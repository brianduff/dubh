package org.freeciv.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class ChooseRaceDialog extends JDialog
implements ActionListener, ChangeListener {

	Client client;
	JButton ok = new JButton(_("OK"));
	JTextField name = new JTextField("LeaderName");
	JRadioButton[] radios = new JRadioButton[races.length];
	ButtonGroup rgroup = new ButtonGroup();
	int selectedRace = -1;

	public ChooseRaceDialog(Client c) {
		super(c,_("Choose race"), false);
		client = c;
		getContentPane().setLayout(new BorderLayout());

		name.setBorder(new TitledBorder(_("Name")));
		getContentPane().add(BorderLayout.NORTH,name ); // name

		JPanel jp = new JPanel(new GridLayout((races.length/2)+(races.length%2),2));
		for ( int i =0; i < races.length; i++ )
		{
			JRadioButton jc = new JRadioButton(races[i]);
			radios[i] = jc;
			rgroup.add(jc);
			jc.addChangeListener(this);
			jp.add(jc);
		}
		jp.setBorder(new TitledBorder(_("Race")));
		getContentPane().add(BorderLayout.CENTER, jp); // races

		getContentPane().add(BorderLayout.SOUTH, ok); // ok button
		ok.addActionListener(this);

		pack();
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		show();
	}

	public void actionPerformed(ActionEvent e)
	{
		int i =0;
		while ( i < radios.length )
		{
			if (radios[i].isSelected())
				break;
			i++;
		}
		if ( i == radios.length )
			return;

		client.setRaceInfo(i,races[i],name.getText());
		client.sendRaceInfo();
	}

	public void toggleAvailableRace(int bitfield)
	{
		for ( int i =0; i < races.length; i++ )
		{
			if ( (bitfield & (1<<i)) != 0 )
			{
				radios[i].setSelected(false);
				radios[i].setEnabled(false);
			}
			else
				radios[i].setEnabled(true);
		}
	}

	public void stateChanged(ChangeEvent evt)
	{
		Object obj = evt.getSource();
		int i =0;
		while ( true )
		{
			if (radios[i] == obj)
				break;
			i++;
		}
		if ( radios[i].isSelected() )
			name.setText(raceLeaders[i]);
	}


	static final String[] raceLeaders = {
		"Caesar",
		"Hammurabi",
		"Frederick",
		"Ramesses",
		"Lincoln",
		"Alexander",
		"Gandhi",
		"Stalin",
		"Shaka",
		"Napoleon",
		"Montezuma",
		"Mao",
		"Elizabeth",
		"Genghis"
		};

	static final String[] races =
		{ "Roman",
		"Babylonian",
		"German",
		"Egyptian",
		"American",
		"Greek",
		"Indian",
		"Russian",
		"Zulu",
		"French",
		"Aztec",
		"Chinese",
		"English",
		"Mongol"};




	private static String _(String txt)
	{
		return Localize.translation.translate(txt);
	}


}
