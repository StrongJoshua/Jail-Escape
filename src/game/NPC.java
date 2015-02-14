package game;

import java.util.Random;

public class NPC implements Entity
{
	private final float speed = 2;
	private float walkSpeedMod;
	private String name, imageFile;
	private final int defSwitchTime = 20, baseScale = 2;
	private int xCord, yCord, width, height, facing, animationSequence, switchTime;
	private AI ai;
	private Level currentLevel;
	private Random random;
	
	public NPC(int x, int y, int w, int h, String s, String s2, Level level)
	{
		xCord = x;
		yCord = y;
		currentLevel = level;
		width = w * (baseScale);
		height = h * (baseScale);
		name = s;
		imageFile = s2;
		walkSpeedMod = 1;
		facing = AI.FORWARD;
		switchTime = defSwitchTime;
		random = new Random();
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
		x += (currentLevel.getCanvasInstance().getWidth()/2);
		return x;
	}
	
	public int getRelativeYCord()
	{
		int cy = currentLevel.getPlayer().getCamera().getYCord();
		int y = getTrueYCord() - cy;
		return y + (currentLevel.getCanvasInstance().getHeight()/2);
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getImageFile()
	{
		return imageFile;
	}
	
	public void tick()
	{
		ai.tick();
		switchTime--;
		//update animation
		if(switchTime <= 0)
		{
			switchTime = defSwitchTime;
			if(facing == AI.FORWARD)
				animationSequence = 0;
			if(facing == AI.RIGHT && animationSequence < 1 && animationSequence > 3)
			{
				animationSequence = 1;
				return;
			}
			if(facing == AI.LEFT && animationSequence < 4)
			{
				animationSequence = 4;
				return;
			}
			if(facing == AI.RIGHT)
			{
				animationSequence++;
				if(animationSequence > 3)
					animationSequence = 1;
			}
			if(facing == AI.LEFT)
			{
				animationSequence++;
				if(animationSequence > 6)
					animationSequence = 4;
			}
		}
	}
	
	public void setAI(AI ai)
	{
		this.ai = ai;
	}
	
	public Level getCurrentLevel()
	{
		return currentLevel;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	public float getWalkSpeedMod()
	{
		return walkSpeedMod;
	}
	
	public void setXCord(int x)
	{
		xCord = x;
	}
	
	public void setYCord(int y)
	{
		yCord = y;
	}
	
	public void setFacing(int i)
	{
		if(i != AI.LEFT && i != AI.RIGHT && i != AI.FORWARD)
		{
			System.out.println(i + " is non-existing direction");
			return;
		}
		if(facing == i)
			return;
		if(i == AI.RIGHT)
		{
			animationSequence = ai.rand.nextInt(2) + 1;
		}
		if(i == AI.LEFT)
		{
			animationSequence = ai.rand.nextInt(2) + 3;
		}
		if(i == AI.FORWARD)
		{
			animationSequence = 0;
		}
		facing = i;
	}
	
	public int getFacing()
	{
		return facing;
	}
	
	public int getAnimationSequence()
	{
		return animationSequence;
	}

	@Override
	public int getBaseScale()
	{
		return baseScale;
	}
	
	@Override
	public float getJumpSpeedMod()
	{
		return ai.getJumpSpeedMod();
	}
	
	public int getBaseDamage()
	{
		return 0;
	}
	
	public int getDamage()
	{
		int i = getBaseDamage();
		int m = random.nextInt(i);
		return i + m;
	}
	
	public int getBaseMoney()
	{
		return 0;
	}
	
	public int getMoney()
	{
		int i = getBaseMoney();
	int m = random.nextInt(i * 5);
		return i + m;
	}
}
