namespace Dubh.WinBlock
{
    using System;
	using System.WinForms;
	using System.Drawing;

    /// <summary>
    ///    The court is the game area - this constrains the location
    ///    of the ball.
    /// </summary>
    public class Court : System.WinForms.Panel
    {
		private void InitializeComponent ()
		{
		}
	
        public Court()
        {
			// Give the panel a nice 3d border.
			this.BorderStyle = BorderStyle.Fixed3D;
			this.BackColor = Color.Black;
        }
    }
}
