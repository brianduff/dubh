namespace Dubh.WinBlock
{
    using System;

    /// <summary>
    ///    Represents a ball.
    /// </summary>
    public class Ball
    {
		public const int BALL_WIDTH = 5;
		public const int BALL_HEIGHT = 5;

		private int m_x = 0;
		private int m_y = 0;

		private bool m_moving = false;

		/// <summary>
		/// The X-coordinate of the ball
		/// </summary>
		public int X
		{
			get 
			{
				return m_x;
			}
		}

		/// <summary>
		/// The Y-coordinate of the ball
		/// </summary>
		public int Y
		{
			get
			{
				return m_y;
			}
		}

		/// <summary>
		/// Whether the ball is currently "on the move", i.e. has been
		/// released from the paddle.
		/// </summary>
		public bool IsMoving
		{
			get
			{
				return m_moving;
			}
		}

		/// <summary>
		/// Is the ball touching the specified block?
		/// </summary>
		/// <param name="b"> </param>
		public bool IsTouching(Block block)
		{
			// Can't be touching a dead block
			if (!block.IsAlive)
			{
				return false;
			}

			int ballright = X + BALL_WIDTH;
			int ballbottom = Y + BALL_HEIGHT;

			int blockright = block.X + Block.BLOCK_WIDTH;
			int blockbottom = block.Y + Block.BLOCK_HEIGHT;

			return (ballright   >= block.X    &&
					X           <= blockright &&
					Y           >= block.Y    &&
					ballbottom  <= blockbottom);
		}

		/// <summary>
		/// Center the ball on the specified paddle.
		/// </summary>
		/// <param name="paddle">The paddle to center the ball on</param>
		public void CenterOn(Paddle paddle)
		{
			// TODO
		}
    }
}
