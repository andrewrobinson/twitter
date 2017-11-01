package com.andrew;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        if (args.length != 2) {
            throw new RuntimeException("exactly two commandline params expected, 1:users filepath and 2:tweets filepath ");
        }

        Stream<String> users = readFileAsStream(args[0], "Problem reading from users filepath.");
        Stream<String> allTweets = readFileAsStream(args[1], "Problem reading from tweets filepath.");

        System.out.println(TwitterParser.getTweetFeedForDisplay(users, allTweets));

    }

    private static Stream<String> readFileAsStream(String filename, String errorMessage) {

        try {
            //7-bit ASCII was specified
            return Files.lines(Paths.get(filename), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            throw new RuntimeException(errorMessage, e);
        }

    }

}
