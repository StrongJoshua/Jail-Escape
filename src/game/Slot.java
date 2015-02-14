package game;

public class Slot
{
	private int xCord, yCord;
	private final static int baseScale = 4;
	public static final String overlay = "SlotOverlay.png", toolTipUnderlay = "ToolTipUnderlay.png";
	private Item item;
	private boolean isSelected, isSelectionSlot;
	
	public Slot(int x, int y, Item i, boolean b)
	{
		xCord = x;
		yCord = y;
		item = i;
		isSelected = false;
		isSelectionSlot = b;
	}
	
	public int getXCord()
	{
		return xCord;
	}
	
	public int getYCord()
	{
		return yCord;
	}
	
	public static int getBaseScale()
	{
		return baseScale;
	}
	
	public Item getItem()
	{
		return item;
	}
	
	public void setItem(Item i)
	{
		item = i;
		item.setXCord(xCord);
		item.setYCord(yCord);
		item.setIfInInv(true);
	}
	
	public void removeItem()
	{
		if(item == null)
			return;
		item.setIfInInv(false);
		item = null;
	}
	
	public boolean getIsSelected()
	{
		return isSelected;
	}
	
	public void setSelected(boolean b)
	{
		isSelected = b;
		if(item != null && !isSelectionSlot)
			item.setSelected(b);
	}
	
	public boolean contains(Item i)
	{
		if(item == i)
		{
			return true;
		}
		return false;
	}
}