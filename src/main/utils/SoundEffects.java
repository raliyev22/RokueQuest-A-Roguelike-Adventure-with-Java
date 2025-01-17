package main.utils;

import javax.sound.sampled.*;

import javafx.scene.media.AudioClip;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundEffects {
    private static SoundEffects instance;
    private Map<String, Clip> soundEffects = new HashMap<>();
    private Map<String, Integer> pausedPositions = new HashMap<>();
    private boolean isInitialized = false;

    private SoundEffects() {}

    public static SoundEffects getInstance() {
        if (instance == null) {
            instance = new SoundEffects();
        }
        return instance;
    }

    public void loadSounds() {
        if (isInitialized) return;

        try {
            addSoundEffect("background", "src/main/sounds/background.wav");
            setVolume("background", -20);
            addSoundEffect("menuButtons", "src/main/sounds/menuButtons.wav");
            addSoundEffect("blueButtons", "src/main/sounds/blueButtons.wav");
            addSoundEffect("save", "src/main/sounds/save.wav");
            addSoundEffect("putting", "src/main/sounds/putting.wav");
            addSoundEffect("step", "src/main/sounds/step.wav");
            setVolume("step", -10);
            addSoundEffect("door", "src/main/sounds/door.wav");
            addSoundEffect("gameWinner", "src/main/sounds/gameWinner.wav");
            setVolume("gameWinner", -15);
            addSoundEffect("gameLoser", "src/main/sounds/gameLoser.wav");
            setVolume("gameLoser", -15);
            addSoundEffect("archer", "src/main/sounds/archer.wav");
            setVolume("archer", -5);
            addSoundEffect("fighter", "src/main/sounds/fighter.wav");
            setVolume("fighter", -5);
            addSoundEffect("wizard", "src/main/sounds/wizard.wav");
            setVolume("wizard", -10);
            addSoundEffect("escButton", "src/main/sounds/escButton.wav");
            addSoundEffect("sparkle", "src/main/sounds/sparkle.wav");
            setVolume("sparkle", -5);

            isInitialized = true;
        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
        }
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
            System.err.println("Error loading sound effect: " + e.getMessage());
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
            System.err.println("No sound effects found with the specified tag: " + label);
        }
    }

    public void stopSoundEffect(String label) {
        Clip clip = soundEffects.get(label);
        if (clip != null && clip.isRunning()) {
            clip.stop();
            System.out.println("Sound effect stopped for label: " + label);
        } else if (clip == null) {
            System.err.println("No sound effects found with the specified tag: " + label);
        } else {
            System.out.println("Sound effect is not currently running for label: " + label);
        }
    }

    public void loopSoundEffect(String label) {
        Clip clip = soundEffects.get(label);
        if (clip != null && !clip.isRunning()) { 
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else if (clip == null) {
            System.err.println("No sound effects found with the specified tag: " + label);
        }
    }

    public void setVolume(String label, float volume) {
        Clip clip = soundEffects.get(label);
        if (clip != null) {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), volume));
                gainControl.setValue(volume);
            } else {
                System.err.println("Volume control not supported for: " + label);
            }
        } else {
            System.err.println("No sound effects found with the specified tag: " + label);
        }
    }

    public void playSoundEffectInThread(String label) {
        new Thread(() -> {
            playSoundEffect(label);
        }).start();
    }   

    public void pauseSoundEffect(String label) {
        new Thread(() -> {
            try {
                // 1500 ms bekle
                Thread.sleep(300);
    
                // Ses efektini durdur
                Clip clip = soundEffects.get(label);
                if (clip != null && clip.isRunning()) {
                    pausedPositions.put(label, clip.getFramePosition());
                    clip.stop();
                    System.out.println("Sound effect paused after 1500 ms.");
                } else {
                    System.err.println("No sound effects playing with the specified tag: " + label);
                }
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted: " + e.getMessage());
            }
        }).start();
    }
    

    public void resumeSoundEffect(String label) {
        Clip clip = soundEffects.get(label);
        Integer pausedPosition = pausedPositions.get(label);
        if (clip != null && pausedPosition != null) {
            clip.setFramePosition(pausedPosition);
            clip.start();
            pausedPositions.remove(label);  // Clear the paused position after resuming
        } else {
            System.err.println("No paused sound effects found with the specified tag: " + label);
        }
    }

    public float getVolume(String key) {
        Clip clip = soundEffects.get(key);
        if (clip != null) {
            try {
                // FloatControl'un mevcut olup olmadığını kontrol et
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                return gainControl.getValue(); // dB olarak mevcut sesi döndür
            } catch (IllegalArgumentException e) {
                System.err.println("MASTER_GAIN control not supported for sound: " + key);
            }
        } else {
            System.err.println("No sound effects found with the specified tag: " + key);
        }
        return -80.0f; // Varsayılan olarak -80 dB döndür
    }
    

    public static void main(String[] args) throws InterruptedException {

            // JavaFX başlatılması gerekiyor
            new javafx.embed.swing.JFXPanel(); // JavaFX runtime'ı başlatır
            
            // Buton sesini yükle
            String buttonSoundPath = new File("src/main/sounds/blueButtons.wav").toURI().toString();
            AudioClip buttonSound = new AudioClip(buttonSoundPath);
            
            // Buton sesini çal
            buttonSound.play();
            System.out.println("Button sound started.");

            // Sesin tamamen çalmasını bekle
            Thread.sleep(2000); // 2 saniye bekle (buton sesinin süresine göre ayarlayın)

            System.out.println("Button sound finished.");
    }
}
