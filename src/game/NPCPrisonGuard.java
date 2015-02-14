package game;

public class NPCPrisonGuard extends NPC
{
	public NPCPrisonGuard(int x, int y, String s, Level level)
	{
		super(x, y, 10, 27, s, "NPCPrisonGuard.png", level);
		setAI(new AIBasic(this));
	}
	
	@Override
	public int getBaseDamage()
	{
		return 5;
	}
	
	@Override
	public int getBaseMoney()
	{
		return 15;
	}
}
