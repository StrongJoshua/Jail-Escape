package game;

public class NPCPoliceMan extends NPC
{
	public NPCPoliceMan(int x, int y, String s, Level level)
	{
		super(x, y, 11, 26, s, "NPCPoliceMan.png", level);
		setAI(new AIBasic(this));
	}
	
	@Override
	public int getBaseDamage()
	{
		return 10;
	}
	
	@Override
	public int getBaseMoney()
	{
		return 25;
	}
}