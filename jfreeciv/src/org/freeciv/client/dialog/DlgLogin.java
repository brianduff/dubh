package org.freeciv.client.dialog;

import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JFrame;
import org.freeciv.client.Client;

/**
 * The main interface to the login dialog
 */
public interface DlgLogin
{
  /**
   * Actually display the dialog
   */
	public void display();
	
	/**
	 * Returns true if the dialog was dismissed successfully
	 */
	public boolean isOK();
	
	/**
	 * Returns an input stream from the selected server
	 */
	public InputStream getInputStream();
	
	/**
	 * Returns an output stream from the selected server
	 */
	public OutputStream getOutputStream();

	public String getServerName();
	public int getPortNumber();
	public String getUserName();
}
