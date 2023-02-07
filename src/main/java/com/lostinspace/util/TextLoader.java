package com.lostinspace.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TextLoader {
    /**
     * Loads text from a file in the 'resources' folder using the classloader.
     *
     * @param filename Name of file in resources folder.
     * @return A String with all the text from that file.
     */
    public static String loadText(String filename) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(TextLoader.class.getClassLoader().getResourceAsStream(filename))))
        ) {

            String separator = System.lineSeparator();

            return Stream.generate(() -> readLine(reader))
                    .takeWhile(Objects::nonNull)
                    .collect(Collectors.joining(separator));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            // Throw a runtime exception if there's an IOException while reading the file
            throw new RuntimeException(e);
        }
    }
}
