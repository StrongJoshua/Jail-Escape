package game;

import java.awt.Graphics;

public class Dialog
{
	private String [] fields, values;
	private InputField [] inputFields;
	private int xCord, yCord, width, height, selectedField, inputOffset;
	private PlatformerCanvas instance;
	private boolean didJustTab, stopTicking;
	public static final int xOffset = 10, yOffset = 10;
	
	public Dialog(String [] ss, boolean [] b, PlatformerCanvas instance)
	{
		fields = ss;
		this.instance = instance;
		inputFields = new InputField[ss.length];
		didJustTab = false;
		for(int i = 0; i < ss.length; i++)
		{
			if(b != null)
				inputFields[i] = new InputField(b[i], instance);
			else
				inputFields[i] = new InputField(instance);
		}
		while(width == 0 || height == 0)
			createBounds();
		xCord = instance.getWidth()/2 - width/2;
		yCord = instance.getHeight()/2 - height/2;
		values = null;
		stopTicking = false;
	}
	
	public Dialog(String [] ss, boolean [] b, int x, int y, PlatformerCanvas instance)
	{
		this(ss, b, instance);
		xCord = x;
		yCord = y;
	}
	
	public Dialog(String [] ss, PlatformerCanvas instance)
	{
		this(ss, null, instance);
	}
	
	public Dialog(String [] ss, int x, int y, PlatformerCanvas instance)
	{
		this(ss, instance);
		xCord = x;
		yCord = y;
	}
	
	public void tick()
	{
		if(!stopTicking)
		{	boolean tab = instance.input.getKeysPressed()[7];
			boolean sTab = instance.input.getKeysPressed()[8];
			if(tab || sTab)
			{
				if(!didJustTab)
				{
					if(tab)
						selectedField++;
					else if(sTab)
						selectedField--;
					if(selectedField >= inputFields.length)
						selectedField = 0;
					if(selectedField < 0)
						selectedField = inputFields.length - 1;
				}
				didJustTab = true;
			}
			if(!tab && !sTab)
				didJustTab = false;
			inputFields[selectedField].tick();
		}
		if(instance.input.getKeysPressed()[6])
		{
			values = new String [fields.length];
			for(int i = 0; i < fields.length; i++)
			{
				String s = inputFields[i].getInputText();
				if(s == null || s.length() == 0)
				{
					values = null;
					instance.painter.setDisplayText("You need to fill out all values!");
					break;
				}
				else
				{
					values[i] = s;
				}
			}
			if(values != null)
				stopTicking = true;
		}
	}
	
	public void createBounds()
	{
		Graphics g = instance.getTempGraphics();
		if(g != null)
		{
			instance.painter.resetFont(PaintGame.defFontSize, g);
			int i = 0;
			int k = 0;
			for(int j = 0; j < fields.length; j++)
			{
				int w = instance.painter.getStringWidth(fields[j], g);
				int h = instance.painter.getStringHeight(fields[j], g);
				if(w > i)
					i = w;
				if(h > k)
					k = h;
			}
			inputOffset = i + xOffset;
			width = i + xOffset * 3 + InputField.width;
			height = k * fields.length + yOffset * (2 + fields.length - 1);
			g.dispose();
		}
	}
	
	public int getXCord()
	{
		return xCord;
	}
	
	public int getYCord()
	{
		return yCord;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getInputOffset()
	{
		return inputOffset;
	}
	
	public int getSelectedField()
	{
		return selectedField;
	}
	
	public String [] getFields()
	{
		return fields;
	}
	
	public InputField [] getInputs()
	{
		return inputFields;
	}
	
	public String [] getValues()
	{
		return values;
	}
}