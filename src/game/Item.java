package game;

public class Item implements Entity
{
	private final int defFallWaitTime = 5, defDropTime = 100, defSlowTime = 6, baseScale = 1;
	private int xCord, yCord, width, height, speed, fallWaitTime, dropTime, slowTime, frictionTimer;
	private float jumpSpeedMod;
	private boolean isInInv, isSelected;
	private String imgFile, name;
	private Level currentLevel;
	
	public Item(int x, int y, boolean b, String s, String s2, Level l)
	{
		xCord = x;
		yCord = y;
		isInInv = b;
		name = s;
		imgFile = s2;
		currentLevel = l;
		width = 40 * baseScale;
		height = 40 * baseScale;
		fallWaitTime = defFallWaitTime;
		dropTime = 0;
		slowTime = defSlowTime;
		frictionTimer = 0;
	}
	
	public int getTrueXCord()
	{
		return xCord;
	}
	
	public int getTrueYCord()
	{
		return yCord;
	}
	
	public void setXCord(int x)
	{
		xCord = x;
	}
	
	public void setYCord(int y)
	{
		yCord = y;
	}
	
	public int getRelativeXCord()
	{
		int cx = currentLevel.getPlayer().getCamera().getXCord();
		int x = getTrueXCord() - cx;
		return x + (currentLevel.getCanvasInstance().getWidth()/2);
	}
	
	public int getRelativeYCord()
	{
		int cy = currentLevel.getPlayer().getCamera().getYCord();
		int y = getTrueYCord() - cy;
		return y + (currentLevel.getCanvasInstance().getHeight()/2);
	}
	
	public boolean isInInventory()
	{
		return isInInv;
	}
	
	public void setIfInInv(boolean b)
	{
		isInInv = b;
		if(!b)
			dropTime = defDropTime;
	}
	
	public String getImageFile()
	{
		return imgFile;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setSelected(boolean b)
	{
		isSelected = b;
	}
	
	public boolean getIsSelected()
	{
		return isSelected;
	}
	
	public void tick()
	{
		if(isInInventory())
		{
			frictionTimer = 0;
			return;
		}
		//drop time lets the player walk over items he recently dropped
		dropTime--;
		
		move();
		//slow down
		slowTime--;
		if(slowTime > 0)
			return;
		slowTime = defSlowTime - (frictionTimer/2);
		frictionTimer++;
		if(speed < 0)
		{
			speed++;
		}
		else if(speed > 0)
		{
			speed--;
		}
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public float getWalkSpeedMod()
	{
		return 1;
	}

	@Override
	public float getSpeed()
	{
		return speed;
	}

	@Override
	public int getFacing()
	{
		return 0;
	}

	@Override
	public int getBaseScale()
	{
		return baseScale;
	}
	
	public void setJumpSpeedMod(int i)
	{
		jumpSpeedMod = i;
	}
	
	private void move()
	{
		int [] lastCord = new int [] {getTrueXCord(), getTrueYCord()};
		/*//move y
		setYCord((int)(getYCord() - (jumpSpeedMod * (scale/2))));
		//check y block collisions
		Object [] o = Level.checkCollisions(this, currentLevel.getBlocks());
		if((boolean) o[0])
		{
			if(jumpSpeedMod <= 0)
			{
				setYCord(((Block)o[1]).getYCord() - getHeight());
			}
			if(jumpSpeedMod > 0)
			{
				setYCord(((Block)o[1]).getYCord() + ((Block)o[1]).getScaledHeight());
				jumpSpeedMod = 0;
			}
		}
		//move x
		setXCord((int)(getTrueXCord() + speed));
		//reset collision check
		o = Level.checkCollisions(this, currentLevel.getBlocks());
		//check x block collisions
		if((boolean) o[0])
		{
			int x = 0;
			if((Block)o[1] instanceof BlockDoor)
				x = ((Block)o[1]).getScaledWidth() - (2 * (scale - 1));
			//check which side you're on
			if(speed > 0)
			{
				setXCord(((Block)o[1]).getTrueXCord() - getWidth() + x);
			}
			else if(speed < 0)
			{
				setXCord(((Block)o[1]).getTrueXCord() + ((Block)o[1]).getScaledWidth());
			}
		}*/
		Object [] o = new Object [2];
		float bs = baseScale;
		
		//check x collisions
		o = Level.checkXCollisions(lastCord, this, currentLevel.getBlocks());
		if((boolean) o[0])
		{
			int x = 0;
			if((Block)o[1] instanceof BlockDoor)
				x = (int) (((Block)o[1]).getScaledWidth() - (2 * (Level.getLevelScale())));
			//check which side you're on
			if(speed > 0)
			{
				setXCord(((Block)o[1]).getTrueXCord() - getWidth() + x);
			}
			else if(speed < 0)
			{
				setXCord(((Block)o[1]).getTrueXCord() + ((Block)o[1]).getScaledWidth());
			}
		}
		else
		{
			//if you don't collide, make sure you don't move through
			o = Level.checkWouldMoveThroughX(this, currentLevel.getBlocks());
			if(!(boolean) o[0])
			{
				//if not, move normally
				setXCord(getTrueXCord() + speed);
			}
			else
			{
				//if you would, then stop at the block
				Block b = (Block) o[1];
				//check which side you're on
				if(speed > 0)
				{
					setXCord(b.getTrueXCord() - getWidth());
				}
				else if(speed < 0)
				{
					setXCord(b.getTrueXCord() + b.getScaledWidth());
				}
			}
		}
		//check y collisions
		lastCord = new int [] {getTrueXCord(), getTrueYCord()};
		o = Level.checkYCollisions(lastCord, this, currentLevel.getBlocks());
		if((boolean) o[0])
		{
			//if you hit, then adjust accordingly
			if(lastCord[1] < ((Block)o[1]).getTrueYCord())
				setYCord(((Block)o[1]).getTrueYCord() - getHeight());
			if(lastCord[1] > ((Block)o[1]).getTrueYCord())
			{
				setYCord(((Block)o[1]).getTrueYCord() + ((Block)o[1]).getScaledHeight());
				jumpSpeedMod = 0;
			}
		}
		else
		{
			//if you don't hit, move as normally
			setYCord((int)(getTrueYCord() - (jumpSpeedMod * (bs/2))));
		}
		//handle fall speed
		fallWaitTime--;
		if(fallWaitTime <= 0)
		{
			jumpSpeedMod -= 2;
			fallWaitTime = defFallWaitTime;
		}
		//reset fall time
		if(Level.isOnGround(this, currentLevel.getBlocks()))
		{
			fallWaitTime = defFallWaitTime;
			jumpSpeedMod = 0;
		}
	}
	
	public float getJumpSpeedMod()
	{
		return jumpSpeedMod;
	}
	
	public void setSpeed(int i)
	{
		speed = i;
	}
	
	public int getDropTime()
	{
		return dropTime;
	}
	
	public boolean canEquip()
	{
		return false;
	}

	@Override
	public int getAnimationSequence()
	{
		return 0;
	}
	
	public int getBuyPrice()
	{
		return 0;
	}
	
	public int getSellPrice()
	{
		return 0;
	}
}
