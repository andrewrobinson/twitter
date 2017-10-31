package com.andrew;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class TwitterParser {

    String userFilepath;

    String tweetsFilepath;

    public TwitterParser(String userFilepath, String tweetsFilepath) {
        this.userFilepath = userFilepath;
        this.tweetsFilepath = tweetsFilepath;
    }

    public void showTweets() {

        //Build up a data structure based on user.txt
        //For each Person as key, I store a unique list of people that they follow as the value
        //I just use a plain String to represent a Person/TwitterParser handle for now but this could become a class later
        Map<String, Set<String>> whoFollowsWho = parseUsersFile(userFilepath);

        //Build up a data structure based on tweet.txt
        List<Tweet> tweets = parseTweetsFile(tweetsFilepath);

        //Martin does not tweet and is only mentioned via being followed, but he still must appear in the output
        //To build a list of all persons involved we need to go through all followed as well as all followers
        //If I was using state in this class I could probably have accomplished this while inside parseUsersFile
        //but I would rather have one method return one thing with no side effects
        Set<String> listOfAllPersons = buildListOfAllPersons(whoFollowsWho);

        //Make each person follow themselves. The input file given does not explicitly specify this.
        //(if it did then the program would not have a problem, since I am using a Set)
        //I prefer to make explicit the fact that this method modifies its input.
        //(IntelliJ's extract method did not do it this way)
        whoFollowsWho = makeEachPersonFollowThemselves(whoFollowsWho);

        Map<String, List<Tweet>> tweetsPerPerson =
                buildTweetsPerPerson(listOfAllPersons, whoFollowsWho, tweets);

        printTweetsPerPerson(tweetsPerPerson);

    }

    private static void printTweetsPerPerson(Map<String, List<Tweet>>
                                                     tweetsPerPerson) {

        for (String person : tweetsPerPerson.keySet()) {

            //I don't feel like using StringBuilder
            //I could stream to stdout to be fancy?
            System.out.println(person + "\n");

            for (Tweet tweet : tweetsPerPerson.get(person)) {
                System.out.println("\t@" + tweet.getPerson() + ": " +
                        tweet.getMessage() + "\n");
            }

        }

    }

    private static Map<String, List<Tweet>>
    buildTweetsPerPerson(Set<String> listOfAllPersons, Map<String,
            Set<String>> whoFollowsWho, List<Tweet> tweets) {

        Map<String, List<Tweet>> tweetsPerPerson = new TreeMap<>();

        for (String person : listOfAllPersons) {

            tweetsPerPerson.put(person, new ArrayList<>());

            for (Tweet tweet : tweets) {

                Set<String> followed = whoFollowsWho.get(person);

                if (followed != null && followed.contains(tweet.getPerson())) {
                    tweetsPerPerson.get(person).add(tweet);
                }

            }
        }

        return tweetsPerPerson;

    }

    private static Map<String, Set<String>> makeEachPersonFollowThemselves(Map<String, Set<String>> whoFollowsWho) {
        for (String person : whoFollowsWho.keySet()) {
            whoFollowsWho.get(person).add(person);
        }
        return whoFollowsWho;
    }

    private static Set<String> buildListOfAllPersons(Map<String, Set<String>> follows) {

        //I use TreeSet to get alphabetical sorting, which will give me the desired console output(Alan then Martin then Ward)
        Set<String> listOfAllPersons = new TreeSet<>();

        for (String person : follows.keySet()) {

            listOfAllPersons.add(person);
            listOfAllPersons.addAll(follows.get(person));
        }

        return listOfAllPersons;

    }


    private static List<Tweet> parseTweetsFile(String tweetsFilepath) {

        try {
            Stream<String> stream = Files.lines(Paths.get(tweetsFilepath));
            return parseTweets(stream);
        } catch (IOException e) {
            throw new RuntimeException("Problem reading from tweetsFilepath:", e);
        }


    }

    private static List<Tweet> parseTweets(Stream<String> stream) {

        List<Tweet> tweets = new ArrayList<>();

        stream.forEach(
                line -> {
                    String[] vals = line.split("> ");
                    if (vals.length != 2) {
                        //todo sprintf
                        throw new RuntimeException("invalid tweet file line:" + line);
                    }
                    Tweet tweet = new Tweet(vals[0], vals[1]);
                    tweets.add(tweet);
                }
        );

        return tweets;

    }

    private static Map<String, Set<String>> parseUsersFile(String usersFilepath) {

        try {
            Stream<String> stream = Files.lines(Paths.get(usersFilepath));
            return parseUsers(stream);
        } catch (IOException e) {
            throw new RuntimeException("Problem reading from usersFilepath:", e);
        }

    }

    private static Map<String, Set<String>> parseUsers(Stream<String> stream) {

        Map<String, Set<String>> follows = new HashMap<>();

        stream.forEach(
                line -> {

                    String[] vals = line.split(" follows ");
                    if (vals.length != 2) {
                        //TODO sprintf
                        throw new RuntimeException("invalid user file line:" + line);
                    }

                    String person = vals[0];
                    //TODO - split on , and chomp any whitespace
                    List<String> peopleFollowedByPerson =
                            Arrays.asList(vals[1].split(", "));

                    if (follows.containsKey(person)) {
                        Set<String> setOfPeopleFollowedByPerson =
                                follows.get(person);

                        setOfPeopleFollowedByPerson.addAll(peopleFollowedByPerson);
                    } else {
                        follows.put(person, new HashSet(peopleFollowedByPerson));
                    }

                }
        );

        return follows;

    }


}