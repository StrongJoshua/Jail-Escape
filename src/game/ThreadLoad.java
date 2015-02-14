package game;

public class ThreadLoad extends Thread
{
	//exists to provide a loading % capability
	
	private PlatformerCanvas instance;
	
	public ThreadLoad(PlatformerCanvas instance)
	{
		this.instance = instance;
	}
	
	public void run()
	{
		instance.input.bufferImages();
		instance.input.bufferSounds();
		instance.game.setSession("MainMenu");
	}
}