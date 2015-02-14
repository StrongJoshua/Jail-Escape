package game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class PlatformerCanvas extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	private Tick tick;
	private ThreadLoad loadThread;
	private Thread tickThread;
	private boolean isRunning, isTicking, didChangeRes;
	public PaintGame painter;
	public Input input;
	private Dimension appSize;
	public GameHandler game;
	private Timer paintTimer;
	public static final Dimension baseResolution = new Dimension(1280, 720);
	private int paintTimerDelay, resolution;
	public static final Dimension [] resolutions = new Dimension [] {new Dimension(1024, 768), new Dimension(1280, 720), new Dimension(1280, 800),
		new Dimension(1280, 1024), new Dimension(1366, 769), new Dimension(1440, 900), new Dimension(1680, 900), new Dimension(1680, 1050), new Dimension(1920, 1080)};
	private static JFrame frame;
	private static JPanel panel;
	private BufferStrategy bs;
	private GraphicsDevice gd;
	
	public PlatformerCanvas()
	{
		resolution = 1;
		didChangeRes = false;
		appSize = resolutions[resolution];
		setSize(appSize);
		setPreferredSize(appSize);
		setMaximumSize(appSize);
		setMinimumSize(appSize);
		setFocusable(true);
		requestFocus();
		isRunning = false;
		isTicking = false;
		tick = new Tick(this);
		loadThread = new ThreadLoad(this);
		tickThread = new Thread(this);
		input = new Input(this);
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		this.setFocusTraversalKeysEnabled(false);
		painter = new PaintGame(this);
		game = new GameHandler(this);
		paintTimerDelay = 1000/60;
		paintTimer = new Timer(paintTimerDelay, new PaintTimer(this));
		gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		this.setIgnoreRepaint(true);
		this.setVisible(true);
	}
	
	public synchronized void start()
	{
		if(isRunning)
			return;
		isRunning = true;
		paintTimer.start();
		loadThread.start();
		tickThread.start();
	}
	
	public synchronized void stop(boolean within)
	{
		if(!isRunning)
			return;
		isRunning = false;
		try {
			if(loadThread.isAlive())
				loadThread.join();
			paintTimer.stop();
			if(!within)
				tickThread.join();
			System.exit(0);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void render()
	{
		bs = getBufferStrategy();
		if(bs == null || didChangeRes)
		{
			createBufferStrategy(3);
			requestFocus();
			didChangeRes = false;
			return;
		}
		try //try and catch clause for the case of the BufferStrategy being null after resizing the frame
		{
			Graphics g = bs.getDrawGraphics();
			painter.paint(g);
			g.dispose();
		}
		catch(Exception e)
		{
			createBufferStrategy(3);
			requestFocus();
			didChangeRes = false;
			return;
		}
		if(!bs.contentsLost())
			bs.show();
	}

	@Override
	public void run()
	{
		int ticks = 0;
		long startTime = System.currentTimeMillis();
		long lastTick = System.nanoTime();
		
		double nanosPerTick = 1000000000.0 / 100;
		double unprocessed = 0;
		
		requestFocus();
		
		while(isRunning)
		{
			long currentTimeMilli = System.currentTimeMillis();
			long currentTimeNano = System.nanoTime();
			
			unprocessed += (currentTimeNano - lastTick) / nanosPerTick;
			lastTick = currentTimeNano;
			
			while(isTicking && unprocessed >= 1)
			{
				ticks++;
				unprocessed -= 1;
				tick.run();
			}
			
			try
			{
				Thread.sleep(2);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			if(currentTimeMilli - startTime > 1000)
			{
				startTime += 1000;
				System.out.println(ticks + " ticks");
				ticks = 0;
			}
		}
	}
	
	public void startTicking()
	{
		isTicking = true;
	}
	
	private class PaintTimer implements ActionListener
	{
		private PlatformerCanvas instance;
		
		public PaintTimer(PlatformerCanvas instance)
		{
			this.instance = instance;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			instance.render();
		}
	}
	
	public static void main(String[] args) {
		PlatformerCanvas canvas = new PlatformerCanvas();

		frame = new JFrame("Jail Escape");
		frame.setUndecorated(true);

		panel = new JPanel(new BorderLayout());
		panel.add(canvas, BorderLayout.CENTER);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SwingUtilities.invokeLater(new Runnable(){
									public void run()
									{
										frame.setVisible(true);
									}});

		canvas.start();
	}
	
	@Override
	public void paint(Graphics g)
	{
		//do nothing, painting is handled by a BufferStrategy
	}
	
	@Override
	public void update(Graphics g)
	{
		//do nothing, painting is handled by a BufferStrategy
	}
	
	public float getXScale()
	{
		float f1 = appSize.width;
		float f2 = baseResolution.width;
		float f = f1/f2;
		
		return f;
	}
	
	public float getYScale()
	{
		float f1 = appSize.height;
		float f2 = baseResolution.height;
		float f = f1/f2;
		
		return f;
	}
	
	@Override
	public int getWidth()
	{
		return baseResolution.width;
	}
	
	@Override
	public int getHeight()
	{
		return baseResolution.height;
	}
	
	public int getAppWidth()
	{
		return super.getWidth();
	}
	
	public int getAppHeight()
	{
		return super.getHeight();
	}
	
	public Dimension getResolution()
	{
		return resolutions[resolution];
	}
	
	public void nextResolution()
	{
		resolution++;
		if(resolution >= resolutions.length)
			resolution = 0;
	}
	
	public void applyResolution()
	{
		appSize = resolutions[resolution];
		if(appSize.equals(Toolkit.getDefaultToolkit().getScreenSize()))
		{
			if(gd.isFullScreenSupported())
				gd.setFullScreenWindow(frame);
		}
		else if(!appSize.equals(Toolkit.getDefaultToolkit().getScreenSize()) && gd.getFullScreenWindow() != null)
		{
			gd.setFullScreenWindow(null);
		}
		setSize(appSize);
		setPreferredSize(appSize);
		setMaximumSize(appSize);
		setMinimumSize(appSize);
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.requestFocus();
		SwingUtilities.invokeLater(new Runnable(){
									public void run()
									{
										frame.setVisible(true);
									}});
		didChangeRes = true;
	}
	
	/***
	 * Make sure to dispose of this Graphics object after it has been used!
	 * @return Graphics object to be used and then disposed.
	 * <br>
	 * Will return null if the BufferStrategy of the Canvas is null.
	 */
	public Graphics getTempGraphics()
	{
		if(bs != null)
			return bs.getDrawGraphics();
		return null;
	}
}