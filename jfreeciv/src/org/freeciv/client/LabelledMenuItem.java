package org.freeciv.client;

import java.awt.*;
import java.beans.*;
import javax.swing.*;

public class LabelledMenuItem extends JMenuItem implements PropertyChangeListener
{

	public LabelledMenuItem(String name, KeyStroke key, Action act)
	{
		super(name);
		setAccelerator(key);
		addActionListener(act);
		act.addPropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		if ( "enabled".equals(evt.getPropertyName()) )
		{
			setEnabled(((Boolean)evt.getNewValue()).booleanValue());
		}
	}

}