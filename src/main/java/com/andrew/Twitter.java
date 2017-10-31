package com.andrew;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Twitter {

    public static void main(String[] args) {

        if (args.length != 2) {
            throw new RuntimeException("exactly two commandline params expected, 1: users Filepath and 2: tweets Filepath");
        }

        //For the given input we get back {Ward=[Alan, Martin], Alan=[Martin]}
        Map<String, Set<String>> follows = parseUsersFile(args[0]);

        System.out.println(follows);

        List<Tweet> tweets = parseTweetsFile(args[1]);

        System.out.println(tweets);

    }


    private static List<Tweet> parseTweetsFile(String tweetsFilepath) {

        List<Tweet> tweets = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(tweetsFilepath))) {
            stream.forEach(
                    line -> {
                        String[] vals = line.split("> ");
                        if (vals.length != 2) {
                            //todo sprintf
                            throw new RuntimeException("invalid tweet file line:"+line);
                        }
                        Tweet tweet = new Tweet(vals[0], vals[1]);
                        tweets.add(tweet);
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException("Problem reading from tweetsFilepath: ", e);
        }
        return tweets;

    }

    private static Map<String, Set<String>> parseUsersFile(String
                                                                   usersFilepath) {

        Map<String, Set<String>> follows = new HashMap<>();

        try (Stream<String> stream = Files.lines(Paths.get(usersFilepath))) {
            stream.forEach(
                    line -> {

                        String[] vals = line.split(" follows ");
                        if (vals.length != 2) {
                            //todo sprintf
                            throw new RuntimeException("invalid user file line:"+line);
                        }

                        String person = vals[0];
//                        TODO - should I chomp and be tolerant?
                        //There may be duplicates so go for a list not a Set
                        List<String> peopleFollowedByPerson = Arrays.asList(vals[1].split(", "));

                        if (follows.containsKey(person)) {
                            Set<String> setOfPeopleFollowedByPerson = follows.get(person);
                            setOfPeopleFollowedByPerson.addAll(peopleFollowedByPerson);

                        } else {
                            follows.put(person, new HashSet(peopleFollowedByPerson));
                        }

                    }
            );
        } catch (IOException e) {
            throw new RuntimeException("Problem reading from usersFilepath: ", e);
        }
        return follows;

    }

}