package game;

import editor.*;

public class GameHandler
{
	private String session;
	private MenuMain mainMenu;
	private PlatformerCanvas instance;
	private Level currentLevel;
	private LevelEditor levelEditor;
	
	public GameHandler(PlatformerCanvas instance)
	{
		this.instance = instance;
		session = "Loading";
	}
	
	public String getSession()
	{
		return session;
	}

	public void setSession(String s)
	{
		if(s.equals(session))
			return;
		if(s.equals("MainMenu"))
		{
			session = s;
			mainMenu = new MenuMain(instance);
			instance.startTicking();
		}
		if(s.startsWith("Level"))
		{
			session = s;
			mainMenu = null;
			currentLevel = null;
			try
			{
				currentLevel = (Level)Class.forName("game." + session).newInstance();
				currentLevel.init(instance);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(session + " does not exist");
			}
		}
		if(s.equals("RestartMenu"))
		{
			session = s;
			mainMenu = new MenuMain(instance, true);
		}
		if(s.equals("Editor"))
		{
			try
			{
				levelEditor = (LevelEditor) Class.forName("editor.LevelEditor").newInstance();
				session = s;
				levelEditor.createNewLevel(mainMenu.getDialog().getValues(), instance);
				mainMenu = null;
			}
			catch(Exception e)
			{
				instance.painter.setDisplayText("You do not own the expansion");
				mainMenu.setDisabled(MenuMain.defDisabledTime);
				mainMenu.reset();
				return;
			}
		}
		System.out.println("Session switched to: " + session);
	}
	
	public MenuMain getMainMenu()
	{
		return mainMenu;
	}
	
	public Level getCurrentLevel()
	{
		return currentLevel;
	}
	
	public void restartLevel()
	{
		setSession(currentLevel.getClass().getName().replaceAll("game.", ""));
	}
	
	public String getCurrentLevelName()
	{
		return currentLevel.getClass().getName().replaceAll("game.", "");
	}
	
	public boolean checkIfOwnsEditor()
	{
		try
		{
			Class.forName("editor.LevelEditor");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public LevelEditor getLevelEditor()
	{
		return levelEditor;
	}
}
