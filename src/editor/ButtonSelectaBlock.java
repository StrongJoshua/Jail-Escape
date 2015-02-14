package editor;

import game.Button;

public class ButtonSelectaBlock extends Button
{
	private LevelEditor editor;
	
	public ButtonSelectaBlock(int x, int y, LevelEditor edit)
	{
		super("ButtonSelectaBlock.png", x, y, 400, 70);
		editor = edit;
	}

	@Override
	public void click()
	{
		System.out.println("clicked");
	}
	
}
