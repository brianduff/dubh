/*
 * Written by Artur Biesiadowski <abies@pg.gda.pl>
 * This file is public domain - you can use/modify/distribute it as long
 * as some credit is given to me. You are not required to keep it open
 * sourced, nor to give back all changes to me, BUT if you do, everybody
 * will benefit.
 * For latest version contact me at <abies@pg.gda.pl> or check
 * http://www.gjt.org/servlets/JCVSlet/list/gjt/top.org.gjt.abies
 * This file comes with no guarantee of anything - you have been WARNED.
 */
package org.gjt.abies;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;


public class TranslationEditor extends JFrame
{

	public TranslationEditor()
	{
		super("Translation File editor");
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(0,0);
		setSize(d.width - 50, d.height -50);
		JDesktopPane jdp = new JDesktopPane();
		jdp.setOpaque(false);
		setContentPane(jdp);
		JMenuBar jmb = new JMenuBar();
		JMenu jm = new JMenu("File");
		jmb.add(jm);
		JMenuItem jmi = new JMenuItem("New");
		jmi.addActionListener( new ActionListener()
			{
				public void actionPerformed(ActionEvent evt )
				{
					addFile(null);
				}
			}
		);
		jm.add(jmi);
		jmi = new JMenuItem("Open");
		jmi.addActionListener( new ActionListener()
			{
				public void actionPerformed(ActionEvent evt )
				{
					JFileChooser jfc = new JFileChooser();
					int answer = jfc.showOpenDialog(TranslationEditor.this);
					if ( answer == JFileChooser.APPROVE_OPTION )
					{
						addFile(jfc.getSelectedFile().getPath());
					}
				}
			}
		);
		jm.add(jmi);
		jm.addSeparator();
		jmi = new JMenuItem("Exit");
		jm.add(jmi);
		setJMenuBar(jmb);

	}

	public void addFile( String str )
	{
		File f = null;
		if ( str != null )
			f = new File(str);
		if ( f != null && (!f.exists() || !f.isFile()) )
		{
			JOptionPane.showInternalMessageDialog(getContentPane(),"File " + f + " not found",
				"File not found", JOptionPane.ERROR_MESSAGE );
			return;
		}
		// addInternalFrame(new Edit(f));
		try {
			Edit e = new Edit(f);
			getContentPane().add(e);
			e.show();
		} catch ( IOException e )
			{
					JOptionPane.showInternalMessageDialog(getContentPane(),e.toString(),
						"Error reading file", JOptionPane.ERROR_MESSAGE );
			}
	}

	public static void main( String argv[] )
	{
		TranslationEditor te = new TranslationEditor();
		te.show();
		for ( int i =0; i < argv.length; i++ )
		{
			te.addFile(argv[i]);
		}
	}

	static int untitledCount = 1;

	static class StringPair
	{
		public String orig;
		public String trans;

		public StringPair(String anOriginal, String aTranslation)
		{
			orig = anOriginal;
			trans = aTranslation;
		}

		public StringPair()
		{
		}

		public String toString()
		{
			return orig + " = " + trans;
		}
	}


	static class MyCellRenderer extends DefaultListCellRenderer
	{
			public MyCellRenderer()
			{
					setOpaque(true);
			}

			public Component getListCellRendererComponent(
					JList list,
					Object value,
					int index,
					boolean isSelected,
					boolean cellHasFocus)
			{

					StringPair sp = (StringPair)value;

					if ( sp.orig.equals(sp.trans) )
					{
						if (isSelected) {
							setBackground(list.getSelectionBackground());
							setForeground(Color.red);
						}
						else {
							setBackground(list.getBackground());
							setForeground(Color.red);
						}
					}
					else
					{
						if (isSelected) {
							setBackground(list.getSelectionBackground());
							setForeground(list.getSelectionForeground());
						}
						else {
							setBackground(list.getBackground());
							setForeground(list.getForeground());
						}
					}

					setText(sp.orig);
					setEnabled(list.isEnabled());
					setFont(list.getFont());
					setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
					return this;
			}
	}



	class Edit extends JInternalFrame implements ListSelectionListener
	{
		File origFile;
		JTextArea origText = new JTextArea(5,50);
		JTextArea translatedText = new JTextArea(5,50);
		DefaultListModel lmodel = new DefaultListModel();
		JList transList = new JList(lmodel);
		StringPair lastPair = null;
		boolean changed = false;
		JButton nextMissing = new JButton("Next missing");
		JButton save = new JButton("Save");
		JButton saveAs = new JButton("Save as..");
		JButton close = new JButton("Close");
		boolean exiting = false;

		public Edit(File f) throws IOException
		{
			super(f != null ? f.getName() : ("Untitled-" +untitledCount++),
				true,true,true,true);
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

			origFile = f;
			TranslationFile tf;
			
			if ( origFile != null )
				tf = new TranslationFile(origFile);
			else
				tf = new TranslationFile();

			origText.setBorder(new TitledBorder("Original Text"));
			origText.setEditable(false);
			translatedText.setBorder(new TitledBorder("Translated Text"));
			getContentPane().setLayout(new BorderLayout());
			((JComponent)getContentPane()).setBorder(new EmptyBorder(10,10,10,10));
			JPanel central = new JPanel();
			central.setLayout(new BoxLayout(central,BoxLayout.Y_AXIS));
			JPanel inset = new JPanel();
			inset.setBorder(new EmptyBorder(2,2,10,2));
			inset.add(origText);
			central.add(inset, BorderLayout.NORTH);
			inset = new JPanel();
			inset.setBorder(new EmptyBorder(10,2,2,2));
			inset.add(translatedText);
			central.add(inset, BorderLayout.SOUTH);
			getContentPane().add(central,BorderLayout.CENTER);
			getContentPane().add(new JScrollPane(transList), BorderLayout.EAST);
			transList.setFixedCellWidth(200);
			transList.setCellRenderer(new MyCellRenderer());
			Enumeration e = tf.getAllKeys();
			while ( e.hasMoreElements() )
			{
				StringPair sp = new StringPair();
				String key = (String)e.nextElement();
				String val = tf.getTranslation(key);
				if ( val == null )
					val = key; // should not happen
				sp.orig = key;
				sp.trans = val;
				lmodel.addElement(sp);
			}
			transList.getSelectionModel().addListSelectionListener(this);
			JPanel buttons = new JPanel();
			buttons.setBorder(new EmptyBorder(2,2,2,2));
			buttons.setLayout(new BoxLayout(buttons,BoxLayout.X_AXIS));
			buttons.add(nextMissing);
			buttons.add(save);
			buttons.add(saveAs);
			buttons.add(close);
			getContentPane().add(buttons, BorderLayout.SOUTH);
			addInternalFrameListener( new InternalFrameAdapter()
				{
					public void internalFrameClosing(InternalFrameEvent e)
					{
						if ( exiting )
						{
							try {
								exiting = true;
								setClosed(true);
								dispose();
							} catch (Exception exc )
								{}
						}
						else
						{
							checkExit();
						}
					}

				}
			);

			close.addActionListener( new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						checkExit();
					}
				}
			);
			save.addActionListener( new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						trySave();
					}
				}
			);
			saveAs.addActionListener( new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						trySaveAs();
					}
				}
			);
			pack();
		}

		public void valueChanged(ListSelectionEvent evt )
		{
			if ( lastPair != null )
			{
				String txt = translatedText.getText();
				if ( !lastPair.trans.equals(txt) )
				{
							lastPair.trans = txt;
							changed = true;
							changeTitle();
				}
			}

			StringPair sp = (StringPair)transList.getSelectedValue();
			if ( sp != null )
			{
				origText.setText(sp.orig);
				translatedText.setText(sp.trans);
			}
			lastPair = sp;
		}

		String[] opts =  { "Cancel", "Save","Save As..", "Exit" };

		public void checkExit()
		{
			int answer;
			valueChanged(null);
			
			if ( changed == false )
				answer = 3;
			else
				answer = JOptionPane.showOptionDialog(null,"Unsaved changed - are you sure ?",
					"Confirm exit", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null,
					opts , "Cancel" );
			boolean shouldExit = false;
			switch ( answer )
			{
				case 1: // save
					shouldExit = trySave();
					break;
				case 2: // save as
					shouldExit = trySaveAs();
					break;
				case 3: // exit
					shouldExit = true;
					break;
				default:
					break;
			}
			if ( shouldExit )
			{
					try {
						exiting = true;
						setClosed(true);
						dispose();
					} catch (Exception exc )
						{}
			}
		}

		public boolean trySave()
		{
			if ( origFile == null )
				return trySaveAs();
			try {
				saveInternal();
				return true;
			} catch ( IOException e )
				{
					return false;
				}
		}

		public boolean trySaveAs()
		{
			try {
				JFileChooser jfc;
				if ( origFile != null )
				{
					jfc = new JFileChooser(origFile.getCanonicalFile().getParentFile());
					jfc.setSelectedFile(origFile);
				}
				else
				{
					jfc = new JFileChooser();
				}
				int answer = jfc.showSaveDialog(this);

				if(answer == JFileChooser.APPROVE_OPTION)
				{
					origFile = jfc.getSelectedFile();
					saveInternal();
					changeTitle();
					return true;
				}
				return false;
								
			} catch ( IOException e )
				{
					return false;
				}
		}

		private void saveInternal() throws IOException
		{
				FileWriter fw = new FileWriter(origFile);
				TranslationFile tf = new TranslationFile();
				Enumeration elem = lmodel.elements();
				while ( elem.hasMoreElements() )
				{
					StringPair sp = (StringPair)elem.nextElement();
					tf.setTranslation(sp.orig,sp.trans);
				}
				tf.save(fw);
				fw.close();
				changed = false;
				changeTitle();
		}

		private void changeTitle()
		{
			String title = (changed ? "*" : "") + origFile.getName();
			if ( !title.equals(getTitle()) )
				setTitle(title);
		}

	}

}