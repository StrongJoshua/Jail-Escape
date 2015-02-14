package game;

import java.util.ArrayList;

public abstract class Inventory
{
	private boolean isOpen;
	private String GUIFile;
	protected Slot [] slots;
	private Slot prevSlot;
	private Item selectedItem;
	private Player player;
	
	abstract void buildSlots();
	abstract Slot getMovementSlot();
	
	public Inventory(String s, Player p)
	{
		GUIFile = s;
		isOpen = false;
		player = p;
	}
	
	public Slot[] getSlots()
	{
		return slots;
	}
	
	public Item [] getAllItems()
	{
		if(slots == null || slots.length == 0)
			return null;
		ArrayList<Item> items = new ArrayList<Item>();
		for(int i = 0; i < slots.length; i++)
		{
			Item item = slots[i].getItem();
			if(item != null)
				items.add(item);
		}
		Object [] o = items.toArray();
		Item [] itemArray = new Item [o.length];
		for(int i = 0; i < o.length; i++)
		{
			itemArray[i] = (Item)o[i];
		}
		return itemArray;
	}
	
	public String getGUIFile()
	{
		return GUIFile;
	}
	
	public boolean getIsOpen()
	{
		return isOpen;
	}
	
	public void setIsOpen(boolean b)
	{
		isOpen = b;
	}
	
	public void setSelectedSlot(int x, int y)
	{
		
		for(int i = 0; i < slots.length; i++)
		{
			if(x < slots[i].getXCord() + (10 * Slot.getBaseScale()) && x > slots[i].getXCord() &&
					y < slots[i].getYCord() + (10 * Slot.getBaseScale()) && y > slots[i].getYCord())
			{
				slots[i].setSelected(true);
			}
			else
			{
				slots[i].setSelected(false);
			}
		}
	}
	
	public Slot getSelectedSlot()
	{
		for(int i = 0; i < slots.length; i++)
		{
			if(slots[i].getIsSelected())
				return slots[i];
		}
		return null;
	}
	
	public void addItem(Item item)
	{
		for(int i = 0; i < slots.length; i++)
		{
			if(slots[i].getItem() == null)
			{
				slots[i].setItem(item);
				return;
			}
		}
	}
	
	public Slot getSlot(int x, int y)
	{
		for(int i = 0; i < slots.length; i++)
		{
			if(x < slots[i].getXCord() + 10 * Slot.getBaseScale() && x > slots[i].getXCord() && y < slots[i].getYCord() + 10 * Slot.getBaseScale() &&
					y > slots[i].getYCord())
				return slots[i];
		}
		return null;
	}
	
	public void tick(PlatformerCanvas instance)
	{
		if(!isOpen && selectedItem != null && this instanceof InventoryPlayer)
		{
			player.dropItem(selectedItem);
			prevSlot = null;
			selectedItem = null;
			return;
		}
		if(!isOpen)
			return;
		setSelectedSlot(instance.input.getMouseX(), instance.input.getMouseY());
		if(instance.input.getMousePressed())
		{
			
			if (prevSlot == null)
			{
				prevSlot = getSelectedSlot();
				return;
			}
			if(selectedItem == null)
			{
				selectedItem = prevSlot.getItem();
				prevSlot.removeItem();
			}
			if(selectedItem == null)
			{
				prevSlot = null;
				return;
			}
			getMovementSlot().setItem(selectedItem);
			selectedItem.setSelected(true);
			selectedItem.setXCord(instance.input.getMouseX());
			selectedItem.setYCord(instance.input.getMouseY());
		}
		if(!instance.input.getMousePressed() && selectedItem != null)
		{
			Slot slot = getSlot(instance.input.getMouseX(), instance.input.getMouseY());
			if(slot != null && slot.getItem() == null)
			{
				slot.setItem(selectedItem);
			}
			else if(slot != null && slot.getItem() != null)
			{
				Slot s = getSelectedSlot();
				prevSlot.setItem(s.getItem());
				s.setItem(selectedItem);
			}
			else
			{
				prevSlot.setItem(selectedItem);
				getMovementSlot().removeItem();
			}
			prevSlot = null;
			selectedItem = null;
		}
	}
	
	public void removeItem(Item i, boolean b)
	{
		for(int j = 0; j < slots.length; j++)
		{
			if(slots[j].getItem() == i)
			{
				slots[j].removeItem();
				if(b)
					player.getCurrentLevel().removeItem(i);
				return;
			}
		}
	}
	
	public boolean removeItemType(String s, boolean b)
	{
		try
		{
			Class<?> c = Class.forName("game." + s);
			Item [] items = getAllItems();
			for(int i = 0; i < items.length; i++)
			{
				if(c.isInstance(items[i]))
				{
					removeItem(items[i], b);
					return true;
				}
			}
			return false;
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			System.out.println(s + " is not a valid class name");
		}
		return false;
	}
	
	public int getAmountOfItems()
	{
		return getAllItems().length;
	}
}
