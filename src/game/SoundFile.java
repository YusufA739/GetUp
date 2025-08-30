package game;

import city.cs.engine.SoundClip;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Class Wrapper for SoundClip for duration of clips and fetching sound clips. SFX do not need a length set if they are relatively short. Long ones do need a length given.
 *
 * For example: Seeking Air Mines need this length given as they have to switch sounds after a certain amount of time.
 */
public class SoundFile {
    float length = 0;//In seconds, does not need to be used, can be set after instantiation or during depending on which signature is used
    SoundClip sound;//SoundClip sound

    public SoundFile(String FilePath) {
        setSound(FilePath);
    }

    public SoundFile(String FilePath, float length) {
        setSound(FilePath);
        setLength(length);
    }

    public void setSound(String FilePath) {
        try {
            sound = new SoundClip(FilePath);
        } catch (Exception e) {
            System.err.println("ERROR - USER BAD PATH REFERENCE:" + FilePath);
            throw new RuntimeException(e);
        }
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setAttributes(String FilePath, float length) {
        setSound(FilePath);
        setLength(length);
    }

    public void start() {play();}//alt name

    public void play() {
        sound.play();
    }

    public void pause() {
        sound.pause();
    }

    public void resume() {sound.resume();}

    public void stop() {
        sound.stop();
    }

    public void loop() {
        sound.loop();
    }
}
