import java.io.IOException;
 
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
 
public class SoundPlayer extends Thread {
	private String filename;
 
	private boolean loop;
	
	private AudioInputStream stream;
	
	private DataLine.Info info;
	
	private SourceDataLine line;
 
	public SoundPlayer(String filename, boolean loop) {
		this.filename = filename;
		this.loop = loop;
	}
 
	public void run() {
		setName("SoundPlayer");
		try {
			do {
				stream = AudioSystem.getAudioInputStream(ClassLoader.getSystemResource(filename));
				info = new DataLine.Info(SourceDataLine.class, stream.getFormat(), ((int) stream.getFrameLength() * stream.getFormat().getFrameSize()));
				line = (SourceDataLine) AudioSystem.getLine(info);
				int len = 0;
				int count = 1024 * stream.getFormat().getFrameSize();
				byte[] buffer = new byte[count];
				line.open(stream.getFormat());
				line.start();
				while ((len = stream.read(buffer, 0, count)) != -1) {
					line.write(buffer, 0, len);
				}
				line.stop();
				line.close();
			} while (loop);
		} catch (UnsupportedAudioFileException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		} catch (LineUnavailableException exception) {
			exception.printStackTrace();
		}
	}
 
	public void exit() {
		line.stop();
		line.close();
	}
}

