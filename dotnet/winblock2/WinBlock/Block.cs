namespace Dubh.WinBlock
{
    using System;

    /// <remarks>
    ///    This class describes the state of a single block in WinBlock
    /// </remarks>
    public class Block
    {

		// Blocks are always a fixed size.
		public const int BLOCK_WIDTH = 10;
		public const int BLOCK_HEIGHT = 5;

		private bool m_alive = false;
		private int m_pointsVal = 0;
		private int m_hitsNeeded = 0;
		private int m_x = 0;
		private int m_y = 0;

		/// <summary>
		///		Construct a block.
		/// </summary>
		/// <param name="points">
		///		Indicates the number of points destroying this block is
		///		worth
		///	</param>
		///	<param name="hits">
		///		Indicates the number of hits required to destroy this block
		///	</param>
        public Block(int points, int hits)
        {
			m_alive = true;
			m_pointsVal = points;
			m_hitsNeeded = hits;
        }

		/// <summary>
		///		Whether the block is alive.
		/// </summary>
		public bool IsAlive
		{
			get
			{
				return m_alive;
			}
			set
			{
				m_alive = value;
			}
		}

		/// <summary>
		///		How many points this block is worth
		/// </summary>
		public int PointsValue
		{
			get
			{
				return m_pointsVal;
			}
		}

		/// <summary>
		///		The number of hits needed before this block is destroyed
		/// </summary>
		public int HitsNeeded
		{
			get
			{
				return m_hitsNeeded;
			}
		}

		/// <summary>
		///		The x co-ordinate of the block
		/// </summary>
		public int X
		{
			get
			{
				return m_x;
			}
		}

		/// <summary>
		///		The y co-ordinate of the block
		/// </summary>
		public int Y
		{
			get
			{
				return m_y;
			}
		}
    }
}
