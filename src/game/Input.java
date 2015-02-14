package game;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Input implements KeyListener, MouseListener, MouseMotionListener
{
	
	private boolean [] keysPressed;
	private Map<String, BufferedImage> imageMap;
	private Map<String, Sound> soundMap;
	private Map<String, Font> fontMap;
	private double loading, toLoad;
	private PlatformerCanvas instance;
	private int mouseX, mouseY;
	private boolean mousePressed;
	private String currentChar;
	
	public Input(PlatformerCanvas instance)
	{
		this.instance = instance;
		keysPressed = new boolean [12];
		currentChar = "";
	}

	public void bufferImages()
	{
		String checkJar = this.getClass().getResource("Input.class").toString();
		imageMap = new HashMap<String, BufferedImage>();
		String imagebase = "images";
		toLoad = 0;
		loading = 0;
		System.out.println(checkJar);
		if(!checkJar.startsWith("rsrc"))
		{
			URL u = ClassLoader.getSystemResource(imagebase);
			File folder = new File(u.getFile());
			String [] imageFiles = folder.list();
			toLoad = imageFiles.length;
			try
			{
				for(; loading < toLoad; loading++)
				{
					imageMap.put(imageFiles[(int)loading], ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(imagebase + "/" + imageFiles[(int)loading])));
				}
				System.out.println("Finished Loading Images");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("... Image Loading Error");
				System.exit(0);
			}
		}
		else
		{
			System.out.println("In a Jar");
			Scanner fileIn = new Scanner(Input.class.getResourceAsStream("/resources.txt"));
			ArrayList<String> paths = new ArrayList<String>();
			while(fileIn.hasNextLine())
			{
				String s = fileIn.nextLine();
				if(s.endsWith("png"))
					paths.add(s);
			}
			fileIn.close();
			toLoad = paths.size();
			try
			{
				for(; loading < toLoad; loading++)
				{
					System.out.println("Loaded: " + paths.get((int) loading));
					imageMap.put(paths.get((int)loading), ImageIO.read(this.getClass().getResourceAsStream("/" + imagebase + "/" + paths.get((int)loading))));
				}
				System.out.println("Finished Loading Images");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("... Image Loading Error");
				System.exit(0);
			}
		}
	}
	
	public void bufferSounds()
	{
		String checkJar = this.getClass().getResource("Input.class").toString();
		soundMap = new HashMap<String, Sound>();
		String soundbase = "sounds";
		toLoad = 0;
		loading = 0;
		System.out.println(checkJar);
		if(!checkJar.startsWith("rsrc"))
		{
			URL u = ClassLoader.getSystemResource(soundbase);
			File folder = new File(u.getFile());
			String [] soundFiles = folder.list();
			toLoad = soundFiles.length;
			for(; loading < toLoad; loading++)
			{
				soundMap.put(soundFiles[(int)loading], Sound.loadSound(soundbase + "/" + soundFiles[(int)loading]));
			}
			System.out.println("Finished Loading Sounds");
		}
		else
		{
			Scanner fileIn = new Scanner(Input.class.getResourceAsStream("/resources.txt"));
			ArrayList<String> paths = new ArrayList<String>();
			while(fileIn.hasNextLine())
			{
				String s = fileIn.nextLine();
				if(s.endsWith("wav"))
					paths.add(s);
			}
			fileIn.close();
			toLoad = paths.size();
			try
			{
				for(; loading < toLoad; loading++)
				{
					System.out.println("Loaded: " + paths.get((int) loading));
					soundMap.put(paths.get((int)loading), Sound.loadSound(soundbase + "/" + paths.get((int)loading)));
				}
				System.out.println("Finished Loading Sounds");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("... Sound Loading Error");
				System.exit(0);
			}
		}
	}
	
	public void loadFonts()
	{
		String checkJar = this.getClass().getResource("Input.class").toString();
		fontMap = new HashMap<String, Font>();
		String fontbase = "fonts";
		toLoad = 0;
		loading = 0;
		System.out.println(checkJar);
		if(!checkJar.startsWith("rsrc"))
		{
			URL u = ClassLoader.getSystemResource(fontbase);
			File folder = new File(u.getFile());
			String [] imageFiles = folder.list();
			toLoad = imageFiles.length;
			try
			{
				for(; loading < toLoad; loading++)
				{
					fontMap.put(imageFiles[(int)loading],
							Font.createFont(Font.TRUETYPE_FONT, this.getClass().getClassLoader().getResourceAsStream(fontbase + "/" + imageFiles[(int)loading])));
				}
				System.out.println("Finished Loading Fonts");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("... Font Loading Error");
				System.exit(0);
			}
		}
		else
		{
			System.out.println("In a Jar");
			Scanner fileIn = new Scanner(Input.class.getResourceAsStream("/resources.txt"));
			ArrayList<String> paths = new ArrayList<String>();
			while(fileIn.hasNextLine())
			{
				String s = fileIn.nextLine();
				if(s.endsWith("ttf"))
					paths.add(s);
			}
			fileIn.close();
			toLoad = paths.size();
			try
			{
				for(; loading < toLoad; loading++)
				{
					System.out.println("Loaded: " + paths.get((int) loading));
					fontMap.put(paths.get((int)loading),
							Font.createFont(Font.TRUETYPE_FONT, this.getClass().getClassLoader().getResourceAsStream(fontbase + "/" + paths.get((int)loading))));
				}
				System.out.println("Finished Loading Fonts");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("... Font Loading Error");
				System.exit(0);
			}
		}
	}
	
	public int getLoading()
	{
		try
		{
			double i = loading/toLoad;
			i *= 100;
			return (int)i;
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		currentChar = "" + e.getKeyChar();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			keysPressed[0] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_W)
		{
			keysPressed[1] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_A)
		{
			keysPressed[2] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_S)
		{
			keysPressed[3] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_D)
		{
			keysPressed[4] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_E)
		{
			keysPressed[5] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			keysPressed[6] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_TAB && !keysPressed[10])
		{
			keysPressed[7] = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_TAB && keysPressed[10])
		{
			keysPressed[8] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
		{
			keysPressed[9] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SHIFT)
		{
			keysPressed[10] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_H)
		{
			keysPressed[11] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			instance.stop(false);
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			keysPressed[0] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_W)
		{
			keysPressed[1] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_A)
		{
			keysPressed[2] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_S)
		{
			keysPressed[3] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_D)
		{
			keysPressed[4] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_E)
		{
			keysPressed[5] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			keysPressed[6] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_TAB && !keysPressed[10])
		{
			keysPressed[7] = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_TAB && keysPressed[10])
		{
			keysPressed[8] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
		{
			keysPressed[9] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_SHIFT)
		{
			keysPressed[10] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_H)
		{
			keysPressed[11] = false;
		}
	}
	
	public boolean [] getKeysPressed()
	{
		boolean [] a = new boolean [keysPressed.length];
		for(int i = 0; i < a.length; i++)
		{
			a[i] = keysPressed [i];
		}
		return a;
	}
	
	public BufferedImage getImage(String s)
	{
		return imageMap.get(s);
	}
	
	public Sound getSound(String s)
	{
		return soundMap.get(s);
	}
	
	public Font getFont(String s)
	{
		return fontMap.get(s);
	}
	
	/*private boolean [] resetKeys(boolean [] keys)
	{
		for(int i = 0; i < keys.length; i++)
			keys[i] = false;
		return keys;
	}
	
	public void resetAllKeys()
	{
		for(int i = 0; i < keysPressed.length; i++)
			keysPressed[i] = false;
	}*/

	@Override
	public void mouseClicked(MouseEvent e)
	{
		//not used
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		mousePressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		mousePressed = false;
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		instance.requestFocus();
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		//not used
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		instance.requestFocus();
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		instance.requestFocus();
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	public int getMouseX()
	{
		float f = mouseX;
		f /= instance.getXScale();
		return (int)f;
	}
	
	public int getMouseY()
	{
		float f = mouseY;
		f /= instance.getYScale();
		return (int) f;
	}
	
	public boolean getMousePressed()
	{
		return mousePressed;
	}
	
	public String getCurrentChar()
	{
		String tmp = currentChar;
		currentChar = "";
		return tmp;
	}
}