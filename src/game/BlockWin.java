package game;

public class BlockWin extends Block
{
	public BlockWin(int x, int y, Level l)
	{
		super(x, y, "BlockWin.png", l);
	}
	
	@Override
	public boolean isWinBlock()
	{
		return true;
	}
}