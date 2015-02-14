package editor;

import game.*;

import java.util.ArrayList;

public class LevelEditor
{
	private ArrayList<Block> blocks;
	private int width, height;
	private String name;
	private PlatformerCanvas instance;
	private EditorCamera camera;
	private Button [] buttons;
	private boolean didJustHide;
	
	public LevelEditor()
	{
		blocks = new ArrayList<Block>();
		buttons = new Button[1];
		didJustHide = false;
	}
	
	public void createNewLevel(String [] nameAndDimensions, PlatformerCanvas ins)
	{
		name = nameAndDimensions[0];
		width = Integer.parseInt(nameAndDimensions[1]) * 40;
		height = Integer.parseInt(nameAndDimensions[2]) * 40;
		instance = ins;
		camera = new EditorCamera(this, instance);
		camera.update();
		buttons[0] = new ButtonSelectaBlock(0, instance.getHeight() - 70, this);
	}
	
	public String getLevelName()
	{
		return name;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void addBlock(Block b)
	{
		blocks.add(b);
	}
	
	public void tick()
	{
		boolean hide = instance.input.getKeysPressed()[11];
		for(int i = 0; i < buttons.length; i++)
		{
			Button b = buttons[i];
			if(hide && !didJustHide)
			{
				//reverse the hidden status of all buttons
				b.setIfHidden(!b.isHidden());
			}
			b.tick(instance);
		}
		didJustHide = hide;
	}
	
	public Button [] getButtons()
	{
		return buttons;
	}
}