package org.freeciv.client.panel;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import org.freeciv.client.ui.util.HyperlinkButton;

/**
 * The quick command panel has a set of hyperlinks which can be used to
 * quickly carry out commands relevant in the current context.
 *
 * @author Brian Duff
 */
public class QuickCommand extends VerticalFlowPanel
{
  public QuickCommand( Client c )
  {
    // Just Testing: We need a more formal way of doing this, and should
    // automatically add / remove actions as they are enabled and disabled..
    // will be cool when finished :)

    this.addRow(new HyperlinkButton( c.getAction( "ACTEndTurn" ) ));
    this.addRow(new HyperlinkButton( c.getAction( "ACTDisconnect")));
    this.addRow(new HyperlinkButton( c.getAction( "ACTQuit")));
    this.addRow(new JLabel( " " ));
    this.addRow(new HyperlinkButton( c.getAction( "UACTPillage")));
    this.addRow(new HyperlinkButton( c.getAction( "UACTFortify")));

    this.addSpacerRow( new JPanel() );
  }
}