package com.andrew;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * I wrote TwitterParser to accept Stream<String> so that I could unit test without faking or reading from files
 * I build input up as List<String> where each entry is equivalent to a line in a File.
 */
public class TestTwitterParser {

    @Test
    public void testShowTweetsForGivenInputs() {

        Stream<String> users = buildGivenUserStream();
        Stream<String> allTweets = buildGivenTweetStream();

        String actual = TwitterParser.getTweetFeedForDisplay(users, allTweets);

//        The instructions say:
//        Your program needs to write console output as follows.
//        For each user / follower (in alphabetical order) output their name on a line.
//        Then for each tweet, emit a line with the following format: <tab>@user: <space>message.

        //another option would have been to compare to a file read from src/test/resources
        //if expected output had been supplied as part of the test, then I would have done it this way
        String expected = "Alan\n" +
                "\t@Alan: If you have a procedure with 10 parameters, you probably missed some.\n" +
                "\t@Alan: Random numbers should not be generated with a method chosen at random.\n" +
                "Martin\n" +
                "Ward\n" +
                "\t@Alan: If you have a procedure with 10 parameters, you probably missed some.\n" +
                "\t@Ward: There are only two hard things in Computer Science: cache invalidation, naming things and off-by-1 errors.\n" +
                "\t@Alan: Random numbers should not be generated with a method chosen at random.";

        assertEquals(expected, actual);

    }

    @Test
    public void testGetTweetsPerPersonForGivenInput() {

        Stream<String> users = buildGivenUserStream();
        Stream<String> allTweets = buildGivenTweetStream();
        Map<String, List<Tweet>> tweetsPerPerson = TwitterParser.getTweetsPerUser(users, allTweets);

        //1. Prove that Users are ordered alphabetically
        List<String> orderedListOfUsers = new ArrayList(tweetsPerPerson.keySet());

        assertEquals("Alan", orderedListOfUsers.get(0));
        assertEquals("Martin", orderedListOfUsers.get(1));
        assertEquals("Ward", orderedListOfUsers.get(2));

        assertEquals(2, tweetsPerPerson.get("Alan").size());
        assertEquals(0, tweetsPerPerson.get("Martin").size());
        assertEquals(3, tweetsPerPerson.get("Ward").size());

    }

    @Test
    public void testShowTweetsForNullInputs() {

        Stream<String> users = null;
        Stream<String> allTweets = null;
        TwitterParser.getTweetsPerUser(users, allTweets);

    }

    @Test(expected=RuntimeException.class)
    public void testShowTweetsForInvalidUserInputs() {

        Stream<String> users = buildBadlyFormedUserStream();
        Stream<String> allTweets = buildGivenTweetStream();
        TwitterParser.getTweetsPerUser(users, allTweets);

    }

    @Test(expected=RuntimeException.class)
    public void testShowTweetsForInvalidTweetInputs() {

        Stream<String> users = buildGivenUserStream();
        Stream<String> allTweets = buildBadlyFormedTweetStream();
        TwitterParser.getTweetsPerUser(users, allTweets);

    }

    @Test
    public void testParseUsersWhereNoCommas() {
        //This proves that parseUsers is tolerant of being comma separated without a space
        //Without the String::trim in parseUsers we get Ward following a single user called "Martin,Alan"
        List<String> usersList = new ArrayList<String>();
        usersList.add("Ward follows Martin,Alan");
        Map<String, Set<String>> whoFollowsWho = TwitterParser.parseUsers(usersList.stream());
        assertEquals(2, whoFollowsWho.get("Ward").size());

    }
    /**
     * This is a Stream / test fixture version of tweet.txt as supplied
     * one list entry corresponds to one line in the file
     * @return
     */
    private Stream<String> buildGivenTweetStream() {
        List<String> tweetList = new ArrayList<String>();
        tweetList.add("Alan> If you have a procedure with 10 parameters, you probably missed some.");
        tweetList.add("Ward> There are only two hard things in Computer Science: cache invalidation, naming things and off-by-1 errors.");
        tweetList.add("Alan> Random numbers should not be generated with a method chosen at random.");
        return tweetList.stream();
    }

    private Stream<String> buildBadlyFormedTweetStream() {
        List<String> tweetList = new ArrayList<String>();
        //It has no > hence nothing to split on
        tweetList.add("Alan If you have a procedure with 10 parameters, you probably missed some.");

        return tweetList.stream();
    }

    /**
     * This is a Stream / test fixture version of user.txt as supplied
     * one list entry corresponds to one line in the file
     * @return
     */
    private Stream<String> buildGivenUserStream() {
        List<String> usersList = new ArrayList<String>();

        usersList.add("Ward follows Alan");
        usersList.add("Alan follows Martin");
        usersList.add("Ward follows Martin, Alan");
        return usersList.stream();
    }

    private Stream<String> buildBadlyFormedUserStream() {
        List<String> usersList = new ArrayList<String>();

        //follows is missing an f, hence nothing to split on
        usersList.add("Ward ollows Alan");
        return usersList.stream();
    }

}
