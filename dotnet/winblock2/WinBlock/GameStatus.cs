namespace Dubh.WinBlock
{
    using System;
    using System.Collections;
    using System.Core;
    using System.ComponentModel;
    using System.Drawing;
    using System.Data;
    using System.WinForms;

    /// <summary>
    ///    A control that displays the current "status" of the 
    ///    game.
    /// </summary>
    public class GameStatus : System.WinForms.UserControl
    {
		private const int PADDING = 8;

        /// <summary> 
        ///    Required designer variable.
        /// </summary>
        private System.ComponentModel.Container components;
		private System.WinForms.Label lblVersion;
		private System.WinForms.Label lblAppName;
		private System.WinForms.Label lblLives;
		private System.WinForms.Label lblScore;
		private System.WinForms.Label lblScoreTitle;
		private System.WinForms.Label lblLivesTitle;

        public GameStatus()
        {
            // This call is required by the WinForms Form Designer.
            InitializeComponent();

            // TODO: Add any initialization after the InitForm call
			initAppDetails();
        }

		/// <summary>
		/// Layout the status area in its container. Oh, for Java
		/// layout managers :)
		/// </summary>
		/// <param name="parent">The parent container</param>
		public void layout(MainWindow parent)
		{
			int borderW = SystemInformation.FrameBorderSize.Width;
			int borderH = SystemInformation.FrameBorderSize.Height;

			this.Width = parent.Width - (borderW * 2);

			lblScoreTitle.Location = new Point(
				parent.Width - borderW - lblScoreTitle.Width - PADDING,
				0
			);

			lblScore.Location = new Point(
				lblScoreTitle.Location.X,
				lblScoreTitle.Height
			);

			lblAppName.Size = new Size(
				parent.Width - (2* borderW) - (4 * PADDING) - lblScore.Width - lblLives.Width,
				lblAppName.Height
			);

			lblVersion.Size = new Size(
				lblAppName.Width,
				lblVersion.Height
			);
			
			Refresh();
		}

		/// <summary>
		/// Initialize the app name and version label text from the
		/// correct values from the Application object.
		/// </summary>
		private void initAppDetails()
		{
			lblVersion.Text = "Version: "+Application.ProductVersion;
			lblAppName.Text = Application.ProductName;
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
		/// The score property as displayed in the control
		/// </summary>
		public string Score
		{
			set
			{
				lblScore.Text = value;
				lblScore.Refresh();
			}
		}

		public string Lives
		{
			set
			{
				lblLives.Text = value;
				lblLives.Refresh();
			}
		}

        /// <summary> 
        ///    Required method for Designer support - do not modify 
        ///    the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
		{
			this.components = new System.ComponentModel.Container ();
			this.lblLivesTitle = new System.WinForms.Label ();
			this.lblLives = new System.WinForms.Label ();
			this.lblVersion = new System.WinForms.Label ();
			this.lblScoreTitle = new System.WinForms.Label ();
			this.lblAppName = new System.WinForms.Label ();
			this.lblScore = new System.WinForms.Label ();
			//@this.TrayLargeIcon = false;
			//@this.TrayAutoArrange = true;
			//@this.TrayHeight = 0;
			lblLivesTitle.Location = new System.Drawing.Point (8, 0);
			lblLivesTitle.Text = "Lives Remaining:";
			lblLivesTitle.Size = new System.Drawing.Size (100, 16);
			lblLivesTitle.Font = new System.Drawing.Font ("Tahoma", 8);
			lblLivesTitle.TabIndex = 0;
			lblLives.Location = new System.Drawing.Point (8, 16);
			lblLives.Text = "0";
			lblLives.Size = new System.Drawing.Size (100, 27);
			lblLives.BorderStyle = System.WinForms.BorderStyle.Fixed3D;
			lblLives.ForeColor = System.Drawing.Color.Lime;
			lblLives.Font = new System.Drawing.Font ("Times New Roman", 18, System.Drawing.FontStyle.Bold);
			lblLives.TabIndex = 3;
			lblLives.BackColor = (System.Drawing.Color) System.Drawing.Color.FromARGB (64, 0, 64);
			lblLives.TextAlign = System.WinForms.HorizontalAlignment.Center;
			lblVersion.Location = new System.Drawing.Point (112, 32);
			lblVersion.Text = "Version: 0.0";
			lblVersion.Size = new System.Drawing.Size (176, 16);
			lblVersion.Font = new System.Drawing.Font ("Tahoma", 8);
			lblVersion.TabIndex = 5;
			lblVersion.TextAlign = System.WinForms.HorizontalAlignment.Center;
			lblScoreTitle.Location = new System.Drawing.Point (288, 0);
			lblScoreTitle.Text = "Score:";
			lblScoreTitle.Size = new System.Drawing.Size (100, 16);
			lblScoreTitle.Font = new System.Drawing.Font ("Tahoma", 8);
			lblScoreTitle.TabIndex = 1;
			lblAppName.Location = new System.Drawing.Point (112, 0);
			lblAppName.Text = "WinBlock";
			lblAppName.Size = new System.Drawing.Size (176, 32);
			lblAppName.ForeColor = System.Drawing.Color.Purple;
			lblAppName.Font = new System.Drawing.Font ("Comic Sans MS", 20, System.Drawing.FontStyle.Bold);
			lblAppName.TabIndex = 4;
			lblAppName.TextAlign = System.WinForms.HorizontalAlignment.Center;
			lblScore.Location = new System.Drawing.Point (288, 16);
			lblScore.Text = "0";
			lblScore.Size = new System.Drawing.Size (100, 27);
			lblScore.BorderStyle = System.WinForms.BorderStyle.Fixed3D;
			lblScore.ForeColor = System.Drawing.Color.Lime;
			lblScore.Font = new System.Drawing.Font ("Times New Roman", 18, System.Drawing.FontStyle.Bold);
			lblScore.TabIndex = 2;
			lblScore.BackColor = (System.Drawing.Color) System.Drawing.Color.FromARGB (64, 0, 64);
			lblScore.TextAlign = System.WinForms.HorizontalAlignment.Center;
			this.Size = new System.Drawing.Size (392, 48);
			this.Controls.Add (this.lblVersion);
			this.Controls.Add (this.lblAppName);
			this.Controls.Add (this.lblLives);
			this.Controls.Add (this.lblScore);
			this.Controls.Add (this.lblScoreTitle);
			this.Controls.Add (this.lblLivesTitle);
		}
    }
}