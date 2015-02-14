package game;

public class MenuMain
{
	//separate of other menus to reduce/eliminate conflicts
	
	public static final int defDisabledTime = 150;
	private final String blip = "Blip.wav", select = "Select.wav";
	private int selected, howMenuPage, deathFade, optionSet, disabledTime;
	private String [] choices, choicesDeath;
	private String imageFile, howImageFile;
	private PlatformerCanvas instance;
	private boolean howMenuOpen, didJustChange, isDeathMenu, disabled;
	private Dialog dialog;
	
	public MenuMain(PlatformerCanvas instance)
	{
		this.instance = instance;
		selected = 0;
		choices = new String [] {"-->New Game", "How to Play", "Options", "Editor", "Quit"};
		imageFile = "JailEscapeMainMenu.png";
		howImageFile = "JailEscapeHowMenu.png";
		howMenuOpen = false;
		didJustChange = false;
		isDeathMenu = false;
		disabled = false;
		optionSet = 0;
		didJustChange = true;
	}
	
	public MenuMain(PlatformerCanvas instance, boolean b)
	{
		this.instance = instance;
		selected = 0;
		choicesDeath = new String [] {"-->Restart Level", "Main Menu"};
		imageFile = "JailEscapeMainMenu.png";
		howImageFile = "JailEscapeHowMenu.png";
		howMenuOpen = false;
		didJustChange = false;
		disabled = false;
		isDeathMenu = b;
		didJustChange = true;
	}
	
	public void tick(boolean [] keysPressed)
	{
		disabledTime--;
		if(dialog != null)
		{
			tickDialog();
			if(dialog.getValues() != null)
			{
				instance.game.setSession("Editor");
			}
		}
		if(disabledTime > 0 || disabled)
			return;
		if(!howMenuOpen && !isDeathMenu)
		{
			int selectedOld = selected;
			if(keysPressed[1] == true && selected > 0 && !didJustChange)
			{
				selected -= 1;
				instance.input.getSound(blip).play();
			}
			if(keysPressed[3] == true && selected < choices.length - 1 && !didJustChange)
			{
				selected += 1;
				instance.input.getSound(blip).play();
			}
			if (selectedOld != selected && !didJustChange)
			{
				resetChoices();
				choices[selected] = "-->" + choices[selected];
			}
			if(keysPressed[0] && !didJustChange)
			{
				instance.input.getSound(select).play();
				resetChoices();
				performAction(choices[selected]);
			}
		}
		else if(!isDeathMenu)
		{
			if(keysPressed[2] == true && howMenuPage > 0 && !didJustChange)
			{
				howMenuPage--;
				instance.input.getSound(blip).play();
			}
			if(keysPressed[4] == true && howMenuPage < 3 && !didJustChange)
			{
				howMenuPage++;
				instance.input.getSound(blip).play();
			}
			if(keysPressed[0] == true && !didJustChange)
			{
				instance.input.getSound(select).play();
				howMenuOpen = false;
				choices[selected] = "-->" + choices[selected];
			}
		}
		else
		{
			if(deathFade < 100)
			{
				deathFade++;
			}
			else
			{
				int selectedOld = selected;
				if(keysPressed[1] == true && selected > 0 && !didJustChange)
				{
					selected -= 1;
					instance.input.getSound(blip).play();
				}
				if(keysPressed[3] == true && selected < choicesDeath.length - 1 && !didJustChange)
				{
					selected += 1;
					instance.input.getSound(blip).play();
				}
				if (selectedOld != selected && !didJustChange)
				{
					resetChoices();
					choicesDeath[selected] = "-->" + choicesDeath[selected];
				}
				if(keysPressed[0] && !didJustChange)
				{
					instance.input.getSound(select).play();
					resetChoices();
					performAction(choicesDeath[selected]);
				}
			}
		}
		didJustChange = false;
		for(int i = 0; i < keysPressed.length; i++)
		{
			if(keysPressed[i] == true) 
			{
				didJustChange = true;
				break;
			}
		}
	}
	
	private void performAction(String s)
	{
		if(s.equals("How to Play"))
		{
			howMenuOpen = true;
			howMenuPage = 0;
			didJustChange = true;
		}
		if(s.equals("New Game"))
		{
			instance.game.setSession("Level1");
		}
		if(s.equals("Main Menu"))
		{
			instance.game.setSession("MainMenu");
			didJustChange = true;
		}
		if(s.equals("Restart Level"))
		{
			instance.game.restartLevel();
		}
		if(s.equals("Options"))
		{
			setOptionSet(1);
		}
		if(s.equals("Back"))
		{
			if(optionSet == 1)
				setOptionSet(0);
			else if(optionSet == 2)
				setOptionSet(1);
			else if(optionSet == 3)
				setOptionSet(0);
		}
		if(s.equals("Video"))
		{
			setOptionSet(2);
		}
		if(s.startsWith("Resolution"))
		{
			instance.nextResolution();
			resetChoices();
			setSelected(0);
		}
		if(s.equals("Apply") && optionSet == 2)
		{
			instance.applyResolution();
			setOptionSet(1);
		}
		if(s.equals("Quit"))
		{
			instance.stop(true);
		}
		if(s.equals("Editor"))
		{
			if(instance.game.checkIfOwnsEditor())
				setOptionSet(3);
			else
			{
				setDisabled(defDisabledTime);
				instance.painter.setDisplayText("You do not own the expansion");
				setSelected(0);
			}
		}
		if(s.equals("New Level"))
		{
			setDisabled(true);
			openInputDialog(new String [] {"Level Name (No Spaces): ", "Width in Blocks: ", "Height in Blocks: "}, new boolean [] {true, false, false});
		}
	}

	public String getImageFileName()
	{
		return imageFile;
	}
	
	public String getHowImageFile()
	{
		return howImageFile;
	}
	
	public int getAmountChoices(boolean b)
	{
		if(b)
			return choicesDeath.length;
		return choices.length;
	}
	
	public String getChoice(int i, boolean b)
	{
		if(b)
			return choicesDeath[i];
		return choices[i];
	}
	
	private void resetChoices()
	{
		if(optionSet == 0)
		{
			choices = new String [] {"New Game", "How to Play", "Options", "Editor", "Quit"};
		}
		else if(optionSet == 1)
		{
			choices = new String [] {"Video", "Back"};
		}
		else if(optionSet == 2)
		{
			choices = new String [] {"Resolution: " + instance.getResolution().width + "x" + instance.getResolution().height, "Apply"};
		}
		else if(optionSet == 3)
		{
			choices = new String [] {"New Level", "Back"};
		}
		choicesDeath = new String [] {"Restart Level", "Main Menu"};
	}
	
	public boolean getHowMenuOpen()
	{
		return howMenuOpen;
	}
	
	public int getHowMenuPage()
	{
		return howMenuPage;
	}
	
	public int getDeathFade()
	{
		return deathFade;
	}
	
	public boolean getIsDeathMenu()
	{
		return isDeathMenu;
	}
	
	public void setSelected(int i)
	{
		String [] ss;
		if(isDeathMenu)
			ss = choicesDeath;
		else
			ss = choices;
		if(i > ss.length || i < 0)
			selected = 0;
		selected = i;
		ss[selected] = "-->" + ss[selected];
	}
	
	public void setOptionSet(int i)
	{
		optionSet = i;
		resetChoices();
		setSelected(0);
	}
	
	private void setDisabled(boolean b)
	{
		disabled = b;
	}
	
	public void setDisabled(int i)
	{
		disabledTime = i;
	}
	
	private void openInputDialog(String [] ss, boolean [] bb)
	{
		dialog = new Dialog(ss, bb, instance);
	}
	
	private void openInputDialog(String [] ss)
	{
		dialog = new Dialog(ss, instance);
	}
	
	private void tickDialog()
	{
		dialog.tick();
	}
	
	public Dialog getDialog()
	{
		return dialog;
	}
	
	public void reset()
	{
		instance.game.setSession("MainMenu");
		dialog = null;
		setOptionSet(0);
		setDisabled(false);
	}
}
