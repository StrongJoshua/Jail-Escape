package game;

import java.awt.Graphics;

public class InputField
{
	public static final int width = 250;
	private final int defMaxBackLimit = 20;
	private int maxBackLimit, backLimit;
	private int xStart, pointer;
	private boolean acceptLetters;
	private PlatformerCanvas instance;
	private String input;
	
	
	public InputField(PlatformerCanvas ins)
	{
		acceptLetters = true;
		instance = ins;
		xStart = 0;
		pointer = 0;
		input = "";
		maxBackLimit = defMaxBackLimit;
		backLimit = 0;
	}
	public InputField(boolean b, PlatformerCanvas ins)
	{
		this(ins);
		acceptLetters = b;
	}
	
	public int getXStart()
	{
		return xStart;
	}
	
	public int getPointer()
	{
		return pointer;
	}
	
	public String getInputText()
	{
		return input;
	}
	
	public void tick()
	{
		String s = instance.input.getCurrentChar();
		if(!s.equals(" ") && !s.equals("") && PaintGame.isTypableChar(s))
		{
			if(!acceptLetters)
			{
				if(onlyNumbers(s))
				{
					input += s;
					int i = getCharWidth(s);
					if(pointer + i > width)
					{
						int x = width - pointer;
						pointer += x;
						i -= x;
						xStart += i;
					}
					else
						pointer += i;
				}
			}
			else
			{
				input += s;
				int i = getCharWidth(s);
				if(pointer + i > width)
				{
					int x = width - pointer;
					pointer += x;
					i -= x;
					xStart += i;
				}
				else
					pointer += i;
			}
		}
		boolean back = instance.input.getKeysPressed()[9];
		if(!back)
		{
			maxBackLimit = defMaxBackLimit;
			backLimit = 0;
		}
		if(back && input.length() != 0)
		{
			backLimit--;
			if(backLimit <= 0)
			{
				maxBackLimit--;
				backLimit = maxBackLimit;
				int i = getCharWidth(input.substring(input.length() - 1, input.length()));
				String s1 = input.substring(0, input.length() - 1);
				input = s1;
				if(pointer - i <= 0)
				{
					pointer = i;
					xStart = getCharWidth(input) - i;
				}
				else
					pointer -= i;
			}
		}
		if(input.length() == 0 || input == null)
		{
			pointer = 0;
			xStart = 0;
		}
	}
	
	public boolean onlyNumbers(String s)
	{
		try
		{
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	private int getCharWidth(String s)
	{
		Graphics g = null;
		while(g == null)
			g = instance.getTempGraphics();
		instance.painter.resetFont(PaintGame.defFontSize, g);
		return instance.painter.getStringWidth(s, g);
	}
}