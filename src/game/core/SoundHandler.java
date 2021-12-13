package game.core;

import javax.sound.sampled.*;
import java.io.IOException;

public class SoundHandler {

    private Clip clip;

    //WAV files only, please.
    public void setMusic(String url){
        if (clip!=null) { clip.stop(); }
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream( Utils.getResource(url) ));
        }
        catch (LineUnavailableException | IOException | UnsupportedAudioFileException e)
        { e.printStackTrace(); }
    }

    public void playMusic(){
        clip.start();
        try { Thread.sleep(clip.getMicrosecondLength()); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void loopMusic(){
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        while(true) {
            try { Thread.sleep(clip.getMicrosecondLength()); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public void stopMusic() {
        clip.stop();
    }
}
