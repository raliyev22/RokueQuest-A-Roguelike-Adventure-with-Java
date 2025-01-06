package main.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundEffects {
    private static SoundEffects instance;
    private Map<String, Clip> soundEffects = new HashMap<>();

    private SoundEffects() {}

    public static SoundEffects getInstance() {
        if (instance == null) {
            instance = new SoundEffects();
        }
        return instance;
    }
    
    public void addSoundEffect(String label, String filePath) {
        try {
            if (!soundEffects.containsKey(label)) { 
                File audioFile = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                soundEffects.put(label, clip);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Ses efektini yüklerken hata oluştu: " + e.getMessage());
        }
    }

    public void playSoundEffect(String label) {
        Clip clip = soundEffects.get(label);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop(); 
            }
            clip.setFramePosition(0); 
            clip.start();
        } else {
            System.err.println("Belirtilen etiketle ses efekti bulunamadı: " + label);
        }
    }

    public void loopSoundEffect(String label) {
        Clip clip = soundEffects.get(label);
        if (clip != null && !clip.isRunning()) { 
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else if (clip == null) {
            System.err.println("Belirtilen etiketle ses efekti bulunamadı: " + label);
        }
    }

    public void setVolume(String label, float volume) {
        Clip clip = soundEffects.get(label);
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), volume));
            gainControl.setValue(volume);
        } else {
            System.err.println("Belirtilen etiketle ses efekti bulunamadı: " + label);
        }
    }

    public void playSoundEffectInThread(String label) {
        new Thread(() -> {
            playSoundEffect(label);
        }).start();
    }   
}
