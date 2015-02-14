package game;

import java.util.Random;

public abstract class AI
{
	public static final int RIGHT = 0, LEFT = 1, FORWARD = 2;
	protected NPC npc;
	private final int defFallWaitTime = 5, defJumpSpeedMod = 0;
	private float fallWaitTime, jumpSpeedMod;
	public Random rand;
	
	public AI(NPC npc)
	{
		this.npc = npc;
		fallWaitTime = defFallWaitTime;
		jumpSpeedMod = defJumpSpeedMod;
	}
	
	abstract void tick();
	
	protected void moveLeft()
	{
		npc.setFacing(LEFT);
		npc.setXCord((int)(npc.getTrueXCord() - (npc.getSpeed() * npc.getWalkSpeedMod())));
		//check world collisions
		if(npc.getTrueXCord() < 0)
		{
			npc.setXCord(0);
			return;
		}
		//check block collisions
		Object [] o = Level.checkCollisions(npc, npc.getCurrentLevel().getBlocks());
		if((boolean) o[0])
		{
				npc.setXCord(((Block)o[1]).getTrueXCord() + ((Block)o[1]).getScaledWidth());
					
		}
	}
	
	protected void moveRight()
	{
		npc.setFacing(RIGHT);
		npc.setXCord((int)(npc.getTrueXCord() + (npc.getSpeed() * npc.getWalkSpeedMod())));
		//check world collisions
		if(npc.getTrueXCord() + npc.getWidth() >= npc.getCurrentLevel().getCanvasInstance().getWidth())
		{
			npc.setXCord(npc.getCurrentLevel().getCanvasInstance().getWidth() - npc.getWidth());
			return;
		}
		//check block collisions
		Object [] o = Level.checkCollisions(npc, npc.getCurrentLevel().getBlocks());
		if((boolean) o[0])
		{
				npc.setXCord(((Block)o[1]).getTrueXCord() - ((Block)o[1]).getScaledWidth());
					
		}
	}
	
	public void fall()
	{
		npc.setYCord((int)(npc.getTrueYCord() - (npc.getSpeed() * jumpSpeedMod)));
		//check block collisions
		Object [] o = Level.checkCollisions(npc, npc.getCurrentLevel().getBlocks());
		if((boolean) o[0])
		{
			if(jumpSpeedMod < 0)
			{
				npc.setYCord(((Block)o[1]).getTrueYCord());
			}
			if(jumpSpeedMod > 0)
			{
				npc.setYCord(((Block)o[1]).getTrueYCord() + ((Block)o[1]).getScaledHeight() + npc.getHeight());
				jumpSpeedMod = defJumpSpeedMod;
			}
		}
		//handle fall speed
		fallWaitTime--;
		if(fallWaitTime <= 0);
		{
			jumpSpeedMod--;
			fallWaitTime = defFallWaitTime;
		}
		//reset fall variables
		if(Level.isOnGround(npc, npc.getCurrentLevel().getBlocks()))
		{
			jumpSpeedMod = defJumpSpeedMod;
			fallWaitTime = defFallWaitTime;
		}
	}
	
	public float getJumpSpeedMod()
	{
		return jumpSpeedMod;
	}
}
