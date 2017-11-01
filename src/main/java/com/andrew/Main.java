package com.andrew;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        if (args.length != 2) {
            throw new RuntimeException("exactly two commandline params expected, 1:users Filepath and 2:tweets Filepath ");
        }

        Stream<String> users = readFileAsStream(args[0], "Problem reading from usersFilepath:");
        Stream<String> allTweets = readFileAsStream(args[1], "Problem reading from tweetsFilepath:");

        TwitterParser.showTweets(users, allTweets);

    }

    private static Stream<String> readFileAsStream(String filename, String errorMessage) {

        try {
            return Files.lines(Paths.get(filename), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            throw new RuntimeException(errorMessage, e);
        }

    }

}
