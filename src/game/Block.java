package game;

public class Block
{
	protected int width, height;
	private int xCord, yCord, scale;
	private String imageFile;
	private boolean isOpen;
	private Level currentLevel;
	
	public Block(int x, int y, String s, Level l)
	{
		xCord = x;
		yCord = y;
		scale = Level.getLevelScale();
		imageFile = s;
		isOpen = false;
		width = 10;
		height = 10;
		currentLevel = l;
	}
	
	public int getTrueXCord()
	{
		return xCord;
	}
	
	public int getTrueYCord()
	{
		return yCord;
	}
	
	public int getRelativeXCord()
	{
		int cx = currentLevel.getPlayer().getCamera().getXCord();
		int x = getTrueXCord() - cx;
		return x + (currentLevel.getCanvasInstance().getWidth()/2);
	}
	
	public int getRelativeYCord()
	{
		int cy = currentLevel.getPlayer().getCamera().getYCord();
		int y = getTrueYCord() - cy;
		return y + (currentLevel.getCanvasInstance().getHeight()/2);
	}
	
	public String getImageFile()
	{
		return imageFile;
	}
	
	public int getBottomLayer()
	{
		return (currentLevel.getHeight()) - (height * scale);
	}
	
	public int getScaledWidth()
	{
		float f = scale;
		//f *= currentLevel.getCanvasInstance().getScale();
		
		return (int) (width * f);
	}
	
	public int getScaledHeight()
	{
		float f = scale;
		//f *= currentLevel.getCanvasInstance().getScale();
		
		return (int) (height * f);
	}
	
	public boolean isWinBlock()
	{
		return false;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setIsOpen(boolean b)
	{
		isOpen = b;
	}
	
	public boolean getIfOpen()
	{
		return isOpen;
	}
	
	public void setScale(int s)
	{
		scale = s;
	}
	
	public int getScale()
	{
		return scale;
	}
}