namespace Dubh.WinBlock
{
    using System;
    using System.Collections;
    using System.Core;
    using System.ComponentModel;
    using System.Drawing;
    using System.Data;
    using System.WinForms;

	public enum LineOrientation
	{
		Horizontal, Vertical
	}

	public enum LineStyle
	{
		Etched
	}

    /// <summary>
    ///    Summary description for Line.
    /// </summary>
    public class Line : System.WinForms.RichControl
    {
		private void InitializeComponent ()
		{
		}
	
		private LineOrientation m_orientation;
		private LineStyle m_style;


        public Line()
        {
            m_orientation = LineOrientation.Horizontal;
			m_style = LineStyle.Etched;
        }

		public LineOrientation Orientation
		{
			get
			{
				return m_orientation;
			}

			set
			{
				m_orientation = value;
				Refresh();
			}
		}

		public LineStyle Style
		{
			get
			{
				return m_style;
			}

			set
			{
				m_style = value;
				Refresh();
			}
		}

		/// <summary>
		/// Override the paint method to actually paint the 
		/// component
		/// </summary>
		/// <param name="paintEvent">the paint event</param>
		protected override void OnPaint(PaintEventArgs paintEvent)
		{
			int left = paintEvent.ClipRectangle.Left;
			int top = paintEvent.ClipRectangle.Top;
			int right = paintEvent.ClipRectangle.Right;
			int bottom = paintEvent.ClipRectangle.Bottom;

			if (Style == LineStyle.Etched)
			{
				Pen hilite = new Pen(Color.FromKnownColor(KnownColor.ControlLightLight));
				Pen lolite = new Pen(Color.FromKnownColor(KnownColor.ControlDark));

				if (Orientation == LineOrientation.Horizontal)
				{
					paintEvent.Graphics.DrawLine(
						lolite, left, top, right, top
					);

					paintEvent.Graphics.DrawLine(
						hilite, left, top+1, right, top+1
					);
				}
				else if (Orientation == LineOrientation.Vertical)
				{
					paintEvent.Graphics.DrawLine(
						lolite, left, top, left, bottom
					);

					paintEvent.Graphics.DrawLine(
						hilite, left+1, top, left+1, bottom
					);
				}

			}
		}
	}
}