namespace Dubh.WinBlock
{
    using System;
    using System.Drawing;
    using System.Collections;
    using System.ComponentModel;
    using System.WinForms;

    /// <summary>
    ///    Summary description for AboutForm.
    /// </summary>
    public class AboutForm : System.WinForms.Form
    {
        /// <summary>
        ///    Required designer variable.
        /// </summary>
        private System.ComponentModel.Container components;
		private System.WinForms.PictureBox pictureBox1;
		private System.WinForms.LinkLabel llabLink;
		private System.WinForms.Label lblLicenseInfo;
		private System.WinForms.Label lblCopyright;
		private System.WinForms.Button btnOK;
		private System.WinForms.Label lblAppVersion;
		private System.WinForms.PictureBox pbWhiteImageArea;

		private Line m_topLine;
		private Line m_bottomLine;

		private System.WinForms.Label lblAppName;

        public AboutForm()
        {
            //
            // Required for Windows Form Designer support
            //
            InitializeComponent();

            //
            // TODO: Add any constructor code after InitializeComponent call
            //
			m_topLine = new Line();
			m_topLine.Location = new Point(0, pbWhiteImageArea.Height+1);
			m_topLine.Size = new Size(pbWhiteImageArea.Width, 3);
			
			m_bottomLine = new Line();
			m_bottomLine.Location = new Point(llabLink.Left, llabLink.Top - 5);
			m_bottomLine.Size = new Size(llabLink.Width, 3);

			this.Controls.Add(m_topLine);
			this.Controls.Add(m_bottomLine);
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
			System.Resources.ResourceManager resources = new System.Resources.ResourceManager (typeof(AboutForm));
			this.components = new System.ComponentModel.Container ();
			this.pictureBox1 = new System.WinForms.PictureBox ();
			this.lblAppVersion = new System.WinForms.Label ();
			this.lblLicenseInfo = new System.WinForms.Label ();
			this.lblAppName = new System.WinForms.Label ();
			this.lblCopyright = new System.WinForms.Label ();
			this.btnOK = new System.WinForms.Button ();
			this.pbWhiteImageArea = new System.WinForms.PictureBox ();
			this.llabLink = new System.WinForms.LinkLabel ();
			//@this.TrayHeight = 90;
			//@this.TrayLargeIcon = false;
			//@this.TrayAutoArrange = true;
			pictureBox1.Location = new System.Drawing.Point (8, 0);
			pictureBox1.Size = new System.Drawing.Size (408, 64);
			pictureBox1.TabIndex = 9;
			pictureBox1.TabStop = false;
			pictureBox1.Image = (System.Drawing.Image) resources.GetObject ("pictureBox1.Image");
			lblAppVersion.Location = new System.Drawing.Point (112, 104);
			lblAppVersion.Text = "Version 2.0.0";
			lblAppVersion.Size = new System.Drawing.Size (288, 16);
			lblAppVersion.TabIndex = 4;
			lblLicenseInfo.Location = new System.Drawing.Point (112, 168);
			lblLicenseInfo.Text = "This product is licenced under the Mozilla Public License, a copy of which is available in the distribution.";
			lblLicenseInfo.Size = new System.Drawing.Size (288, 48);
			lblLicenseInfo.TabIndex = 7;
			lblAppName.Location = new System.Drawing.Point (112, 88);
			lblAppName.Text = "Dubh WinBlock.NET";
			lblAppName.Size = new System.Drawing.Size (296, 16);
			lblAppName.TabIndex = 1;
			lblCopyright.Location = new System.Drawing.Point (112, 120);
			lblCopyright.Text = "Copyright (C) 1996 - 2001 Brian Duff";
			lblCopyright.Size = new System.Drawing.Size (288, 16);
			lblCopyright.TabIndex = 6;
			btnOK.Location = new System.Drawing.Point (328, 272);
			btnOK.Size = new System.Drawing.Size (80, 23);
			btnOK.TabIndex = 5;
			btnOK.Text = "OK";
			pbWhiteImageArea.BackColor = System.Drawing.Color.White;
			pbWhiteImageArea.Dock = System.WinForms.DockStyle.Top;
			pbWhiteImageArea.Size = new System.Drawing.Size (418, 72);
			pbWhiteImageArea.TabIndex = 3;
			pbWhiteImageArea.TabStop = false;
			llabLink.Text = "http://www.dubh.org";
			llabLink.Size = new System.Drawing.Size (296, 16);
			llabLink.TabIndex = 8;
			llabLink.TabStop = true;
			llabLink.Location = new System.Drawing.Point (112, 232);
			this.Text = "About WinBlock";
			this.MaximizeBox = false;
			this.AutoScaleBaseSize = new System.Drawing.Size (5, 13);
			this.BorderStyle = System.WinForms.FormBorderStyle.FixedDialog;
			this.Font = new System.Drawing.Font ("Tahoma", 8);
			this.MinimizeBox = false;
			this.ClientSize = new System.Drawing.Size (418, 303);
			this.Controls.Add (this.pictureBox1);
			this.Controls.Add (this.llabLink);
			this.Controls.Add (this.lblLicenseInfo);
			this.Controls.Add (this.lblCopyright);
			this.Controls.Add (this.btnOK);
			this.Controls.Add (this.lblAppVersion);
			this.Controls.Add (this.pbWhiteImageArea);
			this.Controls.Add (this.lblAppName);
		}
    }
}
