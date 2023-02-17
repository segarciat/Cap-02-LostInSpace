package com.lostinspace.util;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class SoundLoader {
    public static Clip loadMusic(String filename) {
        BufferedInputStream audioStream = new BufferedInputStream(SoundLoader.class.getClassLoader().getResourceAsStream(filename));
        try {
            // Get an AudioInputStream from the audio resource stream. Represents a stream of audio data.
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioStream);
            // Get the audio format of the audio file
            AudioFormat format = ais.getFormat();
            // Create an Info object for the Clip class using the audio format
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            // Get a Clip object from the AudioSystem using the Info object. I believe this step is needed
            Clip clip = (Clip) AudioSystem.getLine(info);
            // Open the Clip object with the audio input stream
            clip.open(ais);
            // set isPlaying bool to true
            return clip;
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            // Print stack trace if an error occurs
            throw new RuntimeException(e);
        }
    }
}
