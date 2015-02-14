package game;

public class Camera
{
	private int x, y;
	private Player player;
	private PlatformerCanvas instance;
	
	public Camera(Player player, PlatformerCanvas instance)
	{
		this.player = player;
		this.instance = instance;
		update();
	}
	
	public void update()
	{
		int x1 = player.getTrueXCord();
		x = x1;
		int w = instance.getWidth();
		int lw = player.getCurrentLevel().getWidth();
		if(lw - x1 < w/2)
		{
			x = lw - (w/2);
		}
		else if(x1 < w/2)
		{
			x = (w/2);
		}
		int y1 = player.getTrueYCord();
		y = y1;
		int h = instance.getHeight();
		int lh = player.getCurrentLevel().getHeight();
		if(y1 < h/2)
		{
			y = h/2;
		}
		else if(lh - y1 < h/2)
		{
			y = lh - (h/2);
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