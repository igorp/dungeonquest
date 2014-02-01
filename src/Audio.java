import java.io.File;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

class Audio {

    public Audio(String fileName) {
        try {
            File file = new File(fileName);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(file);

            int bytesPerFrame = audioInput.getFormat().getFrameSize();
            int byteCount = 1024 * bytesPerFrame;
            byte[] buffer = new byte[byteCount];

            AudioFormat audioFormat = audioInput.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();

            int bytes;

            while ((bytes = audioInput.read(buffer, 0, byteCount)) != -1) {
                line.write(buffer, 0, bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
