package game;

public abstract class Level
{
	private String levelName;
	private static String winShadowFile;
	protected Block refBlock;
	protected boolean hasInit;
	private PlatformerCanvas instance;
	
	protected Level(String s)
	{
		levelName = s;
		hasInit = false;
		winShadowFile = "WinShadow.png";
	}
	public String getLevelName()
	{
		return levelName;
	}
	abstract void tick(boolean [] keys);
	abstract Player getPlayer();
	abstract Block [] getBlocks();
	abstract NPC [] getNPCs();
	abstract Item [] getItems();
	abstract void buildWorld();
	abstract void loadNPCs();
	abstract void loadItems();
	abstract String getGameName();
	abstract String getWinMessage();
	abstract int getWidth();
	abstract int getHeight();
	
	public void init(PlatformerCanvas instance)
	{
		this.instance = instance;
		refBlock = new Block(0, 0, null, this);
	}
	
	public static int getLevelScale()
	{
		return 4;
	}
	
	public int getAmountBlocksForFloor()
	{
		return (getWidth()) / (refBlock.getScaledWidth()) + 1;
	}
	
	@Deprecated
	public static Object [] checkCollisions(Entity e, Block [] b)
	{
		if(b == null)
			return new Object [] {false, null};
		for(int i = 0; i < b.length; i++)
		{
			if(b[i] == null)
				continue;
			int x = 0;
			if(b[i] instanceof BlockDoor)
			{
				x = b[i].getScaledWidth() - ((2 * getLevelScale()));
			}
			int y = e.getHeight();
			int y2 = 0;
			if(e instanceof Item)
			{
				y = 0;
				y2 = e.getHeight();
			}
			if((e.getTrueXCord() < (b[i].getTrueXCord() + b[i].getScaledWidth()) && (e.getTrueXCord() + e.getWidth()) > b[i].getTrueXCord() + x) &&
					((e.getTrueYCord() - y) < (b[i].getTrueYCord() + b[i].getScaledHeight()) &&
							(e.getTrueYCord() + y2 > b[i].getTrueYCord())) && !b[i].getIfOpen())
			{
				return new Object [] {true, b[i]};
			}
		}
		return new Object [] {false, null};
	}
	
	public static Object [] checkXCollisions(int [] lc, Entity e, Block [] b)
	{
		if(b == null)
			return new Object [] {false, null};
		for(int i = 0; i < b.length; i++)
		{
			if(b[i] == null)
				continue;
			int x = 0;
			if(b[i] instanceof BlockDoor)
			{
				x = b[i].getScaledWidth() - ((2 * getLevelScale()));
			}
			int y = e.getHeight();
			int y2 = 0;
			if(e instanceof Item)
			{
				y = 0;
				y2 = e.getHeight();
			}
			if((lc[1] - y) < (b[i].getTrueYCord() + b[i].getScaledHeight()) && lc[1] + y2 > b[i].getTrueYCord() && !b[i].getIfOpen() &&
					lc[0] + (e.getSpeed() * e.getWalkSpeedMod()) < b[i].getTrueXCord() + b[i].getScaledWidth() &&
					lc[0] + (e.getSpeed() * e.getWalkSpeedMod()) + e.getWidth() > b[i].getTrueXCord() + x)
			{
				//System.out.println("x collision: " + b[i].getTrueXCord() + "x" + b[i].getYCord() + "y");
				return new Object [] {true, b[i]};
			}
		}
		return new Object [] {false, null};
	}
	
	public static Object [] checkYCollisions(int [] lc, Entity e, Block [] b)
	{
		if(b == null)
			return new Object [] {false, null};
		for(int i = 0; i < b.length; i++)
		{
			if(b[i] == null)
				continue;
			int x = 0;
			if(b[i] instanceof BlockDoor)
			{
				x = b[i].getScaledWidth() - ((2 * getLevelScale()));
			}
			int y = e.getHeight();
			int y2 = 0;
			if(e instanceof Item)
			{
				y = 0;
				y2 = e.getHeight();
			}
			if(lc[0] < b[i].getTrueXCord() + b[i].getScaledWidth() && lc[0] + e.getWidth() > b[i].getTrueXCord() + x && !b[i].getIfOpen() &&
				(lc[1] - y - (e.getJumpSpeedMod() * (getLevelScale()/2))) < (b[i].getTrueYCord() + b[i].getScaledHeight())
				&& lc[1] + y2 - (e.getJumpSpeedMod() * (getLevelScale()/2)) > b[i].getTrueYCord())
			{
				//System.out.println("y collision: " + b[i].getTrueXCord() + "x" + b[i].getTrueYCord() + "y");
				return new Object [] {true, b[i]};
			}
		}
		return new Object [] {false, null};
	}
	
	public static Object [] checkWouldMoveThroughX(Entity e, Block [] b)
	{
		if(b == null)
			return new Object [] {false, null};
		for(int i = 0; i < b.length; i++)
		{
			if(b[i] == null)
				continue;
			if((e.getTrueYCord() - e.getHeight()) < (b[i].getTrueYCord() + b[i].getScaledWidth()) &&
					(e.getTrueYCord() > b[i].getTrueYCord()) && !b[i].getIfOpen())
			{
				if(e.getSpeed() > 0)
				{
					if(e.getTrueXCord() + (int)(e.getSpeed() * e.getWalkSpeedMod()) > b[i].getTrueXCord() + b[i].getScaledWidth() &&
							e.getTrueXCord() + e.getWidth() < b[i].getTrueXCord())
					{
						return new Object [] {true, b[i]};
					}
				}
				else if(e.getSpeed() < 0)
				{
					if(e.getTrueXCord() + (int)(e.getSpeed() * e.getWalkSpeedMod()) + e.getWidth() < b[i].getTrueXCord() &&
							e.getTrueXCord() > b[i].getTrueXCord() + b[i].getScaledWidth())
					{
						return new Object [] {true, b[i]};
					}
				}
			}
		}
		return new Object [] {false, null};
	}
	
	public static String getWinShadowFile()
	{
		return winShadowFile;
	}
	
	public static boolean isOnGround(Entity e, Block [] b)
	{
		if(b == null)
			return false;
		for(int i = 0; i < b.length; i++)
		{
			if(b[i] == null)
				continue;
			int y = 0;
			if(e instanceof Item)
			{
				y = e.getHeight();
			}
			if((e.getTrueYCord() + y == b[i].getTrueYCord()) && !b[i].getIfOpen() && e.getJumpSpeedMod() <= 0 &&
					(e.getTrueXCord() < (b[i].getTrueXCord() + b[i].getScaledWidth()) && (e.getTrueXCord() + e.getWidth()) > b[i].getTrueXCord()))
			{
				return true;
			}
		}
		return false;
	}
	
	public static Object [] checkNPCCollisions(Entity e, NPC [] npcs)
	{
		if(npcs == null)
			return new Object [] {false, null};
		for(int i = 0; i < npcs.length; i++)
		{
			if(npcs[i] == null)
				continue;
			if((e.getTrueXCord() < (npcs[i].getTrueXCord() + npcs[i].getWidth()) && (e.getTrueXCord() + e.getWidth()) > npcs[i].getTrueXCord()) &&
					((e.getTrueYCord() - e.getHeight()) < (npcs[i].getTrueYCord())) &&
							(e.getTrueYCord() > npcs[i].getTrueYCord() - npcs[i].getHeight()))
				return new Object [] {true, npcs[i]};
		}
		return new Object [] {false, null};
	}
	
	public static Object [] checkItemCollisions(Entity e, Item [] items)
	{
		if(items == null)
			return new Object [] {false, null};
		for(int i = 0; i < items.length; i++)
		{
			if(items[i] == null)
				continue;
			if(e.getTrueXCord() < items[i].getTrueXCord() + items[i].getWidth() && e.getTrueXCord() + e.getWidth() > items[i].getTrueXCord() &&
					!items[i].isInInventory() &&
					e.getTrueYCord() > items[i].getTrueYCord() && e.getTrueYCord() - e.getHeight() < items[i].getTrueYCord() + items[i].getHeight())
				return new Object [] {true, items[i]};
		}
		return new Object [] {false, null};
	}
	
	protected void setScale(int scale)
	{
		refBlock.setScale(scale);
	}
	
	public void removeItem(Item i)
	{
		for(int j = 0; j < getItems().length; j++)
		{
			if(getItems()[j] == i)
				getItems()[j] = null;
		}
	}
	
	public static int getMiddleX(Entity e)
	{
		return e.getTrueXCord() + (e.getWidth()/2);
	}
	
	public PlatformerCanvas getCanvasInstance()
	{
		return instance;
	}
}