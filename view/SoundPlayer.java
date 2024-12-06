package view;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {

    public static void playSound(String soundName) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile())) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Start the clip
            System.out.println("Playing sound...");
            clip.start();

            // Ensure the program waits for the clip to finish
           
        } catch (UnsupportedAudioFileException e) {
            System.err.println("The specified audio file is not supported: " + soundName);
        } catch (IOException e) {
            System.err.println("Error reading the audio file: " + soundName);
        } catch (LineUnavailableException e) {
            System.err.println("Audio line for playback is unavailable.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    // public static void main(String[] args) {
    //     String soundName = "./assets/music.wav";
    //     playSound(soundName);
    // }
}