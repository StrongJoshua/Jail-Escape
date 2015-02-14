package game;

public abstract class Button
{
	private String imageFile;
	private boolean isHighlighted, didJustPress, isHidden;
	private int width, height, xCord, yCord;
	public static final String highlight = "ButtonHighlight.png";
	
	public Button(String s, int x, int y, int w, int h)
	{
		imageFile = s;
		isHighlighted = false;
		width = w;
		height = h;
		didJustPress = false;
		xCord = x;
		yCord = y;
		isHidden = false;
	}
	
	public void tick(PlatformerCanvas instance)
	{
		if(isHidden)
			return;
		int x = instance.input.getMouseX();
		int y = instance.input.getMouseY();
		
		if(x > xCord && x < xCord + width && y > yCord && y < yCord + height)
			isHighlighted = true;
		else
			isHighlighted = false;
		if(instance.input.getMousePressed() && isHighlighted && !didJustPress)
		{
			didJustPress = true;
			click();
		}
		if(!instance.input.getMousePressed())
		{
			didJustPress = false;
		}
	}
	
	public String getImageFile()
	{
		return imageFile;
	}
	
	public boolean isHighlighted()
	{
		return isHighlighted;
	}
	
	public boolean isHidden()
	{
		return isHidden;
	}
	
	public int getXCord()
	{
		return xCord;
	}
	
	public int getYCord()
	{
		return yCord;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setIfHidden(boolean b)
	{
		isHidden = b;
		if(isHidden)
		{
			isHighlighted = false;
			didJustPress = false;
		}
	}
	
	public abstract void click();
}