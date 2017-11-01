package com.andrew;

import java.util.*;
import java.util.stream.Stream;

public class TwitterParser {

    /**
     *
     * This builds up the tweet feed as specified by the instructions
     *
     * @param users the Stream<String> from the user.txt file
     * @param allTweets the Stream<String> from the tweet.txt
     * @return the output as a String
     */
    public static String getTweetFeedForDisplay(Stream<String> users, Stream<String> allTweets) {

        Map<String, List<Tweet>> tweetsPerUser = getTweetsPerUser(users, allTweets);

        StringJoiner lineJoiner = new StringJoiner("\n");

        for (Map.Entry<String, List<Tweet>> entry : tweetsPerUser.entrySet()) {

            lineJoiner.add(entry.getKey());

            for (Tweet tweet : entry.getValue()) {
                lineJoiner.add(new StringBuilder("\t@").append(tweet.getUser()).append(": ").append(tweet.getMessage()));
            }

        }

        return lineJoiner.toString();

    }

    /**
     * This returns the desired output, but still in data structure form
     *
     * @param users
     * @param allTweets
     * @return  the desired output still in data structure form
     */
    protected static Map<String, List<Tweet>> getTweetsPerUser(Stream<String> users, Stream<String> allTweets) {

        //Build up a data structure based on user.txt
        //For each User as key, I store a unique list of users that they follow as the value
        //I just use a plain String to represent a User/Twitter handle for now but this could become a class later
        Map<String, Set<String>> whoFollowsWho = parseUsers(users);

        //Build up a data structure based on tweet.txt
        List<Tweet> tweets = parseTweets(allTweets);

        //Martin does not tweet and is only mentioned via being followed, but he still must appear in the output
        //To build a list of all persons involved we need to go through all followed as well as all followers
        //If I was using state in this class I could probably have accomplished this while inside parseUsers instead
        //of this extra iteration but I would rather have one method return one thing with no side effects
        Set<String> listOfAllPersons = buildListOfAllPersons(whoFollowsWho);

        //Make each user follow themselves. The input file given does not explicitly specify this.
        //(if it did then the program would not have a problem, since I am using a Set)
        //I prefer to make explicit the fact that this method modifies its input.
        //(IntelliJ's extract method did not do it this way)
        whoFollowsWho = makeEachPersonFollowThemselves(whoFollowsWho);

        return buildTweetsPerPerson(listOfAllPersons, whoFollowsWho, tweets);

    }

    private static Map<String, List<Tweet>> buildTweetsPerPerson(Set<String> listOfAllPersons, Map<String, Set<String>> whoFollowsWho, List<Tweet> tweets) {

        //I use TreeMap to get alphabetical key sorting, which will give me the desired console output(Alan then Martin then Ward)
        Map<String, List<Tweet>> tweetsPerPerson = new TreeMap<>();

        for (String person : listOfAllPersons) {

            tweetsPerPerson.put(person, new ArrayList<>());

            for (Tweet tweet : tweets) {

                Set<String> followed = whoFollowsWho.get(person);

                if (followed != null && followed.contains(tweet.getUser())) {
                    tweetsPerPerson.get(person).add(tweet);
                }

            }
        }

        return tweetsPerPerson;

    }

    private static Map<String, Set<String>> makeEachPersonFollowThemselves(Map<String, Set<String>> whoFollowsWho) {

        //Sonarlint made me use this syntax instead of iterating over keySet()
        for (Map.Entry<String, Set<String>> entry : whoFollowsWho.entrySet()) {
            entry.getValue().add(entry.getKey());
        }
        return whoFollowsWho;
    }

    private static Set<String> buildListOfAllPersons(Map<String, Set<String>> follows) {

       Set<String> listOfAllPersons = new HashSet<>();

        for (Map.Entry<String, Set<String>> entry : follows.entrySet()) {
            listOfAllPersons.add(entry.getKey());
            listOfAllPersons.addAll(entry.getValue());
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

    protected static Map<String, Set<String>> parseUsers(Stream<String> stream) {

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

                    //"Each line of a well-formed user file contains a user, followed by the word 'follows' and then a comma separated list of users they follow."
                    //Although the supplied user file has a comma and then a space between users, the space is not specified

                    //If it weren't for this, I would split on ", " and not need the String::trim below
                    List<String> peopleFollowedByPerson = Arrays.asList(vals[1].split(","));

                    //If there was a space after the comma, we need to remove it as it will be part of the "user name"
                    peopleFollowedByPerson.replaceAll(String::trim);

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