package game;

public class BlockShop extends Block
{
	private InventoryShop inventory;
	
	public BlockShop(int x, int y, Level l, InventoryShop inv)
	{
		super(x, y, "BlockShop.png", l);
		height = 20;
		inventory = inv;
	}
	
	public InventoryShop getShopInventory()
	{
		return inventory;
	}
	
	@Override
	public boolean getIfOpen()
	{
		return true;
	}
}
