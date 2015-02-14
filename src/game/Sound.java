package game;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Sound implements LineListener
{		
	private Clip clip;
	
	public Sound()
	{
		
	}
	public Sound(String s)
	{
		loadAudio(s);
	}
	public static Sound loadSound(String fileName) {
		Sound sound = new Sound();
		try {
			InputStream in = Sound.class.getClassLoader().getResourceAsStream(fileName);
			BufferedInputStream buffIn = new BufferedInputStream(in);
			AudioInputStream ais = AudioSystem.getAudioInputStream(buffIn);
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			sound.clip = clip;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sound;
	}
	
	public void loadAudio(String fileName)
	{
		try {
			Object as = AudioSystem.getAudioInputStream(Sound.class.getResource(fileName));
		           try {
		                AudioInputStream stream = (AudioInputStream) as;
		                AudioFormat format = stream.getFormat();

		                /**
		                 * we can't yet open the device for ALAW/ULAW playback,
		                 * convert ALAW/ULAW to PCM
		                 */
		                if ((format.getEncoding() == AudioFormat.Encoding.ULAW) ||
		                    (format.getEncoding() == AudioFormat.Encoding.ALAW)) 
		                {
		                    AudioFormat tmp = new AudioFormat(
		                                              AudioFormat.Encoding.PCM_SIGNED, 
		                                              format.getSampleRate(),
		                                              format.getSampleSizeInBits() * 2,
		                                              format.getChannels(),
		                                              format.getFrameSize() * 2,
		                                              format.getFrameRate(),
		                                              true);
		                    stream = AudioSystem.getAudioInputStream(tmp, stream);
		                    format = tmp;
		                }
		                DataLine.Info info = new DataLine.Info(
		                                          Clip.class, 
		                                          stream.getFormat(), 
		                                          ((int) stream.getFrameLength() *
		                                              format.getFrameSize()));

		                Clip c = (Clip) AudioSystem.getLine(info);
		                c.addLineListener(this);
		                c.open(stream);
		                clip = c;
		            } catch (Exception ex) { 
				ex.printStackTrace(); 
				as = null;
		            }
			}
		catch (Exception e) {
			e.printStackTrace();
		    }
	}

	public void play() {
		try {
			if (clip != null) {
				new Thread() {
					public void run() {
						synchronized (clip) {
							clip.stop();
							clip.setFramePosition(0);
							clip.start();
						}
					}
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isPlaying()
	{
		if(clip.isActive())
			return true;
		else
			return false;
	}
	
	public void loop()
	{
		try {
			if (clip != null) {
				new Thread() {
					public void run() {
						synchronized (clip) {
							clip.stop();
							clip.setFramePosition(0);
							clip.loop(Clip.LOOP_CONTINUOUSLY);
						}
					}
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop()
	{
		clip.stop();
	}
	
	public void update(LineEvent arg0)
	{
		
	}
}