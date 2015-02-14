package game;

public class BlockDoor extends Block
{
	public BlockDoor(int x, int y, boolean b, String s, Level l)
	{
		super(x, y, s, l);
		setIsOpen(b);
		height = 20;
	}
}
