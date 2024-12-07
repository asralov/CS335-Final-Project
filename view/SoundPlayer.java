package view;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * A utility class to handle audio playback in the application.
 * This class plays a sound file specified by its file path.
 */
public class SoundPlayer {

    /**
     * Plays the specified sound file.
     *
     * @param soundName The path to the sound file to be played.
     */
    public static void playSound(String soundName) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile())) {
            // Obtain a clip for audio playback
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream); // Open the audio input stream with the clip

            // Start playing the sound
            System.out.println("Playing sound...");
            clip.start();

            // Block until the clip has finished playing
            while (clip.isRunning()) {
                Thread.sleep(50); // Sleep to prevent busy-waiting
            }

            System.out.println("Sound playback finished.");
        } catch (UnsupportedAudioFileException e) {
            System.err.println("The specified audio file is not supported: " + soundName);
        } catch (IOException e) {
            System.err.println("Error reading the audio file: " + soundName);
        } catch (LineUnavailableException e) {
            System.err.println("Audio line for playback is unavailable.");
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted while waiting for audio to finish.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
