namespace Dubh.WinBlock
{
    using System;
    using System.Drawing;
    using System.Collections;
    using System.ComponentModel;
    using System.WinForms;

    /// <summary>
    ///    The main window of WinBlock.NET
    /// </summary>
    public class MainWindow : System.WinForms.Form
    {

		private Court m_court;
		private GameStatus m_status;

        /// <summary>
        ///    Required designer variable.
        /// </summary>
        private System.ComponentModel.Container components;
		private System.WinForms.MenuItem miAbout;
		private System.WinForms.MenuItem menuItem10;
		private System.WinForms.MenuItem miContents;
		private System.WinForms.MenuItem miExit;
		private System.WinForms.MenuItem menuItem7;
		private System.WinForms.MenuItem miOptions;
		private System.WinForms.MenuItem menuItem5;
		private System.WinForms.MenuItem miPause;
		private System.WinForms.MenuItem miNewGame;
		private System.WinForms.MenuItem miHelp;
		private System.WinForms.MenuItem miGame;
		private System.WinForms.MainMenu mainMenu1;

        public MainWindow()
        {
            //
            // Required for Windows Form Designer support
            //
            InitializeComponent();

            //
            // TODO: Add any constructor code after InitializeComponent call
            //
			m_court = new Court();
			m_status = new GameStatus();
			this.Controls.Add(m_court);
			this.Controls.Add(m_status);
			m_status.Refresh();

			// Prevent the window being made smaller than it's original 
			// size.
			this.MinTrackSize = this.Size;
			
			layout();
		}

		/// <summary>
		/// Calculate the size of the court, based on the size and location of
		/// other controls in the main window.
		/// </summary>
		private void layout()
		{
			m_court.Location.X = 0;
			m_court.Location.Y = 0;
			m_court.Size = new Size(
				this.Width - (2 * SystemInformation.FrameBorderSize.Width),
				this.Height - m_status.Height - SystemInformation.FrameBorderSize.Height -
					SystemInformation.CaptionHeight - SystemInformation.MenuHeight
			);
			m_court.Refresh();

			m_status.Location = new Point(
				0,
				this.Height - m_status.Height - SystemInformation.FrameBorderSize.Height -
				SystemInformation.MenuHeight - SystemInformation.CaptionHeight
			);

			m_status.layout(this);


		}

		/// <summary>
		/// Show the options dialog.
		/// </summary>
		private void showOptions()
		{
			Preferences prefs = new Preferences();
			prefs.ShowDialog(this);
		}

		private void showAbout()
		{
			AboutForm f = new AboutForm();
			f.ShowDialog(this);
		}

        /// <summary>
        ///    Clean up any resources being used.
        /// </summary>
        public override void Dispose()
        {
            base.Dispose();
            components.Dispose();
        }

        /// <summary>
        ///    Required method for Designer support - do not modify
        ///    the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
		{
			this.components = new System.ComponentModel.Container ();
			this.miExit = new System.WinForms.MenuItem ();
			this.miPause = new System.WinForms.MenuItem ();
			this.miOptions = new System.WinForms.MenuItem ();
			this.menuItem5 = new System.WinForms.MenuItem ();
			this.miHelp = new System.WinForms.MenuItem ();
			this.miNewGame = new System.WinForms.MenuItem ();
			this.miAbout = new System.WinForms.MenuItem ();
			this.menuItem10 = new System.WinForms.MenuItem ();
			this.menuItem7 = new System.WinForms.MenuItem ();
			this.miGame = new System.WinForms.MenuItem ();
			this.mainMenu1 = new System.WinForms.MainMenu ();
			this.miContents = new System.WinForms.MenuItem ();
			//@this.TrayHeight = 90;
			//@this.TrayLargeIcon = false;
			//@this.TrayAutoArrange = true;
			miExit.Text = "Exit";
			miExit.Index = 5;
			miPause.Text = "Pause";
			miPause.Shortcut = System.WinForms.Shortcut.F3;
			miPause.Index = 1;
			miOptions.Text = "Options...";
			miOptions.Shortcut = System.WinForms.Shortcut.CtrlO;
			miOptions.Index = 3;
			miOptions.Click += new System.EventHandler (this.miOptions_Click);
			menuItem5.Text = "-";
			menuItem5.Index = 2;
			miHelp.Text = "Help";
			miHelp.Index = 1;
			miHelp.MenuItems.All = new System.WinForms.MenuItem[3] {this.miContents, this.menuItem10, this.miAbout};
			miNewGame.Text = "New Game";
			miNewGame.Shortcut = System.WinForms.Shortcut.F2;
			miNewGame.Index = 0;
			miAbout.Text = "About WinBlock...";
			miAbout.Shortcut = System.WinForms.Shortcut.CtrlA;
			miAbout.Index = 2;
			miAbout.Click += new System.EventHandler (this.miAbout_Click);
			menuItem10.Text = "-";
			menuItem10.Index = 1;
			menuItem7.Text = "-";
			menuItem7.Index = 4;
			miGame.Text = "Game";
			miGame.Index = 0;
			miGame.MenuItems.All = new System.WinForms.MenuItem[6] {this.miNewGame, this.miPause, this.menuItem5, this.miOptions, this.menuItem7, this.miExit};
			//@mainMenu1.SetLocation (new System.Drawing.Point (7, 7));
			mainMenu1.MenuItems.All = new System.WinForms.MenuItem[2] {this.miGame, this.miHelp};
			miContents.Text = "Contents...";
			miContents.Shortcut = System.WinForms.Shortcut.F1;
			miContents.Index = 0;
			this.Text = "WinBlock.NET";
			this.AutoScaleBaseSize = new System.Drawing.Size (5, 13);
			this.Menu = this.mainMenu1;
			this.ClientSize = new System.Drawing.Size (456, 425);
			this.Resize += new System.EventHandler (this.MainWindow_Resize);
		}

		protected void miAbout_Click (object sender, System.EventArgs e)
		{
			showAbout();
		}

		protected void miOptions_Click (object sender, System.EventArgs e)
		{
			showOptions();
		}

		protected void MainWindow_Resize (object sender, System.EventArgs e)
		{
			layout();
		}

		/// <summary>
        /// The main entry point for the application.
        /// </summary>
        public static void Main(string[] args) 
        {
            Application.Run(new MainWindow());
        }
    }


}
