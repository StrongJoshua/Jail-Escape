package game;

import java.util.Random;

public class AIBasic extends AI
{
	private final int defWaitTime = 3;
	private int waitTime, tendency, direction;
	
	public AIBasic(NPC npc)
	{
		super(npc);
		rand = new Random();
		waitTime = defWaitTime;
		tendency = 0;
	}
	
	public void tick()
	{
		fall();
		waitTime--;
		if(waitTime > 0)
			return;
		waitTime = defWaitTime;
		direction = tendency;
		if(rand.nextInt(100) >= 90)
		{
			direction = rand.nextInt(10);
		}
		if(direction <= 3)
		{
			tendency = 0;
			npc.setFacing(AI.FORWARD);
			return;
		}
		if(direction == 4 || direction == 5 || direction == 6)
		{
			moveLeft();
			tendency = 6;
		}
		if(direction >= 7)
		{
			moveRight();
			tendency = 8;
		}
	}
}
