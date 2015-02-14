package editor;

import game.PlatformerCanvas;

public class EditorCamera
{
	private int x, y;
	private PlatformerCanvas instance;
	private LevelEditor edit;
	
	public EditorCamera(LevelEditor edit, PlatformerCanvas instance)
	{
		this.instance = instance;
		this.edit = edit;
		x = instance.getWidth()/2;
		y = instance.getHeight()/2;
	}
	
	public void update()
	{
		int mx = instance.input.getMouseX();
		int my = instance.input.getMouseY();
		int w = instance.getWidth();
		int h = instance.getHeight();
		int lw = edit.getWidth();
		int lh = edit.getHeight();
		//move the camera if the mouse gets to the edge of the screen
		if(mx < 100)
		{
			x--;
		}
		else if(mx > w - 100)
		{
			x++;
		}
		if(my < 100)
		{
			y--;
		}
		else if(my > h - 100)
		{
			y++;
		}
		//check that the camera doesn't leave the bounds
		if(x < w/2)
		{
			x = w/2;
		}
		else if(x > lw - w/2)
		{
			x = lw - w/2;
		}
		if(y < h/2)
		{
			y = h/2;
		}
		else if(y > lh - h/2)
		{
			y = lh - h/2;
		}
	}
	
	public int getXCord()
	{
		return x;
	}
	
	public int getYCord()
	{
		return y;
	}
}
