namespace Dubh.WinBlock
{
    using System;
    using System.Drawing;
    using System.Collections;
    using System.ComponentModel;
    using System.WinForms;
    using System.Data;

    /// <summary>
    ///    Summary description for Form1.
    /// </summary>
    public class Preferences : System.WinForms.Form
    {
        /// <summary>
        ///    Required designer variable.
        /// </summary>
        private System.ComponentModel.Container components;
		private System.WinForms.Button btnCancel;
		private System.WinForms.Button btnOK;
		private System.WinForms.CheckBox cbLockMouse;
		private System.WinForms.CheckBox cbUseSound;
		private System.WinForms.PictureBox pictureBox3;
		private System.WinForms.PictureBox pictureBox2;
		private System.WinForms.PictureBox pictureBox1;
		private System.WinForms.GroupBox groupBox3;
		private System.WinForms.GroupBox groupBox2;
		private System.WinForms.ListBox lbBallColors;
		private System.WinForms.Label label4;
		private System.WinForms.Label label3;
		private System.WinForms.Label label2;
		private System.WinForms.TrackBar trbBallSpeed;
		private System.WinForms.Label label1;
		private System.WinForms.GroupBox groupBox1;

        public Preferences()
        {
            //
            // Required for Windows Form Designer support
            //
            InitializeComponent();

            //
            // TODO: Add any constructor code after InitializeComponent call
            //
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
			System.Resources.ResourceManager resources = new System.Resources.ResourceManager (typeof(Preferences));
			this.components = new System.ComponentModel.Container ();
			this.groupBox2 = new System.WinForms.GroupBox ();
			this.groupBox3 = new System.WinForms.GroupBox ();
			this.lbBallColors = new System.WinForms.ListBox ();
			this.groupBox1 = new System.WinForms.GroupBox ();
			this.cbLockMouse = new System.WinForms.CheckBox ();
			this.label3 = new System.WinForms.Label ();
			this.btnCancel = new System.WinForms.Button ();
			this.cbUseSound = new System.WinForms.CheckBox ();
			this.btnOK = new System.WinForms.Button ();
			this.label1 = new System.WinForms.Label ();
			this.pictureBox3 = new System.WinForms.PictureBox ();
			this.trbBallSpeed = new System.WinForms.TrackBar ();
			this.label4 = new System.WinForms.Label ();
			this.label2 = new System.WinForms.Label ();
			this.pictureBox2 = new System.WinForms.PictureBox ();
			this.pictureBox1 = new System.WinForms.PictureBox ();
			trbBallSpeed.BeginInit ();
			//@this.TrayHeight = 0;
			//@this.TrayLargeIcon = false;
			//@this.TrayAutoArrange = true;
			groupBox2.Location = new System.Drawing.Point (8, 184);
			groupBox2.TabIndex = 1;
			groupBox2.TabStop = false;
			groupBox2.Text = "Sound";
			groupBox2.Size = new System.Drawing.Size (328, 64);
			groupBox3.Location = new System.Drawing.Point (8, 256);
			groupBox3.TabIndex = 2;
			groupBox3.TabStop = false;
			groupBox3.Text = "Control";
			groupBox3.Size = new System.Drawing.Size (328, 64);
			lbBallColors.Location = new System.Drawing.Point (104, 72);
			lbBallColors.Size = new System.Drawing.Size (208, 82);
			lbBallColors.TabIndex = 5;
			lbBallColors.Items.All = new object[5] {"Blue", "Cyan", "Green", "Gray", "Red"};
			groupBox1.Location = new System.Drawing.Point (8, 8);
			groupBox1.TabIndex = 0;
			groupBox1.TabStop = false;
			groupBox1.Text = "Ball";
			groupBox1.Size = new System.Drawing.Size (328, 168);
			cbLockMouse.Location = new System.Drawing.Point (56, 16);
			cbLockMouse.Text = "Lock mouse into game area during play";
			cbLockMouse.Size = new System.Drawing.Size (224, 24);
			cbLockMouse.TabIndex = 1;
			label3.Location = new System.Drawing.Point (296, 24);
			label3.Text = "Fast";
			label3.Size = new System.Drawing.Size (29, 16);
			label3.TabIndex = 3;
			btnCancel.Location = new System.Drawing.Point (184, 328);
			btnCancel.DialogResult = System.WinForms.DialogResult.Cancel;
			btnCancel.Size = new System.Drawing.Size (75, 23);
			btnCancel.TabIndex = 4;
			btnCancel.Text = "Cancel";
			cbUseSound.Location = new System.Drawing.Point (56, 16);
			cbUseSound.Text = "&Use Sound";
			cbUseSound.Size = new System.Drawing.Size (120, 24);
			cbUseSound.TabIndex = 1;
			btnOK.Location = new System.Drawing.Point (264, 328);
			btnOK.DialogResult = System.WinForms.DialogResult.OK;
			btnOK.Size = new System.Drawing.Size (75, 23);
			btnOK.TabIndex = 3;
			btnOK.Text = "OK";
			label1.Location = new System.Drawing.Point (56, 24);
			label1.Text = "Speed:";
			label1.Size = new System.Drawing.Size (48, 16);
			label1.TabIndex = 0;
			pictureBox3.Location = new System.Drawing.Point (8, 16);
			pictureBox3.Size = new System.Drawing.Size (40, 40);
			pictureBox3.TabIndex = 6;
			pictureBox3.TabStop = false;
			pictureBox3.Image = (System.Drawing.Image) resources.GetObject ("pictureBox3.Image");
			trbBallSpeed.Location = new System.Drawing.Point (128, 24);
			trbBallSpeed.TabIndex = 1;
			trbBallSpeed.Size = new System.Drawing.Size (168, 42);
			label4.Location = new System.Drawing.Point (56, 72);
			label4.Text = "Color:";
			label4.Size = new System.Drawing.Size (48, 16);
			label4.TabIndex = 4;
			label2.Location = new System.Drawing.Point (104, 24);
			label2.Text = "Slow";
			label2.Size = new System.Drawing.Size (31, 16);
			label2.TabIndex = 2;
			pictureBox2.Location = new System.Drawing.Point (8, 16);
			pictureBox2.Size = new System.Drawing.Size (40, 40);
			pictureBox2.TabIndex = 0;
			pictureBox2.TabStop = false;
			pictureBox2.Image = (System.Drawing.Image) resources.GetObject ("pictureBox2.Image");
			pictureBox1.Location = new System.Drawing.Point (8, 16);
			pictureBox1.Size = new System.Drawing.Size (40, 40);
			pictureBox1.TabIndex = 0;
			pictureBox1.TabStop = false;
			pictureBox1.Image = (System.Drawing.Image) resources.GetObject ("pictureBox1.Image");
			this.Text = "WinBlock Options";
			this.MaximizeBox = false;
			this.AutoScaleBaseSize = new System.Drawing.Size (5, 13);
			this.CancelButton = this.btnCancel;
			this.BorderStyle = System.WinForms.FormBorderStyle.FixedDialog;
			this.Font = new System.Drawing.Font ("Tahoma", 8);
			this.Icon = (System.Drawing.Icon) resources.GetObject ("$this.Icon");
			this.MinimizeBox = false;
			this.ClientSize = new System.Drawing.Size (346, 359);
			this.Controls.Add (this.btnCancel);
			this.Controls.Add (this.btnOK);
			this.Controls.Add (this.groupBox3);
			this.Controls.Add (this.groupBox2);
			this.Controls.Add (this.groupBox1);
			groupBox1.Controls.Add (this.pictureBox3);
			groupBox1.Controls.Add (this.lbBallColors);
			groupBox1.Controls.Add (this.label4);
			groupBox1.Controls.Add (this.label3);
			groupBox1.Controls.Add (this.label2);
			groupBox1.Controls.Add (this.trbBallSpeed);
			groupBox1.Controls.Add (this.label1);
			groupBox2.Controls.Add (this.cbUseSound);
			groupBox2.Controls.Add (this.pictureBox1);
			groupBox3.Controls.Add (this.cbLockMouse);
			groupBox3.Controls.Add (this.pictureBox2);
			trbBallSpeed.EndInit ();
		}



    }
}
