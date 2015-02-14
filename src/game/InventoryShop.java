package game;

public class InventoryShop extends Inventory
{
	public InventoryShop(Player p)
	{
		super("ShopInventoryGUI.png", p);
	}
	
	@Override
	public void buildSlots()
	{
		slots = new Slot [46];
		int i = 0;
		for(int x = 0; x < 15; x++)
		{
			for(int y = 0; y < 3; y++)
			{
				slots[i++] = new Slot((x * 50) + 31 + 240, (y * 50) + 85, null, false);
			}
		}
		slots[45] = new Slot(1000, 1000, null, true);
	}
	
	@Override
	public Slot getMovementSlot()
	{
		return slots[45];
	}
	
	@Override
	public void tick(PlatformerCanvas instance)
	{
		
	}
}
