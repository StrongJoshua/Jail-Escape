package game;

public class Tick// extends Thread
{
	
	private PlatformerCanvas instance;
	
	public Tick(PlatformerCanvas instance)
	{
		this.instance = instance;
	}
	
	public void run()
	{
		String session = instance.game.getSession();
		if(session.equals("MainMenu") || session.equals("RestartMenu"))
		{
			instance.game.getMainMenu().tick(instance.input.getKeysPressed());
		}
		if(session.startsWith("Level"))
		{
			try
			{
				if(instance.painter.isShowingTitle())
					return;
				Level currentLevel = instance.game.getCurrentLevel();
				if(currentLevel == null)
					return;
				currentLevel.tick(instance.input.getKeysPressed());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(session + " does not exist or there in an error within it");
			}
		}
		if(session.equals("Editor"))
		{
			instance.game.getLevelEditor().tick();
		}
	}
}