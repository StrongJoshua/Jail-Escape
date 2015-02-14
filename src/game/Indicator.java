package game;

public class Indicator
{
	private String text;
	private int xCord, yCord, timer, yOrig;
	private Player player;
	public static final int RED = 0xFF3B3B, YELLOW = 0xFFD24A, GREEN = 0x00FF00, WHITE = 0xFFFFFF;
	public static final String IndicatorNums = "BoldNumbers.png";
	
	public Indicator(String s, int x, int y, Player p)
	{
		text = s;
		xCord = x;
		yCord = y;
		yOrig = yCord;
		player = p;
		timer = 0;
	}
	
	public int getRelativeYCord()
	{
		int cy = player.getCamera().getYCord();
		int y = yCord - cy;
		return y + (player.getCurrentLevel().getCanvasInstance().getHeight()/2);
	}
	
	public int getRelativeXCord()
	{
		int cx = player.getCamera().getXCord();
		int x = xCord - cx;
		return x + (player.getCurrentLevel().getCanvasInstance().getWidth()/2);
	}
	
	public String getMessage()
	{
		return text;
	}
	
	public int getColor()
	{
		return GREEN;
	}
	
	public void tick()
	{
		timer++;
		setYCord(yOrig - (getTimer()/10));
	}
	
	public int getTimer()
	{
		return timer;
	}
	
	public void setYCord(int yNew)
	{
		yCord = yNew;
	}
}
