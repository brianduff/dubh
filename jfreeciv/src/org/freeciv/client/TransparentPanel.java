package org.freeciv.client;

import java.awt.*;
import javax.swing.*;


public class TransparentPanel extends JPanel
{
	AlphaComposite alpha;

	public TransparentPanel(Color c, float anAlpha)
	{
		setBackground(c);
		setOpaque(false);
		alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,anAlpha);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Insets b = getInsets();
		Graphics2D g2 = (Graphics2D)g;
		g2.setComposite(alpha);
		g2.setColor(getBackground());
		g2.fillRect(b.left,b.top,
			getWidth()-b.left-b.right,getHeight()-b.top-b.bottom);
		g2.setPaintMode();
	}

} 