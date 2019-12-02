package org.freeciv.client.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.freeciv.client.Client;
import org.freeciv.client.dialog.util.VerticalFlowPanel;
import org.freeciv.net.PacketConstants;
import org.freeciv.net.PktPlayerRequest;

/**
 * Implementation of the tax rate panel.  This updates the economy values
 * itself when "Okay" is pressed, or simply hides when "Cancel" is pressed.
 *
 * Maybe the economy values should be updated by another class, but where?
 *
 * @author Ben Mazur
 */
class ImplTaxRates extends VerticalFlowPanel implements DlgTaxRates
{
  private JLabel m_labMaxRate = new JLabel( "", JLabel.CENTER );
  private RatePanel m_panTax = new RatePanel( translate( "Taxes" ) );
  private RatePanel m_panScience = new RatePanel( translate( "Science" ) );
  private RatePanel m_panLuxury = new RatePanel( translate( "Luxuries" ) );
  private JPanel m_panButtons = new JPanel();
  private JButton m_butOK = new JButton( translate( "OK" ) );
  private JButton m_butCancel = new JButton( translate( "Cancel" ) );

  private Client m_client;
  JDialog m_dialog;
  private DialogManager m_dlgManager;

  public ImplTaxRates( DialogManager mgr, Client c )
  {
    m_client = c;
    m_dlgManager = mgr;
    m_panTax.getSlider().addChangeListener( new ChangeListener()
    {
      public void stateChanged( ChangeEvent ev )
      {
        adjustRates( m_panTax, m_panScience, m_panLuxury );
      }
    } );
    m_panScience.getSlider().addChangeListener( new ChangeListener()
    {
      public void stateChanged( ChangeEvent ev )
      {
        adjustRates( m_panScience, m_panTax, m_panLuxury );
      }
    } );
    m_panLuxury.getSlider().addChangeListener( new ChangeListener()
    {
      public void stateChanged( ChangeEvent ev )
      {
        adjustRates( m_panLuxury, m_panTax, m_panScience );
      }
    } );
    this.addRow( m_labMaxRate );
    this.addRow( m_panTax );
    this.addRow( m_panScience );
    this.addRow( m_panLuxury );
    setupButtonPanel();
  }

  private Client getClient()
  {
    return m_client;
  }

  private void setupButtonPanel()
  {
    m_butOK.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        requestChangeRates( m_panTax.getRate(),  m_panScience.getRate(),
                            m_panLuxury.getRate() );
        //getClient().updateInfoLabel();
        undisplay();
      }
    } );
    m_butCancel.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        undisplay();
      }
    } );

    m_panButtons.setLayout( new FlowLayout() );
    m_panButtons.add( m_butOK );
    m_panButtons.add( m_butCancel );
    this.addRow( m_panButtons );
  }

  /**
   * Resets the displayed rates from the client object.
   */
  public void refresh()
  {
    m_labMaxRate.setText( translate(
      getClient().getGame().getCurrentPlayer().getGovernment().getName() +
      " max rate: " +
      getClient().getGame().getCurrentPlayer().getGovernment().getMaxRate() + "%"
    ) );

    m_panTax.setRate( getClient().getGame().getCurrentPlayer().getEconomy().getTax() );
    m_panScience.setRate( getClient().getGame().getCurrentPlayer().getEconomy().getScience() );
    m_panLuxury.setRate( getClient().getGame().getCurrentPlayer().getEconomy().getLuxury() );
  }

  /**
   * Sends a packet specifying the new tax rates
   */
  private void requestChangeRates( int tax, int science, int luxury )
  {
    PktPlayerRequest packet = new PktPlayerRequest();
    packet.setType( PacketConstants.PACKET_PLAYER_RATES );
    packet.tax = tax;
    packet.science = science;
    packet.luxury = luxury;
    getClient().sendToServer( packet );
  }



  /**
   * Adjust all three rates based on the movement of one of them.
   *
   * @param changed the RatePanel being changed
   * @param other0 one of the other two RatePanels
   * @param other1 the second other RatePanel
   */
  private void adjustRates( RatePanel changed, RatePanel other0, RatePanel other1 )
  {
    int min, max;
    int rate = (int)Math.round( changed.getRate() / 10.0 ) * 10;
    int govtMax = getClient().getGame().getCurrentPlayer().getGovernment().getMaxRate();
    // determine minimum value
    if( other0.isLocked() && other1.isLocked() )
    {
      min = 100 - ( other0.getRate() + other1.getRate() );
    }
    else if( other0.isLocked() )
    {
      min = Math.max( 100 - other1.getRate() - govtMax, 0 );
    }
    else if( other1.isLocked() )
    {
      min = Math.max( 100 - other0.getRate() - govtMax, 0 );
    }
    else
    {
      min = 0;
    }
    // determine maximum value
    max = 100;
    if( other0.isLocked() )
    {
      max = 100 - other0.getRate();
    }
    if( other1.isLocked() )
    {
      max = 100 - other1.getRate();
    }
    // don't exceed gov't max rate
    max = Math.min( max, govtMax );
    // adjust the rate
    rate = Math.min( rate, max );
    rate = Math.max( rate, min );
    changed.setRate( rate );
    // deal with leftover percentage
    int leftover = 100 - rate - other0.getRate() - other1.getRate();
    if(leftover > 0)
    {
      // the lower rate gets adjusted up
      if( !other1.isLocked() && ( other0.isLocked() || other0.getRate() > other1.getRate() ) ) {
        other1.setRate( other1.getRate() + leftover );
      }
      else
      {
        other0.setRate( other0.getRate() + leftover );
      }
    }
    else
    {
      // the higher rate gets adjusted down
      if( !other1.isLocked() && ( other0.isLocked() || other0.getRate() < other1.getRate() ) ) {
        other1.setRate( other1.getRate() + leftover );
      }
      else
      {
        other0.setRate( other0.getRate() + leftover );
      }
    }

  }

  public void display()
  {
    JDialog dlg = new JDialog(
      m_client.getMainWindow(), translate( "Set Tax Rates" ), true
    );
    dlg.getContentPane().setLayout( new BorderLayout() );
    dlg.getContentPane().add( ImplTaxRates.this, BorderLayout.CENTER );
    m_dialog = dlg;

    refresh();

    m_dlgManager.showDialog( m_dialog );
  }

  public void undisplay()
  {
    m_dlgManager.hideDialog( m_dialog );
  }

  /**
   * A quick class with a slider, a percentage label and a checkbox for locking.
   */
  private class RatePanel extends JPanel
  {
    JSlider m_sliRate = new JSlider( 0, 100 );
    JLabel m_labRate = new JLabel( "100%", JLabel.CENTER );
    JCheckBox m_chkLock = new JCheckBox( translate( "Lock" ) );
    int lastRate = 50;

    public RatePanel( String title )
    {
      m_sliRate.setMajorTickSpacing( 50 );
      m_sliRate.setMinorTickSpacing( 10 );
      m_sliRate.setPaintTicks( true );
      m_sliRate.setSnapToTicks( true );
      m_sliRate.setPaintTrack( true );

      setBorder( BorderFactory.createTitledBorder( translate( title ) ) );
      setLayout( new FlowLayout() );
      add( m_sliRate );
      add( m_labRate );
      add( m_chkLock );
    }

    public void setRate( int rate )
    {
      m_sliRate.setValue( rate );
      //TODO: this resizes the whole thing.  aarrrgh.
      m_labRate.setText( new Integer( m_sliRate.getValue() ).toString() + "%" );
    }

    public int getRate()
    {
      return m_sliRate.getValue();
    }

    public JSlider getSlider()
    {
      return m_sliRate;
    }

    public boolean isLocked()
    {
      return m_chkLock.isSelected();
    }

  }

  // localization
  private static String translate( String txt )
  {
    return org.freeciv.util.Localize.translate( txt );
  }
}
