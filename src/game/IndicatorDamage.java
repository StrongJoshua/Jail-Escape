package game;

public class IndicatorDamage extends Indicator
{
	private int color;
	
	public IndicatorDamage(int d, int x, int y, boolean done, Player p)
	{
		super("" + d, x, y, p);
		if(done)
			color = Indicator.YELLOW;
		else
			color = Indicator.RED;
	}
	
	@Override
	public int getColor()
	{
		return color;
	}
}
