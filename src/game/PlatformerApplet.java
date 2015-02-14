package game;

import java.applet.*;
import java.awt.*;

public class PlatformerApplet extends Applet
{
	private static final long serialVersionUID = 1L;
	public PlatformerCanvas canvas = new PlatformerCanvas();
	
	public void init()
	{
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
	}
	
	public void start()
	{
		canvas.start();
	}
	
	public void stop()
	{
		canvas.stop(false);
	}
}