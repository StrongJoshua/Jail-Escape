package game;

public class Level1 extends Level
{
	private Player player;
	private Block [] blocks;
	private NPC [] npcs;
	private Item [] items;
	private String gameName, winMessage;
	private int levelWidth, levelHeight;
	
	public Level1()
	{
		super("Level1");
		gameName = "Testing - The Basics";
		winMessage = "You Escaped!";
		player = new Player();
	}

	@Override
	public void tick(boolean[] keys)
	{
		if(!hasInit)
			return;
		player.tick(keys);
		if(player.getInventory().getIsOpen())
		{
			return;
		}
		if(player.getIfWon() && keys[0] == true)
		{
			getCanvasInstance().game.setSession("Level2");
		}
		if(npcs != null && npcs.length != 0)
		{
			for(int i = 0; i < npcs.length; i++)
			{
				npcs[i].tick();
			}
		}
		
		if(items != null || items.length != 0)
		{
			for(int i = 0; i < items.length; i++)
			{
				if(items[i] != null)
					items[i].tick();
			}
		}
	}

	@Override
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public Block [] getBlocks()
	{
		return blocks;
	}
	
	@Override
	public NPC [] getNPCs()
	{
		return npcs;
	}
	
	@Override
	public Item [] getItems()
	{
		return items;
	}
	
	@Override
	public String getGameName()
	{
		return gameName;
	}
	
	@Override
	public String getWinMessage()
	{
		return winMessage;
	}
	
	@Override
	public void init(PlatformerCanvas instance)
	{
		super.init(instance);
		levelWidth = refBlock.getScaledWidth() * 50;
		levelHeight = refBlock.getScaledHeight() * 50;
		player.giveInstance(instance);
		player.initPlayer(refBlock.getScaledWidth() * 3, refBlock.getBottomLayer(), this);
		player.pickupItem(new ItemKey(0, 0, true, this));
		buildWorld();
		loadNPCs();
		loadItems();
		hasInit = true;
	}
	
	@Override
	protected void buildWorld()
	{
		int i;
		int nonFloorBlocks = 31;
		blocks = new Block [getAmountBlocksForFloor() + nonFloorBlocks];
		for(i = 0; i < (blocks.length - nonFloorBlocks); i++)
		{
			blocks[i] = new BlockColdHardStone(i * refBlock.getScaledWidth(), refBlock.getBottomLayer(), this);
		}
		blocks[i++] = new BlockColdHardStone(5 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - refBlock.getScaledHeight(), this);
		blocks[i++] = new BlockColdHardStone(7 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 3), this);
		blocks[i++] = new BlockColdHardStone(5 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 4), this);
		blocks[i++] = new BlockColdHardStone(4 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 4), this);
		blocks[i++] = new BlockColdHardStone(3 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 4), this);
		blocks[i++] = new BlockColdHardStone(2 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 4), this);
		blocks[i++] = new BlockColdHardStone(1 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 4), this);
		blocks[i++] = new BlockColdHardStone(0 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 4), this);
		blocks[i++] = new BlockColdHardStone(0 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 3), this);
		blocks[i++] = new BlockColdHardStone(0 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 2), this);
		blocks[i++] = new BlockColdHardStone(0 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 1), this);
		blocks[i++] = new BlockColdHardStone(6 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 1), this);
		blocks[i++] = new BlockColdHardStone(7 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 1), this);
		blocks[i++] = new BlockColdHardStone(8 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 1), this);
		blocks[i++] = new BlockColdHardStone(7 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 2), this);
		blocks[i++] = new BlockColdHardStone(9 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 5), this);
		blocks[i++] = new BlockColdHardStone(10 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 5), this);
		blocks[i++] = new BlockColdHardStone(11 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 5), this);
		blocks[i++] = new BlockColdHardStone(12 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 5), this);
		blocks[i++] = new BlockColdHardStone(13 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 5), this);
		blocks[i++] = new BlockColdHardStone(14 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 5), this);
		blocks[i++] = new BlockColdHardStone(15 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 5), this);
		blocks[i++] = new BlockColdHardStone(16 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 5), this);
		blocks[i++] = new BlockColdHardStone(17 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 5), this);
		blocks[i++] = new BlockColdHardStone(18 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 5), this);
		blocks[i++] = new BlockColdHardStone(19 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 5), this);
		blocks[i++] = new BlockColdHardStone(35 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 1), this);
		blocks[i++] = new BlockShop(35 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 3), this, null);
		blocks[i++] = new BlockDoorMetal(5 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 3), false, this);
		blocks[i++] = new BlockColdHardStone(20 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 7), this);
		blocks[i++] = new BlockColdHardStone(22 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 9), this);
		//blocks[i++] = new BlockWin(19 * refBlock.getScaledWidth(), refBlock.getBottomLayer() - (refBlock.getScaledHeight() * 6), scale, this);
	}
	
	@Override
	protected void loadNPCs()
	{
		int i = 0;
		npcs = new NPC [1];
		npcs[i++] = new NPCPrisonGuard(10 * refBlock.getScaledWidth(), refBlock.getBottomLayer(), "Prison Guard", this);
	}
	
	@Override
	protected void loadItems()
	{
		int i = 0;
		items = new Item [player.getInventory().getAmountOfItems() + 1];
		for(; i < player.getInventory().getAmountOfItems(); i++)
		{
			items[i] = player.getInventory().getAllItems()[i];
		}
		items[i++] = new ItemPrisonShank(refBlock.getScaledWidth(), refBlock.getBottomLayer() - refBlock.getScaledHeight(), false, this);
	}
	
	@Override
	public int getWidth()
	{
		return levelWidth;
	}
	
	@Override
	public int getHeight()
	{
		return levelHeight;
	}
}
