package game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import editor.LevelEditor;

public class PaintGame
{
	private PlatformerCanvas instance;
	public static final int defFontSize = 27;
	private final int defTitleDelay = 120, defDisplayTextTimer = 90;
	private int titleDelay, displayTextTimer;
	private boolean showingTitle;
	public static final Color red = new Color(196, 59, 57), glow = new Color(255, 254, 199), grey = new Color(82, 82, 82),
			black = new Color(0, 0, 0);
	private String alphabet = "1234567890";
	private BufferedImage deathFade;
	private String displayText;
	
	public PaintGame(PlatformerCanvas instance)
	{
		this.instance = instance;
		titleDelay = defTitleDelay;
		showingTitle = false;
		instance.input.loadFonts();
	}
	
	public void paint(Graphics g)
	{
		BufferedImage game = new BufferedImage(PlatformerCanvas.baseResolution.width, PlatformerCanvas.baseResolution.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = game.createGraphics();
		paintGame(g2);
		g2.dispose();
		
		g.drawImage(game, 0, 0, instance.getAppWidth(), instance.getAppHeight(), instance);
	}
	
	public void paintGame(Graphics g)
	{
		if(deathFade == null)
		{
			deathFade = new BufferedImage(instance.getWidth(), instance.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = deathFade.createGraphics();
			g2.setColor(black);
			g2.fillRect(0, 0, deathFade.getWidth(), deathFade.getHeight());
			g2.dispose();
		}
		String session = instance.game.getSession();
		resetFont(defFontSize, g);
		if(session.equals("Loading"))
			paintLoading(g);
		else if(session.equals("MainMenu"))
			paintMain(g);
		else if(session.startsWith("Level"))
			paintLevel(g);
		else if(session.equals("RestartMenu"))
		{
			paintLevel(g);
			paintMain(g);
		}
		else if(session.equals("Editor"))
		{
			paintEditor(g);
		}
		if(displayText != null)
			drawDisplayText(g);
	}
	
	private void paintLoading(Graphics g)
	{
		g.setColor(grey);
		g.fillRect(0, 0, instance.getWidth(), instance.getHeight());
		g.setColor(glow);
		String loading = "Loading: " + instance.input.getLoading() + "%";
		g.drawString(loading, getCenterX(loading, g), 450);
		g.setColor(red);
		String title = "Platformer by StrongJoshua";
		g.drawString(title, getCenterX(title, g), 150);
	}
	
	private void paintMain(Graphics g)
	{
		MenuMain menu = instance.game.getMainMenu();
		if(menu == null)
			return;
		if(menu.getHowMenuOpen() == false && !menu.getIsDeathMenu())
		{
			g.drawImage(instance.input.getImage(menu.getImageFileName()), 0, 0, instance.getWidth(), instance.getHeight(), instance);
			for(int i = 0; i < menu.getAmountChoices(false); i++)
			{
				String choice = menu.getChoice(i, false);
				g.drawString(choice, getCenterX(choice, g), 200 + i*(getStringHeight(choice, g) + 20));
			}
			String choice = menu.getChoice(menu.getAmountChoices(false) - 1, false);
			g.drawString(choice, getCenterX(choice, g), instance.getAppHeight());
		}
		else if(!menu.getIsDeathMenu())
		{
			g.drawImage(instance.input.getImage(getSequenceImage(menu.getHowImageFile(), menu.getHowMenuPage())), 0, 0, instance.getWidth(), instance.getHeight(),
					instance);
		}
		else
		{
			if(menu.getDeathFade() < 100)
			{
				float f = menu.getDeathFade();
				f /= 100;
				g.drawImage(setOpacity(deathFade, f), 0, 0, instance);
			}
			else
			{
				g.fillRect(0, 0, instance.getWidth(), instance.getHeight());
				g.setColor(glow);
				for(int i = 0; i < menu.getAmountChoices(true); i++)
				{
					String choice = menu.getChoice(i, true);
					g.drawString(choice, getCenterX(choice, g), 200 + i*(getStringHeight(choice, g) + 20));
				}
				//draw YOU DIED
				g.setColor(red);
				String mes = "YOU DIED";
				g.drawString(mes, getCenterX(mes, g), 50 + getStringHeight(mes, g));
			}
		}
		resetFont(defFontSize, g);
		Dialog d = menu.getDialog();
		if(d != null)
		{
			drawDialog(g, d);
		}
	}
	
	private void paintLevel(Graphics g)
	{
		Level level = instance.game.getCurrentLevel();
		if(level == null)
			return;
		//paint title screen with delay
		if(titleDelay > 0)
		{
			showingTitle = true;
			paintTitle(level.getGameName(), g);
			titleDelay--;
			return;
		}
		showingTitle = false;
		//draw background
		g.drawImage(instance.input.getImage(instance.game.getCurrentLevelName() + "Background.png"), 0, 0, instance.getWidth(), instance.getHeight(), instance);
		//draw blocks
		drawBlocks(g, level);
		//draw player
		drawPlayer(g);
		//draw NPCs
		drawNPCs(g, level);
		
		Player player = instance.game.getCurrentLevel().getPlayer();
		//draw the items on the ground
		Item [] items = level.getItems();
		drawItemsOnGround(g, items);
		//draw indicators
		drawIndicators(g, player);
		//draw inventories if open
		Inventory inv = player.getInventory();
		if(inv.getIsOpen())
		{
			drawInventory(g, inv);
		}
		//check if you won, and display win message if you did
		if(player.getIfWon())
		{
			g.drawImage(instance.input.getImage(Level.getWinShadowFile()), 0, 0, instance.getWidth(), instance.getHeight(), instance);
			resetFont(defFontSize, g);
			drawEmbossedText(level.getWinMessage(), getCenterX(level.getWinMessage(), g), getCenterY(level.getWinMessage(), g), 6, g);
		}
		resetFont(defFontSize, g);
	}
	
	private void drawNPCName(NPC npc, Graphics g)
	{
		resetFont(9, g);
		int x = npc.getRelativeXCord() - (getStringWidth(npc.getName(), g)/2) + (npc.getWidth()/2);
		int y = npc.getRelativeYCord() - npc.getHeight() - 10;
		g.drawString(npc.getName(), x, y);
	}

	public int getStringWidth(String s, Graphics g)
	{
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		Rectangle2D bounds = metrics.getStringBounds(s, g);
		return (int)bounds.getWidth();
	}

	public int getStringHeight(String s, Graphics g)
	{
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		Rectangle2D bounds = metrics.getStringBounds(s, g);
		return (int)bounds.getHeight();
	}
	
	private int getCenterX(String s, Graphics g)
	{
		return (int) ((instance.getWidth()/2) - (getStringWidth(s, g)/2));
	}
	
	private int getCenterX(BufferedImage img)
	{
		return (int) ((instance.getWidth()/2) - (img.getWidth()/2));
	}
	
	private int getCenterY(String s, Graphics g)
	{
		return (int) ((instance.getHeight()/2) - (getStringHeight(s, g)/2));
	}
	
	private int getCenterY(BufferedImage img)
	{
		return (int) ((instance.getHeight()/2) - (img.getHeight()/2));
	}
	
	public void clearScreen(Graphics g)
	{
		g.clearRect(0, 0, instance.getWidth(), instance.getHeight());
	}
	
	public void paintTitle(String s, Graphics g)
	{
		clearScreen(g);
		g.setColor(black);
		g.fillRect(0, 0, instance.getWidth(), instance.getHeight());
		g.setColor(glow);
		g.drawString(s, getCenterX(s, g), 100);
	}
	
	public void resetTitleDelay()
	{
		titleDelay = defTitleDelay;
	}
	
	public void drawEmbossedText(String s, int x, int y, int amount, Graphics g)
	{
		g.drawString(s, x + (amount/2), y - (amount/2));
		g.setColor(glow);
		g.drawString(s, x, y);
		resetFont(defFontSize, g);
		
	}
	
	/***
	 * Reset the given Graphics object's font to the PressStart2P font with the given font size
	 * @param fontSize
	 * @param g
	 */
	public void resetFont(int fontSize, Graphics g)
	{
		g.setColor(black);
		Font f = instance.input.getFont("PressStart2P.ttf");
		g.setFont(f.deriveFont((float)fontSize));
	}
	
	public boolean isShowingTitle()
	{
		return showingTitle;
	}
	
	private String getOverlay(String s)
	{
		if(s == null)
			return null;
		else
		{
			 return s.replaceAll(".png", "Overlay.png");
		}
	}
	
	private String getSequenceImage(String s, int i)
	{
		if(s == null)
			return null;
		else
		{
			return s.replaceAll(".png", i + ".png");
		}
	}
	
	private void drawInventory(Graphics g, Inventory inv)
	{
		BufferedImage img = instance.input.getImage(inv.getGUIFile());
		g.drawImage(img, getCenterX(img), 0, getImgWidth(img, 1), getImgHeight(img, 1), instance);
		drawInventoryItems(g, inv);
		Slot slot = inv.getSelectedSlot();
		if(slot != null)
		{
			BufferedImage img1 = instance.input.getImage(Slot.overlay);
			g.drawImage(img1, slot.getXCord(), slot.getYCord(), getImgWidth(img1, Slot.getBaseScale()), getImgHeight(img1, Slot.getBaseScale()), instance);
		}
	}

	private void drawInventoryItems(Graphics g, Inventory inv)
	{
		Item [] items = inv.getAllItems();
		BufferedImage img;
		
		for(int i = 0; i < items.length; i++)
		{
			img = instance.input.getImage(items[i].getImageFile());
			g.drawImage(img, items[i].getTrueXCord(), items[i].getTrueYCord(), getImgWidth(img, items[i].getBaseScale()), getImgHeight(img, items[i].getBaseScale()),
					instance);
			if(items[i].getIsSelected())
				drawToolTip(g, items[i]);
		}
	}
	
	private void drawItemsOnGround(Graphics g, Item [] items)
	{
		BufferedImage img;
		
		for(int i = 0; i < items.length; i++)
		{
			if(items[i] != null)
			{
				if(!items[i].isInInventory())
				{
					if(items[i].getRelativeXCord() > (items[i].getWidth() * -1) && items[i].getRelativeXCord() < instance.getWidth())
					{
						img = instance.input.getImage(items[i].getImageFile());
						g.drawImage(img, items[i].getRelativeXCord(), items[i].getRelativeYCord(),
								getImgWidth(img, items[i].getBaseScale()), getImgHeight(img, items[i].getBaseScale()), instance);
					}
				}
			}
		}
	}
	
	private void drawToolTip(Graphics g, Item item)
	{
		String name = item.getName();
		resetFont(21, g);
		g.setColor(Color.WHITE);
		int width = getStringWidth(name, g);
		int height = getStringHeight(name, g);
		int direction = AI.LEFT;
		if(wouldLeaveScreen(instance.input.getMouseX(), width))
			direction = AI.RIGHT;
		int x = instance.input.getMouseX() - width + (width * direction) - 5;
		int y = instance.input.getMouseY() - height - 5;
		
		g.drawImage(instance.input.getImage(Slot.toolTipUnderlay), x - 5, y - 5, width + 5, height + 5, instance);
		g.drawString(name, x, y + height);
	}
	
	private boolean wouldLeaveScreen(int x, int i)
	{
		if(x + i > instance.getWidth())
			return true;
		return false;
	}
	
	private String getDoorImage(String s, boolean b)
	{
		if(b)
			return s.replaceAll(".png", "Open.png");
		else
			return s.replaceAll(".png", "Closed.png");
	}
	
	public BufferedImage getImage(Entity e)
	{
		if(e instanceof Player && e.getAnimationSequence() != 0)
			return instance.input.getImage(this.getSequenceImage(e.getImageFile(), e.getAnimationSequence()).replaceAll(".png", "Arm.png"));
		return instance.input.getImage(this.getSequenceImage(e.getImageFile(), e.getAnimationSequence()));
	}
	
	private void drawPlayer(Graphics g)
	{
		Player player = instance.game.getCurrentLevel().getPlayer();
		//draw player
		BufferedImage pi = instance.input.getImage(getSequenceImage(player.getImageFile(), player.getAnimationSequence())); 
		g.drawImage(pi, player.getRelativeXCord(), player.getRelativeYCord() - player.getHeight(), getImgWidth(pi, player.getBaseScale()),
				getImgHeight(pi, player.getBaseScale()), instance);
		//draw weapon, if equipped
		
		if(player.getAnimationSequence() != 0)
		{
			//draw outside arm
			BufferedImage pai = instance.input.getImage(this.getSequenceImage(player.getImageFile(), player.getAnimationSequence()).replaceAll(".png", "Arm.png"));
			g.drawImage(pai, player.getRelativeXCord(), player.getRelativeYCord() - player.getHeight(), getImgWidth(pai, player.getBaseScale()),
					getImgHeight(pai, player.getBaseScale()), instance);
		}
		//draw player hurt overlay if hurt time is > 0
		if(player.getHurtTime() > 0)
		{
			BufferedImage pih = instance.input.getImage(getOverlay(getSequenceImage(player.getImageFile(), player.getAnimationSequence())));
			g.drawImage(pih, player.getRelativeXCord(), player.getRelativeYCord() - player.getHeight(), getImgWidth(pih, player.getBaseScale()),
					getImgHeight(pih, player.getBaseScale()), instance);
		}
		//draw the health bar border
		g.drawImage(instance.input.getImage(player.getHealthBorderFile()), 10, 10, instance);
		//draw the health
		int h = player.getHealth();
		g.drawImage(instance.input.getImage(player.getHealthFile()), 15, 15, h, 10, instance);
		//write the health out of total to the right of the bar
		resetFont(10, g);
		g.drawString(h + "/" + player.getMaxHealth(), 125, 25);
	}
	
	private void drawNPCs(Graphics g, Level level)
	{
		BufferedImage img;
		NPC [] npcs = level.getNPCs();
		
		if(npcs != null)
		{	for(int i = 0; i < npcs.length; i++)
			{
				if(npcs[i] == null)
					continue;
				if(npcs[i].getRelativeXCord() > (npcs[i].getWidth() * -1) && npcs[i].getRelativeXCord() < instance.getWidth())
				{
					img = instance.input.getImage(getSequenceImage(npcs[i].getImageFile(), npcs[i].getAnimationSequence()));
					g.drawImage(img, npcs[i].getRelativeXCord(), npcs[i].getRelativeYCord() - npcs[i].getHeight(), getImgWidth(img, npcs[i].getBaseScale()),
							getImgHeight(img, npcs[i].getBaseScale()), instance);
					//draw their name if not null
					if(npcs[i].getName() != null)
					{
						drawNPCName(npcs[i], g);
					}
				}
			}
		}
	}
	
	private void drawBlocks(Graphics g, Level level)
	{
		Block [] blocks = level.getBlocks();
		BufferedImage img;
		
		for(int i = 0; i < blocks.length; i++)
		{
			if(blocks[i] == null)
				continue;
			if(blocks[i].getRelativeXCord() + blocks[i].getScaledWidth() > 0 && blocks[i].getRelativeXCord() < instance.getWidth())
			{
				String s;
				if(blocks[i] instanceof BlockDoor)
				{
					s = getDoorImage(blocks[i].getImageFile(), blocks[i].getIfOpen());
				}
				else
				{
					s = blocks[i].getImageFile();
				}
				img = instance.input.getImage(s);
				g.drawImage(img, blocks[i].getRelativeXCord(), blocks[i].getRelativeYCord(),
						getImgWidth(img, blocks[i].getScale()), getImgHeight(img, blocks[i].getScale()), instance);
			}
		}
	}
	
	private int getAlphabetIndex(char c)
	{
		return alphabet.indexOf(c);
	}
	
	private void drawIndicators(Graphics g, Player player)
	{
		Indicator [] inds = new Indicator [player.getIndicators().size()];
		inds = player.getIndicators().toArray(inds);
		for(int i = 0; i < inds.length; i++)
		{
			drawIndicator(g, inds[i]);
		}
	}
	
	private void drawIndicator(Graphics g, Indicator ind)
	{
		int size = 15;
		if(ind instanceof IndicatorDamage)
		{
			int mod = Integer.parseInt(ind.getMessage());
			mod /= 10;
			if(mod != 0)
				size *= mod;
		}
		BufferedImage mes = getCustomText(instance.input.getImage(Indicator.IndicatorNums), ind.getMessage(), size);
		mes = convertColors(mes, Indicator.WHITE, ind.getColor());
		float convert = ind.getTimer();
		mes = setOpacity(mes, 1 - (convert/100));
		g.drawImage(mes, ind.getRelativeXCord(), ind.getRelativeYCord(), instance);
	}
	
	private int [] getPixels(BufferedImage img, int x, int y, int w, int h)
	{
		int [] pixels = new int [w * h];
		img.getRGB(x, y, w, h, pixels, 0, w);
		return pixels;
	}
	
	private BufferedImage getCustomText(BufferedImage font, String s, int size)
	{
		BufferedImage mes = new BufferedImage(s.length() * size, size, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < s.length(); i++)
		{
			if(size == font.getHeight())
				mes.setRGB(i * size, 0, size, size, getPixels(font, getAlphabetIndex(s.charAt(i)) * font.getHeight(), 0, font.getHeight(), font.getHeight()),
						0, size);
			else
			{
				mes.setRGB(i * size, 0, size, size,
						resizePixels(getPixels(font, getAlphabetIndex(s.charAt(i)) * font.getHeight(), 0, font.getHeight(), font.getHeight()), font.getHeight(), size),
						0, size);
			}
		}
		return mes;
	}
	
	private BufferedImage convertColors(BufferedImage img, int oldColor, int newColor)
	{
		int [] pixels = new int [img.getWidth() * img.getHeight()];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
		Color old = new Color(oldColor);//create the colors using the hex values
		Color newC = new Color(newColor);
		
		for(int x = 0; x < img.getWidth(); x++)
		{
			for(int y = 0; y < img.getHeight(); y++)
			{
				Color tmp = new Color(pixels[x + y * img.getWidth()]);//create the temporary color from the RGB value of the pixel
				if(tmp.equals(old))//check if the temporary color matches the old
				{
					pixels[x + y * img.getWidth()] = newC.getRGB();//convert the pixel to the new color
				}
			}
		}
		img.setRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
		
		return img;
	}
	
	private int [] resizePixels(int [] pixels, int origSize, int newSize)
	{
		BufferedImage img = new BufferedImage(origSize, origSize, BufferedImage.TYPE_INT_ARGB);
		img.setRGB(0, 0, origSize, origSize, pixels, 0, origSize);
		
		BufferedImage nimg = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = nimg.getGraphics();
		g.drawImage(img, 0, 0, newSize, newSize, instance);
		g.dispose();
		
		int [] pixNew = new int [newSize * newSize]; 
		nimg.getRGB(0, 0, newSize, newSize, pixNew, 0, newSize);
		
		return pixNew;
	}
	
	private BufferedImage setOpacity(BufferedImage img, float f)
	{
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImg.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, f));
		g.drawImage(img, 0, 0, instance);
		g.dispose();

		return newImg;
	}
	
	private int getImgWidth(BufferedImage img, int bs)
	{
		//float f = instance.getScale();
		//f *= bs;
		return (int) (img.getWidth() * bs);
	}
	
	private int getImgHeight(BufferedImage img, int bs)
	{
		//float f = instance.getScale();
		//f *= bs;
		return (int) (img.getHeight() * bs);
	}
	
	public void setDisplayText(String s)
	{
		displayText = s;
		displayTextTimer = defDisplayTextTimer;
	}
	
	private void drawDisplayText(Graphics g)
	{
		displayTextTimer--;
		resetFont(defFontSize, g);
		drawBorderBox(g, instance.getWidth(), getStringHeight(displayText, g) + 20, 0, getStringHeight(displayText, g) + 10);
		resetFont(defFontSize + 9, g);
		g.drawString(displayText, getCenterX(displayText, g) + 5, getCenterY(displayText, g));
		g.setColor(red);
		g.drawString(displayText, getCenterX(displayText, g), getCenterY(displayText, g));
		if(displayTextTimer <= 0)
			displayText = null;
	}
	
	private void drawBorderBox(Graphics g, int w, int h, int xOffset, int yOffset)
	{
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		g2.setColor(black);
		g2.fillRect(0, 0, img.getWidth(), img.getHeight());
		g2.setColor(grey);
		g2.fillRect(5, 5, img.getWidth() - 10, img.getHeight() - 10);
		g2.dispose();
		g.drawImage(img, getCenterX(img), getCenterY(img) - yOffset, w, h, instance);
	}
	
	private void drawDialog(Graphics g, Dialog d)
	{
		drawBorderBox(g, d.getWidth(), d.getHeight(), 0, 0);
		InputField [] ifs = d.getInputs();
		String [] ss = d.getFields();
		for(int i = 0; i < ss.length; i++)
		{
			BufferedImage img = new BufferedImage(InputField.width, getStringHeight(ss[0], g), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = img.createGraphics();
			resetFont(defFontSize, g2);
			setOpacity(img, 0);
			g2.setComposite(AlphaComposite.SrcOver);
			//draw a slight border
			g2.setColor(glow);
			g2.drawRect(0, 0, img.getWidth() - 1, img.getHeight() - 1);
			g2.setColor(black);
			g2.drawString(ifs[i].getInputText(), 0 - ifs[i].getXStart(), img.getHeight());
			if(i == d.getSelectedField())
			{
				g2.drawLine(ifs[i].getPointer(), 0, ifs[i].getPointer(), img.getHeight());
			}
			int y = d.getYCord() + Dialog.yOffset * (i + 1) + getStringHeight(ss[i], g) * (i + 1);
			g.drawString(ss[i], d.getXCord() + Dialog.xOffset, y);
			g.drawImage(img, d.getXCord() + d.getInputOffset(), y - getStringHeight(ss[i], g), instance);
			g2.dispose();
		}
	}
	
	public static boolean isTypableChar(String s)
	{
		String a = "1234567890-=+!*()qwertyuiopasdfghjkl;':\"zxcvbnm?,./";
		int l = a.length();
		for(int i = 0; i < l; i++)
		{
			if(s.equalsIgnoreCase("" + a.charAt(i)))
			{
				return true;
			}
		}
		return false;
	}
	
	private void paintEditor(Graphics g)
	{
		LevelEditor edit = instance.game.getLevelEditor();
		//draw background, this will be enhanced with customizable backgrounds in the future
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, instance.getWidth(), instance.getHeight());
		//draw buttons
		drawButtons(g, edit.getButtons());
	}
	
	private void drawButtons(Graphics g, Button [] bs)
	{
		for(int i = 0; i < bs.length; i++)
		{
			Button b = bs[i];
			if(b.isHidden())
				continue;
			g.drawImage(instance.input.getImage(b.getImageFile()), b.getXCord(), b.getYCord(), instance);
			if(b.isHighlighted())
			{
				g.drawImage(instance.input.getImage(Button.highlight), b.getXCord(), b.getYCord(), b.getWidth(), b.getHeight(), instance);
			}
		}
	}
}