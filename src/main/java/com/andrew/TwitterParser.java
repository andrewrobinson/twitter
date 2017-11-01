package com.andrew;

import java.util.*;
import java.util.stream.Stream;

public class TwitterParser {

    public static void showTweets(Stream<String> users, Stream<String> allTweets) {

        //Build up a data structure based on user.txt
        //For each Person as key, I store a unique list of people that they follow as the value
        //I just use a plain String to represent a Person/TwitterParser handle for now but this could become a class later
        Map<String, Set<String>> whoFollowsWho = parseUsers(users);

        //Build up a data structure based on tweet.txt
        List<Tweet> tweets = parseTweets(allTweets);

        //Martin does not tweet and is only mentioned via being followed, but he still must appear in the output
        //To build a list of all persons involved we need to go through all followed as well as all followers
        //If I was using state in this class I could probably have accomplished this while inside parseUsers instead
        //of this extra iteration but I would rather have one method return one thing with no side effects
        Set<String> listOfAllPersons = buildListOfAllPersons(whoFollowsWho);

        //Make each person follow themselves. The input file given does not explicitly specify this.
        //(if it did then the program would not have a problem, since I am using a Set)
        //I prefer to make explicit the fact that this method modifies its input.
        //(IntelliJ's extract method did not do it this way)
        whoFollowsWho = makeEachPersonFollowThemselves(whoFollowsWho);

        Map<String, List<Tweet>> tweetsPerPerson = buildTweetsPerPerson(listOfAllPersons, whoFollowsWho, tweets);

        printTweetsPerPerson(tweetsPerPerson);

    }

    private static void printTweetsPerPerson(Map<String, List<Tweet>> tweetsPerPerson) {

        for (String person : tweetsPerPerson.keySet()) {

            System.out.println(person + "\n");

            for (Tweet tweet : tweetsPerPerson.get(person)) {
                System.out.println("\t@" + tweet.getPerson() + ": " +
                        tweet.getMessage() + "\n");
            }

        }

    }

    private static Map<String, List<Tweet>> buildTweetsPerPerson(Set<String> listOfAllPersons, Map<String, Set<String>> whoFollowsWho, List<Tweet> tweets) {

        //I use TreeMap to get alphabetical key sorting, which will give me the desired console output(Alan then Martin then Ward)
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

       Set<String> listOfAllPersons = new HashSet<>();

        for (String person : follows.keySet()) {

            listOfAllPersons.add(person);
            listOfAllPersons.addAll(follows.get(person));
        }

        return listOfAllPersons;

    }

    private static List<Tweet> parseTweets(Stream<String> stream) {

        List<Tweet> tweets = new ArrayList<>();

        if (stream == null) {
            return tweets;
        }

        stream.forEach(
                line -> {
                    String[] vals = line.split("> ");
                    if (vals.length != 2) {
                        throw new RuntimeException("invalid tweet file line:" + line);
                    }
                    Tweet tweet = new Tweet(vals[0], vals[1]);
                    tweets.add(tweet);
                }
        );

        return tweets;

    }

    private static Map<String, Set<String>> parseUsers(Stream<String> stream) {

        Map<String, Set<String>> follows = new HashMap<>();

        if (stream == null) {
            return follows;
        }

        stream.forEach(
                line -> {

                    String[] vals = line.split(" follows ");
                    if (vals.length != 2) {
                        throw new RuntimeException("invalid user file line:" + line);
                    }

                    String person = vals[0];
                    List<String> peopleFollowedByPerson =
                            Arrays.asList(vals[1].split(", "));

                    if (follows.containsKey(person)) {
                        Set<String> setOfPeopleFollowedByPerson = follows.get(person);
                        setOfPeopleFollowedByPerson.addAll(peopleFollowedByPerson);
                    } else {
                        follows.put(person, new HashSet(peopleFollowedByPerson));
                    }

                }
        );

        return follows;

    }

}