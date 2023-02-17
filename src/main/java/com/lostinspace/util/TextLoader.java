package com.lostinspace.util;

import com.lostinspace.app.ConsoleApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class for loading text from text files.
 */
public class TextLoader {
    /**
     * Loads text from a file in the 'resources' folder using the classloader.
     *
     * @param filename Name of file in resources folder.
     * @return A String with all the text from that file.
     */
    public static String loadText(String filename) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(TextLoader.class.getClassLoader().getResourceAsStream(filename)))
        ) {

            String separator = System.lineSeparator();

            return Stream.generate(() -> readLine(reader))
                    .takeWhile(Objects::nonNull)
                    .collect(Collectors.joining(separator));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads a line from the BufferedReader stream and returns it.
     * @param reader The BufferedReader from which to read a line.
     * @return The line that was read.
     */
    private static String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            // Throw a runtime exception if there's an IOException while reading the file
            throw new RuntimeException(e);
        }
    }
}
