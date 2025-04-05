package audio;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class SoundManager {
    private static SoundManager instance = null;
    private SoundUtilityJOAL soundJOAL;
    private Map<String, Boolean> loadedSounds;
    private Timer soundTimer;


    private SoundManager() {
        soundJOAL = new SoundUtilityJOAL(); // only initialized once!
        loadedSounds = new HashMap<>();
        soundTimer = new Timer();

    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void loadSound(String name) {
        if (!loadedSounds.containsKey(name)) {
            if (!soundJOAL.load(name, 0f, 0f, 10f, true)) {
                System.out.println("Could not load " + name);
            } else {
                loadedSounds.put(name, true);
            }
        }
    }

    public void playSound(String name) {
        loadSound(name); // auto-load
        soundJOAL.play(name);
    }

    public void stopSound(String name) {
        soundJOAL.stop(name);
    }

    public void pauseSound(String name) {
        soundJOAL.pause(name);
    }
    public void resumeSound(String name) {
        soundJOAL.play(name); // Resume by playing again (JOAL typically continues from pause point)
    }
    
    public void playSoundForDuration(String name, long durationMs) {
        playSound(name);
        soundTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                stopSound(name);
            }
        }, durationMs);
    }
    public void cleanup() {
        soundTimer.cancel();  // Stop all pending TimerTasks
        soundTimer.purge();   // Remove references to cancelled tasks
        soundJOAL.cleanUp();  // Release OpenAL resources (if such a method exists)
    }
    
}