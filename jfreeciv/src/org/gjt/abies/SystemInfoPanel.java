package org.gjt.abies;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class SystemInfoPanel extends JTabbedPane
{

	public SystemInfoPanel()
	{
		super();
		addTab(_("VM info"), createVMPane() );
		addTab(_("Memory"), createMemoryPane() );
		addTab(_("Properties"), createPropertiesPane() );
	}

	public SystemInfoPanel(JPanel p, String name )
	{
		this();
		addTab(name,p);
	}


	public static Box createVMPane()
	{
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(6,1));

		JLabel jl;
		Font f = new Font("sansserif",Font.BOLD,18);


		jl = new JLabel(System.getProperty("java.version"));
		jl.setBorder(new TitledBorder(_("Java Version")));
		jl.setFont(f);
		jl.setForeground(Color.black);
		jp.add(jl);
		jl = new JLabel(System.getProperty("java.vendor"));
		jl.setBorder(new TitledBorder(_("Java Vendor")));
		jl.setFont(f);
		jl.setForeground(Color.black);
		jp.add(jl);
		jl = new JLabel(System.getProperty("java.vm.info"));
		jl.setBorder(new TitledBorder(_("VM Info")));
		jl.setFont(f);
		jl.setForeground(Color.black);
		jp.add(jl);
		jl = new JLabel(System.getProperty("os.name"));
		jl.setBorder(new TitledBorder(_("Operating System")));
		jl.setFont(f);
		jl.setForeground(Color.black);
		jp.add(jl);
		jl = new JLabel(System.getProperty("os.arch"));
		jl.setBorder(new TitledBorder(_("Architecture")));
		jl.setFont(f);
		jl.setForeground(Color.black);
		jp.add(jl);
		jl = new JLabel(System.getProperty("os.version"));
		jl.setBorder(new TitledBorder(_("OS Version")));
		jl.setFont(f);
		jl.setForeground(Color.black);
		jp.add(jl);
		jp.setMaximumSize(jp.getPreferredSize());


		Box box = new Box(BoxLayout.X_AXIS);
		box.add(Box.createHorizontalGlue());
		box.add(jp);
		box.add(Box.createHorizontalGlue());

		return box;
	}


	private static void updateLabels( JLabel freeMem,JLabel usedMem,
		JProgressBar jpb )
	{
		long free = Runtime.getRuntime().freeMemory()/1024;
		long total = Runtime.getRuntime().totalMemory()/1024;
		long used = total - free;

		freeMem.setText(_("Free memory ") + free + "k / " + total + "k");
		usedMem.setText(_("Used memory ") + used + "k / " + total + "k");
		jpb.setMinimum(0);
		jpb.setMaximum((int)(total));
		jpb.setValue((int)(used));
		jpb.setString(_("Used ") + ((used*100)/total) + "%" );
	}

	public static JPanel createMemoryPane()
	{
		JPanel jp = new JPanel();
		Font font = new Font("sanserif",Font.BOLD,26);
		jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
		final JLabel freeMem = new JLabel();
		freeMem.setFont(font);
		final JLabel usedMem = new JLabel();
		usedMem.setFont(font);
		final JProgressBar jpb = new JProgressBar(JProgressBar.HORIZONTAL);
		jpb.setStringPainted(true);
		jpb.setFont(font);

		final JButton runGC = new JButton(_("RunGC"));
		runGC.setFont(font);
		final JButton refresh = new JButton(_("Refresh"));
		refresh.setFont(font);
		updateLabels(freeMem,usedMem,jpb);
		jp.add(freeMem);
		jp.add(usedMem);
		jp.add(jpb);
		jp.add(runGC);
		jp.add(refresh);

		runGC.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.gc();
				try {
					Thread.currentThread().sleep(100);
				} catch ( InterruptedException exc)
					{}
				updateLabels(freeMem,usedMem,jpb);
			}
		} );

		refresh.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				updateLabels(freeMem,usedMem,jpb);
			}
		} );

		return jp;
	}

	public static JPanel createPropertiesPane()
	{
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		Properties p = System.getProperties();
		Object[][] rowdata = new Object[p.size()][2];
		int i =0;
		Iterator e = p.entrySet().iterator();
		while ( e.hasNext() )
		{
			Map.Entry val = (Map.Entry)e.next();
			rowdata[i][0] = val.getKey();
			rowdata[i][1] = val.getValue();
			i++;
		}
		JTable jt = new JTable(rowdata, new String[] {_("name"), _("value")} );
		jp.add(BorderLayout.CENTER,new JScrollPane(jt));
		return jp;
	}


	public static void main(String argv[])
	{
		JFrame jf = new JFrame(_("System info"));
		jf.getContentPane().add(new SystemInfoPanel());
		jf.pack();
		jf.show();
	}



	private static String _(String txt)
	{
		return Localize.translation.translate(txt);
	}

	
} 